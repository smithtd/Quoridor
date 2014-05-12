package ai;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;

import network.Messages;
import network.MoveServer;


public class Nigel extends MoveServer {

	private static int[][] playerMatrix;
	private static boolean[][] vWallMatrix;
	private static boolean[][] hWallMatrix;
	private static int totalTurns = 0;
	private static int numPlayers = 1;
	public static final String AI_IDENTIFIER = "tactical";
	private static Map<Integer, String[] > endZones;
	private static int [] wallCount;
	
	/** Map of starting positions to player numbers */
	private Map<Integer, String> startPos;
	/** Board Representations */
	char[][] board;
	// Do we need to keep two??
	char[][] tempBoard;
	/** Number of walls we have */
	int walls;
	private String currentPosition;
	
	public Nigel( int port ){
		super( port ); 
		this.startPos = new HashMap<Integer, String>();
		this.startPos.put(1, "04"); this.startPos.put(2, "84");
		this.startPos.put(3, "48"); this.startPos.put(4, "40");
		
		endZones = new HashMap<Integer, String[]>();
		String[] end1 = new String[]{ "80", "81", "82", "83", "84", "85", "86", "87", "88" };
		String[] end2 = new String[]{ "00", "01", "02", "03", "04", "05", "06", "07", "08" };
		String[] end3 = new String[]{ "00", "10", "20", "30", "40", "50", "60", "70", "80" };
		String[] end4 = new String[]{ "08", "18", "28", "38", "48", "58", "68", "78", "88" };
				
		endZones.put( 1, end1 );
		endZones.put( 2, end2 );
		endZones.put( 3, end3 );
		endZones.put( 4, end4 );
		
		if( numPlayers == 2 )
			wallCount = new int[]{ 10, 10 };
		else if ( numPlayers == 4 )
			wallCount = new int[]{ 5, 5, 5, 5 };
		
	}
	
	public void setupBoards( int numPlayers ){

		if( numPlayers == 2 ){
			playerMatrix = new int [][]
					 { { 0, 0, 0, 0, 1, 0, 0, 0, 0 },
					   { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					   { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					   { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					   { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					   { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					   { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					   { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					   { 0, 0, 0, 0, 2, 0, 0, 0, 0 } };
		} else {
			playerMatrix = new int [][]
					 { { 0, 0, 0, 0, 1, 0, 0, 0, 0 },
					   { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					   { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					   { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					   { 4, 0, 0, 0, 0, 0, 0, 0, 3 },
					   { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					   { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					   { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					   { 0, 0, 0, 0, 2, 0, 0, 0, 0 } };
		}
		
		/*
		 *  Coordinate System for walls in correspondance to the player
		 * 
		 * 				| h(y-1, x)	|
		 * 			  -----------------
		 * 				|			|
		 * 				|			|
		 *	  v(y, x-1)	|	p(y,x)	| v(y, x)
		 * 				|			|
		 * 				|			|
		 * 			  -----------------
		 * 				|  h(y,x)	|
		 */
		
		
		vWallMatrix = new boolean [][] // 8x9
				{  { false, false, false, false, false, false, false, false }, 
				   { false, false, false, false, false, false, false, false }, 
				   { false, false, false, false, false, false, false, false }, 
				   { false, false, false, false, false, false, false, false }, 
				   { false, false, false, false, false, false, false, false }, 
				   { false, false, false, false, false, false, false, false }, 
				   { false, false, false, false, false, false, false, false }, 
				   { false, false, false, false, false, false, false, false }, 
				   { false, false, false, false, false, false, false, false } };
	
		hWallMatrix = new boolean [][] // 9x8
				{  { false, false, false, false, false, false, false, false, false }, 
				   { false, false, false, false, false, false, false, false, false }, 
				   { false, false, false, false, false, false, false, false, false }, 
				   { false, false, false, false, false, false, false, false, false }, 
				   { false, false, false, false, false, false, false, false, false }, 
				   { false, false, false, false, false, false, false, false, false }, 
				   { false, false, false, false, false, false, false, false, false }, 
				   { false, false, false, false, false, false, false, false, false } };
	}
	
	
	/**
	 *  
	 * Allows the AI to start listening on the port. Once connected it will
	 * send the Hello message with the AI_IDENTIFIER message. It will then
	 * wait its turn and update moves as they are seen and taken care of. 
	 * 
	 */
	public void run() { 

		// Protocol says first message after connecting should be 
		// HELLO <AI-IDENTIFIER>
		System.out.println("Your AI Identifier is: " + AI_IDENTIFIER);
		this.identifier = AI_IDENTIFIER;

		// Remove this when AI is making its own decisions
		this.playerInput = new Scanner(System.in);
		while(true) {
			System.out.println("Starting the move server");
			this.hasWon = true;  
			try{

				// Start the listening Socket on port
				ServerSocket server = new ServerSocket(this.port);
				System.out.println("Waiting for a client...");
				Socket gameClient;

				while((gameClient = server.accept()) != null) {

					server.close();
					System.out.println("Now connected to client at: " + gameClient);

					this.clientOutput = new PrintStream(gameClient.getOutputStream());

					// Send output stream 
					System.out.println("Sending HELLO " + AI_IDENTIFIER + " message to " + gameClient);
					this.clientOutput.println(Messages.HELLO_MESSAGE + " " + AI_IDENTIFIER);

					// Read input that comes in from the game client
					this.clientInput = new Scanner(gameClient.getInputStream());

					while(this.clientInput.hasNext() && this.hasWon) { 
						String input = this.clientInput.nextLine();
						getResponse(input);
					}
				}
			} catch (IOException ioe) {
				System.out.println("Connection Terminated, Restarting :)");
			} catch (InterruptedException e) {	}
		}
	}


	/** 
	 * Starts an instance of this AI, handles command line args 
	 * @param args -- Port number to listen on. 
	 */
	public static void main(String[] args) {

		// Should have a port number as a command line argument
		if(args.length != 1) 
			usage();

		// Try and parse the port from the command
		int port = Integer.parseInt(args[0]);
		
		Nigel aiNigel = new Nigel(port);

		aiNigel.run();
	}

	public void getResponse(String input) throws InterruptedException {
		Thread.sleep(250); // try to watch what is happening

		if(input.equalsIgnoreCase(Messages.ASK_FOR_MOVE)){
			String move = getMove();
			this.currentPosition = move;

			this.clientOutput.println("MOVE " + move);

		} else if(input.startsWith(Messages.TELL_MOVE)){
			Scanner info = new Scanner(input);
			info.next();
			String playerID = info.next();
			String moveMade = info.next();
			parseIncomingMove( moveMade );
			System.out.println("Player " + playerID + " made move: " + moveMade );
			info.close();
		} else if(input.startsWith(Messages.START_GAME)){
			Scanner getID = new Scanner(input);
			getID.next();
			this.playerId = Integer.parseInt(getID.next());
			this.clientOutput.println(Messages.READY + " " + this.identifier);
			System.out.println("Playing as player # " + this.playerId);
			this.currentPosition = this.startPos.get(this.playerId);
			getID.close();
		} else if(input.startsWith(Messages.REMOVED)){
			Scanner removed = new Scanner(input);
			removed.next();
			int player = Integer.parseInt(removed.next());
			if(player == this.playerId) {
				System.out.println("You have been removed for illegal moves.");
				this.hasWon = false;
			} else {
				System.out.println("Player " + player + " has been removed");
			}
			removed.close();
		} else if(input.startsWith(Messages.WINNER)) {
			Scanner win = new Scanner(input);
			win.next();
			int player = Integer.parseInt(win.next());
			win.close();
			this.hasWon = false;
			if(player == this.playerId){
				System.out.println("You won!");
			} else {
				System.out.println("Player " + player + " won! You lose!");
			}
		}
	}

	
	
	
	
	
	
	
	private static void parseIncomingMove( String move ){
		int playerID = ( totalTurns % numPlayers ) + 1;
		int moveY = Integer.parseInt( "" + move.charAt( 1 ) );
		int moveX = move.charAt( 0 )-'a';
		
		if( move.length() == 2 ){
			for( int y=0; y<9; y++ ){
				for( int x=0; x<9; x++ ){
					if( playerMatrix[ y ][ x ] == playerID )
						playerMatrix[ y ][ x ] = 0;
				}
			}
			playerMatrix[ moveY ][ moveX ] = playerID;
		} else {
			if( move.charAt( 2 )=='h' ){
				hWallMatrix[ moveY ][ moveX ] = true;
				hWallMatrix[ moveY ][ moveX+1 ] = true;
			} else {
				vWallMatrix[ moveY ][ moveX ] = true;
				vWallMatrix[ moveY+1 ][ moveX ] = true;
			}
			wallCount[ playerID-1 ]--;
		}
	}
	
	private String getMove(){
		String p1 = "", p2 = "", p3 = "", p4 = "";
		for( int y=0; y<9; y++ ){
			for( int x=0; x<9; x++ ){
				if( playerMatrix[ y ][ x ] == 1 ) p1 = "" + y + x;
				if( playerMatrix[ y ][ x ] == 2 ) p2 = "" + y + x;
				if( playerMatrix[ y ][ x ] == 3 ) p3 = "" + y + x;
				if( playerMatrix[ y ][ x ] == 4 ) p4 = "" + y + x;
			}
		}
		ArrayList< ArrayList< String > > playerPaths = new ArrayList< ArrayList< String > > ();
		
		ArrayList<String> path1 = getPathToEnd( p1 );
		ArrayList<String> path2 = getPathToEnd( p2 );
		playerPaths.add( path1 );
		playerPaths.add( path2 );
		if( ! p3.equals( "" ) ){
			ArrayList<String> path3 = getPathToEnd( p3 );
			ArrayList<String> path4 = getPathToEnd( p4 );
			playerPaths.add( path3 );
			playerPaths.add( path4 );
		}
		/*
		if( isPlayerBehindOrInDangerOfLosing( playerPaths ) && wallCount[ totalTurns%numPlayers]>0 ){
			String move = getWallPlacement( playerPaths );
			parseIncomingMove( move );
			return move;
		} else { 
		*/
			String s = playerPaths.get( totalTurns%numPlayers ).get(1);
			String move = "" + ('a' + Integer.parseInt( "" + s.charAt( 1 ) ) ) + s.charAt( 0 );
			parseIncomingMove( move );
			return move;
//		}
		/*
		 * for all players find their paths. if any one player is
		 * more than 3 spaces closer OR within 3 spaces to the 
		 * end, block with wall. else, return the next position
		 * in the AI's shortest path.
		 */
	}
	
	public static String getWallPlacement( ArrayList< ArrayList< String > > playerPaths ){
		
		//TODO return wall to block
		return "";
	}
	
	public static boolean isPlayerBehindOrInDangerOfLosing( ArrayList< ArrayList< String > > playerPaths ){
		int playerIndex = totalTurns%numPlayers;
		int playerPathLength = playerPaths.get( playerIndex ).size();
		
		for( int index=0; index<playerPaths.size(); index++ ){
			if( index != playerIndex ){
				int opponetPathLength = playerPaths.get( index ).size();
				if( opponetPathLength - playerPathLength >= 3 )
					return true;
				if( opponetPathLength < 4 )
					return true;
			}
		}
		return false;
	}
	
	private ArrayList<String> getPathToEnd( String locationOfPlayer ){
		ArrayList<ArrayList<String>> possiblePaths = new ArrayList<ArrayList<String>>();
		ArrayList<String> seen = new ArrayList<String>();

		possiblePaths.add( new ArrayList<String>() );
		possiblePaths.get( 0 ).add( locationOfPlayer );
		seen.add( locationOfPlayer );

		return getPath( seen, possiblePaths );
	}

	private ArrayList<String> getPath( ArrayList<String> seen, ArrayList<ArrayList<String>> possiblePaths ){

		ArrayList<ArrayList<String>> newPossiblePaths = new ArrayList<ArrayList<String>>();
		for( int pathIndex=0; pathIndex<possiblePaths.size(); pathIndex++ ){			//Go through all possible paths so far
			ArrayList<String> list = possiblePaths.get( pathIndex );   // for every list, take the end button and find its 4 directions. If one of the directions has already been seen,  ignore it. Else add a new path with it attached at the end
			
			String current = list.get( list.size()-1 );
			String next = null;

			int currY = Integer.parseInt( "" + current.charAt( 0 ) );
			int currX = Integer.parseInt( "" + current.charAt( 1 ) );
			
			DirectionLoop:
			for( int direction=0; direction<4; direction++ ){
				int nextY, nextX;
				if	   ( direction==0 )	{ nextX=currX-1; nextY=currY; } //k==0 is left
				else if( direction==1 )	{ nextX=currX+1; nextY=currY; }	//k==1 is right
				else if( direction==2 )	{ nextX=currX; nextY=currY+1; }	//k==2 is down
				else{ 			  		  nextX=currX; nextY=currY-1; }	//k==3 is up

				try{
					int spotID = playerMatrix[nextY][nextX];

					if( ( direction==0 && vWallMatrix[currY][currX-1] ) 
					 || ( direction==1 && vWallMatrix[currY][currX] ) 
					 || ( direction==2 && hWallMatrix[currY][currX] ) 
					 || ( direction==3 && hWallMatrix[currY-1][currX] ) ) 
						continue DirectionLoop; 

					
					next = "" + nextY + nextX;
					if( !seen.contains( next ) ){
						seen.add( next );
						ArrayList<String> newList = new ArrayList<String>();
						for( int spotIndex=0; spotIndex<list.size(); spotIndex++ )
							newList.add( list.get( spotIndex ) );
						newList.add( next );		//add next to the new copy
						for( int endZoneIndex=0; endZoneIndex<9; endZoneIndex++ )
							if( endZones.get( (totalTurns%numPlayers) )[ endZoneIndex ] == next )
								return newList;
						newPossiblePaths.add( newList );	//add new list to the new possible paths
					}
				} catch( Exception e ){}
			}
		}
		possiblePaths = null;
		return getPath( seen, newPossiblePaths );
	}
}



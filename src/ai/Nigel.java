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
	private static int numPlayers = 4;
	public static final String AI_IDENTIFIER = "tactical";
	private static String[][] endZones;
	private static int [] wallCount;
	private static int playerID;
	
	/** Map of starting positions to player numbers */
	private String[] startPos;
	/** Board Representations */
	char[][] board;
	// Do we need to keep two??
	char[][] tempBoard;
	/** Number of walls we have */
	int walls;
	
	public Nigel( int port ){
		super( port ); 
	}
	
	public void setupBoards( int numPlayers ){
		startPos = new String[]{ "04", "84", "40", "48" };
		
		endZones = new String[][]{ 
				new String[]{ "80", "81", "82", "83", "84", "85", "86", "87", "88" }, 
				new String[]{ "00", "01", "02", "03", "04", "05", "06", "07", "08" }, 
				new String[]{ "08", "18", "28", "38", "48", "58", "68", "78", "88" }, 
				new String[]{ "00", "10", "20", "30", "40", "50", "60", "70", "80" } };
				
		if( numPlayers == 2 )
			wallCount = new int[]{ 10, 10 };
		else if ( numPlayers == 4 )
			wallCount = new int[]{ 5, 5, 5, 5 };
		
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
					   { 3, 0, 0, 0, 0, 0, 0, 0, 4 },
					   { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					   { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					   { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					   { 0, 0, 0, 0, 2, 0, 0, 0, 0 } };
		}
		
		/*
		 *  Coordinate System for walls in correspondence to the player
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

				setupBoards( numPlayers );
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
		Thread.sleep(750); // try to watch what is happening

		if(input.equalsIgnoreCase(Messages.ASK_FOR_MOVE)){
			String move = getMove();

			this.clientOutput.println("MOVE " + move);

		} else if(input.startsWith(Messages.TELL_MOVE)){
			Scanner info = new Scanner(input);
			info.next();
			String playerID = info.next();
			String moveMade = info.next();
			parseIncomingMove( moveMade, Integer.parseInt( playerID ) );
			System.out.println("Player " + playerID + " made move: " + moveMade );
			info.close();
		} else if(input.startsWith(Messages.START_GAME)){
			Scanner getID = new Scanner(input);
			getID.next();
			this.playerId = Integer.parseInt(getID.next());
			Nigel.playerID = this.playerId-1;
			this.clientOutput.println(Messages.READY + " " + this.identifier);
			System.out.println("Playing as player # " + this.playerId);
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

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private static void parseOurMove( String move ){
		int moveY = Integer.parseInt( "" + move.charAt( 1 ) ) - 1;
		int moveX = move.charAt( 0 ) - 'a';
		if( move.length() == 2 ){
			System.out.println( "Moving " + Nigel.playerID );
			for( int y=0; y<9; y++ ){
				for( int x=0; x<9; x++ ){
					if( playerMatrix[ y ][ x ] == Nigel.playerID )
						playerMatrix[ y ][ x ] = 0;
				}
			}
			playerMatrix[ moveY ][ moveX ] = Nigel.playerID;
		} else {
			if( move.charAt( 2 )=='h' ){
				hWallMatrix[ moveY ][ moveX ] = true;
				hWallMatrix[ moveY ][ moveX+1 ] = true;
			} else {
				vWallMatrix[ moveY ][ moveX ] = true;
				vWallMatrix[ moveY+1 ][ moveX ] = true;
			}
			wallCount[ Nigel.playerID-1 ]--;
		}
		System.out.println("\n\n\n\n\n");
	}	
	
	
	
	private static void parseIncomingMove( String move, int opponentID ){
		int moveY = Integer.parseInt( "" + move.charAt( 1 ) ) - 1;
		int moveX = move.charAt( 0 ) - 'a';
		if( move.length() == 2 ){
			System.out.println( "Moving " + opponentID );
			for( int y=0; y<9; y++ ){
				for( int x=0; x<9; x++ ){
					if( playerMatrix[ y ][ x ] == opponentID )
						playerMatrix[ y ][ x ] = 0;
				}
			}
			playerMatrix[ moveY ][ moveX ] = opponentID;
		} else {
			if( move.charAt( 2 )=='h' ){
				hWallMatrix[ moveY ][ moveX ] = true;
				hWallMatrix[ moveY ][ moveX+1 ] = true;
			} else {
				vWallMatrix[ moveY ][ moveX ] = true;
				vWallMatrix[ moveY+1 ][ moveX ] = true;
			}
			wallCount[ opponentID - 1 ]--;
		}
		System.out.println("\n\n\n\n\n");
	}
	
	private String getMove(){
		System.out.println( "GETTING MOVE FROM BLUE" );
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
		if( isPlayerBehindOrInDangerOfLosing( playerPaths ) && wallCount[ Nigel.playerID ]>0 ){
			String move = getWallPlacement( playerPaths );
			parseOurMove( move );
			return move;
		} else {
		*/ 
			String s = playerPaths.get( Nigel.playerID ).get(1);
			String move = "" + (char)('a' + Integer.parseInt( "" + s.charAt( 1 ) ) ) + (Integer.parseInt( "" + s.charAt( 0 ) ) + 1 );
			parseOurMove( move );
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

		int currentShortestPathLength = 0;
		
		for( int yP =0; yP<9; yP++ )
			for( int xP=0; xP<9; xP++ )
				if( playerMatrix[ yP ][ xP ] == Nigel.playerID )
					currentShortestPathLength = getPathToEnd( "" + yP + xP ).size();
		
		String bestHWallOpt = getBestHWallOpt( currentShortestPathLength );
		String bestVWallOpt = getBestVWallOpt( currentShortestPathLength );
		return "";
	}
	
	public static String getBestVWallOpt( int length ){
		
		String best = "";
		int currentLength = length;
		
		int length1 = 0;
		int length2 = 0;
		int length3 = 0;
		int length4 = 0;
		
		boolean[][] vWallCopy = new boolean[9][8];
		for( int y=0; y< vWallCopy.length; y++ )
			for( int x=0; x<vWallCopy[ y ].length; x++ )
				vWallCopy[ y ][ x ] = vWallMatrix[ y ][ x ];
		
		for( int yV=0; yV< vWallMatrix.length-1; yV++ ){
			for( int xV=0; xV<vWallMatrix[ yV ].length; xV++ ){
				if( vWallMatrix[ yV ][ xV ] == false && vWallMatrix[ yV ][ xV ] == false ){
					vWallMatrix[ yV ][ xV ] = true;
					vWallMatrix[ yV+1 ][ xV ] = true;
					String p1 = "", p2 = "", p3 = "", p4 = "";
					for( int yP =0; yP<9; yP++ ){
						for( int xP=0; xP<9; xP++ ){
							if( playerMatrix[ yP ][ xP ] == 1 ) p1 = "" + yP + xP;
							if( playerMatrix[ yP ][ xP ] == 2 ) p2 = "" + yP + xP;
							if( playerMatrix[ yP ][ xP ] == 3 ) p3 = "" + yP + xP;
							if( playerMatrix[ yP ][ xP ] == 4 ) p4 = "" + yP + xP;
						}
					}
					
					int length1a, length2a, length3a, length4a;
					length1a = getPathToEnd( p1 ).size();
					length2a = getPathToEnd( p2 ).size();
					if( ! p3.equals( "" ) ){
						length3a = getPathToEnd( p3 ).size();
						length4a = getPathToEnd( p4 ).size();
					}
				}//end if(false && false)
				vWallMatrix[ yV ][ xV ] = vWallCopy[ yV ][ xV ]; //reset every time
			}// end xV
		}//end yV
		
		
		
		return "";
	}

	public static String getBestHWallOpt( int length ){
		
		String best = "";
		int currentLength = length;
		
		boolean[][] hWallCopy = new boolean[8][9];
		for( int y=0; y< hWallCopy.length; y++ )
			for( int x=0; x<hWallCopy[ y ].length; x++ )
				hWallCopy[ y ][ x ] = hWallMatrix[ y ][ x ];
		
		
		return "";
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static boolean isPlayerBehindOrInDangerOfLosing( ArrayList< ArrayList< String > > playerPaths ){

		int playerPathLength = playerPaths.get( Nigel.playerID ).size();
		
		for( int index=0; index<playerPaths.size(); index++ ){
			if( index != Nigel.playerID ){
				int opponetPathLength = playerPaths.get( index ).size();
				if( opponetPathLength - playerPathLength >= 3 )
					return true;
				if( opponetPathLength < 4 )
					return true;
			}
		}
		return false;
	}
	
	private static ArrayList<String> getPathToEnd( String locationOfPlayer ){
		ArrayList<ArrayList<String>> possiblePaths = new ArrayList<ArrayList<String>>();
		ArrayList<String> seen = new ArrayList<String>();
		System.out.println( locationOfPlayer );
		possiblePaths.add( new ArrayList<String>() );
		possiblePaths.get( 0 ).add( locationOfPlayer );
		seen.add( locationOfPlayer );

		return getPath( seen, possiblePaths );
	}

	private static ArrayList<String> getPath( ArrayList<String> seen, ArrayList<ArrayList<String>> possiblePaths ){
		ArrayList<ArrayList<String>> newPossiblePaths = new ArrayList<ArrayList<String>>();
		for( int pathIndex=0; pathIndex<possiblePaths.size(); pathIndex++ ){			//Go through all possible paths so far
			ArrayList<String> list = possiblePaths.get( pathIndex );   // for every list, take the end button and find its 4 directions. If one of the directions has already been seen,  ignore it. Else add a new path with it attached at the end
			
			String current = list.get( list.size()-1 );
			String next = null;

			int currY = Integer.parseInt( "" + current.charAt( 0 ) );
			int currX = Integer.parseInt( "" + current.charAt( 1 ) );
			
			for( int direction=0; direction<4; direction++ ){
				int nextY, nextX;
				if	   ( direction==0 )	{ nextX=currX-1; nextY=currY; } //k==0 is left
				else if( direction==1 )	{ nextX=currX+1; nextY=currY; }	//k==1 is right
				else if( direction==2 )	{ nextX=currX; nextY=currY+1; }	//k==2 is down
				else{ 			  		  nextX=currX; nextY=currY-1; }	//k==3 is up

				if( nextX>=0 && nextX<=8 && nextY>=0 && nextY<=8 ){
					if( ( direction==0 && vWallMatrix[currY][currX-1] ) 
					 || ( direction==1 && vWallMatrix[currY][currX] ) 
					 || ( direction==2 && hWallMatrix[currY][currX] ) 
					 || ( direction==3 && hWallMatrix[currY-1][currX] ) ) {
						System.out.println( "ABORT" );
					}else{
						int nextPiece = playerMatrix[ nextY ][ nextX ];
						next = "" + nextY + nextX;
						if( !seen.contains( next ) ){
							seen.add( next );
							ArrayList<String> newList = new ArrayList<String>();
							
							
							if( nextPiece == 1 || nextPiece == 2 || nextPiece == 3 || nextPiece == 4 ){
								for( int spotIndex=0; spotIndex<list.size(); spotIndex++ )
									newList.add( list.get( spotIndex ) );
								ArrayList<String> tempList = calculateJumps( seen, nextY, nextX, new ArrayList<String> () );
								for( int tempIndex=0; tempIndex<tempList.size(); tempIndex++ ){
									String found = tempList.get( tempIndex );
									if( !seen.contains( found ) )
										seen.add( found );
									newList.add( tempList.get( tempIndex ) );
								}
							} else {
								for( int spotIndex=0; spotIndex<list.size(); spotIndex++ )
									newList.add( list.get( spotIndex ) );
								newList.add( next );		//add next to the new copy
							}
							
							for( int endZoneIndex=0; endZoneIndex<9; endZoneIndex++ )
								if( endZones[ Nigel.playerID ][ endZoneIndex ].equals( next ) ){
									System.out.println( "found path " + newList );
									return newList;
								}
							
							
							newPossiblePaths.add( newList );	//add new list to the new possible paths
						}
					}
				}
			}
		}
		possiblePaths = null;
		System.out.println( newPossiblePaths );
		return getPath( seen, newPossiblePaths );
	}
	
	private static ArrayList<String> calculateJumps( ArrayList<String> seen, int currY, int currX, ArrayList<String> foundSpots ){
		System.out.println( "Point: " + currY + currX );
		for( int direction=0; direction<4; direction++ ){
			int nextY, nextX;
			if	   ( direction==0 )	{ nextX=currX-1; nextY=currY; } //k==0 is left
			else if( direction==1 )	{ nextX=currX+1; nextY=currY; }	//k==1 is right
			else if( direction==2 )	{ nextX=currX; nextY=currY+1; }	//k==2 is down
			else{ 			  		  nextX=currX; nextY=currY-1; }	//k==3 is up
			
			if( nextX>=0 && nextX<=8 && nextY>=0 && nextY<=8 ){
				if( ( direction==0 && vWallMatrix[currY][currX-1] ) 
				 || ( direction==1 && vWallMatrix[currY][currX] ) 
				 || ( direction==2 && hWallMatrix[currY][currX] ) 
				 || ( direction==3 && hWallMatrix[currY-1][currX] ) ) {
					System.out.println( "ABORT" );
				} else{
					int nextPiece = playerMatrix[ nextY ][ nextX ];
					String next = "" + nextY + nextX;
					if( !seen.contains( next ) ){
						seen.add( next );
						if( nextPiece == 1 || nextPiece == 2 || nextPiece == 3 || nextPiece == 4 )
							return calculateJumps( seen, nextY, nextX, foundSpots );
						else
							foundSpots.add( next );
					}
				}
			}
		}
		return foundSpots;
	}
}



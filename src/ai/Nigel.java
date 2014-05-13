package ai;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import network.Messages;
import network.MoveServer;


public class Nigel extends MoveServer {

	private static int[][] playerMatrix;
	private static boolean[][] vWallMatrix;
	private static boolean[][] hWallMatrix;
	private static int numPlayers;
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
		Nigel.numPlayers = numPlayers;
		endZones = new String[][]{ 
				new String[]{ "80", "81", "82", "83", "84", "85", "86", "87", "88" }, 
				new String[]{ "00", "01", "02", "03", "04", "05", "06", "07", "08" }, 
				new String[]{ "08", "18", "28", "38", "48", "58", "68", "78", "88" }, 
				new String[]{ "00", "10", "20", "30", "40", "50", "60", "70", "80" } };

		if( numPlayers == 2 )
			wallCount = new int[]{ 10, 10 };
		else //( numPlayers == 4 )
			wallCount = new int[]{ 5, 5, 5, 5 };

		if( numPlayers == 2 ){
			playerMatrix = new int [][]{
					{ 0, 0, 0, 0, 1, 0, 0, 0, 0 },
					{ 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					{ 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					{ 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					{ 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					{ 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					{ 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					{ 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					{ 0, 0, 0, 0, 2, 0, 0, 0, 0 } };
		} else {
			playerMatrix = new int [][]{
					{ 0, 0, 0, 0, 1, 0, 0, 0, 0 },
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
		//System.out.println("Your AI Identifier is: " + AI_IDENTIFIER);
		this.identifier = AI_IDENTIFIER;

		// Remove this when AI is making its own decisions
		this.playerInput = new Scanner(System.in);
		while(true) {
			//System.out.println("Starting the move server");
			this.hasWon = true;  
			try{

				setupBoards( numPlayers );
				// Start the listening Socket on port
				ServerSocket server = new ServerSocket(this.port);
				//System.out.println("Waiting for a client...");
				Socket gameClient;

				while((gameClient = server.accept()) != null) {

					server.close();
					//System.out.println("Now connected to client at: " + gameClient);

					this.clientOutput = new PrintStream(gameClient.getOutputStream());

					// Send output stream 
					//System.out.println("Sending HELLO " + AI_IDENTIFIER + " message to " + gameClient);
					this.clientOutput.println(Messages.HELLO_MESSAGE + " " + AI_IDENTIFIER);

					// Read input that comes in from the game client
					this.clientInput = new Scanner(gameClient.getInputStream());

					while(this.clientInput.hasNext() && this.hasWon) { 
						String input = this.clientInput.nextLine();
						getResponse(input);
					}
				}
			} catch (IOException ioe) {
				//System.out.println("Connection Terminated, Restarting :)");
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
		Thread.sleep(150); // try to watch what is happening

		if(input.equalsIgnoreCase(Messages.ASK_FOR_MOVE)){
			String move = getMove();

			this.clientOutput.println("MOVE " + move);

		} else if(input.startsWith(Messages.TELL_MOVE)){
			Scanner info = new Scanner(input);
			info.next();
			String playerID = info.next();
			String moveMade = info.next();
			parseIncomingMove( moveMade, Integer.parseInt( playerID ) );
			//System.out.println("Player " + playerID + " made move: " + moveMade );
			info.close();
		} else if(input.startsWith(Messages.START_GAME)){
			Scanner getID = new Scanner(input);
			getID.next();
			this.playerId = Integer.parseInt(getID.next());
			int numLeft = 0;
			while( getID.hasNext() ){
				numLeft++;
				getID.next();
			}
			if( numLeft == 1 )
				setupBoards( 2 );
			else
				setupBoards( 4 );
			Nigel.playerID = this.playerId-1;
			this.clientOutput.println(Messages.READY + " " + this.identifier);
			//System.out.println("Playing as player # " + this.playerId);
			getID.close();
		} else if(input.startsWith(Messages.REMOVED)){
			Scanner removed = new Scanner(input);
			removed.next();
			int player = Integer.parseInt(removed.next());
			if(player == this.playerId) {
				//System.out.println("You have been removed for illegal moves.");
				this.hasWon = false;
			} else {
				//System.out.println("Player " + player + " has been removed");
			}
			removed.close();
		} else if(input.startsWith(Messages.WINNER)) {
			Scanner win = new Scanner(input);
			win.next();
			int player = Integer.parseInt(win.next());
			win.close();
			this.hasWon = false;
			if(player == this.playerId){
				//System.out.println("You won!");
			} else {
				//System.out.println("Player " + player + " won! You lose!");
			}
		}
	}


	private static void parseOurMove( String move ){ //have our moves come in as 1 indexed to stay consistent
		System.out.println( "MOVE before: " + move );
		
		int moveY = Integer.parseInt( "" + move.charAt( 1 ) ) - 1;
		int moveX = move.charAt( 0 ) - 'a';
		if( move.length() == 2 ){
			//System.out.println( "Moving " + Nigel.playerID );
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
			wallCount[ Nigel.playerID ]--;
		}

	}	



	private static void parseIncomingMove( String move, int opponentID ){
		int moveY = Integer.parseInt( "" + move.charAt( 1 ) ) - 1; //move is 1 indexed, [][] is 0
		int moveX = move.charAt( 0 ) - 'a';
		if( move.length() == 2 ){
			//System.out.println( "Moving " + opponentID );
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
			wallCount[ opponentID - 1]--; //opponentID is 1 indexed from server, 0 indexed here
		}

	}

	private String getMove(){
		//System.out.println( "GETTING MOVE FROM BLUE" );
		String p1 = "", p2 = "", p3 = "", p4 = "";
		for( int y=0; y<9; y++ ){
			for( int x=0; x<9; x++ ){
				if( playerMatrix[ y ][ x ] == 1 ) p1 = "" + y + x;
				if( playerMatrix[ y ][ x ] == 2 ) p2 = "" + y + x;
				if( playerMatrix[ y ][ x ] == 3 ) p3 = "" + y + x;
				if( playerMatrix[ y ][ x ] == 4 ) p4 = "" + y + x;
			}
		}
		ArrayList<ArrayList<String>> playerPaths = new ArrayList<ArrayList<String>> ();

		ArrayList<String> path1 = getPathToEnd( p1 );
		ArrayList<String> path2 = getPathToEnd( p2 );
		playerPaths.add( path1 );
		playerPaths.add( path2 );
		if( numPlayers == 4 ){
			ArrayList<String> path3 = getPathToEnd( p3 );
			ArrayList<String> path4 = getPathToEnd( p4 );
			playerPaths.add( path3 );
			playerPaths.add( path4 );
		}
		if( wallCount[ Nigel.playerID ]>0 )
			;
		String move = "";
	
		if( isPlayerBehindOrInDangerOfLosing( playerPaths ) && wallCount[ Nigel.playerID ]>0 ){
			String s = getWallPlacement( playerPaths );
			move = "" + (char)('a' + Integer.parseInt( "" + s.charAt( 1 ) ) ) + (Integer.parseInt( "" + s.charAt( 0 ) ) + 1 ) + s.charAt( 2 );
			parseOurMove( move );
		}else{
	
			String s = playerPaths.get( Nigel.playerID ).get(1);
			move = "" + (char)('a' + Integer.parseInt( "" + s.charAt( 1 ) ) ) + (Integer.parseInt( "" + s.charAt( 0 ) ) + 1 );
			parseOurMove( move );
		}
		/*
		 * for all players find their paths. if any one player is
		 * more than 3 spaces closer OR within 3 spaces to the 
		 * end, block with wall. else, return the next position
		 * in the AI's shortest path.
		 */
		return move;

	}

	public static String getWallPlacement( ArrayList< ArrayList< String > > playerPaths ){

		String bestHWallOpt = getBestHWallOpt( );
		String bestVWallOpt = getBestVWallOpt( );

		int yH = Integer.parseInt( "" + bestHWallOpt.charAt( 0 ) );
		int xH = Integer.parseInt( "" + bestHWallOpt.charAt( 1 ) );
		int yV = Integer.parseInt( "" + bestVWallOpt.charAt( 0 ) );
		int xV = Integer.parseInt( "" + bestVWallOpt.charAt( 1 ) );

		hWallMatrix[ yH ][ xH ] = true;
		hWallMatrix[ yH ][ xH+1 ] = true;
		int hPathLength = 100 ;
		for( int yP =0; yP<9; yP++ )
			for( int xP=0; xP<9; xP++ )
				if( playerMatrix[ yP ][ xP ] == Nigel.playerID )
					hPathLength = getPathToEnd( "" + yP+xP ).size();
		hWallMatrix[ yH ][ xH ] = false;
		hWallMatrix[ yH ][ xH+1 ] = false;


		hWallMatrix[ yV ][ xV ] = true;
		hWallMatrix[ yV ][ xV+1 ] = true;
		int vPathLength = 100;
		for( int yP =0; yP<9; yP++ )
			for( int xP=0; xP<9; xP++ )
				if( playerMatrix[ yP ][ xP ] == Nigel.playerID )
					hPathLength = getPathToEnd( "" + yP+xP ).size();
		hWallMatrix[ yV ][ xV ] = false;
		hWallMatrix[ yV ][ xV+1 ] = false;

		bestHWallOpt = "" + bestHWallOpt + 'h';
		bestVWallOpt = "" + bestVWallOpt + 'v';
		Random rand = new Random();
		return (rand.nextInt(2) == 0) ? bestHWallOpt : bestVWallOpt;
	}


	public static String getBestVWallOpt(){

		//Find Player Locations
		String [] playerLocal = new String [ numPlayers ];
		for( int yP =0; yP<9; yP++ ){
			for( int xP=0; xP<9; xP++ ){
				if( playerMatrix[ yP ][ xP ] == 1 ) playerLocal[ 0 ] = "" + yP + xP;
				if( playerMatrix[ yP ][ xP ] == 2 ) playerLocal[ 1 ] = "" + yP + xP;
				if( playerMatrix[ yP ][ xP ] == 3 ) playerLocal[ 2 ] = "" + yP + xP;
				if( playerMatrix[ yP ][ xP ] == 4 ) playerLocal[ 3 ] = "" + yP + xP;
			}
		}

		//Make copy of walls
		boolean[][] vWallCopy = new boolean[9][8];
		for( int y=0; y< vWallCopy.length; y++ )
			for( int x=0; x<vWallCopy[ y ].length; x++ )
				vWallCopy[ y ][ x ] = vWallMatrix[ y ][ x ];

		@SuppressWarnings("unchecked")
		Map<Integer, ArrayList<String>> [] HOLYFUCKMAP = new HashMap[ numPlayers ];

		for( int z=0; z<numPlayers; z++){
			Map<Integer, ArrayList<String>> playerWallMap = new HashMap<Integer, ArrayList<String>> ();
			for( int y=0; y< vWallMatrix.length-1; y++ ){
				for( int x=0; x<vWallMatrix[ y ].length; x++ ){

					//reset before tests
					for( int i=0; i< vWallCopy.length; i++ )
						for( int j=0; j<vWallCopy[ i ].length; j++ )
							vWallMatrix[ i ][ j ] = vWallCopy[ i ][ j ]; 

					// if no wall then place temp wall and test
					if( vWallMatrix[ y ][ x ] == false && vWallMatrix[ y+1 ][ x ] == false ){

						vWallMatrix[ y ][ x ] = true;
						vWallMatrix[ y+1 ][ x ] = true;
						int pathLength = getPathToEnd( playerLocal[ z ] ).size();
						ArrayList<String> temp;

						if( playerWallMap.keySet().contains( pathLength ) )
							temp = playerWallMap.get( pathLength );
						else 
							temp = new ArrayList<String> ();

						temp.add( "" + y + x );
						playerWallMap.put( pathLength, temp );

					} // end if( false && false )
				} // end x
			} // end y
			HOLYFUCKMAP[ z ] = playerWallMap;
		} // end z

		for( int i=0; i< numPlayers; i++ ){
			for( int key : HOLYFUCKMAP[ i ].keySet() ){
				System.out.println( "Key: " + key + "v == " + HOLYFUCKMAP[ i ].get(key));
			}
		}
		System.out.println("\n\n\n");
				
		Map<Integer, ArrayList<String>> minimal = HOLYFUCKMAP[ Nigel.playerID ];

		ArrayList<String> bestForNigel = minimal.get( Collections.min( minimal.keySet() ) );

		for( int i=0; i< bestForNigel.size(); i++ ){
			String targetWall = bestForNigel.get( i );
			boolean checksOut = true;
			for( int j=0; j<numPlayers; j++ ){
				if( j != Nigel.playerID ){
					Map<Integer, ArrayList<String>> maximum = HOLYFUCKMAP[ j ];
					ArrayList<String> worstForOpponent = maximum.get( Collections.max( maximum.keySet() ) );
					if( !worstForOpponent.contains( targetWall ) )
						checksOut = false;
				}
			}
			if( checksOut )
				return targetWall;
		}
		Random rand = new Random();
		return bestForNigel.get( rand.nextInt( bestForNigel.size() ) );
	}

	public static String getBestHWallOpt( ){

		//Find Player Locations
		String [] playerLocal = new String [ numPlayers ];
		for( int yP =0; yP<9; yP++ ){
			for( int xP=0; xP<9; xP++ ){
				if( playerMatrix[ yP ][ xP ] == 1 ) playerLocal[ 0 ] = "" + yP + xP;
				if( playerMatrix[ yP ][ xP ] == 2 ) playerLocal[ 1 ] = "" + yP + xP;
				if( playerMatrix[ yP ][ xP ] == 3 ) playerLocal[ 2 ] = "" + yP + xP;
				if( playerMatrix[ yP ][ xP ] == 4 ) playerLocal[ 3 ] = "" + yP + xP;
			}
		}

		//Make copy of walls
		boolean[][] hWallCopy = new boolean[ 8 ][ 9 ];
		for( int y=0; y< hWallCopy.length; y++ )
			for( int x=0; x<hWallCopy[ y ].length; x++ )
				hWallCopy[ y ][ x ] = hWallMatrix[ y ][ x ];

		@SuppressWarnings("unchecked")
		Map<Integer, ArrayList<String>> [] HOLYFUCKMAP = new HashMap[ numPlayers ];

		for( int z=0; z<numPlayers; z++){
			Map<Integer, ArrayList<String>> playerWallMap = new HashMap<Integer, ArrayList<String>> ();
			for( int y=0; y< hWallMatrix.length; y++ ){
				for( int x=0; x<hWallMatrix[ y ].length-1; x++ ){

					//reset before tests
					for( int i=0; i< hWallCopy.length; i++ )
						for( int j=0; j<hWallCopy[ i ].length; j++ )
							hWallMatrix[ i ][ j ] = hWallCopy[ i ][ j ]; 

					// if no wall then place temp wall and test
					if( hWallMatrix[ y ][ x ] == false && hWallMatrix[ y ][ x+1 ] == false ){

						hWallMatrix[ y ][ x ] = true;
						hWallMatrix[ y ][ x+1 ] = true;
						int pathLength = getPathToEnd( playerLocal[ z ] ).size();
						ArrayList<String> temp;

						if( playerWallMap.keySet().contains( pathLength ) )
							temp = playerWallMap.get( pathLength );
						else 
							temp = new ArrayList<String> ();

						temp.add( "" + y + x );
						playerWallMap.put( pathLength, temp );

					} // end if( false && false )
				} // end x
			} // end y
			HOLYFUCKMAP[ z ] = playerWallMap;
		} // end z
		
		for( int i=0; i< numPlayers; i++ ){
			for( int key : HOLYFUCKMAP[ i ].keySet() ){
				System.out.println( "Key: " + key + "h == " + HOLYFUCKMAP[ i ].get(key));
			}
		}
		System.out.println("\n\n\n");
		
		Map<Integer, ArrayList<String>> minimal = HOLYFUCKMAP[ Nigel.playerID ];

		ArrayList<String> bestForNigel = minimal.get( Collections.min( minimal.keySet() ) );

		for( int i=0; i< bestForNigel.size(); i++ ){
			String targetWall = bestForNigel.get( i );
			boolean checksOut = true;
			for( int j=0; j<numPlayers; j++ ){
				if( j != Nigel.playerID ){
					Map<Integer, ArrayList<String>> maximum = HOLYFUCKMAP[ j ];
					ArrayList<String> worstForOpponent = maximum.get( Collections.max( maximum.keySet() ) );
					if( !worstForOpponent.contains( targetWall ) )
						checksOut = false;
				}
			}
			if( checksOut )
				return targetWall;
		}
		Random rand = new Random();
		return bestForNigel.get( rand.nextInt( bestForNigel.size() ) );
	}

	public static boolean isPlayerBehindOrInDangerOfLosing( ArrayList< ArrayList< String > > playerPaths ){

		for( int i = 0; i < playerPaths.size(); i++ )
			System.out.println( playerPaths.get(i));
		
		
		int playerPathLength = playerPaths.get( Nigel.playerID ).size();

		for( int index=0; index<playerPaths.size(); index++ ){
			if( index != Nigel.playerID ){
				int opponetPathLength = playerPaths.get( index ).size();
				if( opponetPathLength - playerPathLength >= 3 ){
					System.out.println("HERE");
					return true;
				}
				if( opponetPathLength < 4 ){
					System.out.println("THERE");
					return true;
				}
			}
		}
		return false;
	}

	private static ArrayList<String> getPathToEnd( String locationOfPlayer ){
		ArrayList<ArrayList<String>> possiblePaths = new ArrayList<ArrayList<String>>();
		ArrayList<String> seen = new ArrayList<String>();
		//System.out.println( locationOfPlayer );
		possiblePaths.add( new ArrayList<String>() );
		possiblePaths.get( 0 ).add( locationOfPlayer );
		seen.add( locationOfPlayer );

		return getPath( seen, possiblePaths );
	}

	private static ArrayList<String> getPath( ArrayList<String> seen, ArrayList<ArrayList<String>> possiblePaths ){
		if( possiblePaths.size() == 0 )
			return new ArrayList<String> ();
		ArrayList<ArrayList<String>> newPossiblePaths = new ArrayList<ArrayList<String>>();
		for( int pathIndex=0; pathIndex<possiblePaths.size(); pathIndex++ ){			//Go through all possible paths so far
			ArrayList<String> list = possiblePaths.get( pathIndex );   // for every list, take the end button and find its 4 directions. If one of the directions has already been seen,  ignore it. Else add a new path with it attached at the end

			String current = list.get( list.size()-1 );
			System.out.println( current + " : CURRENT from " + list);
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
						//System.out.println( "ABORT" );
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
									//System.out.println( "found path " + newList );
									return newList;
								}


							newPossiblePaths.add( newList );	//add new list to the new possible paths
						}
					}
				}
			}
		}
		possiblePaths = null;
		//System.out.println( newPossiblePaths );
		return getPath( seen, newPossiblePaths );
	}

	private static ArrayList<String> calculateJumps( ArrayList<String> seen, int currY, int currX, ArrayList<String> foundSpots ){
		//System.out.println( "Point: " + currY + currX );
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
					//System.out.println( "ABORT" );
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



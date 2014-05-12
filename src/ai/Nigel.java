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
	private static int playerTurnNum = 1;
	public static final String AI_IDENTIFIER = "tactical";

	
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
		this.startPos.put(1, "e1"); this.startPos.put(2, "e9");
		this.startPos.put(3, "a5"); this.startPos.put(4, "i5");
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
		
		/* Coordinate System for walls in correspondance to the player
		 * 
		 * 				| h(x, y-1)	|
		 * 			  -----------------
		 * 				|			|
		 * 				|			|
		 *	  v(x-1, y)	|	p(x,y)	| v(x, y)
		 * 				|			|
		 * 				|			|
		 * 			  -----------------
		 * 				|  h(x, y)	|
		 */
		
		
		vWallMatrix = new boolean [][] // 8x9
				{  { true, true, true, true, true, true, true, true }, 
				   { true, true, true, true, true, true, true, true }, 
				   { true, true, true, true, true, true, true, true }, 
				   { true, true, true, true, true, true, true, true }, 
				   { true, true, true, true, true, true, true, true }, 
				   { true, true, true, true, true, true, true, true }, 
				   { true, true, true, true, true, true, true, true }, 
				   { true, true, true, true, true, true, true, true }, 
				   { true, true, true, true, true, true, true, true } };
	
		hWallMatrix = new boolean [][] // 9x8
				{  { true, true, true, true, true, true, true, true, true }, 
				   { true, true, true, true, true, true, true, true, true }, 
				   { true, true, true, true, true, true, true, true, true }, 
				   { true, true, true, true, true, true, true, true, true }, 
				   { true, true, true, true, true, true, true, true, true }, 
				   { true, true, true, true, true, true, true, true, true }, 
				   { true, true, true, true, true, true, true, true, true }, 
				   { true, true, true, true, true, true, true, true, true } };
	}
	
	
	private ArrayList<String> getPathToVictim( int x, int y ){
		ArrayList<ArrayList<String>> possiblePaths = new ArrayList<ArrayList<String>>();
		ArrayList<String> seen = new ArrayList<String>();

		possiblePaths.add( new ArrayList<String>() );
		possiblePaths.get( 0 ).add( "" + x + y );
		seen.add( "" + x + y );

		return getPath( seen, possiblePaths );
	}

	private ArrayList<String> getPath( ArrayList<String> seen, ArrayList<ArrayList<String>> possiblePaths ){

		ArrayList<ArrayList<String>> newPossiblePaths = new ArrayList<ArrayList<String>>();
		//Go through all possible paths so far
		for( int i=0; i<possiblePaths.size(); i++ ){
			ArrayList<String> list = possiblePaths.get( i );
			// for every list, take the end button and find its 4 directions. If one of the directions has already been seen,  ignore it. Else add a new path with it attached at the end
			String current = list.get( list.size()-1 );
			String next = null;
			int currX = Integer.parseInt( "" + current.charAt( 0 ) );
			int currY = Integer.parseInt( "" + current.charAt( 1 ) );
			
			for( int k=0; k<4; k++ ){
				int nextX, nextY;
				if	   ( k==0 )	{ nextX=currX+1; nextY=currY; } //k==0 is left
				else if( k==1 )	{ nextX=currX-1; nextY=currY; }	//k==1 is right
				else if( k==2 )	{ nextX=currX; nextY=currY+1; }	//k==2 is down
				else{ 			  nextX=currX; nextY=currY-1; }	//k==3 is up

				try{
					boolean winning = true;
					next = "" + nextX + nextY;
					
					if( !seen.contains( next ) ){
						seen.add( next );
						ArrayList<String> newList = new ArrayList<String>();
						for( int j=0; j<list.size(); j++ )
							newList.add( list.get( j ) );
						//add next to the new copy
						newList.add( next );
								if( next.getPlayerColor() == list.get( 0 ).getOpponetColor() )
									return newList;
							}
							//add new list to the new possible paths
							newPossiblePaths.add( newList );
						}
					}
				} catch( Exception e ){}
			}
		}
//		System.out.println( newPossiblePaths );																					//DEBUG
		possiblePaths = null;
		return getPath( seen, newPossiblePaths );
	}
	
	*/
	
	
	
	
	
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
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

	
	
	
	
	
	
	
	

}

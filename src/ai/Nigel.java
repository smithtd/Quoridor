package ai;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import network.Messages;
import network.MoveServer;


public class Nigel extends MoveServer {

	private static int[][] playerMatrix;
	private static boolean[][] wallMatrix;
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
		wallMatrix = new boolean [][] 
				{  { false, false, false, false, false, false, false, false, false }, 
				   { false, false, false, false, false, false, false, false, false }, 
				   { false, false, false, false, false, false, false, false, false }, 
				   { false, false, false, false, false, false, false, false, false }, 
				   { false, false, false, false, false, false, false, false, false }, 
				   { false, false, false, false, false, false, false, false, false }, 
				   { false, false, false, false, false, false, false, false, false }, 
				   { false, false, false, false, false, false, false, false, false }, 
				   { false, false, false, false, false, false, false, false, false } };
	}
	
	/*
	private ArrayList<GameButton> getPathToVictim( int x, int y ){
		PlayerButton button = buttonMatrix[ x ][ y ];
		ArrayList<ArrayList<GameButton>> possiblePaths = new ArrayList<ArrayList<GameButton>>();
		ArrayList<GameButton> seen = new ArrayList<GameButton>();

		possiblePaths.add( new ArrayList<GameButton>() );
		possiblePaths.get( 0 ).add( button );
		seen.add( button );

		return getPath( seen, possiblePaths );
	}

	private ArrayList<GameButton> getPath( ArrayList<GameButton> seen, ArrayList<ArrayList<GameButton>> possiblePaths ){
		if( possiblePaths.size() == 0 ){
			int x = seen.get( 0 ).x();
			int y = seen.get( 0 ).y();
			chances[ x ][ y ] = 0;
			distances[ x ][ y ] = 0;
			return null;
		}
		ArrayList<ArrayList<GameButton>> newPossiblePaths = new ArrayList<ArrayList<GameButton>>();
		//Go through all possible paths so far
		for( int i=0; i<possiblePaths.size(); i++ ){
			ArrayList<GameButton> list = possiblePaths.get( i );
//			 *
//			 * for every list, take the end button and find its 4 directions
//			 * 
//			 * if one of the directions has already been seen,  ignore it. 
//			 * else add a new path with it attached at the end
//			 *
			GameButton current = list.get( list.size()-1 );
			GameButton next = null;

			for( int k=0; k<4; k++ ){
				int x, y;
				if( k==0 ){ 	 x=current.x()+1; y=current.y(); }
				else if( k==1 ){ x=current.x()-1; y=current.y(); }
				else if( k==2 ){ x=current.x(); y=current.y()+1; }
				else{ 			 x=current.x(); y=current.y()-1; }

				try{
					boolean winning = true;
					next = buttonMatrix[ x ][ y ];
					// if next isnt black and isnt the color of start node
					if( ( next.getPlayerColor() != Color.BLACK ) && ( next.getPlayerColor()!=list.get( 0 ).getPlayerColor()) ){
						//and not in already seen
						if( !seen.contains( next ) ){
							seen.add( next );
							if( next.getPlayerColor() == Color.BLUE ) 
								if ( ( next.getVisibility() == 3 ) && ( Battle.getResult( list.get( 0 ), next ).equals( "BLUE" ) ) ){
									winning = false;
//									System.out.println( "Lost obvious battle " + k );													//DEBUG
								}
							//make copy of current list
							ArrayList<GameButton> newList = new ArrayList<GameButton>();
							for( int j=0; j<list.size(); j++ )
								newList.add( list.get( j ) );
							//add next to the new copy
							if( winning ){
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

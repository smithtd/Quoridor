/**
 * @author marc
 * Artificial Intelligence for Quoridor - An attempt
 */
package ai;

import network.MoveServer;

public class Artie extends MoveServer {
	
	/** AI-Identifier -- Team Decided */
	public static final String AI_IDENTIFIER = "tactical";
	
	// Player representations
	public static final char PLAYER_NORTH = 1;
	public static final char PLAYER_SOUTH = 2; 
	public static final char PLAYER_WEST = 3;
	public static final char PLAYER_EAST = 4;
	
	/** Board Representations */
	char[][] board;
	// Do we need to keep two??
	char[][] tempBoard;
	/** Number of walls we have */
	int walls;
	
	public Artie (int port) {
		super(port);
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
		Artie aiArtie = new Artie(port);
	
		aiArtie.run();
	}
	
	
	

}

/**
 * @author marc
 * Artificial Intelligence for Quoridor - An attempt
 */
package ai;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import network.Messages;
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
	
	/**
	 * Create an instance of our AI
	 * @param - Port to listen on 
	 * 
	 */
	public Artie (int port) {
		super(port);
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
		
		// Remove this when AI is making its own decisions
		this.playerInput = new Scanner(System.in);
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
			System.out.println("Connection Terminated or Error in Artie");
		}
		
		this.playerInput.close();
		
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

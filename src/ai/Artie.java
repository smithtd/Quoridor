/**
 * @author marc
 * Artificial Intelligence for Quoridor - An attempt
 */
package ai;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import network.Messages;
import network.MoveServer;

public class Artie extends MoveServer {

	/** AI-Identifier -- Team Decided */
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

	/**
	 * Create an instance of our AI
	 * @param - Port to listen on 
	 * 
	 */
	public Artie (int port) {
		super(port);
		
		this.startPos = new HashMap<Integer, String>();
		this.startPos.put(1, "e1"); this.startPos.put(2, "e9");
		this.startPos.put(3, "a5"); this.startPos.put(4, "i5");
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
		
		Artie aiArtie = new Artie(port);

		aiArtie.run();
	}


	/**
	 * Handles the messages that should be seen from the game client
	 * and makes them easy to understand for the player. 
	 * @param input - Message from the GameClient. 
	 * @throws InterruptedException 
	 */
	public void getResponse(String input) throws InterruptedException {
		Thread.sleep(250); // try to watch what is happening

		if(input.equalsIgnoreCase(Messages.ASK_FOR_MOVE)){
			String move = getMove();
			this.currentPosition = move;

			this.clientOutput.println("MOVE " + move);

		} else if(input.startsWith(Messages.TELL_MOVE)){
			Scanner info = new Scanner(input);
			info.next();
			System.out.println("Player " + info.next() + " made move: " + info.next());
			info.close();
		} else if(input.startsWith(Messages.START_GAME)){
			System.out.println( input );///////////////////////////////////////////////////////////////////////////////////////
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

	private String getMove()  { 

		String curPos = this.currentPosition;
		Random rand = new Random();
		int whichWay = rand.nextInt(2);
		if(whichWay == 0) { 
			// we are going to go forward
			if(this.startPos.get(this.playerId).equals("e9")) {
				curPos = curPos.charAt(0) + "" + (Integer.parseInt(curPos.substring(1)) - 1);
				return curPos;
			} else { 
				curPos = curPos.charAt(0) + "" + (Integer.parseInt(curPos.substring(1)) + 1);
				return curPos;
			}

		} else {
			// we are going to go side to side
			int leftOrRight = rand.nextInt(2);
			// check that we wouldn't be going out of bounds
			if(curPos.startsWith("a"))
				return "b" + curPos.charAt(1);
			else if(curPos.startsWith("i")) 
				return "h" + curPos.charAt(1);
			else {
				if(leftOrRight == 0)
					leftOrRight--;
				return (char)(curPos.charAt(0) + leftOrRight) + "" + curPos.charAt(1);
			}
		}
	}
}

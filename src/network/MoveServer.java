
/**
 * MoveServer - Quoridor
 * Team 511-Tactical
 * @author marc
 */
package network;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class MoveServer {

	/** Port that this server will listen on */
	private int port;
	/** get input from the game client */
	private Scanner clientInput;
	/** Send moves/placements to the game client */
	private PrintStream clientOutput;
	/** AI-Identifier or name */
	private String identifier;
	/** Player ID number */
	private int playerId;

	/**
	 * Construct an instance of a MoveServer
	 * @param pNum - Port Number for the server to listen on.
	 */
	public MoveServer(int pNum) {
		this.port = pNum;
	}

	public void run() {

		// Protocol says first message after connecting should
		// be HELLO <ai-identifier>
		System.out.print("Name or AI-Identifier >> ");
		Scanner nameGetter = new Scanner(System.in);
		this.identifier = nameGetter.next();
		nameGetter.close();

		System.out.println("Starting a move server");

		try {
			// Listening socket on a certain port
			ServerSocket server = new ServerSocket(this.port);

			System.out.println("Waiting for a client...");
			Socket gameClient;

			while((gameClient = server.accept()) != null){

				server.close();
				System.out.println("Now connected to client at: " + gameClient);

				// Send output back to game client
				this.clientOutput = new PrintStream(gameClient.getOutputStream());

				System.out.println("Sending HELLO <ai-identifier> message to client");
				this.clientOutput.println(Messages.HELLO_MESSAGE + " " + this.identifier);

				// Read input that comes in from the game client
				this.clientInput = new Scanner(gameClient.getInputStream());

				while(this.clientInput.hasNext()){
					String input = this.clientInput.nextLine();
					getResponse(input);
				}


			}

		} catch (IOException e) {
			System.out.println("Connection Terminated");
		}

	}

	/**
	 * 
	 * @param input - Message from the GameClient. 
	 */
	private void getResponse(String input) {

		if(input.equalsIgnoreCase(Messages.ASK_FOR_MOVE)){
			String move = getMove();
			this.clientOutput.println(move);

		} else if(input.startsWith(Messages.TELL_MOVE)){
			Scanner info = new Scanner(input);
			info.next();
			System.out.println("Player " + info.next() + " made move: " + info.next());
			info.close();
		} else if(input.startsWith(Messages.START_GAME)){
			Scanner getID = new Scanner(input);
			getID.next();
			this.playerId = Integer.parseInt(getID.next());
			this.clientOutput.println(Messages.READY + " " + this.playerId);
			System.out.println("Playing as player # " + this.playerId);
			getID.close();
		} else if(input.startsWith(Messages.REMOVED)){
			Scanner removed = new Scanner(input);
			removed.next();
			int player = Integer.parseInt(removed.next());
			if(player == this.playerId) {
				System.out.println("You have been removed for illegal moves.");
				System.exit(0);
			} else {
				System.out.println("Player " + player + " has been removed");
			}
			removed.close();
		} else if(input.startsWith(Messages.WINNER)) {
			Scanner win = new Scanner(input);
			win.next();
			int player = Integer.parseInt(win.next());
			win.close();
			if(player == this.playerId){
				System.out.println("You won!");
				System.exit(0);
			} else {
				System.out.println("Player " + player + " won! You lose!");
				System.exit(0);
			}
		}


	}

	private String getMove() {

		System.out.print("Enter a move/wall placement >> ");
		Scanner moveGet = new Scanner(System.in);
		while(true) {
			String move = moveGet.next();
			moveGet.close();
			return move;
		}

	}


	/**
	 * Handle Command Line arguments, start a server and 
	 * wait for the connection.
	 * @param args
	 */
	public static void main(String[] args) {

		// Should have a port number as a command line argument
		if(args.length != 1)
			usage();

		int port = Integer.parseInt(args[0]);
		MoveServer m = new MoveServer(port);

		m.run();

	}

	/**
	 * Tell the user the correct parameters to be used to start this program 
	 * then quit.
	 */
	private static void usage() {
		System.out.println("\tTo start MoveServer : \n \n\t$ java MoveServer <portNumber>");
		System.exit(0);
	}
}


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
	Scanner clientInput;
	/** Send moves/placements to the gameclient */
	PrintStream clientOutput;
	
	/**
	 * Construct an instance of a MoveServer
	 * @param pNum - Port Number for the server to listen on.
	 */
	public MoveServer(int pNum) {
		this.port = pNum;
	}
	
	public void run() {
		System.out.println("Starting a move server");
		
		try {
			// Listening socket on a certain port
			ServerSocket server = new ServerSocket(this.port);
			
			System.out.println("Waiting for a client...");
			Socket gameClient;
			
			while((gameClient = server.accept()) != null){
				
				server.close();
				System.out.println("Now connected to client at: " + gameClient);
				
				// Read input that comes in from the game client
				this.clientInput = new Scanner(gameClient.getInputStream());
				// Send output back
				this.clientOutput = new PrintStream(gameClient.getOutputStream());
				
				while(this.clientInput.hasNext()){
					String input = this.clientInput.nextLine();
					getResponse(input);
				}
				
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void getResponse(String input) {
		
		
	}
	
	/**
	 * Handle Command Line arguments, start a server and 
	 * wait for the connection.
	 * @param args
	 */
	public static void main(String[] args) {
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
		System.out.println("$ java MoveServer <portNumber>");
		System.exit(0);
	}
}

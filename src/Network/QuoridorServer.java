/**
 * Quoridor Network Server
 * @author Marc Dean
 * Team - 511 Tactical
 */



package Network;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;



/**
 * 
 * The QuoridorServer is used for the networking portion
 * of the Quoridor game. Each player will have an instance of
 * this server where they will be able to input moves/walls/get kicked. 
 * 
 * 
 * How to use this: Netcat provides an easy to use tool to generate a server 
 * to connect to it.
 * 
 * In the terminal on the lab machines say: nc -l 3939
 * This starts netcat and allows you to run the tests I have written
 *
 */

/**
 *  LOG :
 * 	TODO 1: setup connection - works with netcat
 *  TODO 2: Take input
 *  TODO 3: Make a parser to decode input for moves/wall placements
 *  TODO 4: Allow GUI to send input to this input mechanism so players
 *     can click on spaces rather than type in their moves.
 * 
 */

public class QuoridorServer   {
	
	/** Port number of distant machine */
	private int portNumber;
	
	/** Socket to connect to the client */
	private ServerSocket socket;
	
	/** Scanner for input */
	private Scanner movementInput;

	
	/**
	 * 
	 * @param port - port number where communication can be made at server
	 * 
	 * 
	 */
	public QuoridorServer(int port) {
		
		this.portNumber = port;
		this.movementInput = new Scanner("System.in");
		
	    try {
			this.socket = new ServerSocket(this.portNumber);
			this.socket.accept();
	    } catch (IOException ioe) {
	    		ioe.printStackTrace();
	    }
	
	}
	
		
	/**
	 * @return the next move/placement in the queue
	 */
	public String getMove() {			
			System.out.print("\nMove:>");
			String move = this.movementInput.next().trim();
			return move;
	}
}

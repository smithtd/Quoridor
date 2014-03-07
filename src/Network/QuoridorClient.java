/**
 * Quoridor Network Client
 * @author Marc Dean
 * Team - 511 Tactical
 */



package Network;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;



/**
 * 
 * The QuoridorClient is used for the networking portion
 * of the Quoridor game. Each player will have an instance of
 * this client where they will be able to input moves. 
 *
 */

/**
 *  LOG :
 * 	TODO 1: setup connection
 *  TODO 2: Take input
 *  TODO 3: Make a parser to decode input for moves/wall placements
 *  TODO 4: Allow GUI to send input to this input mechanism so players
 *     can click on spaces rather than type in their moves.
 * 
 */

public class QuoridorClient extends Thread  {
	
	/** Store input from player/ai */
	private BlockingQueue<String> moveQueue;
	
	/** Machine Name where server is running */
	private String serverMachineName;
	
	/** Port number of distant machine */
	private int portNumber;
	
	/** Socket to connect to the server */
	private Socket socket;
	
	/**
	 * 
	 * @param name - name of distant machine where server is running
	 * @param port - port number where communication can be made at server
	 * 
	 * Creates a new instance of the QuoridorClient with the input stream, 
	 * name, and port number to connect to.
	 */
	public QuoridorClient(String name, int port) {
		this.moveQueue = new LinkedBlockingQueue<String>();
		this.serverMachineName = name;
		this.portNumber = port;
	    try {
			this.socket = new Socket(this.serverMachineName, this.portNumber);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	
	/**
	 * 
	 * @param move - move to add to the queue of moves
	 * @throws InterruptedException 
	 */
	public void addMove(String move) {
		// TODO check if move is valid, but when?
			try {
				this.moveQueue.put(move);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	/**
	 * 
	 * @return the next move/placement in the queue
	 * 
	 * Assumes the queue has a move waiting
	 * @throws InterruptedException 
	 * 
	 */
	public String getMove() {
		
			/**
			 *  take(), according to Java will wait until an element
			 *  is available, so we shouldn't have to handle cases
			 *  where the user is stalling on entering their move
			 *  or making decisions for some strategy.
			 *  
			 *  We could implement some sort of timeout feature by using
			 *  poll()
			 */
			try {
				return this.moveQueue.take();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;	
	}
	
	
}

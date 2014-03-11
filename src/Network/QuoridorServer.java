/**
 * Quoridor Network Server
 * @author Marc Dean
 * Team - 511 Tactical
 */



package Network;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;



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

public class QuoridorServer extends Thread  {
	
	/** Store input from player/ai */
	private BlockingQueue<String> moveQueue;
	
	/** Machine Name where client is running */
	private String serverMachineName;
	
	/** Port number of distant machine */
	private int portNumber;
	
	/** Socket to connect to the client */
	private Socket socket;
	
	/**
	 * 
	 * @param name - name of distant machine where server is running
	 * @param port - port number where communication can be made at server
	 * 
	 * Creates a new instance of the QuoridorClient with the input stream, 
	 * name, and port number to connect to.
	 */
	public QuoridorServer(String name, int port) {
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
				e.printStackTrace();
			}
	}
	
	/**
	 * 
	 * @return the next move/placement in the queue
	 *
	 * If there is nothing waiting in the queue when called it will automatically
	 * prompt for a move
	 * @throws InterruptedException 
	 * 
	 */
	public String getMove() throws InterruptedException  {
		
			/**
			 *  take(), according to Java will wait until an element
			 *  is available, so we shouldn't have to handle cases
			 *  where the user is stalling on entering their move
			 *  or making decisions for some strategy.
			 *  
			 *  We could implement some sort of timeout feature by using
			 *  poll()
			 */
		if(this.moveQueue.peek() == null){
			Scanner sc = new Scanner(System.in);
			System.out.print("\nMove:>");
			this.addMove(sc.nextLine());
		}
		
		return this.moveQueue.take();
	}
	
	public void run() {
		
	}
	
	
}

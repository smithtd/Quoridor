/**
 * Quoridor Networking Client
 * @author Marc Dean Jr.
 * Team - 511Tactical
 */
package Network;

import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;



/**
 * The client when started, will determine how many people to look for 
 * for this specific instance of the game. This is where the game will spin
 * up and allow servers to connect to it. It will then ask each person for a
 * name and their moves, and process the information.
 */


/**
 *	Log: keep an array of players(servers??? sockets??) to process moves
 *		 wait for all connections from all servers
 *		 Initiate game, start requesting moves/wall placements 
 *
 */

public class QuoridorClient {
	
	
	/** Store each socket that will be communicating with the client */
	public static ArrayBlockingQueue<Socket> channels;
	
	/** Max # of players **/
	public static final int MAX_PLAYERS = 4;
	/** Min # of players **/ 
	public static final int MIN_PLAYERS = 2;
	
	
	public QuoridorClient(int players) {
		
		channels = new ArrayBlockingQueue<Socket>(players);
		
	}
	
	/** Handle CLA's, make a new client, start the game asking 
	 *  person to make their moves, handle legal moves, booting,
	 *  etc...
	 * @param args - <hostName:portNumber> X 2 || 4 (Hopefully)
	 */
	public static void main(String[] args) {
		
		int arguments = args.length;
		
		System.out.println(args.length);
		
		if(!(arguments == MAX_PLAYERS || arguments == MIN_PLAYERS)) {

			System.err.println("Illegal # of players instantiated");
			System.exit(1);
			
		}
		
		/** Make the connections with the servers */
		makeConnections(args);
	
		
	}
	
	/** 
	 * 
	 * @param args
	 */
	private static void makeConnections(String[] args) {
		
		/** Connect to the socket and store them into the channels queue */
		
		for(int i = 0; i < args.length; i++) {
			
			String host = args[i].substring(0, (args[i].indexOf(':')));
			int portNumber = Integer.parseInt((args[i]).substring(args[i].indexOf(':') + 1));
			
			System.out.println(host + " " + portNumber);
			
		}
		
	}
	
	
	
	

}

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
 *	Log: keep an array of players(servers???) to process moves
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
		
		if(players != MAX_PLAYERS || players != MIN_PLAYERS) {
			System.err.println("Illegal # of players instantiated");
			System.exit(1);
		}
		
		channels = new ArrayBlockingQueue<Socket>(players);
		
	}
	
	
	

}

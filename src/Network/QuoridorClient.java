/**
 * Quoridor Network Client
 * @author Marc Dean
 * Team - 511 Tactical
 */



package Network;

import java.io.BufferedInputStream;
import java.io.InputStream;



/**
 * 
 * The QuoridorClient is used for the networking portion
 * of the Quoridor game. Each player will have an instance of
 * this client where they will be able to input moves. 
 *
 */

/**
 *  LOG :
 * 	1: setup connection
 *  2: Take input
 *  3: Make a parser to decode input for moves/wall placements
 *  4: Allow GUI to send input to this input mechanism so players
 *     can click on spaces rather than type in their moves.
 * 
 */

public class QuoridorClient {
	
	/** Store input from player/ai */
	private BufferedInputStream inputFromPlayer;
	
	/** Machine Name where server is running */
	private String serverMachineName;
	
	/** Port number of distant machine */
	private int portNumber;
	
	/**
	 * 
	 * @param in - Inputstream where text is coming from
	 * @param name - name of distant machine where server is running
	 * @param port - port number where communication can be made at server
	 * 
	 * Creates a new instance of the QuoridorClient with the input stream, 
	 * name, and port number to connect to.
	 */
	public QuoridorClient(InputStream in, String name, int port) {
		this.inputFromPlayer = new BufferedInputStream(in);
		this.serverMachineName = name;
		this.portNumber = port;
	}
	
	
	
	

}

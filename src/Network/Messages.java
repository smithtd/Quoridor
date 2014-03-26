/**
 * 	Quoridor Networking Client
 * @author Marc Dean Jr.
 * Team - 511 Tactical
 * 
 */



package network;

/** 
 * The Messages interface will allow shared messaging guidelines for the 
 * client/server. If we ever encounter an error in understanding the protocol
 * or the protocol is changed then we will be able to change the communication
 * methods quickly. 
 *
 */

public interface Messages {

	/** Hello Message takes the form of HELLO <ai-identifier>*/
	public final String HELLO_MESSAGE = "HELLO";

	/** To start the game - sent to move-servers to start game */
	/** QUORIDOR <player-id> <ai-id2> [<ai-id3> <ai-id4>] */
	public final String START_GAME = "QUORIDOR";
	
	/** Ready message - READY <display-name> */
	public final String READY = "READY";
	
	/** Ask for move - Move? */
	public final String ASK_FOR_MOVE = "MOVE?";
	
	/** Move returned from server - MOVE <move-string> */
	public final String MOVE = "MOVE";
	
	/** Move message to send to the other servers - 
	 * MOVED <player-id> <move-string> 
	 */
	public final String TELL_MOVE = "MOVED";
	
	/** Removal message - REMOVED <player-id> */
	public final String REMOVED = "REMOVED";
	
	/** Winner message - WINNER <player-id> */
	public final String WINNER = "WINNER";
	
}

/**
 * 
 * GameClient - Quoridor
 * Team - 511Tactical
 * @author marc
 * 
 */

package network;

/**
 *
 * The GameClient is the "game player" or a display GUI. The model of
 * starting is that the client starts and receives two machine:port pairs
 * where two (or four) move servers live. It then contacts the servers
 * (giving each its player number and the number of competitors at this
 * time). 
 *
 */
public class GameClient {
	

	
	
	
	public static void main(String[] args) {
		
		if(args.length != 2 || args.length != 4)
			usage();
		
		
	}

	/** 
	 * Prints out the required way to start the gameClient
	 */
	private static void usage() {
		System.out.println("\n\tTo start a game: \n\t$ java GameClient <ipp1> " +
				"<ipp2> [<ipp3> <ipp4>]\n\n\tIPP = machine:port of players. " +
				"Must have 2 or 4.\n");
	}
	
	

}

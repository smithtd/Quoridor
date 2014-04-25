/**
 * 
 * GameClient - Quoridor
 * Team - 511Tactical
 * @author marc
 * 
 */

package network;

import parser.Parser;

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
	

	/** Hold the information of the player */
	private Portal[] players;
	
	public GameClient(String[] args){
		// The number of portals(connections) should equal the number
		// of command line arguments.
		this.players = new Portal[args.length];
		String[] p;
		for(int i = 0; i < args.length; i++){
			p = args[i].split(":");
			this.players[i] = new Portal(p[0], Integer.parseInt(p[1]));
		}
		
	}
	
	
	public void run() {
		// Send the initial QUORIDOR message to each move server
		for(int i = 0; i < players.length; i++) {
			players[i].sendMessage(Messages.START_GAME + " " + (i+1) + " " + players[players.length/(i+1)].getAIIdentifier());
		}
		
	}
	
	/**
	 * Handle command line parameters, set up an instance of the GameClient
	 * start the game
	 * @param args - IPP Pairs.. Hopefully.
	 */
	public static void main(String[] args) {
		
		if(args.length != 2 || args.length != 4)
			usage();	// This exits the program
		
		if(testArgs(args)) {
			GameClient gameClient = new GameClient(args);
			gameClient.run();
		} 	else
			usage();	// This exits the program.
			
		
		
		
		
	}
	
	/**
	 * Test if the strings given from command line are valid
	 * IPP Pairs.
	 * 
	 * @param args - IPP Pairs from the command line. 
	 * 
	 * @return - True if the Command line values are IPP Pairs.
	 */
	private static boolean testArgs(String[] args) {
		
		Parser p = new Parser();
		
		for(int i = 0; i < args.length; i++) {
			if(!p.isPair(args[i]))
				return false;
		}
		
		
		return true;
	}

	/** 
	 * Prints out the required way to start the gameClient
	 */
	private static void usage() {
		System.out.println("\n\tTo start a game: \n\t$ java GameClient <ipp1> " +
				"<ipp2> [<ipp3> <ipp4>]\n\n\tIPP = machine:port of players. " +
				"Must have 2 or 4.\n");
		System.exit(0);
	}
	
	

}

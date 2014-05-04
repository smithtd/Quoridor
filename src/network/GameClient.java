/**
 * 
 * GameClient - Quoridor
 * Team - 511Tactical
 * @author marc
 * 
 */

package network;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import main.Game;
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
	//	private Portal[] players;
	private List<Portal> players;
	//private int[] pId;
	private List<Integer> pId;

	/** Access the correct player for moves, etc */
	private int turnNumber;

	private Game game;

	public GameClient(String[] args){
		// The number of portals(connections) should equal the number
		// of command line arguments.
		this.players = new ArrayList<Portal>(args.length);
		String[] p;
		for(int i = 0; i < args.length; i++){
			p = args[i].split(":");
			this.players.add(i, new Portal(p[0], Integer.parseInt(p[1])));
		}
		this.turnNumber = 0;
		this.pId =  new ArrayList<Integer>(args.length);


	}



	public void start() {
		// Send the initial QUORIDOR message to each move server
		this.game = new Game(this.players.size(), this);

		if(this.players.size() == 2) {
			this.players.get(0).sendMessage(Messages.START_GAME + " " + 1 + " " + players.get(1).getAIIdentifier());
			this.pId.add(0, 1);

			players.get(1).sendMessage(Messages.START_GAME + " " + 2 + " " + players.get(0).getAIIdentifier());
			this.pId.add(1,2);
		} else {

			players.get(0).sendMessage(Messages.START_GAME + " " + 1 + " " + players.get(1).getAIIdentifier() 
					+ " " + players.get(2).getAIIdentifier() + " " + players.get(3).getAIIdentifier());
			this.pId.add(0,1);
			players.get(1).sendMessage(Messages.START_GAME + " " + 4 + " " + players.get(2).getAIIdentifier() 
					+ " " + players.get(3).getAIIdentifier() + " " + players.get(0).getAIIdentifier());
			this.pId.add(1,4);
			players.get(2).sendMessage(Messages.START_GAME + " " + 3 + " " + players.get(3).getAIIdentifier() 
					+ " " + players.get(0).getAIIdentifier() + " " + players.get(1).getAIIdentifier());
			this.pId.add(2,3);
			players.get(3).sendMessage(Messages.START_GAME + " " + 2 + " " + players.get(0).getAIIdentifier() 
					+ " " + players.get(1).getAIIdentifier() + " " + players.get(2).getAIIdentifier());
			this.pId.add(3,2);
		}

		// get the messages back from everyone
		for(int i = 0; i < this.players.size(); i++) 
			System.out.println(this.players.get(i).getMessage());

		// Start the game
		this.game.startGame();

		// Parser for the moves, walls
		Parser p = new Parser(); 
		game.playGame(p);

		game.notifyObservers(game, Game.getBoard());

		terminate();


	}

	/** 
	 * Terminate all portals.
	 */
	private void terminate() {
		for(int i = 0; i < this.players.size(); i++) {
			this.players.get(i).endSession();
		}
	}

	/**
	 * Handle command line parameters, set up an instance of the GameClient
	 * start the game
	 * @param args - IPP Pairs.. Hopefully.
	 */
	public static void main(String[] args) {

		System.out.println(args.length);

		if(!(args.length == 2 || args.length == 4))
			usage();	// This exits the program

		if(testArgs(args)) {
			GameClient gameClient = new GameClient(args);
			gameClient.start();
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

	/**
	 * Get a move from a specific portal
	 */
	public String getMove() {

		while(!this.players.get(this.turnNumber).inGame())
			this.turnNumber = (this.turnNumber+1) % this.players.size();

		this.players.get(this.turnNumber).sendMessage(Messages.ASK_FOR_MOVE);
		int temp = this.turnNumber;
		this.turnNumber++; this.turnNumber = this.turnNumber % this.players.size();
		String move = this.players.get(temp).getMessage();
		Scanner sc = new Scanner(move);
		sc.next(); 
		String mv =  sc.next();

		String notify = Messages.TELL_MOVE + " " + this.pId.get(temp) + " " + mv;
		this.sendAll(notify);
		sc.close();

		return mv;

	}

	/** 
	 * Sends a message to everyone that is currently playing
	 * @param s - Message to send.
	 */
	private void sendAll(String s) { 
		System.out.println("In sendAll");
		for(int i = 0; i < this.players.size(); i++) {
			if(this.players.get(i).inGame()){
				this.players.get(i).sendMessage(s);

			}
		}

	}

	/**
	 * Find the last person who went, and kick them.
	 */
	public void kickLastPlayer() {
		// find the last player
		System.out.println("Here I am");
		int temp = this.turnNumber;
		do {
			// reverse the player list
			if(temp == 0) 
				temp = this.players.size() - 1;
			else
				temp = temp - 1;
		} while(!this.players.get(temp).inGame());
		this.sendAll(Messages.REMOVED + " " + this.players.get(temp).getAIIdentifier());
		this.players.get(temp).bootPlayer();
		this.players.get(temp).endSession();
		this.players.remove(temp);
		this.pId.remove(temp);

	}

	/** tells how many people are in the current game
	 * 
	 * @return Number of players.
	 */
	public int numOfPlayers() {
		return this.players.size();
	}

}

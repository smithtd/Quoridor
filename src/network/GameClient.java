/**
 * 
 * GameClient - Quoridor
 * Team - 511Tactical
 * @author marc
 * 
 */

package network;

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
	private Portal[] players;

	/** Access the correct player for moves, etc */
	private int turnNumber;

	private Game game;

	public GameClient(String[] args){
		// The number of portals(connections) should equal the number
		// of command line arguments.
		this.players = new Portal[args.length];
		String[] p;
		for(int i = 0; i < args.length; i++){
			p = args[i].split(":");
			this.players[i] = new Portal(p[0], Integer.parseInt(p[1]));
		}
		this.turnNumber = 0;
		

	}



	public void start() {
		// Send the initial QUORIDOR message to each move server
		this.game = new Game(this.players.length, this);
		
		if(players.length == 2) {
			players[0].sendMessage(Messages.START_GAME + " " + 1 + " " + players[1].getAIIdentifier());

			players[1].sendMessage(Messages.START_GAME + " " + 2 + " " + players[0].getAIIdentifier());

		} else {

			players[0].sendMessage(Messages.START_GAME + " " + 1 + " " + players[1].getAIIdentifier() 
					+ " " + players[2].getAIIdentifier() + " " + players[3].getAIIdentifier());

			players[1].sendMessage(Messages.START_GAME + " " + 4 + " " + players[2].getAIIdentifier() 
					+ " " + players[3].getAIIdentifier() + " " + players[0].getAIIdentifier());

			players[2].sendMessage(Messages.START_GAME + " " + 3 + " " + players[3].getAIIdentifier() 
					+ " " + players[0].getAIIdentifier() + " " + players[1].getAIIdentifier());

			players[3].sendMessage(Messages.START_GAME + " " + 2 + " " + players[0].getAIIdentifier() 
					+ " " + players[1].getAIIdentifier() + " " + players[2].getAIIdentifier());

		}

		// get the messages back from everyone
		for(int i = 0; i < this.players.length; i++) 
			System.out.println(this.players[i].getMessage());

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
		for(int i = 0; i < this.players.length; i++) {
			this.players[i].endSession();
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
		
		while(!this.players[this.turnNumber].inGame())
			this.turnNumber = (this.turnNumber+1) % this.players.length;

		this.players[this.turnNumber].sendMessage(Messages.ASK_FOR_MOVE);
		int temp = this.turnNumber;
		this.turnNumber++; this.turnNumber = this.turnNumber % this.players.length;
		String move = this.players[temp].getMessage();
		Scanner sc = new Scanner(move);
		sc.next(); 
		String mv =  sc.next();

		String notify = Messages.TELL_MOVE + " " + this.players[temp].getAIIdentifier() + " " + mv;
		this.sendAll(notify);

		return mv;

	}

	/** 
	 * Sends a message to everyone that is currently playing
	 * @param s - Message to send.
	 */
	private void sendAll(String s) { 
		System.out.println("In sendAll");
		for(int i = 0; i < this.players.length; i++) {
			if(this.players[i].inGame()){
				this.players[i].sendMessage(s);

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
				temp = this.players.length - 1;
			else
				temp = temp - 1;
		} while(!this.players[temp].inGame());
		this.sendAll(Messages.REMOVED + " " + this.players[temp].getAIIdentifier());
		this.players[temp].bootPlayer();
		this.players[temp].endSession();

	}
	
	/** tells how many people are in the current game
	 * 
	 * @return Number of players.
	 */
	public int numOfPlayers() {
		return this.players.length;
	}

}

/* Author: Eli Donahue
 * This class exists as the "main" program. A new game begins when
 * a Game object is initialized. The Game will handle serve as the
 * client.
 */

package game;

import ui.GameBoard;
import players.Player;
import board.Board;

public class Game {
	
	private static final int NUM_OF_WALLS = 20; // number of walls is fixed
	private static final int MAX_NUMBER_PLAYERS = 4;
	// Instance variables
	Board board;				// Board to track data about board's contents
	Player[] players;			// Player[] to hold players
	int numPlayers;				// number of players
	Player curr; 				// Player or index to track whose turn it is
	int moves;					// Move count
	
	// Methods
	
	// constructor
	public Game(int numPlayers, int numWalls) {
		board = new Board(numPlayers);
		Player[] initPlayer = new Player[MAX_NUMBER_PLAYERS];
		this.numPlayers = numPlayers;
		int wallRations = (NUM_OF_WALLS / this.numPlayers);
		this.players = new Player[numPlayers];
		/*
		 * Initialize Players
		 * Order of players (clockwise from the top of the board)
		 * 1, 4, 2, 3.
		 */
		// player one
		initPlayer[0] = new Player("1",wallRations, "e1");
		// player two
		initPlayer[2] = new Player("2", wallRations, "e9");
		
		if(this.numPlayers == MAX_NUMBER_PLAYERS){
			initPlayer[1] = new Player("4", wallRations, "i5");
			initPlayer[3] = new Player("3", wallRations, "a5");
			this.players = initPlayer;
		} else {
			this.players[0] = initPlayer[0];
			this.players[1] = initPlayer[2];
		}
		
		
		curr = players[0];
		moves = 0;
	}
	
	// start game
	public void startGame(){
		new GameBoard();
	}
	
	// exit game (report errors if any)
	public void quit(){
		System.exit(0);
	}
	
	// client/server communication methods (Dylan)
	
	// report errors
	public static void main(String[] args) {
		Game game = new Game(2, NUM_OF_WALLS);
		game.startGame();
	}

}

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
	
	// Instance variables
	Board board;				// Board to track data about board's contents
	Player[] players;			// Player[] to hold players
	int numPlayers;				// int players
	int numWalls;				// int numWalls
	Player curr; 				// Player or index to track whose turn it is
	int moves;					// Move count
	
	// Methods
	
	// constructor
	public Game(int numPlayers, int numWalls) {
		board = new Board(numPlayers);
		players = new Player[numPlayers];
		// initialize players
		for(int i=0; i<numPlayers; i++)
			players[i] = new Player("Player"+(i+1), (int)(numWalls/numPlayers));
		this.numPlayers = numPlayers;
		this.numWalls = numWalls;
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
		Game game = new Game(2, 10);
		game.startGame();
	}

}

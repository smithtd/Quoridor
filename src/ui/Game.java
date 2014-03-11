/*
 * This class exists as the "main" program. A new game begins when
 * a Game object is initialized. The Game will handle serve as the
 * client.
 */

package ui;

import java.util.Arrays;

import players.Player;
import board.Board;

public class Game {
	
	private static final int NUM_OF_WALLS = 20; // number of walls is fixed
	private static final int MAX_NUMBER_PLAYERS = 4;
	// Instance variables
	Board board;				// Board to track data about board's contents
	public static Player[] players;			// Player[] to hold players
	int numPlayers;				// number of players
	Player curr; 				// Player or index to track whose turn it is
	int moves;					// Move count
	public GameBoard gb;
	
	// constructor
	public Game(int numPlayers, int numWalls) {
		board = new Board(numPlayers);
		this.numPlayers = numPlayers;
		int wallRations = (NUM_OF_WALLS / this.numPlayers);
		Game.players = new Player[numPlayers];
		/*
		 * Initialize Players
		 * Order of players (clockwise from the top of the board)
		 * 1, 4, 2, 3.
		 */

		// players one & two
/*		initPlayer[0] = new Player("1",wallRations, "a5", 1);
		initPlayer[2] = new Player("2", wallRations, "i5", 2);
		
		if(this.numPlayers == MAX_NUMBER_PLAYERS){
			initPlayer[1] = new Player("4", wallRations, "e1", 3);
			initPlayer[3] = new Player("3", wallRations, "e9", 4);
			Game.players = initPlayer;
		} else {
			Game.players[0] = initPlayer[0];
			Game.players[1] = initPlayer[2];
		}
*/
/**/
		Player[] initPlayer = new Player[MAX_NUMBER_PLAYERS];
		
		if(this.numPlayers == MAX_NUMBER_PLAYERS){
			initPlayer = new Player[MAX_NUMBER_PLAYERS];
			initPlayer[0] = new Player("1",wallRations, 0, 4, 1);
			initPlayer[1] = new Player("2", wallRations, 4, 8, 2);
			initPlayer[2] = new Player("3", wallRations, 8, 4, 3);
			initPlayer[3] = new Player("4", wallRations, 4, 0, 4);
		} else {
			initPlayer = new Player[2];
			initPlayer[0] = new Player("1",wallRations, 0, 4, 1);
			initPlayer[1] = new Player("2", wallRations, 8, 4, 2);
		}
		Game.players = initPlayer;

/**/
		System.out.println(Arrays.toString(initPlayer));
		curr = players[0];
		moves = 0;
	}
	
	// start game
	public void startGame(){
		gb = new GameBoard( this );
/*
		for(int i = 1;i<=numPlayers;i++){
			Board.placePawn(players[i-1], players[i-1].getStartx(), players[i-1].getStarty());
		}
*/
		for(int i=0; i<numPlayers; i++){
			Player p = players[i];
			GameBoard.cont.getPlayerButtons()[p.x()][p.y()].setBackground(p.getColor());
		}
		GameBoard.cont.addPlyrAry( players );
		GameBoard.cont.showPlyrMoves();
	}
	
	public void new4PlayerGame(){
		gb.getFrame().dispose();
		Game g = new Game( 4, NUM_OF_WALLS );
		g.startGame();
	}
	
	public void new2PlayerGame(){
		gb.getFrame().dispose();
		Game g = new Game( 2, NUM_OF_WALLS );
		g.startGame();
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

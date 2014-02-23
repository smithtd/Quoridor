/* Author: Eli Donahue
 * This class exists as a data container to track data about the
 * contents of the board and to provide methods for players to 
 * interact with the board.
 */

package board;

import players.Player;
import java.awt.Point;

public class Board {
	
	// Instance variables
	int[][] bitmap;	// tracks whether square is available
	
	// Methods
	// constructor(s)
	public Board() {
		bitmap = new int[9][9];
	}
	
	// if we know number of players, we know where to start pawns
	public Board(int players){
		bitmap = new int[9][9];
		// place pawns depending on number of players
	}
	
	// produce a board based on an existing matrix
	public Board(int[][] map) {
		bitmap = map;
	}
	
	/*
	 * Purpose: check whether a square is occupied
	 * Parameters: x and y coordinates
	 * Preconditions: a method needs to know if a square is empty
	 * Postconditions: report T/F, whether square is empty 
	 * 			returns false if out of bounds since can't place piece
	 */
	public boolean isEmpty(int x, int y) {
		// check xy to make sure in range
		if (x > 8 || y > 8 || x < 0 || y < 0)
			return false;
		
		// return whether square value = 0
		return this.bitmap[x][y] == 0;
	}
	
	/*
	 * Purpose: place a pawn at given coordinates
	 * Parameters: player making move, x and y coordinates
	 * Preconditions: a player wants to move its pawn
	 * Postconditions: if move is valid, set new loc=1 and old loc=0
	 * 		return T/F based on success
	 */
	public boolean placePawn(Player p, int x, int y) {
		// if isLegalMove(pawn, x, y)
			// set bitmap[p.x][p.y] to 0
			// set bitmap[x][y] to 1
			// return true
			return true;
		// else return false
		
	}
	
	/*
	 * Purpose: place a wall of given orientation starting at (x,y)
	 * Parameters: orientation of wall, x and y coordinates
	 * Preconditions: player has decided to place wall
	 * Postconditions: marked bitmap if move was legal, 
	 * 			return T/F based on success
	 */
	public boolean placeWall(String orientation, int x, int y) {
		// if isLegalMove(horizontal/vertical, x, y)
		// set bitmap[x][y] and bitmap[x+1][y] for horizontal
		// set bitmap[x][y] and bitmap[x][y+1] for vertical
		return true;
	}
	
	/*
	 * Purpose: check whether move is legal
	 * Parameters: player, move type, x and y coordinates
	 * Preconditions: player is trying to make move, need to check if legal
	 * Postconditions: return true if ok, else report error and return false
	 */
	public boolean isLegalMove(Player p, String type, int x, int y) {
		// check that it's the player's turn
		// check that (x,y) is on grid
		// check that square(s) not occupied
		// if pawn:
			// make sure not more than one space unless jumping another pawn
		// if wall: 
			// make sure player has enough walls left (p.wallTotal > 0)
			// make sure it doesn't prevent a player from reaching end
			// make sure wall doesn't hang off edge of board
			// make sure wall doesn't intersect another wall
		// report error
		return true;
	}
}

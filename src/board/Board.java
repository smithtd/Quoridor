/* Author: Eli Donahue
 * This class exists as a data container to track data about the
 * contents of the board and to provide methods for players to 
 * interact with the board.
 */

package board;

import java.awt.Color;

import players.Player;
import ui.GameBoard;

public class Board {
	
	// Instance variables
	static int[][] bitmap;	// tracks whether square is available
	
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
	public static boolean isEmpty(int x, int y) {
		// check xy to make sure in range
		if (x > 8 || y > 8 || x < 0 || y < 0)
			return false;
		
		// return whether square value = 0
		return bitmap[x][y] == 0;
	}
	
	/*
	 * Purpose: place a pawn at given coordinates
	 * Parameters: player making move, x and y coordinates
	 * Preconditions: a player wants to move its pawn
	 * Postconditions: if move is valid, set new loc=1 and old loc=0
	 * 		return T/F based on success
	 */
	public static void placePawn(Player p, int x, int y) {
		if(p.getPnum() == 1)
			GameBoard.pbAry[x][y].setBackground(Color.BLUE);
		else if(p.getPnum() == 2)
			GameBoard.pbAry[x][y].setBackground(Color.RED);
		else if(p.getPnum() == 3)
			GameBoard.pbAry[x][y].setBackground(Color.GREEN);
		else if(p.getPnum() == 4)
			GameBoard.pbAry[x][y].setBackground(Color.YELLOW);
	}
	
	/*
	 * Purpose: place a wall of given orientation starting at (x,y)
	 * Parameters: orientation of wall, x and y coordinates
	 * Preconditions: player has decided to place wall
	 * Postconditions: marked bitmap if move was legal, 
	 * 			return T/F based on success
	 */
	public boolean placeWall(Player p, String orientation, int x, int y) {
		// set bitmap[x][y] and bitmap[x+1][y] for horizontal
		if(orientation=="horizontal") {
			// if isLegalMove(horizontal/vertical, x, y)
			if(isLegalMove(p, "horizontal", x, y)){
				bitmap[x][y] = 1;
				bitmap[x+1][y] = 1;
				
				// return true
				return true;
			}
		} else if(orientation=="vertical") {				
			// if isLegalMove(horizontal/vertical, x, y)
			if(isLegalMove(p, "vertical", x, y)){
				// set bitmap[x][y] and bitmap[x][y+1] for vertical
				bitmap[x][y] = 1;
				bitmap[x][y+1] = 1;
				
				// return true
				return true;
			}
		}
		
		// else return false
		return false;
	}
	
	/*
	 * Purpose: check whether move is legal
	 * Parameters: player, move type, x and y coordinates
	 * Preconditions: player is trying to make move, need to check if legal
	 * Postconditions: return true if ok, else report error and return false
	 */
	public static boolean isLegalMove(Player p, String type, int x, int y) {
		// check that type is valid
		if(!(type.equals("horizontal") || type.equals("vertical") || type.equals("pawn")))
			return false;
		
		// check that it's the player's turn
		
		// check that (x,y) is on grid
		if (x > 8 || y > 8 || x < 0 || y < 0)
			return false;
		
		// check that (x,y) not occupied
		if(!isEmpty(x, y))
			return false;
		
		// if type = horizontal
		if(type.equals("horizontal")){
			// check that player has wall to place
			if(p.getWalls()<=0)
				return false;
			// check (x+1, y)
			if(!isEmpty(x+1, y))
				return false;
			// check that x+1 < 9
			if(x+1>8)
				return false;
		}
			
		// if type = vertical
		if(type.equals("vertical")){
			// check that player has wall to place
			if(p.getWalls()<=0)
				return false;
			// check (x, y+1)
			if(!isEmpty(x, y+1))
				return false;
			// check that y+1 < 9
			if(y+1>8)
				return false;
		}
				
		// if type = pawn
			// make sure not more than one space unless jumping another pawn
		
		// if wall: 
			// make sure it doesn't prevent a player from reaching end
		return true;
	}
}

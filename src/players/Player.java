/**
 * 
 */
package players;

import java.awt.Point;
import java.util.Scanner;

/**
 * @author marc dean jr
 *
 */
public class Player implements Players {

		/* Fields */
	
		@SuppressWarnings("unused")
		private String name; 	// players name
		private int wallTotal;	// how many walls player has
		private Scanner sc;		// prompt player for moves
		@SuppressWarnings("unused")
		private Point position; // current position of the player
		
		/* Constructor(s) */
		public Player(String name, int walls) {
			this.name = name;
			this.wallTotal = walls;
			// to get moves
			this.sc = new Scanner("System.in");
			//TODO implement way to find initial position
		}
		
		/* Methods */
		
	/* 
	 * Purpose: Prompts the user for a move
	 * Precondition: Player's turn, assumes legal move for now
	 * Postcondition: Player will have moved to spot
	 * Returns: Coordinates of movements
	 * 
	 */
	
	public String getMove() {
		//Verify move is entered
		// valid, is legal move, when the person's turn
		// comes up
		System.out.println("Enter move: ");
		return sc.nextLine();
	}

	/* 
	 * Purpose: checks for available walls, if any for 
	 * player to place
	 * Precondition: none
	 * Postcondition: none
	 * Returns: # of walls available to place
	 */
	public int getWalls() {
		// Auto-generated method stub
		return this.wallTotal;
	}
	
	/* 
	 * Purpose: gets player's x position 
	 * Precondition: none
	 * Postcondition: none
	 * Returns: player's x position 
	 */
	public int x() {
		return (int)this.position.getX();
	}
	
	/* 
	 * Purpose: gets player's y position 
	 * Precondition: none
	 * Postcondition: none
	 * Returns: player's y position 
	 */
	public int y() {
		return (int)this.position.getY();
	}
	
	/* 
	 * Purpose: sets player's x position 
	 * Precondition: none
	 * Postcondition: none
	 * Returns: none
	 */
	public void setX(int x) {
		this.position.setLocation(x, this.y());
	}
	
	/* 
	 * Purpose: sets player's y position 
	 * Precondition: none
	 * Postcondition: none
	 * Returns: none
	 */
	public void setY(int y) {
		this.position.setLocation(this.x(), y);
	}
	
	/* 
	 * Purpose: sets player's position 
	 * Precondition: none
	 * Postcondition: none
	 * Returns: none
	 */
	public void setPos(int x, int y) {
		Point tmp = new Point(x, y);
		this.position = tmp;
	}

}

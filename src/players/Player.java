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


/*
 * Player handles what happens when you make new players and
 * defines what types of actions the player has.
 */
public class Player implements Players {

		/* Fields */
		private String name; 	// players name
		private int wallTotal;	// how many walls player has
		private Scanner sc;		// prompt player for moves
		private Point position; // current position of the player
		private Point winArea;  // where to win
		
		
		/* Constructor(s) */
		public Player(String name, int walls, String pos) {
			this.name = name;
			this.wallTotal = walls;
			// to get moves (DEBUG)
			this.sc = new Scanner("System.in");
			//TODO implement way to find initial position
			this.position = setPosition(pos);
		}
		
		/* Methods */
		
	/*
	 * Purpose: setPosition based on a string of form "<char><int>"
	 * @params - string representation of position
	 * Postcondition - new position set
	 * 
	 */
	public static Point setPosition(String pos) {
		
		pos = pos.trim();
		if(pos.length() < 2) {
			System.err.println("Something is wrong with pos");
			return new Point(0,0);
		}
		char column = pos.charAt(0);
		int xCoord = (int)((column - 'a') - 1);
		String yCoord = "" + pos.charAt(1);
		return new Point(xCoord, (Integer.parseInt(yCoord)));
		
	}
		
	/* 
	 * Purpose: Prompts the user for a move
	 * Precondition: Player's turn, assumes legal move for now
	 * Postcondition: Player will have moved to spot
	 * Returns: Coordinates of movements
	 * 
	 */
	
	public String getMove() {
		// TODO talk with the GUI
		//Verify move is entered
		// valid, is legal move, when the person's turn
		// comes up
		System.out.println("Enter move: ");
		// TODO figure a check mechanism for moves.
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

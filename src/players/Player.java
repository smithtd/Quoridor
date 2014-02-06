/**
 * 
 */
package players;

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
		
		/* Constructor(s) */
		public Player(String name, int walls) {
			this.name = name;
			this.wallTotal = walls;
			// to get moves
			this.sc = new Scanner("System.in");
		}
		
		/* Methods */
		
	/* (non-Javadoc)
	 * @see players.Players#getMove()
	 */
	public String getMove() {
		//Verify move is entered
		// valid, is legal move, when the person's turn
		// comes up
		System.out.println("Enter move: ");
		return sc.nextLine();
	}

	/* (non-Javadoc)
	 * @see players.Players#getWalls()
	 */
	public int getWalls() {
		// Auto-generated method stub
		return this.wallTotal;
	}

}

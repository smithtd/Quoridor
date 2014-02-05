/**
 * 
 */
package team511Tactical.Quoridor.Player;

import java.util.Scanner;

/**
 * @author marc dean jr. 
 *
 */
public class Player implements Players {
	
	// fields: 
	
	private String name;	// players name
	private int wallTotal;	// how many walls player has left
	private Scanner sc;		// prompt player for moves
	/* Constructor(s) */
	
	public Player(String name, int walls){
		this.name = name;
		this.wallTotal = walls;
		/* This 
		this.sc = new Scanner("System.in");
	}

	/* Methods */
	
	
	
	/* (non-Javadoc)
	 * @see team511Tactical.Quoridor.Player.Players#wallTotal()
	 */
	@Override
	public int wallTotal() {
		// TODO Auto-generated method stub
		return this.wallTotal;
	}

	/* (non-Javadoc)
	 * @see team511Tactical.Quoridor.Player.Players#getMove()
	 */
	@Override
	public String getMove() {
		// TODO Auto-generated method stub
		return this.name;
	}

}

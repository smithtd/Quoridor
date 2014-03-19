/* Author: Eli Donahue
 * This class exists as a data container to track data about the
 * contents of the board and to provide methods for players to 
 * interact with the board.
 */

package board;

import players.Player;
import walls.Wall;

public class Board {
	
	// Instance variables
	private Player[] players;	
	private Wall[] walls;
	private int numWalls;
	
	// Methods
	// constructor(s)
	public Board(Player[] players, int walls) {
		this.players = players;			// passed in from Game
		this.walls = new Wall[walls];	// Wall array length = max num of walls
		numWalls = 0;
	}
	
	public Player[] players(){
		return players;
	}
	
	public Wall[] walls(){
		return walls;
	}
	
	public int numWalls(){
		return numWalls;
	}
	
	// If move is valid, update player coordinates, return true
	public boolean placePawn(Player p, int x, int y) {
		if(isLegalMove(p, x, y)){
			p.setPos(x,y);
			return true;
		}else{
			return false;
		}
	}
	
	// if move is valid, add wall to walls[], return true
	public boolean placeWall(Player p, int x, int y, String type){
		Wall w = new Wall(x, y, type);
		if(isLegalMove(p, w)){
			walls[numWalls] = w;
			numWalls++;;
			return true;
		}else{
			return false;
		}
	}
	
	// isLegalMove is an overloaded method
	// this version checks whether a player's pawn can be placed at (x,y)
	public boolean isLegalMove(Player p, int x, int y){
		// check that coordinates are valid
		if(x > 8 || x < 0 || y > 8 || y < 0){
			System.err.println("Position ("+x+","+y+") out of range");
			return false;
		}
		
		// check that the space is not occupied by another player
		for(int i = 0; i < players.length; i++){
			if(x == players[i].x() && y == players[i].y()){
				System.err.println("There's already a player there.");
				return false;
			}
		}
				
		// check that the space is only one away (add logic for jumping later)
		if(x > p.x()+1 || x < p.x()-1){
			System.err.println("("+x+","+y+") is too far away from ("+p.x()+","+p.y()+").");
			return false;
		}
			
		if(y > p.y()+1 || y < p.y()-1){
			System.out.println("Space is too far away.");
			return false;
		}
		
		// make sure no wall is in the way
		for(int i=0; i<numWalls; i++){
			if(walls[i].isBetween(p.x(), p.y(), x, y)){
				System.err.println("There is a wall between "+p.x()+p.y()+" and "+x+y);
				return false;
			}
		}
		
		return true;
	}
	
	// isLegalMove is an overloaded method
	// this version checks whether a player can place a wall at (x,y)
	public boolean isLegalMove(Player p, Wall w) {
		// check that player can play a wall
		if(p.getWalls()<=0)
			return false;
		
		// check that type is valid
		if(!(w.type().equals("h") || w.type().equals("v")))
			return false;
		
		// check that (x,y) is on grid and ok to place a wall at
		if(w.getX() > 7 || w.getY() > 7 || w.getX() < 0 || w.getY() < 0)
			return false;
		
		if(w.type().equals("v") && w.getX()==0)
			return false;		// can't place vertical wall on left edge
		
		if(w.type().equals("h") && w.getY()==0)
			return false;		// can't place horizontal wall on edge
		
		// check that (x,y) not occupied or intersected by another wall
		for(int i=0; i<numWalls; i++)
			if(walls[i].intersects(w))
				return false;
						
		// make sure it doesn't prevent a player from reaching end
		return true;
	}
	
	public String toString(){
		String s = "Board: ";
		s += players.length + " players |";
		s += numWalls + " walls";
		return s;
	}
}

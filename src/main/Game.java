/*
 * This class exists as the "main" program. A new game begins when
 * a Game object is initialized. The Game will handle serve as the
 * client.
 */

package main;

// use to implement subject/observer
import java.util.ArrayList;  
import java.util.Observable;  
import java.util.Observer;  

import players.Player;
import board.Board;
import ui.GameBoard;

public class Game extends Observable{
	
	// Static variables
	private static final int NUM_OF_WALLS = 20; // number of walls is fixed
	private static final int MAX_NUMBER_PLAYERS = 4;
	
	// Instance variables
	private ArrayList<Observer> ui = new ArrayList<Observer>();  
	private static Board board;					// holds board info
	private static Player[] players;			// Player[] to hold players
	private int numPlayers;						// number of players
	//public GameBoard gb;
	
	// constructor
	public Game(int numPlayers, int numWalls) {
		this.numPlayers = numPlayers;
		players = new Player[numPlayers];
		int wallsEach = numWalls/numPlayers;
		
		// Initialize Players: 1, 4, 2, 3 (clockwise from the top of the board)
		if(this.numPlayers == MAX_NUMBER_PLAYERS){
			players[0] = new Player("1", 0, 4, 1, wallsEach);
			players[1] = new Player("2", 4, 8, 2, wallsEach);
			players[2] = new Player("3", 8, 4, 3, wallsEach);
			players[3] = new Player("4", 4, 0, 4, wallsEach);
		} else {
			players[0] = new Player("1", 0, 4, 1, wallsEach);
			players[1] = new Player("2", 8, 4, 2, wallsEach);
		}
		
		board = new Board(players, numWalls);
	}
	
	public ArrayList<Observer> getObservers() {  
        return ui;  
    }  
	
    public void setObservers(ArrayList<Observer> observers) {  
        this.ui = observers;  
    }
    
    public void notifyObservers(Observable observable,String availability) {    
    	this.ui.get(0).update(observable,board);  
    }   
  
    public void registerObserver(Observer observer) {  
         ui.add(observer);  
          
    }  
  
    public void removeObserver(Observer observer) {  
         ui.remove(observer);  
          
    }
	
	// start game
	public void startGame(){
		GameBoard gb = new GameBoard();
		this.registerObserver(gb);
		
/*		gb = new GameBoard( this );

		for(int i = 1;i<=numPlayers;i++){
			Board.placePawn(players[i-1], players[i-1].getStartx(), players[i-1].getStarty());
		}

		for(int i=0; i<numPlayers; i++){
			Player p = players[i];
			GameBoard.cont.getPlayerButtons()[p.x()][p.y()].setBackground(p.getColor());
		}
		GameBoard.cont.addPlyrAry( players );
		GameBoard.cont.addWallsRem(wallsRem);
		GameBoard.cont.showPlyrMoves();
*/
	}
	
	public static void new4PlayerGame(){
		//gb.getFrame().dispose();
		Game g = new Game( 4, NUM_OF_WALLS );
		g.startGame();
	}
	
	public static void new2PlayerGame(){
		//gb.getFrame().dispose();
		Game g = new Game( 2, NUM_OF_WALLS );
		g.startGame();
	}
	
	// exit game (report errors if any)
	public void quit(){
		System.exit(0);
	}
	
	// returns the number of players in the game
	public int getNumPlayers(){
		return this.numPlayers;
	}
	
	// client/server communication methods (Dylan)
	// report errors
	public static void main(String[] args) {
		new2PlayerGame();
	}
}

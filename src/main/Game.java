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
import parser.Parser;

public class Game extends Observable{
	
	// Static variables
	private static final int NUM_OF_WALLS = 20; // number of walls is fixed
	private static final int MAX_NUMBER_PLAYERS = 4;
	
	// Instance variables
	private ArrayList<Observer> ui = new ArrayList<Observer>();  
	private static Board board;					// holds board info
	private static Player[] players;			// Player[] to hold players
	private int numPlayers;						// number of players
	private int curr;
	
	// constructor

	public Game(int numPlayers, int numWalls) {
		curr = 0;
		this.numPlayers = numPlayers;
		players = new Player[numPlayers];
		int wallsEach = numWalls/numPlayers;
		
		// Initialize Players: 1, 4, 2, 3 (clockwise from the top of the board)
		if(this.numPlayers == MAX_NUMBER_PLAYERS){
			players[0] = new Player("1", 4, 0, 1, wallsEach);
			players[1] = new Player("2", 8, 4, 2, wallsEach);
			players[2] = new Player("3", 4, 8, 3, wallsEach);
			players[3] = new Player("4", 0, 4, 4, wallsEach);
		} else {
			players[0] = new Player("1", 4, 0, 1, wallsEach);
			players[1] = new Player("2", 4, 8, 2, wallsEach);
		}
		
		board = new Board(players, numWalls);
	}
	
	public ArrayList<Observer> getObservers() {  
        return ui;  
    }  
	
    public void setObservers(ArrayList<Observer> observers) {  
        this.ui = observers;  
    }
    
    public void notifyObservers(Observable observable,Board board) {    
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
		gb.update(this, board);
	}
	
	public Board getBoard(){
		return board;
	}
	
	public void nextTurn(){
		curr++;
		if(curr >= numPlayers)
			curr=0;
	}
	
	public boolean playTurn(String s){
		int x = Integer.parseInt(""+s.charAt(0));
		int y = Integer.parseInt(""+s.charAt(1));
		
		if(s.length()==2){
			return board.placePawn(this.currPlayer(), x, y);
		}
		return false;
	}
	
	public Player currPlayer(){
		return players[curr];
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
		// parser to parse moves
		Parser p = new Parser();
		
		// start game and call up UI
		Game g = new Game( 2, NUM_OF_WALLS );
		g.startGame();
		
		// until someone wins, loop through turns
		boolean playerWon = false;
		while(!playerWon){
			System.out.println("Player "+g.currPlayer().getColor()+" turn");
			
			// get move from player
			String move = g.currPlayer().getMove();
			move = p.moveTranslate(move);
			System.out.println(move);
			
			// try to play turn
			if(g.playTurn(move)){
				System.out.println("Valid move");
				g.notifyObservers(g, g.getBoard());
			}
			
			g.nextTurn();
		}
	}
}

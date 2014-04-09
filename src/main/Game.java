package main;

// use to implement subject/observer
import java.util.ArrayList;  
import java.util.Observable;  
import java.util.Observer; 

// use to get input from command line/file
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

import players.Player;
import board.Board;
import ui.GameBoard;
import parser.Parser;

/**
 * The Game class drives the Quoridor game logic. It extends 
 * java.util.Observable and acts as a subject for the 
 * GameBoard class, which observes/subscribes to a Game object.
 * 
 * @author Eli Donahue
 */
public class Game extends Observable{
	
	/* Static variables */
	
	private static final int NUM_OF_WALLS = 20; 
	private static final int MAX_NUMBER_PLAYERS = 4;
	
	/* Instance variables */
	
	private static ArrayList<Observer> ui = new ArrayList<Observer>();  
	private static Board board;					// holds board info
	private static Player[] players;			// Player[] to hold players
	private static int numPlayers;				// number of players
	private static int curr;					// index of current Player
	private boolean gameWon;					// whether the game has been won
	
	/* Constructor */
	
	/**
	 * Constructs a Game object with an array of initialized players 
	 * and a Board object.
	 * 
	 * @param numPlayers	the number of players in this Game
	 * @param numWalls		the maximum number of walls to divide among Players
	 */
	public Game(int numPlayers, int numWalls) {
		curr= 0;
		Game.numPlayers = numPlayers;
		players = new Player[numPlayers];
		int wallsEach = numWalls/numPlayers;
		
		if(Game.numPlayers == MAX_NUMBER_PLAYERS){
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
	
	/* Game Play Methods */
	
	/**
	 * Main method constructs and starts a Game based on optional command line
	 * arguments. Arguments may be (int numberOfPlayers) or 
	 * (int numberOfPlayers, String fileName).
	 * 
	 * @param args	(optional) (int numberOfPlayers) or (int numberOfPlayers, String fileName)
	 */
	public static void main(String[] args) {
		// optionally can pass in file to run as demo or pass in num of players
		int players = 2;
		String fileName = "";
		
		if(args.length == 1)
			players = Integer.parseInt(args[0]);

		if(args.length == 2){
			players = Integer.parseInt(args[0]);
			fileName = args[1];
		}
		
		// parser to parse moves
		Parser p = new Parser();
		
		// start game and call up UI
		Game g = new Game( players, NUM_OF_WALLS );
		g.startGame();
		
		if(fileName.length() == 0)
			g.playGame(p);
		else
			g.playGame(p, fileName);
		
		// notify observer, since we have a winner, ui will execute end of game
		g.notifyObservers(g, Game.getBoard());
	}
	
	/**
	 * Starts the game by adding a GameBoard (UI) to this Game.
	 * 
	 */
	public void startGame(){
		Game.updatePlayer(players[curr]);
		GameBoard gb = new GameBoard();
		this.registerObserver(gb);
		gb.update(this, board);
	}
	
	/**
	 * Loops through the Players' turns until someone wins.
	 * Gets the move, translates the move, plays the move, and then updates
	 * the UI.
	 * 
	 * @param p	the Parser to parse Players' turns
	 */
	public void playGame(Parser p){
		// until someone wins, loop through turns
		while(!this.gameWon){
			System.out.println(this.getCurrPlayer().getColorName()+" player's turn");
			
			// get move from player
			String move = this.getCurrPlayer().getMove();
			if(move.length()==2){
				move = p.moveTranslate(move);
			}else{
				move = p.wallTranslate(move);
			}
			
			if(move.isEmpty()){
				// invalid move, kick player when network is integrated
				// for now, freeze game
				System.err.println("Invalid turn. Parser returned empty string.");
				break;
			}
			
			// try to play turn
			if(this.playTurn(move)){
				if(this.checkForWin())
					break;
				Game.nextTurn();
				Game.updatePlayer(players[curr]);
				this.notifyObservers(this, Game.getBoard());
			}else{
				System.err.println("Player turn failed!");
				break;
			}
		}
	}
	
	/**
	 * Loops through a file of Players' turns until someone wins.
	 * Gets the move, translates the move, plays the move, and then updates
	 * the UI.
	 * 
	 * @param p	the Parser to parse Players' turns
	 * @param fileName	the name of the file to scan for moves
	 */
	public void playGame(Parser p, String fileName){
		try{
			Scanner sc = new Scanner(new File(fileName));
			
			// until someone wins, loop through turns
			while(!this.gameWon && sc.hasNextLine()){
				System.out.println(this.getCurrPlayer().getColorName()+" player's turn");
				
				// get move from player
				String move = sc.nextLine();
				if(move.length()==2){
					move = p.moveTranslate(move);
				}else{
					move = p.wallTranslate(move);
				}
			
				// try to play turn
				if(this.playTurn(move)){
					if(this.checkForWin())
						break;
					Game.nextTurn();
					Game.updatePlayer(players[curr]);
					this.notifyObservers(this, Game.getBoard());
					// sleep 1 second so game is watchable
					Thread.sleep(1000); 
				}else{
					System.err.println("Player turn failed!");
					break;
				}
			}
			sc.close();
		}catch(FileNotFoundException e){	
		}catch (InterruptedException e) {}
	}
	
	/**
	 * Resets Player's moves and retrieves current available moves.
	 * 
	 * @param p Player
	 */
	public static void updatePlayer(Player p){
		p.clearMoves();
		board.possibleMoves(p);
	}
	
	/**
	 * Parse and play the turn.
	 * 
	 * @param s	a String containing a Player's move in [a-i][1-9] or [a-i][1-9][vh] format
	 * @return	a boolean based on the success of playing the turn
	 */
	public boolean playTurn(String s){
		int x = Integer.parseInt(""+s.charAt(0));
		int y = Integer.parseInt(""+s.charAt(1));
		
		if(s.length()==2){
			return board.placePawn(this.getCurrPlayer(), x, y);
		}else{
			return board.placeWall(this.getCurrPlayer(), x, y, ""+s.charAt(2));
		}
	}
	
	/**
	 * Checks to see if the current Player has made it into its "win area."
	 * Sets Game.gameWon to true if the Player won.
	 * 
	 * @return	a boolean telling whether or not this Player has won
	 */
	public boolean checkForWin(){
		Player p = this.getCurrPlayer();
		if(p.won()){
			this.gameWon = true;
			return true;
		}
		return false;
	}
	
	/**
	 * Increments the current player to begin the next Player's turn.
	 * 
	 */
	public static void nextTurn(){
		curr++;
		if(curr >= numPlayers)
			curr=0;
	}
	
    /**
     * Notifies Observer (the UI) of changes to the Game.
     * 
     * @param observable	an Observable object (this Game)
     * @param board			this Game's Board
     */
    public void notifyObservers(Observable observable,Board board) {    
    	Game.ui.get(0).update(observable,board);  
    }
	
	/**
	 * Disposes of the previous GameBoard (UI) and creates a new Game.
	 * 
	 */
	public static void new4PlayerGame(){
		GameBoard ui = (GameBoard) Game.ui.get(0);
		ui.getFrame().dispose();
		Game g = new Game( 4, NUM_OF_WALLS );
		g.startGame();
	}
	
	/**
	 * Disposes of the previous GameBoard (UI) and creates a new Game.
	 * 
	 */
	public static void new2PlayerGame(){
		GameBoard ui = (GameBoard) Game.ui.get(0);
		ui.getFrame().dispose();
		Game g = new Game( 2, NUM_OF_WALLS );
		g.startGame();
	}
    
	/**
	 * Exits the program with exit code 0.
	 * 
	 */
	public void quit(){
		System.exit(0);
	}
	
	/* Get Methods */
	
	/**
	 * Gets an ArrayList of this Game's Observers. We only ever use 
	 * the first one.
	 * 
	 * @return	the list of observers for this game (the UI)	
	 */
	public ArrayList<Observer> getObservers() {  
        return ui;  
    }
	
	/**
	 * Gets the Board from this Game.
	 * 
	 * @return		a Board object
	 */
	public static Board getBoard(){
		return board;
	}
	
	/**
	 * Gets the current Player.
	 * 
	 * @return	the Player currently taking its turn
	 */
	public static Player getCurrPlayer(){
		return players[curr];
	}
	
	/**
	 * Gets the previous Player.
	 * 
	 * @return	the Player whose turn it just was
	 */
	public static Player getPrevPlayer(){
		if(players[curr].getPnum()==1)
			return players[players.length-1];
		else return players[curr-1];
	}
	
	public static Player[] getPlayerAry(){
		return players;
	}
	/**
	 * Gets the number of Players in this Game.
	 * 
	 * @return	an integer number of Players
	 */
	public static int getNumPlayers(){
		return numPlayers;
	}
	
	/**
	 * Gets this Game's gameWon boolean.
	 * 
	 * @return	a boolean telling whether or not this Game has been won
	 */
	public boolean gameWon(){
		return this.gameWon;
	}
	
	/* Set Methods */
	
    /**
     * Sets an ArrayList of Observers as this Game's UI.
     * 
     * @param observers		an ArrayList of Observers (one GameBoard)
     */
    public void setObservers(ArrayList<Observer> observers) {  
        Game.ui = observers;  
    }
    
    /**
     * Adds an Observer (GameBoard) to this Game's Observers.
     * 
     * @param observer		a GameBoard object
     */
    public void registerObserver(Observer observer) {  
         ui.add(observer);  
          
    }
  
    /**
     * Removes an Observer from this Game's Observers
     * 
     * @param observer		a GameBoard object
     */
    public void removeObserver(Observer observer) {  
         ui.remove(observer);  
          
    }
		
}

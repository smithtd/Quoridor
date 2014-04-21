
package main;

// use to implement subject/observer
import java.util.ArrayList;  
import java.util.Observable;  
import java.util.Observer; 

// use to get input from command line/file
import java.util.Scanner;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;

import network.GameClient;
import players.Player;
import board.Board;
import ui.GameBoard;
import parser.Parser;

/**
 * The Game class drives the Quoridor game logic. It extends 
 * java.util.Observable and acts as a subject for the 
 * GameBoard class, which observes/subscribes to a Game object.
 * 
 * @author Eli Donahue, M. Dean
 * Team 511-tactical
 */
public class Game extends Observable{
	
	/* Static variables */
	public static int WallGap = 10;
	public static int PlayerWidth = 40;
	public static int PlayerHeight = 40;
	public static int sleepTime = 333;
	
	public static Dimension HWall = new Dimension( Game.PlayerWidth, Game.WallGap );
	public static Dimension VWall = new Dimension( Game.WallGap, Game.PlayerHeight );
	public static Dimension Intersection = new Dimension( VWall.width, HWall.height );
	public static Dimension PlayerSize = new Dimension( HWall.width, VWall.height );
	
	public static Game g;
	
	/* Private Instance variables */
	
	private static final int NUM_OF_WALLS = 20; 
	private static final int MAX_NUMBER_PLAYERS = 4;
	
	private static ArrayList<Observer> ui = new ArrayList<Observer>();  
	private static Board board;					// holds board info
	private static ArrayList<Player> players;	// Player ArrayList to hold players
	private static int numPlayers;				// number of players
	private static int curr;					// index of current Player
	private static boolean gameWon;				// whether the game has been won
	private static GameClient networker; 		// networking client
	
	
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
		players = new ArrayList<Player>();
		int wallsEach = numWalls/numPlayers;
		
		if(Game.numPlayers == MAX_NUMBER_PLAYERS){
			players.add(new Player("1", 0, 4, 1, wallsEach));
			players.add(new Player("2", 4, 8, 2, wallsEach));
			players.add(new Player("3", 8, 4, 3, wallsEach));
			players.add(new Player("4", 4, 0, 4, wallsEach));
		} else {
			players.add(new Player("1", 0, 4, 1, wallsEach));
			players.add(new Player("2", 8, 4, 2, wallsEach));
		}
		
		board = new Board(players, numWalls);
	}
	
	/* Game Play Methods */
	

	
	/**
	 * Starts the game by adding a GameBoard (UI) to this Game.
	 * 
	 */
	public void startGame(){
		Game.updatePlayer(players.get(curr));
		GameBoard gb = new GameBoard();
		gameWon = false;
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
		while(!Game.gameWon){
			System.out.println(Game.getCurrPlayer().getColorName()+" player's turn");
			
			// get move from player
			String move = Game.getCurrPlayer().getMove();
			if(move.length()==2){
				move = p.moveTranslate(move);
			}else{
				move = p.wallTranslate(move);
			}
			
			if(move.isEmpty()){
				kickPlayer(players.get(curr));
				System.out.println("Calling checkForWin");
				if(this.checkForWin()){
					notifyObservers(ui);
					break;
				}
			}
			
			// try to play turn
			if(this.playTurn(move)){
				System.out.println("Player took turn, checking if won.");
				if(this.checkForWin()){
					System.out.println(Game.getCurrPlayer().getColorName()+" won by reaching the end: "+players.get(curr).x()+","+players.get(curr).y());
					players.get(curr).clearMoves();
					notifyObservers(ui);
					break;
				}
				Game.nextTurn();					
				System.out.println("CHECKING AVAILABLE MOVES FOR "+players.get(curr).getColorName()+"!!!!!!!!!!!");
				Game.updatePlayer(players.get(curr));
				this.notifyObservers(this, Game.getBoard());
			}else{
				System.err.println("Player turn failed!");
				kickPlayer(players.get(curr));
				System.out.println("Calling checkForWin");
				if(this.checkForWin()){
					System.out.println(Game.getCurrPlayer().getColorName()+" won by default.");
					players.get(curr).clearMoves();
					notifyObservers(ui);
					break;
				}
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
			while(!Game.gameWon && sc.hasNextLine()){
				// sleep 1 second so game is watchable
				Thread.sleep( sleepTime ); 
				System.out.println(Game.getCurrPlayer().getColorName()+" player's turn");
				
				// get move from player
				String move = sc.nextLine();
				if(move.length()==2){
					move = p.moveTranslate(move);
				}else{
					move = p.wallTranslate(move);
				}
			
				// try to play turn
				if(this.playTurn(move)){
					System.out.println(Game.getCurrPlayer().getColorName()+" took turn, checking if won.");
					if(this.checkForWin()){
						System.out.println(Game.getCurrPlayer().getColorName()+" won by reaching the end: "+players.get(curr).x()+","+players.get(curr).y());
						players.get(curr).clearMoves();
						notifyObservers(ui);
						break;
					}
					Game.nextTurn();
					System.out.println("CHECKING AVAILABLE MOVES FOR "+players.get(curr).getColorName()+"!!!!!!!!!!!");
					Game.updatePlayer(players.get(curr));
					this.notifyObservers(this, Game.getBoard());
				}else{
					System.err.println("Player turn failed!");
					kickPlayer(players.get(curr));
					System.out.println("Calling checkForWin");
					if(this.checkForWin()){
						System.out.println(Game.getCurrPlayer().getColorName()+" won by default.");
						players.get(curr).clearMoves();
						notifyObservers(ui);
						break;
					}
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
			return board.placePawn(Game.getCurrPlayer(), x, y);
		}else{
			return board.placeWall(Game.getCurrPlayer(), x, y, ""+s.charAt(2));
		}
	}
	
	/**
	 * Checks to see if the current Player has made it into its "win area."
	 * Sets Game.gameWon to true if the Player won.
	 * 
	 * @return	a boolean telling whether or not this Player has won
	 */
	public boolean checkForWin(){
		Player p = Game.getCurrPlayer();
		// if player made it to win area, player won
		if(p.won()){
			Game.gameWon = true;
			return true;
		}
		// if player is the last player on the board, player won
		if(numPlayers == 1){
			System.out.println(Game.getCurrPlayer().getColorName()+" won!");
			Game.gameWon = true;
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
		g = new Game( 4, NUM_OF_WALLS );
		g.startGame();
	}
	
	/**
	 * Disposes of the previous GameBoard (UI) and creates a new Game.
	 * 
	 */
	public static void new2PlayerGame(){
		GameBoard ui = (GameBoard) Game.ui.get(0);
		ui.getFrame().dispose();
		g = new Game( 2, NUM_OF_WALLS );
		g.startGame();
	}
	
	/**
	 * Kicks a Player from the game by removing it from the player array.
	 * Does not remove Player's placed walls. Player will not be asked for 
	 * turns or displayed on the board.
	 * 
	 * @param p Player
	 */
	public static void kickPlayer(Player p){
		int beforeKick = numPlayers;
		// remove player
		players.remove(p);
		// decrement number of players
		numPlayers--;
		// adjust curr if necessary
		if(curr >= numPlayers)
			curr--;
		// update players on the board
		board.updatePlayers(players);
		
		if((beforeKick==numPlayers+1) && (players.size()==numPlayers))
			System.out.println(p.getColorName()+" was kicked. "+numPlayers+" players left.");
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
		return players.get(curr);
	}
	
	/**
	 * Gets the previous Player.
	 * 
	 * @return	the Player whose turn it just was
	 */
	public static Player getPrevPlayer(){
		if(players.get(curr).getPnum()==1)
			return players.get(players.size()-1);
		else return players.get(curr-1);
	}
	
	public static ArrayList<Player> getPlayerAry(){
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
		return Game.gameWon;
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
		
		if( args.length == 1 )
			players = Integer.parseInt(args[0]);

		if( args.length == 2 ){
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
    
}

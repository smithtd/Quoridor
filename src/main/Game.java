
package main;

// use to implement subject/observer
import java.util.ArrayList;  
import java.util.Arrays;
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
import terminal.TextFieldListener;
import ui.GameBoard;
import parser.Parser;

/**
 * The this class drives the Quoridor this logic. It extends 
 * java.util.Observable and acts as a subject for the 
 * GameBoard class, which observes/subscribes to a this object.
 * 
 * @author Eli Donahue, M. Dean
 * Team 511-tactical
 */
public class Game extends Observable{

	/*  variables */
	private  int WallGap = 10;
	private  int PlayerWidth = 40;
	private  int PlayerHeight = 40;
	private  int sleepTime = 250;
	private  int colorIncrement = 200;
	
	private  Dimension HWall = new Dimension( this.getPlayerWidth(), this.WallGap );
	private  Dimension VWall = new Dimension( this.WallGap, this.getPlayerHeight() );
	private  Dimension Intersection = new Dimension( getVWall().width, getHWall().height );
	private  Dimension PlayerSize = new Dimension( getHWall().width, getVWall().height );
	private static Game g;
	private static GameBoard gb;

	/* Private Instance variables */

	private  final int NUM_OF_WALLS = 20; 
	private  final int MAX_NUMBER_PLAYERS = 4;

	private  ArrayList<Observer> ui = new ArrayList<Observer>();
	private  ArrayList<Player> players;	// Player ArrayList to hold players
	private  int numPlayers;				// number of players
	private  int curr;					// index of current Player
	private  boolean gameWon;				// whether the this has been won
	@SuppressWarnings("unused")
	private  GameClient networker; 		// networking client  
	private  Board board;					// holds board info
	private  Parser parser;

	/* Constructor */

	/**
	 * Constructs a this object with an array of initialized players 
	 * and a Board object.
	 * 
	 * @param numPlayers	the number of players in this this
	 * @param numWalls		the maximum number of walls to divide among Players
	 */
	public Game() {

	}

	/* this Play Methods */



	/**
	 * Loops through the Players' turns until someone wins.
	 * Gets the move, translates the move, plays the move, and then updates
	 * the UI.
	 * 
	 * @param p	the Parser to parse Players' turns
	 */
	public void playthis(int numStartingPlayers, String fileName){

		curr= 0;
		this.numPlayers = numStartingPlayers;
		players = new ArrayList<Player>();
		int wallsEach = this.NUM_OF_WALLS/numPlayers;

		if(this.numPlayers == MAX_NUMBER_PLAYERS){
			players.add(new Player("1", 0, 4, 1, wallsEach));
			players.add(new Player("2", 4, 8, 2, wallsEach));
			players.add(new Player("3", 8, 4, 3, wallsEach));
			players.add(new Player("4", 4, 0, 4, wallsEach));
		} else {
			players.add(new Player("1", 0, 4, 1, wallsEach));
			players.add(new Player("2", 8, 4, 2, wallsEach));
		}

		board = new Board(players, NUM_OF_WALLS, gb);
		// parser to parse moves
		parser = new Parser();
		// start this and call up UI

		this.updatePlayer(players.get(curr));
		gameWon = false;

		gb = new GameBoard(this);
		this.registerObserver(gb);
		gb.update(this, board);
		
		Scanner sc = null;
		if( !fileName.equals("") )
			try { sc = new Scanner( new File( fileName ) ); } catch (FileNotFoundException e) {}
		
		// until someone wins, loop through turns
		while( !this.gameWon ){
			// get move from player
			TextFieldListener.textArea.setForeground( this.getCurrPlayer().getColor() );
			TextFieldListener.textArea.append(this.getCurrPlayer().getColorName() + ": ");
			if( !fileName.equals("") )
				try{ Thread.sleep(sleepTime/4*3);}catch(Exception e){}
			String move = (fileName.equals("")) ? this.getCurrPlayer().getMove() : sc.nextLine();
			TextFieldListener.textArea.append( move + "\n" );
			move = (move.length()==2) ? parser.moveTranslate(move) : parser.wallTranslate(move);

			if( !fileName.equals("") )
				if(move.isEmpty()){
					kickPlayer(players.get(curr));
//					System.out.println("Calling checkForWin");
					if(this.checkForWin()){
						notifyObservers(ui);
						break;
					}
				}

			// try to play turn
			if(this.playTurn(move)){
//				System.out.println("Player took turn, checking if won.");
 				if(this.checkForWin()){
//					System.out.println(this.getCurrPlayer().getColorName()+" won by reaching the end: "+players.get(curr).x()+","+players.get(curr).y());
					players.get(curr).clearMoves();
					notifyObservers(ui);
					break;
				}
				this.nextTurn();					
//				System.out.println("CHECKING AVAILABLE MOVES FOR "+players.get(curr).getColorName()+"!!!!!!!!!!!");
				this.updatePlayer(players.get(curr));
				this.notifyObservers(this, this.getBoard());
			}else{
//				System.err.println("Player turn failed!");
				kickPlayer(players.get(curr));
//				System.out.println("Calling checkForWin");
				if(this.checkForWin()){
//					System.out.println(this.getCurrPlayer().getColorName()+" won by default.");
					players.get(curr).clearMoves();
					notifyObservers(ui);
					break;
				}
			}
			System.out.println(Arrays.toString(gb.getStatAry()));

			if(sc.hasNext() == false)
				fileName = "";
			if( !fileName.equals("") )
				try{ Thread.sleep(sleepTime/4);}catch(Exception e){}
		}
		System.out.println("WE GOT HERE");
	}

	/**
	 * Resets Player's moves and retrieves current available moves.
	 * 
	 * @param p Player
	 */
	public void updatePlayer(Player p){
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
	 * Sets this.gameWon to true if the Player won.
	 * 
	 * @return	a boolean telling whether or not this Player has won
	 */
	public boolean checkForWin(){
		Player p = this.getCurrPlayer();
		// if player made it to win area, player won
		if(p.won()){
			this.gameWon = true;
			return true;
		}
		// if player is the last player on the board, player won
		if(numPlayers == 1){
//			System.out.println(this.getCurrPlayer().getColorName()+" won!");
			this.gameWon = true;
			return true;
		}

		return false;
	}

	/**
	 * Increments the current player to begin the next Player's turn.
	 * 
	 */
	public  void nextTurn(){
		curr++;
		if(curr >= numPlayers)
			curr=0;
	}

	/**
	 * Notifies Observer (the UI) of changes to the this.
	 * 
	 * @param observable	an Observable object (this this)
	 * @param board			this this's Board
	 */
	public void notifyObservers(Observable observable,Board board) {    
		this.ui.get(0).update(observable,board);  
	}

	/**
	 * Disposes of the previous GameBoard (UI) and creates a new this.
	 * 
	 */

	public  void newGame( int numStartingPlayers ){
		gb.closeFrame();
		/*
		for(Player p : this.players)
			p = null;
		for(Observer o : this.ui)
			o=null;
		*/
//		System.out.println("newGame");
		g = new Game();
		g.playthis(numStartingPlayers, "");
	}

	/**
	 * Kicks a Player from the this by removing it from the player array.
	 * Does not remove Player's placed walls. Player will not be asked for 
	 * turns or displayed on the board.
	 * 
	 * @param p Player
	 */
	public  void kickPlayer(Player p){
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
	 * Gets an ArrayList of this this's Observers. We only ever use 
	 * the first one.
	 * 
	 * @return	the list of observers for this this (the UI)	
	 */
	public ArrayList<Observer> getObservers() {  
		return ui;  
	}

	/**
	 * Gets the Board from this this.
	 * 
	 * @return		a Board object
	 */
	public  Board getBoard(){
		return board;
	}

	/**
	 * Gets the current Player.
	 * 
	 * @return	the Player currently taking its turn
	 */
	public  Player getCurrPlayer(){
		return players.get(curr);
	}

	/**
	 * Gets the previous Player.
	 * 
	 * @return	the Player whose turn it just was
	 */
	public  Player getPrevPlayer(){
		if(players.get(curr).getPnum()==1)
			return players.get(players.size()-1);
		else return players.get(curr-1);
	}

	public  ArrayList<Player> getPlayerAry(){
		return players;
	}
	/**
	 * Gets the number of Players in this this.
	 * 
	 * @return	an integer number of Players
	 */
	public  int getNumPlayers(){
		return numPlayers;
	}

	/**
	 * Gets this this's thisWon boolean.
	 * 
	 * @return	a boolean telling whether or not this this has been won
	 */
	public boolean gameWon(){
		return this.gameWon;
	}

	/* Set Methods */

	/**
	 * Sets an ArrayList of Observers as this this's UI.
	 * 
	 * @param observers		an ArrayList of Observers (one GameBoard)
	 */
	public void setObservers(ArrayList<Observer> observers) {  
		this.ui = observers;  
	}

	/**
	 * Adds an Observer (GameBoard) to this this's Observers.
	 * 
	 * @param observer		a GameBoard object
	 */
	public void registerObserver(Observer observer) {  
		ui.add(observer);  

	}

	/**
	 * Removes an Observer from this this's Observers
	 * 
	 * @param observer		a GameBoard object
	 */
	public void removeObserver(Observer observer) {  
		ui.remove(observer);  

	}
	
	public int getWallGap(){
		return this.WallGap;
	}

	public Dimension getHWall() {
		return HWall;
	}

	public Dimension getIntersection() {
		return Intersection;
	}

	public Dimension getVWall() {
		return VWall;
	}

	public int getPlayerHeight() {
		return PlayerHeight;
	}

	public int getColorIncrement() {
		return colorIncrement;
	}

	public int getPlayerWidth() {
		return PlayerWidth;
	}

	public Dimension getPlayerSize() {
		return PlayerSize;
	}

	/**
	 * Main method constructs and starts a this based on optional command line
	 * arguments. Arguments may be (int numberOfPlayers) or 
	 * (int numberOfPlayers, String fileName).
	 * 
	 * @param args	(optional) (int numberOfPlayers) or (int numberOfPlayers, String fileName)
	 */
	public static void main(String[] args) {
		// optionally can pass in file to run as demo or pass in num of players
		int numStartingPlayers = 2;
		String fileName = "";

		if( args.length == 1 )
			numStartingPlayers = Integer.parseInt(args[0]);

		if( args.length == 2 ){
			numStartingPlayers = Integer.parseInt(args[0]);
			fileName = args[1];
		}
		Game g = new Game();
		g.playthis( numStartingPlayers, fileName);
		g.notifyObservers(g, g.getBoard());
	}
}

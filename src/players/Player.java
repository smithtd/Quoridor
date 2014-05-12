package players;

import java.awt.Color;
import java.awt.Point;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * Player handles what happens when you make new players and
 * defines what types of actions the player has.
 * 
 * @author Marc Dean Jr.
 */
public class Player implements Players {

	/* Fields */
	@SuppressWarnings("unused")
	private String name; 	// players name
	private int pNumber; 	// which player it is (1-4)
	private int wallTotal;	// how many walls player has
	private Scanner sc;		// prompt player for moves
	private Point position; // current position of the player
	private int startX;	
	private int startY;
	private Color c;
	private ArrayList<String> availableMoves;	// list of available moves
	private Point[] winArea;  // where to win
	private boolean kicked;
	private ArrayList<String> path;
	
	/* Constructor(s) */
	
	/**
	 * Constructs a Player at a given position (string) with player number.
	 * 
	 * @param name String name
	 * @param walls integer number of walls
	 * @param pos String position that is translated to a Point
	 * @param pNum integer Player number
	 */
	public Player(String name, int walls, String pos, int pNum) {
		this.name = name;
		this.wallTotal = walls;
		// to get moves (DEBUG)
		this.sc = new Scanner("System.in");
		//TODO implement way to find initial position
		this.position = setPosition(pos);
		this.pNumber = pNum;
		if(pNum==1){
			startX=0;
			startY=4;
		}
		if(pNum==2){
			startX=8;
			startY=4;
		}
		if(pNum==3){
			startX=4;
			startY=0;
		}
		if(pNum==4){
			startX=4;
			startY=8;
		}
		this.setWinArea();
		this.kicked = false;
		this.path = new ArrayList<String>();
	}
	
	
	/**
	 * Constructs a Player at a given position (x,y) with player number.
	 * 
	 * @param name String name
	 * @param startX integer initial x coordinate
	 * @param startY integer intital y coordinate
	 * @param pNum integer Player number
	 * @param walls integer number of walls
	 */
	public Player( String name, int x, int y, int pNum, int walls, ArrayList<String> path ){
		this.name = name;
		if(pNum==1){
			startX=0;
			startY=4;
		}
		if(pNum==2){
			startX=8;
			startY=4;
		}
		if(pNum==3){
			startX=4;
			startY=0;
		}
		if(pNum==4){
			startX=4;
			startY=8;
		}
		this.setPos(x, y);
		this.pNumber = pNum;
		if(pNumber== 1)
			c = Color.BLUE;
		else if(pNumber == 2)
			c = Color.RED;
		else if(pNumber == 3)
			c = Color.GREEN;
		else if(pNumber == 4)
			c = Color.YELLOW;
		wallTotal = walls; 
		this.setWinArea();
		this.availableMoves = new ArrayList<String>();
		this.kicked = false;
		this.path = new ArrayList<String>();
	}
	
	public Player( String name, int startX, int startY, int pNum, int walls ){
		this.name = name;
		this.setPos(startX, startY);
		this.startX = startX;
		this.startY = startY;
		this.pNumber = pNum;
		if(pNumber== 1)
			c = Color.BLUE;
		else if(pNumber == 2)
			c = Color.RED;
		else if(pNumber == 3)
			c = Color.GREEN;
		else if(pNumber == 4)
			c = Color.YELLOW;
		wallTotal = walls; 
		this.setWinArea();
		this.availableMoves = new ArrayList<String>();
		this.kicked = false;
		this.path = new ArrayList<String>();
	}
		
	/* Get Methods */
	
	/** 
	 * Purpose: Prompts the user for a move
	 * Precondition: Player's turn, assumes legal move for now
	 * Postcondition: Player will have moved to spot
	 * 
	 * @return String move
	 */
	public String getMove() {
		// TODO talk with the GUI
		//Verify move is entered
		// valid, is legal move, when the person's turn
		// comes up
		sc = new Scanner(System.in);
		System.out.println("Enter move: ");
		// TODO figure a check mechanism for moves.
		if(sc.hasNextLine())
			return sc.nextLine().trim();
		return "";
	}

	/** 
	 * Purpose: checks for available walls, if any for player to place
	 * Precondition: none
	 * Postcondition: none
	 * 
	 * @return integer number of walls available to place
	 */
	public int getWalls() {
		return this.wallTotal;
	}
	
	/**
	 * Purpose: Gets the initial x coordinate for this Player.
	 * @return integer x coordinate
	 */
	public int getStartx(){
		return startX;
	}
	
	public void setStartx(int x){
		this.startX = x;
	}
	
	/**
	 * Purpose: Gets the initial y coordinate for this Player.
	 * 
	 * @return integer y coordinate
	 */
	public int getStarty(){
		return startY;
	}
	
	public void setStarty(int y){
		startY = y;
	}
	
	/**
	 * Purpose: Gets the player number for this Player.
	 * 
	 * @return integer player number
	 */
	public int getPnum(){
		return this.pNumber;
	}
	
	/**
	 * Purpose: gets player's x position 
	 * Precondition: none
	 * Postcondition: none
	 * 
	 * @return integer x coordinate
	 */
	public int x() {
		try{
			return (int)this.position.getX();
		}catch(Exception e){
			return startX;
		}
	}
	
	/**
	 * Purpose: gets player's y position 
	 * Precondition: none
	 * Postcondition: none
	 * 
	 * @return integer y coordinate
	 */
	public int y() {
		try{
			return (int)this.position.getY();
		}catch(Exception e){
			return startY;
		}	
	}
	
	/**
	 * Gets this Player's Color object.
	 * 
	 * @return Color of player
	 */
	public Color getColor(){
		return c;
	}
	
	/**
	 * Determines whether Player has won by checking if it is in its win area.
	 * 
	 * @return
	 */
	public boolean won(){
		//System.out.println(Arrays.toString(winArea));
		for(Point p : winArea)
			if(p.getX()==this.x() && p.getY()==this.y())
				return true;
		return false;
	}
	
	public boolean won(int x, int y){
		//System.out.println(Arrays.toString(winArea));
		for(Point p : winArea)
			if(p.getX()==x && p.getY()==y)
				return true;
		return false;
	}
	
	/**
	 * Gets the name of this Player's color. 
	 * Prompting Player by color is easier to follow than pnum.
	 * 
	 * @return String name of color
	 */
	public String getColorName(){
		if(pNumber== 1)
			return "Blue";
		else if(pNumber == 2)
			return "Red";
		else if(pNumber == 3)
			return "Green";
		else
			return "Yellow";
	}
	
	/**
	 * Gets this Player's available moves.
	 * 
	 * @return ArrayList of moves
	 */
	public ArrayList<String> getAvailableMoves(){
		return availableMoves;
	}
	
	/* Set Methods */

	/**
	 * Purpose: setPosition based on a string of form "<char><int>"
	 * Postcondition - new position set
	 * 
	 * @param pos String representation of position
	 * @return Point representation of position
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

	/**
	 * Purpose: Decrements this Player's wall total.
	 * Precondition: Player has placed a wall.
	 * Postcondition: Player's wall total is decremented.
	 */
	public void useWall(){
		wallTotal--;
	}

	/**
	 * Purpose: sets player's x position 
	 * Precondition: none
	 * Postcondition: none
	 * 
	 * @param x integer x coordinate
	 */
	public void setX(int x) {
		this.position.setLocation(x, this.y());
	}

	/**
	 * Purpose: sets player's y position 
	 * Precondition: none
	 * Postcondition: none
	 * 
	 * @param y integer y coordinate
	 */
	public void setY(int y) {
		this.position.setLocation(this.x(), y);
	}

	/**
	 * Purpose: sets player's position 
	 * Precondition: none
	 * Postcondition: none
	 * 
	 * @param x integer x coordinate
	 * @param y integer y coordinate
	 */
	public void setPos(int x, int y) {
		Point tmp = new Point(x, y);
		this.position = tmp;
	}

	/**
	 * Sets the win area for this Player based on pnum and start position.
	 */
	public void setWinArea(){
		winArea = new Point[9];
		
		if(this.getPnum()==1){
			for(int y=0; y<9; y++)
				this.winArea[y] = new Point(8,y);
		}
		
		if(this.getPnum()==2 && this.getStartx()==8){
			for(int y=0; y<9; y++)
				this.winArea[y] = new Point(0,y);
		
		}else if(this.getPnum()==2 && this.getStartx()==4){
			for(int x=0; x<9; x++)
				this.winArea[x] = new Point(x,0);
		}
		
		if(this.getPnum()==3){
			for(int y=0; y<9; y++)
				this.winArea[y] = new Point(0,y);
		}
		
		if(this.getPnum()==4){
			for(int x=0; x<9; x++)
				this.winArea[x] = new Point(x,8);
		}
	}
	
	/**
	 * Adds a move to a Player's available moves.
	 * 
	 * @param s String move
	 */
	public void addToMoves(String s){
		this.availableMoves.add(s);
	}
		
	/**
	 * Clears this Player's list of available moves.
	 */
	public void clearMoves(){
		this.availableMoves.clear();
	}
	
	
	/**
	 * Sets a Player's kicked flag to true. Method is called by 
	 * kickPlayer() in Game.
	 */
	public void kick(){
		System.out.println("Kicking "+this.getColorName());
		kicked = true;
	}

	/**
	 * Checks whether this player has been kicked.
	 * 
	 * @return boolean whether Player has been kicked
	 */
	public boolean hasBeenKicked(){
		return kicked;
	}
	
	public void setPath(ArrayList<String> a){
		this.path = a;
	}
	
	public ArrayList<String> getPath(){
		return this.path;
	}
}

package board;

import java.util.ArrayList;
import java.awt.Point;

import players.Player;
import walls.Wall;

/**
 * This class exists as a data container to track data about the
 * contents of the board and to provide methods to interact with the board.
 * 
 * @author Eli Donahue
 *
 */
public class Board {
	
	/* Instance variables */
	
	private ArrayList<Player> players;	// holds players on board
	private Wall[] walls;		// holds walls on board
	private int numWalls;		// total # walls on board
	
	/* Constructor */
	
	/**
	 * Constructs a Board object with the players and max number of walls
	 * passed in by the Game.
	 * 
	 * @param players array of Players in the game
	 * @param walls array of Wall objects to hold walls as they are played
	 */
	public Board(ArrayList<Player> players, int walls) {
		this.players = players;			// passed in from Game
		this.walls = new Wall[walls];	// Wall array length = max num of walls
		numWalls = 0;
	}
	
	/* Get Methods */
	
	/**
	 * Gets the array of Players.
	 * 
	 * @return array of Players
	 */
	public ArrayList<Player> players(){
		return players;
	}
	
	/**
	 * Gets the array of Walls.
	 * 
	 * @return array of Walls
	 */
	public Wall[] walls(){
		return walls;
	}
	
	/**
	 * Gets the number of walls played. This is the number of actual walls in
	 * the Walls array, which has a length equal to the max number of 
	 * playable walls.
	 * 
	 * @return an integer, number of walls played
	 */
	public int numWalls(){
		return numWalls;
	}
	
	/**
	 * Checks if a player has a path from its current location to its win area.
	 * Path can be accessed by Player.getPath().
	 * 
	 * @param x int x coord of player
	 * @param y int y coord of player
	 * @param mark positive int to mark were we've checked
	 * @param isJump boolean whether we're jumping/adding to path
	 * @param board int[][] to track where we've checked
	 * @param p Player
	 * @param path ArrayList<String> of moves
	 */
	public void checkPath(int x, int y, int mark, boolean isJump, int[][] board, Player p, ArrayList<String> path){
		// we already found the path. stop looking.
		if(!p.getPath().isEmpty())
			return;
		
		// make sure we're on the board
		if (x < 0 || y < 0 || x >= 9 || y >= 9){
        	path = new ArrayList<String>();
        	return;
		}
		
        // make sure we haven't already been here
        if(board[x][y]>0){
        	path = new ArrayList<String>();
        	return;
        }
        
        // make sure this is a legal move
        if (!isLegalMove(p, x, y)){
        	System.out.println("\t[Path error]: "+x+""+y+" is not a legal move for player at "+p.x()+""+p.y());
        	path = new ArrayList<String>();
        	return;
        }
        
        // set visited
        board[x][y]=mark;
        if(!isJump || p.won(x,y))
        	path.add(x+""+y);
        
        // see if it's a winner
        if(p.won(x,y)){
    		if(p.getPath().isEmpty()){
    			p.setPath(path);
    		}
    		return;
    	}else{
    		// look down first
    		if(p.getPnum()==1){
    			if (isLegalMove(p, x+1, y)&&p.getPath().isEmpty()){
    				isJump = isOccupiedByPlayer(x+1, y);
	    			p.setPos(x+1, y);
	    			checkPath(x+1, y, mark+1, isJump, board, p, path);
	    			p.setPos(x, y);
    			}
    			if (isLegalMove(p, x, y+1)&&p.getPath().isEmpty()){
    				isJump = isOccupiedByPlayer(x, y+1);
    				p.setPos(x, y+1);
    				checkPath(x, y+1, mark+1, isJump, board, p, path);
	    			p.setPos(x, y);
    			}
    			if (isLegalMove(p, x, y-1)&&p.getPath().isEmpty()){
    				isJump = isOccupiedByPlayer(x, y-1);
	    			p.setPos(x, y-1);
	    			checkPath(x, y-1, mark+1, isJump, board, p, path);
	    			p.setPos(x, y);
    			}
    			if (isLegalMove(p, x-1, y)&&p.getPath().isEmpty()){
    				isJump = isOccupiedByPlayer(x-1, y);
    				p.setPos(x-1, y);
    				checkPath(x-1, y, mark+1,  isJump, board, p, path);
	    			p.setPos(x, y);
    			}
    		// up first
    		}else if(p.getPnum()==2){
    			if (isLegalMove(p, x-1, y)&&p.getPath().isEmpty()){
    				isJump = isOccupiedByPlayer(x-1, y);
    				p.setPos(x-1, y);
    				checkPath(x-1, y, mark+1,  isJump, board, p, path);
    				p.setPos(x, y);
    			}
    			if (isLegalMove(p, x, y+1)&&p.getPath().isEmpty()){
    				isJump = isOccupiedByPlayer(x, y+1);
    				p.setPos(x, y+1);
    				checkPath(x, y+1, mark+1, isJump, board, p, path);
    				p.setPos(x, y);
    			}
    			if (isLegalMove(p, x, y-1)&&p.getPath().isEmpty()){
    				isJump = isOccupiedByPlayer(x, y-1);
	    			p.setPos(x, y-1);
	    			checkPath(x, y-1, mark+1, isJump, board, p, path);
	    			p.setPos(x, y);
    			}
    			if (isLegalMove(p, x+1, y)&&p.getPath().isEmpty()){
    				isJump = isOccupiedByPlayer(x+1, y);
	    			p.setPos(x+1, y);
	    			checkPath(x+1, y, mark+1, isJump, board, p, path);
	    			p.setPos(x, y);
    			}
    		// right first
    		}else if(p.getPnum()==3){
    			if (isLegalMove(p, x, y+1)&&p.getPath().isEmpty()){
    				isJump = isOccupiedByPlayer(x, y+1);
	    			p.setPos(x, y+1);
	    			checkPath(x, y+1, mark+1, isJump, board, p, path);
	    			p.setPos(x, y);
    			}
    			if (isLegalMove(p, x-1, y)&&p.getPath().isEmpty()){
    				isJump = isOccupiedByPlayer(x-1, y);
    				p.setPos(x-1, y);
    				checkPath(x-1, y, mark+1,  isJump, board, p, path);
    				p.setPos(x, y);
    			}
    			if (isLegalMove(p, x+1, y)&&p.getPath().isEmpty()){
    				isJump = isOccupiedByPlayer(x+1, y);
	    			p.setPos(x+1, y);
	    			checkPath(x+1, y, mark+1, isJump, board, p, path);
	    			p.setPos(x, y);
    			}
    			if (isLegalMove(p, x, y-1)&&p.getPath().isEmpty()){
    				isJump = isOccupiedByPlayer(x, y-1);
    				p.setPos(x, y-1);
    				checkPath(x, y-1, mark+1, isJump, board, p, path);
    				p.setPos(x, y);
    			}
    		// left first
    		}else{
    			if (isLegalMove(p, x, y-1)&&p.getPath().isEmpty()){
    				isJump = isOccupiedByPlayer(x, y-1);
    				p.setPos(x, y-1);
    				checkPath(x, y-1, mark+1, isJump, board, p, path);
    				p.setPos(x, y);
    			}
    			if (isLegalMove(p, x-1, y)&&p.getPath().isEmpty()){
    				isJump = isOccupiedByPlayer(x-1, y);
    				p.setPos(x-1, y);
    				checkPath(x-1, y, mark+1,  isJump, board, p, path);
    				p.setPos(x, y);
    			}
    			if (isLegalMove(p, x+1, y)&&p.getPath().isEmpty()){
    				isJump = isOccupiedByPlayer(x+1, y);
	    			p.setPos(x+1, y);
	    			checkPath(x+1, y, mark+1, isJump, board, p, path);
	    			p.setPos(x, y);
    			}
    			if (isLegalMove(p, x, y+1)&&p.getPath().isEmpty()){
    				isJump = isOccupiedByPlayer(x, y+1);
	    			p.setPos(x, y+1);
	    			checkPath(x, y+1, mark+1, isJump, board, p, path);
	    			p.setPos(x, y);
    			}
    		}
    	}
	}
	
	/**
	 * Finds the legal moves for a Player's pawn.
	 * This method is used to highlight possible moves in the UI.
	 * 
	 * @param p Player that we're checking moves for
	 * @return ArrayList of Strings indicating possible moves
	 */
	public ArrayList<String> possibleMoves(Player p){
		// check four directions
		Point[] directions = {new Point(p.x(), p.y()+1), 
				new Point(p.x(), p.y()-1), new Point(p.x()+1, p.y()), 
				new Point(p.x()-1, p.y())};
		
		for(Point d : directions){
			int x = (int) d.getX();
			int y = (int) d.getY();
			
			if(this.isOccupiedByPlayer(x,y)&&this.isLegalMove(p, x, y)){
				ArrayList<Player> checked = new ArrayList<Player>();
				checked.add(p);
				isLegalMove(p, checked, this.getPlayerAt(x,y));
			}else{
				if(isLegalMove(p, x, y)){
					p.addToMoves(x+""+y);
				}
			}
		}
		
		return p.getAvailableMoves();
	}
	
	/**
	 * Checks whether a player's pawn can be placed at (x,y). 
	 * This method is overloaded.
	 * 
	 * @param p Player trying to make a move
	 * @param x integer x coordinate of move
	 * @param y integer y coordinate of move
	 * @return boolean, whether or not the move is legal
	 */
	public boolean isLegalMove(Player p, int x, int y){
		// check that coordinates are in the valid range
		if(x > 8 || x < 0 || y > 8 || y < 0){
			return false;
		}
				
		// check that the space is only one away (add logic for jumping later)
		if(x > p.x()+1 || x < p.x()-1){
			System.out.println("\t[Illegal move error]: "+x+""+y+" is too far away.");
			return false;
		}
			
		if(y > p.y()+1 || y < p.y()-1){
			System.out.println("\t[Illegal move error]: "+x+""+y+" is too far away.");
			return false;
		}
		
		// make sure no wall is in the way
		for(int i=0; i<numWalls; i++){
			if(walls[i].isBetween(p.x(), p.y(), x, y)){
				System.out.println("\t[Illegal move error]: "+walls[i]+" is in the way.");
				return false;
			}
		}
				
		// we've checked illegal conditions 
		return true;
	}
	
	/**
	 * Checks available spaces of an adjacent Player. Method is called when
	 * a Player can jump off another Player. The adjacent Player's available
	 * moves are added to the calling Player's availableMove list.
	 * 
	 * @param calling Player, original Player seeking moves
	 * @param checking Player, adjacent to original Player
	 */
	private void isLegalMove(Player initial, ArrayList<Player> calling, Player checking){
		// check four directions
		Point[] directions = {new Point(checking.x(), checking.y()+1), 
				new Point(checking.x(), checking.y()-1), new Point(checking.x()+1, checking.y()), 
				new Point(checking.x()-1, checking.y())};
		
		for(Point d : directions){
			int x = (int) d.getX();
			int y = (int) d.getY();
			
			if(this.isOccupiedByPlayer(x,y) && calling.contains(this.getPlayerAt(x,y))){
				// ignore, already checked
			}else if(this.isOccupiedByPlayer(x,y)&&this.isLegalMove(checking, x, y)){
				// there's a player here, so we get that player's moves
				calling.add(checking);
				isLegalMove(initial, calling, this.getPlayerAt(x,y));
			}else{
				if(isLegalMove(checking, x, y)){
					initial.addToMoves(x+""+y);
				}
			}
		}
	}
	
	/**
	 * Checks whether a player can place the wall passed in. 
	 * This method is overloaded.
	 * 
	 * @param p Player trying to make a move
	 * @param w Wall that the Player is trying to place
	 * @return boolean, whether or not the move is legal
	 */
	public boolean isLegalWallPlacement(Player p, Wall w) {
		// check that player can play a wall
		if(p.getWalls()<=0){
			System.out.println("\t[Wall error]: Player "+p.getPnum()+" has no walls to place");
			return false;
		}
		
		// check that the wall's type is valid
		if(!(w.type().equals("h") || w.type().equals("v"))){
			System.out.println("\t[Wall error]: '"+w.type()+"' is not a legal wall type.");
			return false;
		}
		
		// check that (x,y) is on grid and it's ok to place a wall there
		if(w.getX() > 7 || w.getY() > 7 || w.getX() < 0 || w.getY() < 0){
			System.out.println("\t[Wall error]: "+w.getX()+""+w.getY()+" is out of bounds.");
			return false;	
		}
		
		// check that wall is not occupied or intersected by another wall
		for(int i=0; i<numWalls; i++){
			if(walls[i].intersects(w) || walls[i].overlaps(w)){
				System.out.println("\t[Wall error]: "+w+" collides with "+walls[i]);
				return false;
			}
		}
						
		// make sure it doesn't prevent a player from reaching end
		Board tmp = this;
		if(tmp.addWallWithoutValidation(w)){
			for(Player player : tmp.players()){
				checkPath(player.x(), player.y(), 1, true, new int[9][9], player, new ArrayList<String>());
				//System.out.println("Player "+player.getPnum()+" at "+player.x()+""+player.y());
				//System.out.println(player.getPath());
				if(player.getPath().isEmpty()){
					System.out.println("\t[Wall error]: "+w+" will block Player "+player.getPnum()+" from reaching win area.");
					tmp.removeLastWall();
					return false;
				}
				player.setPath(new ArrayList<String>());
			}
		}else{
			return false;
		}
		return true;
	}
	
	/**
	 * Tells whether a given space is occupied by a player.
	 * 
	 * @param x integer x coordinate
	 * @param y integer y coordinate
	 * @return boolean whether a player occupies space (x,y)
	 */
	public boolean isOccupiedByPlayer(int x, int y){
		for(Player p: players)
			if(p.x()==x && p.y()==y)
				return true;
		return false;
	}
	
	/**
	 * Gets the player occupying a given space.
	 * 
	 * @param x integer x coordinate
	 * @param y integer y coordinate
	 * @return Player at space (x,y)
	 */
	public Player getPlayerAt(int x, int y){
		for(Player p: players)
			if(p.x()==x && p.y()==y)
				return p;
		return null;
	}
	
	/**
	 * Gives a simple string representation of the Board. 
	 * Includes the number of players and number of walls played.
	 * 
	 * @return a String with number of players and walls played
	 */
	public String toString(){
		String s = "Board: ";
		s += players.size() + " players | ";
		s += numWalls + " walls";
		return s;
	}
	
	/* Set Methods */
	
	/**
	 * Updates the Player's location, if legal.
	 * Returns whether or not move was successfully made.
	 * 
	 * @param p Player attempting the move
	 * @param x integer x coordinate
	 * @param y integer y coordinate 
	 * @return boolean, whether or not move was successfully made
	 */
	public boolean placePawn(Player p, int x, int y) {
		if(p.getAvailableMoves().contains(x+""+y)){
			p.setPos(x,y);
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Adds a wall to the wall array, if legal.
	 * Returns whether or not wall was successfully placed.
	 * 
	 * @param p Player attempting the wall placement
	 * @param x integer x coordinate
	 * @param y integer y coordinate 
	 * @param type String indicating wall orientation ("v" or "h")
	 * @return boolean, whether or not wall was successfully placed
	 */
	public boolean placeWall(Player p, int x, int y, String type){
		Wall w = new Wall(x, y, type);
		if(isLegalWallPlacement(p, w)){
			p.useWall();
			walls[numWalls] = w;
			numWalls++;
			return true;
		}else{
			return false;
		}
	}	
	
	/**
	 * Updates the board's player list after the game has kicked someone.
	 * 
	 * @param p ArrayList of Players
	 */
	public void updatePlayers(ArrayList<Player> p){
		players = p;
	}
	
	/**
	 * Add a wall without checking validity. Use only for testing.
	 * 
	 * @param w Wall
	 */
	public boolean addWallWithoutValidation(Wall w){
		if(numWalls>=walls.length)
			return false;
		walls[numWalls] = w;
		numWalls++;
		return true;
	}
	
	/**
	 * Remove the last wall we placed. 
	 */
	public boolean removeLastWall(){
		if(numWalls>=walls.length)
			return false;
		this.walls[numWalls-1]=null;
		numWalls--;
		return true;
	}
}

package walls;

/**
 * Wall object holds info about an individual wall. 
 * 
 * @author Eli Donahue
 *
 */
/**
 * @author emilydonahue
 *
 */
public class Wall {
	
	/* Instance Variables */
	
	private int x;
	private int y;
	private String type;	// h or v
	
	/* Constructor */
	
	public Wall(int x, int y, String type){
		this.x = x;
		this.y = y;
		this.type = type;
	}
	
	/* Get Methods */
	
	/**
	 * Gets this Wall's x value.
	 * 
	 * @return integer, x coordinate
	 */
	public int getX(){
		return this.x;
	}
	
	/**
	 * Gets this Wall's y value.
	 * 
	 * @return integer, y coordinate
	 */
	public int getY(){
		return this.y;
	}
	
	/**
	 * Gets this Wall's type.
	 * 
	 * @return String, orientation type (h or v)
	 */
	public String type(){
		return this.type;
	}
	
	/**
	 * Checks whether given Wall intersects this Wall. Method works by checking 
	 * the Walls' centers, so it also returns true if the Walls are the same.
	 * 
	 * @param w Wall to check against this Wall
	 * @return boolean, whether or not given Wall intersects this one
	 */
	public boolean intersects(Wall w){
		return this.x == w.getX() && this.y == w.getY();
	}
	
	
	/**
	 * Checks if a given wall overlaps this wall. Used to check whether two 
	 * vertical or two horizontal walls overlap.
	 * 
	 * @param w
	 * @return
	 */
	public boolean overlaps(Wall w){
		if(this.type.equals(w.type()))
			return false;
		if(this.x == w.getX()-1 && this.y == w.getY())
			return true;
		if(this.x == w.getX() && this.y == w.getY()-1)
			return true;
		return false;
	}
	
	/**
	 * Checks if this Wall is directly between two points. 
	 * Used for checking if there is a Wall between a Player and a position it 
	 * wants to move to.
	 * 
	 * @param x1 integer, x coordinate of the first point
	 * @param y1 integer, y coordinate of the first point
	 * @param x2 integer, x coordinate of the second point
	 * @param y2 integer, y coordinate of the second point
	 * @return boolean, whether or not this Wall is directly between the points
	 */
	public boolean isBetween(int x1, int y1, int x2, int y2){
		// check: top right, top left
		// bottom right, bottom left
		if(type.equals("h")){
			return ((this.x==x1 && this.y==y1 && this.x==x2-1 && this.y==y2) || 
				(this.x==x1-1 && this.y==y1 && this.x==x2 && this.y==y2) ||
				(this.x==x1 && this.y==y1-1 && this.x==x2-1 && this.y==y2-1) ||
				(this.x==x1-1 && this.y==y1-1 && this.x==x2 && this.y==y2-1));	
		}else if(type.equals("v")){	
			// check: left down, left up, 
			// right down, right up
			return ((this.x==x1 && this.y==y1 && this.x==x2 && this.y==y2-1) || 
				(this.x==x1 && this.y==y1-1 && this.x==x2 && this.y==y2) ||
				(this.x==x1-1 && this.y==y1 && this.x==x2-1 && this.y==y2-1) ||
				(this.x==x1-1 && this.y==y1-1 && this.x==x2-1 && this.y==y2));
		}
		return false;
	}
}


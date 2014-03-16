/* This will be an object to hold info about an individual wall */

package walls;

import java.awt.Point;

public class Wall {
	
	// instance variables
	private int x;
	private int y;
	private String type;	// horizontal or vertical
	
	// constructor
	public Wall(int x, int y, String type){
		this.x = x;
		this.y = y;
		this.type = type;
	}
	
	// returns the wall's x value
	public int getX(){
		return this.x;
	}
	
	// returns the wall's y value
	public int getY(){
		return this.y;
	}
	
	// returns the wall's type
	public String type(){
		return this.type;
	}
	
	// returns whether the wall passed in intersects this wall
	public boolean intersects(Wall w){
		return this.getCenter().equals(w.getCenter());
	}
	
	// returns a point representing center of this wall
	public Point getCenter(){
		Point p;
		if(this.type == "v"){
			p = new Point(this.x, this.y + 1);
		}else{
			p = new Point(this.x + 1, this.y);
		}
		return p;
	}
	
}

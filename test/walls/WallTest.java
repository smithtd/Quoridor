/* Author: Eli Donahue
 * Tests for Wall class
 */
package walls;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import walls.Wall;
import java.awt.Point;

public class WallTest {
	private Wall wall;
	
	@Before
	public void initialize() {
		wall = new Wall(1,0,"v");		// vertical wall starting at (0,1)
	}
	
	// test intersects(Wall)
	private void testResults(Wall w, boolean expectedResult) {
		boolean result = wall.intersects(w);
	    assertThat(result, equalTo(expectedResult));    
	}
	
	// test intersects(Wall)
	private void testResults(Wall w, Point expectedResult) {
		Point result = w.getCenter();
	    assertThat(result, equalTo(expectedResult));    
	}
	
	/* Test intersects() */
	
	@Test
	public void checkIfAdjacentVerticalWallIntersects(){
		Wall w = new Wall(2,0,"v");
		boolean expected = false;
		
		testResults(w, expected);
	}
	
	@Test
	public void checkIfAdjacentHorizontalWallIntersects(){
		Wall w = new Wall(0,2,"h");
		boolean expected = false;
		
		testResults(w, expected);
	}
	
	@Test
	public void checkIfIntersectingWallIntersects(){
		Wall w = new Wall(0,1,"h");
		boolean expected = true;
		
		testResults(w, expected);
	}
	
	/* Test getCenter() */
	
	@Test
	public void checkCenterOfVerticalWall(){
		Point expected = new Point(1,1);
		
		testResults(wall, expected);
	}
	
	@Test
	public void checkCenterOfHorizontalWall(){
		Wall w = new Wall(3,5,"h");
		Point expected = new Point(4,5);
		
		testResults(w, expected);
	}
}
/* Author: Eli Donahue
 * Tests for Wall class
 */
package walls;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import walls.Wall;

public class WallTest {
	private Wall wall;
	private Wall wall2;
	private Wall wall3;
	
	@Before
	public void initialize() {
		wall = new Wall(1,0,"v");		// vertical wall starting at (0,1)
		wall2 = new Wall(3,3,"h");		// horizontal wall starting at (3,3)
		wall3 = new Wall(5,0,"v");		// vertical wall starting at (0,5)
	}
	
	// test intersects(Wall)
	private void testResults(Wall w, boolean expectedResult) {
		boolean result = wall.intersects(w);
	    assertThat(result, equalTo(expectedResult));    
	}
	
	// test overlaps(Wall)
		private void testResults(Wall w, Wall original, boolean expectedResult) {
			boolean result = original.overlaps(w);
		    assertThat(result, equalTo(expectedResult));    
		}
	
	// test isBetween(x1, y1, x2, y2)
	private void testResults(Wall w, int x1, int y1, int x2, int y2, boolean expectedResult) {
		boolean result = w.isBetween(x1, y1, x2, y2);
	    assertThat(result, equalTo(expectedResult));    
	}
	
	/*
	 * X,Y COORDINATES ARE REVERSED IN TEST BECAUSE THEY WEREN'T PRODUCED
	 * BY THE PARSER. WEIRD QUIRK, I DON'T WANT TO DEAL WITH IT.
	 */
	
	
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
	public void checkIfPerpindicularWallIntersects(){
		Wall w = new Wall(0,1,"h");
		boolean expected = false;
		
		testResults(w, expected);
	}
	
	@Test
	public void checkIfIntersectingWallIntersects(){
		Wall w = new Wall(1,0,"h");
		boolean expected = true;
		
		testResults(w, expected);
	}
	
	/* Test overlaps() */
	
	@Test
	public void checkThatHorizontalWallOnLeftOverlaps(){
		Wall w = new Wall(3,2,"h");
		boolean expected = true;
		
		testResults(w, wall2, expected);
	}
	
	@Test
	public void checkThatHorizontalWallOnLeftDoesNotOverlap(){
		Wall w = new Wall(3,1,"h");
		boolean expected = false;
		
		testResults(w, wall2, expected);
	}
	
	@Test
	public void checkThatHorizontalWallOnRightOverlaps(){
		Wall w = new Wall(3,4,"h");
		boolean expected = true;
		
		testResults(w, wall2, expected);
	}
	
	@Test
	public void checkThatHorizontalWallOnRightDoesNotOverlap(){
		Wall w = new Wall(3,5,"h");
		boolean expected = false;
		
		testResults(w, wall2, expected);
	}
	
	@Test
	public void checkThatVerticalWallOnTopOverlaps(){
		Wall w = new Wall(4,0,"v");
		boolean expected = true;
		
		testResults(w, wall3, expected);
	}
	
	@Test
	public void checkThatVerticalWallOnTopDoesNotOverlap(){
		Wall w = new Wall(3,0,"v");
		boolean expected = false;
		
		testResults(w, wall3, expected);
	}
	
	@Test
	public void checkThatVerticalWallOnBottomOverlaps(){
		Wall w = new Wall(6,0,"v");
		boolean expected = true;
		
		testResults(w, wall3, expected);
	}
	
	@Test
	public void checkThatVerticalWallOnBottomDoesNotOverlap(){
		Wall w = new Wall(7,0,"v");
		boolean expected = false;
		
		testResults(w, wall3, expected);
	}
	
	/* Test isBetween() for Horizontal Walls */
	
	@Test
	public void checkLeftSideOfHorizontalWallBetweenPoints(){
		Wall w = new Wall(5, 1, "h");
		boolean expected = true;
		
		testResults(w,5,1,6,1,expected);
	}
	
	@Test
	public void checkPointsToLeftOfHorizontalWall(){
		Wall w = new Wall(5, 1, "h");
		boolean expected = false;
		
		testResults(w,4,1,5,1,expected);
	}
	
	@Test
	public void checkPointsAdjacentToLeftSideOfHorizontalWall(){
		Wall w = new Wall(5, 1, "h");
		boolean expected = false;
		
		testResults(w,5,0,5,1,expected);
	}
	
	@Test
	public void checkRightSideOfHorizontalWallBetweenPoints(){
		Wall w = new Wall(5, 1, "h");
		boolean expected = true;
		
		testResults(w,5,2,6,2,expected);
	}
	
	@Test
	public void checkPointsToRightOfHorizontalWall(){
		Wall w = new Wall(5, 1, "h");
		boolean expected = false;
		
		testResults(w,5,3,6,3,expected);
	}
	
	@Test
	public void checkPointsAdjacentToRightSideOfHorizontalWall(){
		Wall w = new Wall(5, 1, "h");
		boolean expected = false;
		
		testResults(w,5,3,5,4,expected);
	}
	
	@Test
	public void checkLeftToRightOverHorizontalWall(){
		Wall w = new Wall(5, 1, "h");
		boolean expected = false;
		
		testResults(w,5,1,5,2,expected);
	}
	
	@Test
	public void checkRightToLeftOverHorizontalWall(){
		Wall w = new Wall(5, 1, "h");
		boolean expected = false;
		
		testResults(w,5,2,5,1,expected);
	}
	
	@Test
	public void checkRightToLeftBelowHorizontalWall(){
		Wall w = new Wall(5, 1, "h");
		boolean expected = false;
		
		testResults(w,6,1,6,2,expected);
	}
	
	@Test
	public void checkLeftToRightBelowHorizontalWall(){
		Wall w = new Wall(5, 1, "h");
		boolean expected = false;
		
		testResults(w,6,2,6,1,expected);
	}
	
/* Test isBetween() for Vertical Walls */
	
	@Test
	public void checkUpperSideOfVerticalWallBetweenPoints(){
		Wall w = new Wall(5, 1, "v");
		boolean expected = true;
		
		testResults(w,5,1,5,2,expected);
	}
	
	@Test
	public void checkPointsAboveVerticalWall(){
		Wall w = new Wall(5, 1, "v");
		boolean expected = false;
		
		testResults(w,4,1,5,1,expected);
	}
	
	@Test
	public void checkPointsAdjacentToTopOfVerticalWall(){
		Wall w = new Wall(5, 1, "v");
		boolean expected = false;
		
		testResults(w,5,0,6,0,expected);
	}
	
	@Test
	public void checkLowerSideOfVerticalWallBetweenPoints(){
		Wall w = new Wall(5, 1, "v");
		boolean expected = true;
		
		testResults(w,6,1,6,2,expected);
	}
	
	@Test
	public void checkPointsBelowVerticalWall(){
		Wall w = new Wall(5, 1, "v");
		boolean expected = false;
		
		testResults(w,7,1,7,2,expected);
	}
	
	@Test
	public void checkPointsAdjacentToBottomOfVerticalWall(){
		Wall w = new Wall(5, 1, "v");
		boolean expected = false;
		
		testResults(w,5,3,5,4,expected);
	}
	
	@Test
	public void checkTopToBottomOnLeftOfVerticalWall(){
		Wall w = new Wall(5, 1, "v");
		boolean expected = false;
		
		testResults(w,5,1,6,1,expected);
	}
	
	@Test
	public void checkBottomToTopOnLeftOfVerticalWall(){
		Wall w = new Wall(5, 1, "v");
		boolean expected = false;
		
		testResults(w,6,1,5,1,expected);
	}
	
	@Test
	public void checkTopToBottomOnRightOfVerticalWall(){
		Wall w = new Wall(5, 1, "v");
		boolean expected = false;
		
		testResults(w,5,2,6,2,expected);
	}
	
	@Test
	public void checkBottomToTopOnRightOfVerticalWall(){
		Wall w = new Wall(5, 1, "v");
		boolean expected = false;
		
		testResults(w,6,2,5,2,expected);
	}
}
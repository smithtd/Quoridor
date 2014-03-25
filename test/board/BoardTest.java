/* Author: Eli Donahue
 * Tests for Board class
 */
package board;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import board.Board;
import players.Player;
import walls.Wall;

public class BoardTest {
	private Board board;
	private Player[] players;
	
	@Before
	public void initialize() {
		players = new Player[4];
		players[0] = new Player("p1", 1, 0, 1);
		players[1] = new Player("p2", 2, 8, 2);
		players[2] = new Player("p3", 8, 1, 3);
		players[3] = new Player("p4", 8, 2, 4);
		board = new Board(players, 20);
		board.placeWall(players[0], 3, 3, "v");
	}
	
	// test placePawn 
	private void testPawnResults(Player p, int x, int y, boolean expected) {
		boolean result = board.placePawn(p, x, y);
	    assertThat(result, equalTo(expected));    
	}
	
	// test placeWall 
	private void testResults(Player p, int x, int y, String type, boolean expected) {
		boolean result = board.placeWall(p, x, y, type);
	    assertThat(result, equalTo(expected));    
	}
	
	// test isLegalMove (pawn version)
	private void testResults(Player p, int x, int y, boolean expected) {
		boolean result = board.isLegalMove(p, x, y);
	    assertThat(result, equalTo(expected));  
	}
	
	// test isLegalMove (wall version)
	private void testResults(Player p, Wall w, boolean expected) {
		boolean result = board.isLegalMove(p, w);
	    assertThat(result, equalTo(expected));  
	}

	/* Test placePawn() */
	
	@Test
	public void checkIfCanMovePawnToEmptySquare(){
		int x=2;
		int y=0;
		boolean expected = true;
		
		testPawnResults(players[0], x, y, expected);
	}

	@Test
	public void checkIfCanMovePawnToOccupiedSquare(){
		int x=8;
		int y=1;
		boolean expected = false;
	
		testResults(players[3], x, y, expected);
	}
	
	@Test
	public void checkIfMovePawnOutsideBoardHigh(){
		int x=9;
		int y=1;
		boolean expected = false;
	
		testResults(players[2], x, y, expected);
	}
	
	@Test
	public void checkIfCanMoveOutsideBoardLow(){
		int x=1;
		int y=-1;
		boolean expected = false;
	
		testResults(players[0], x, y, expected);
	}

	/* Test placeWall() */
	
	@Test
	public void checkIfCanPlaceVerticalWallInAValidSpace(){
		int x = 1;
		int y = 1;
		String type = "v";
		boolean expected = true;
		
		testResults(players[0], x, y, type, expected);
	}
	
	@Test
	public void checkIfCanPlaceHorizontalWallInAValidSpace(){
		int x = 0;
		int y = 6;
		String type = "h";
		boolean expected = true;
		
		testResults(players[0], x, y, type, expected);
	}
	
	/* Test isLegalMove() - pawn version */
	/* NEEDS MORE TESTING WHEN LOGIC COMPLETE */
	
	@Test
	public void checkValidPawnMoveLeft(){
		int x=0;
		int y=0;
		boolean expected = true;
	
		testResults(players[0], x, y, expected);
	} 
	
	@Test
	public void checkValidPawnMoveRight(){
		int x=2;
		int y=0;
		boolean expected = true;
	
		testResults(players[0], x, y, expected);
	} 
	
	@Test
	public void checkValidPawnMoveDown(){
		int x=1;
		int y=1;
		boolean expected = true;
	
		testResults(players[0], x, y, expected);
	}
	
	@Test
	public void checkValidPawnMoveUp(){
		int x=2;
		int y=7;
		boolean expected = true;
	
		testResults(players[1], x, y, expected);
	}
	
	@Test
	public void checkInvalidPawnMoveUp(){
		int x=1;
		int y=-1;
		boolean expected = false;
	
		testResults(players[0], x, y, expected);
	} 
	
	@Test
	public void checkInvalidPawnMoveLeft(){
		int x=0;
		int y=7;
		boolean expected = false;
	
		testResults(players[1], x, y, expected);
	}
	
	@Test
	public void checkInvalidPawnMoveRight(){
		int x=0;
		int y=7;
		boolean expected = false;
	
		testResults(players[1], x, y, expected);
	}
	
	@Test
	public void checkInvalidPawnMoveDown(){
		int x=0;
		int y=9;
		boolean expected = false;
	
		testResults(players[1], x, y, expected);
	}
	
	/* Test isLegalMove() - wall version */
	/* NEEDS MORE TESTING WHEN LOGIC COMPLETE */
	
	@Test
	public void checkInvalidType(){
		Wall w = new Wall(1, 1, "Pizza");
		boolean expected = false;
	
		testResults(players[0], w, expected);
	} 
	
	@Test
	public void checkValidHorizontalType(){
		Wall w = new Wall(5, 1, "h");
		boolean expected = true;
	
		testResults(players[0], w, expected);
	} 
	
	@Test
	public void checkValidVerticalType(){
		Wall w = new Wall(1, 1, "v");
		boolean expected = true;

		testResults(players[0], w, expected);
	}
	
	@Test
	public void checkHorizontalOutOfBoundsLeft(){
		Wall w = new Wall(-1, 1, "h");
		boolean expected = false;
	
		testResults(players[0], w, expected);
	}
	
	@Test
	public void checkHorizontalOutOfBoundsRight(){
		Wall w = new Wall(8, 1, "h");
		boolean expected = false;
	
		testResults(players[0], w, expected);
	}
	
	@Test
	public void checkVerticalOutOfBoundsTop(){
		Wall w = new Wall(1, -1, "v");
		boolean expected = false;
	
		testResults(players[0], w, expected);
	}
	
	@Test
	public void checkVerticalOutOfBoundsBottom(){
		Wall w = new Wall(1, 8, "v");
		boolean expected = false;
	
		testResults(players[0], w, expected);
	}
	
	@Test
	public void checkWallOnExistingWall(){
		Wall w = new Wall(3, 3, "v");
		boolean expected = false;
	
		testResults(players[0], w, expected);
	}
	
}


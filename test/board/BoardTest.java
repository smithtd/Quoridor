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

public class BoardTest {
	private Board board;
	private Player player;
	
	@Before
	public void initialize() {
		board = new Board();
		board.bitmap[0][1] = 1; 
		player = new Player("player1", 5);
	}
	
	// test isEmpty
	private void testResults(int x, int y, boolean expectedResult) {
		boolean result = board.isEmpty(x, y);
	    assertThat(result, equalTo(expectedResult));    
	}
	
	// test placePawn by comparing board
	private void testResults(Player p, int x, int y, Board expectedBoard) {
		board.placePawn(p, x, y);
	    assertThat(board.bitmap, equalTo(expectedBoard.bitmap));    
	}
	
	// test placePawn by checking for "false" return
	private void testResults(Player p, int x, int y, boolean expected) {
		boolean result = board.placePawn(p, x, y);
	    assertThat(result, equalTo(expected));    
	}
	
	// test isLegalMove
	private void testResults(Player p, String type, int x, int y, boolean expected) {
		boolean result = board.isLegalMove(p, type, x, y);
	    assertThat(result, equalTo(expected));  
	}
	
	// test placeWall, type horizontal, by comparing board
	private void testResults(Player p, String orientation, int x, int y, Board expectedBoard) {
		board.placeWall(p,orientation, x, y);
	    assertThat(board.bitmap, equalTo(expectedBoard.bitmap));    
	}
	
	// test placeWall by checking for "false" return
	private void testResults(String orientation, Player p, int x, int y, boolean expected) {
		boolean result = board.placeWall(p, orientation, x, y);
	    assertThat(result, equalTo(expected));    
	}
	
	/* Test isEmpty() */
	
	@Test
	public void checkIfSingleSquareIsEmpty(){
		int x = 0;
		int y = 0; 
		boolean expected = true;
		
		testResults(x, y, expected);
	}
	
	@Test 
	public void checkIfSingleOccupiedSquareIsEmpty() {
		int x = 0;
		int y = 1; 
		boolean expected = false;
		
		testResults(x, y, expected);
	}
	
	@Test 
	public void checkIfOutOfBoundsHighSquareIsEmpty() {
		int x = 0;
		int y = 10; 
		boolean expected = false;
		
		testResults(x, y, expected);
	}
	
	@Test 
	public void checkIfOutOfBoundsLowSquareIsEmpty() {
		int x = -1;
		int y = 0; 
		boolean expected = false;
		
		testResults(x, y, expected);
	}

	/* Test placePawn() */
	
	@Test
	public void checkIfCanAddNewPawnToEmptySquare(){
		int x=0;
		int y=0;
		Board expected = new Board();
		expected.bitmap[0][1] = 1;
		expected.bitmap[0][0] = 1;
		
		testResults(player, x, y, expected);
	}
	
	@Test
	public void checkIfCanMoveOldPawnToEmptySquare(){
		// player's old pos is (0,0)
		player.setPos(0, 0);
		// new pos will be (0,2)
		int x=0;
		int y=2;
		Board expected = new Board();
		expected.bitmap[0][1] = 1;
		expected.bitmap[0][2] = 1;
		
		testResults(player, x, y, expected);
	}

	@Test
	public void checkIfCanAddNewPawnToOccupiedSquare(){
		int x=0;
		int y=1;
		boolean expected = false;
	
		testResults(player, x, y, expected);
	}
	
	@Test
	public void checkIfCanAddNewPawnOutsideBoardHigh(){
		int x=10;
		int y=1;
		boolean expected = false;
	
		testResults(player, x, y, expected);
	}
	
	@Test
	public void checkIfCanAddNewPawnOutsideBoardLow(){
		int x=-1;
		int y=1;
		boolean expected = false;
	
		testResults(player, x, y, expected);
	}

	/* Test isLegalMove() */
	/* NEEDS MORE TESTING WHEN LOGIC COMPLETE */
	
	@Test
	public void checkIfCanMakeMoveOfInvalidType(){
		int x=0;
		int y=0;
		String type = "pizza";
		boolean expected = false;
	
		testResults(player, type, x, y, expected);
	} 
	
	@Test
	public void checkIfCanMakeMoveOfValidType(){
		int x=1;
		int y=0;
		String type = "horizontal";
		boolean expected = true;
	
		testResults(player, type, x, y, expected);
	} 

	/* Test placeWall() */
	
	@Test
	public void checkIfCanAddHorizontalWallInEmptySquares(){
		int x=1;
		int y=0;
		String orientation = "horizontal";
		Board expected = new Board();
		expected.bitmap[0][1] = 1;
		expected.bitmap[1][0] = 1;
		expected.bitmap[2][0] = 1;
	
		testResults(player, orientation, x, y, expected);
	} 

	@Test
	public void checkIfCanAddVerticalWallInEmptySquares(){
		int x=1;
		int y=0;
		String orientation = "vertical";
		Board expected = new Board();
		expected.bitmap[0][1] = 1;
		expected.bitmap[1][0] = 1;
		expected.bitmap[1][1] = 1;
	
		testResults(player, orientation, x, y, expected);
	} 

	@Test
	public void checkIfCanAddNewHorizontalWallOutsideBoardHigh(){
		int x=8;
		int y=1;
		boolean expected = false;
	
		testResults("horizontal", player, x, y, expected);
	}
	
	@Test
	public void checkIfCanAddNewHorizontalWallOutsideBoardLow(){
		int x=-1;
		int y=0;
		boolean expected = false;
	
		testResults("horizontal", player, x, y, expected);
	}
	
	@Test
	public void checkIfCanAddNewVerticalWallOutsideBoardHigh(){
		int x=0;
		int y=8;
		boolean expected = false;
	
		testResults("vertical", player, x, y, expected);
	}
	
	@Test
	public void checkIfCanAddNewVerticalWallOutsideBoardLow(){
		int x=0;
		int y=-1;
		boolean expected = false;
	
		testResults("vertical", player, x, y, expected);
	}
}


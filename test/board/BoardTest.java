/* Author: Eli Donahue
 * Tests for Board class
 */
package board;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import board.Board;

public class BoardTest {
	private Board board;
	
	@Before
	public void initialize() {
		board = new Board();
		board.bitmap[0][1] = 1; 
	}
	
	private void testResults(int x, int y, boolean expectedResult) {
		boolean result = board.isEmpty(x, y);
	    assertThat(result, equalTo(expectedResult));    
	}
	
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
	
}

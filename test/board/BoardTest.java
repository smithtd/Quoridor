/* Author: Eli Donahue
 * Tests for Board class
 */
package board;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

//import java.awt.Point;
import java.util.ArrayList;
//import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import board.Board;
import players.Player;
import walls.Wall;

public class BoardTest {
	
	private Board board;
	private ArrayList<Player> players;
	
	@Before
	public void initialize() {
		players = new ArrayList<Player>();
		players.add( new Player("p1", 1, 0, 1, 5, null));	//e1 (top)(check x+1 first)
		players.add( new Player("p2", 2, 8, 2, 5, null));	//e9 (bottom)(check x-1 first)
		players.add( new Player("p3", 8, 1, 3, 5, null));	//a5 (left)(check y+1 first)
		players.add( new Player("p4", 8, 2, 4, 5, null));	//i5 (right)(check y-1 first)
		board = new Board(players, 20);
		board.addWallWithoutValidation(new Wall(3, 3, "v"));
		board.addWallWithoutValidation(new Wall(5, 1, "h"));
		board.addWallWithoutValidation(new Wall(2, 0, "h"));
		for(Player p : players){
			board.possibleMoves(p);
		}
		
	}
	
	// test checkForPath
	private void testResults(Player p, int x, int y, ArrayList<String> expected) {
		System.out.println("Player loc = "+p.x()+""+p.y());
		board.checkPath(x, y, 1, true, new int[9][9], p, new ArrayList<String>());
		System.out.println("Player after loc = "+p.x()+""+p.y());
		System.out.println("path = "+p.getPath());
		ArrayList<String> result = p.getPath();
		assertThat(result, equalTo(expected));  
	}
	
	// test checkForPath with specific board
	private void testResults(Board b, Player p, int x, int y, ArrayList<String> expected) {
		b.checkPath(x, y, 1, true, new int[9][9], p, new ArrayList<String>());
		System.out.println("path = "+p.getPath());
		ArrayList<String> result = p.getPath();
		assertThat(result, equalTo(expected));  
	}
	
	// test placePawn - unused param is to distinguish from isLegalMove test
	private void testResults(Player p, int x, int y, boolean expected, int unused) {
		boolean result = board.placePawn(p, x, y);
	    assertThat(result, equalTo(expected));    
	}
	
	// test placeWall
	private void testResults(Player p, int x, int y, String type, boolean expected) {
		boolean result = board.placeWall(p, x, y, type);
	    assertThat(result, equalTo(expected));    
	}

	// test placeWall with specific board
		private void testResults(Board b, Player p, int x, int y, String type, boolean expected) {
			boolean result = b.placeWall(p, x, y, type);
		    assertThat(result, equalTo(expected));    
		}
	
	// test isLegalMove 
	private void testResults(Player p, int x, int y, boolean expected) {
		boolean result = board.isLegalMove(p, x, y);
	    assertThat(result, equalTo(expected));  
	}
	
	// test isLegalWall 
	private void testResults(Player p, Wall w, boolean expected) {
		boolean result = board.isLegalWallPlacement(p, w);
	    assertThat(result, equalTo(expected));  
	}

	
	/* Test checkForPath() */
	
	@Test
	public void check1(){
		Player p = players.get(0);
		int x=p.x();
		int y=p.y();
		System.out.println("Starting point = "+x+""+y);
		ArrayList<String> expected = new ArrayList<String>();
		expected.add("20");
		expected.add("21");
		expected.add("22");
		expected.add("32");
		expected.add("42");
		expected.add("52");
		expected.add("53");
		expected.add("63");
		expected.add("73");
		expected.add("83");
		
		testResults(p, x, y, expected);
	}
	
	@Test
	public void check2(){
		Player p = players.get(1);
		int x=p.x();
		int y=p.y();
		System.out.println("Starting point = "+x+""+y);
		ArrayList<String> expected = new ArrayList<String>();
		expected.add("18");
		expected.add("08");
		
		testResults(p, x, y, expected);
	}
	
	@Test
	public void check3(){
		Player p = players.get(2);
		int x=p.x();
		int y=p.y();
		System.out.println("Starting point = "+x+""+y);
		ArrayList<String> expected = new ArrayList<String>();
		expected.add("83");
		expected.add("84");
		expected.add("85");
		expected.add("86");
		expected.add("87");
		expected.add("88");
		
		testResults(p, x, y, expected);
	}
	
	@Test
	public void check4(){
		Player p = players.get(3);
		int x=p.x();
		int y=p.y();
		System.out.println("Starting point = "+x+""+y);
		ArrayList<String> expected = new ArrayList<String>();
		expected.add("80");
		
		testResults(p, x, y, expected);
	}
	
	@Test
	public void checkBoxedInPath(){
		Player p = players.get(0);
		int x=p.x();
		int y=p.y();
		// box in
		Board b = board;
		b.addWallWithoutValidation(new Wall(0,0,"h"));
		b.addWallWithoutValidation(new Wall(1,1,"v"));
		ArrayList<String> expected = new ArrayList<String>();
		
		testResults(b, p, x, y, expected);
	}
	
	@Test
	public void checkWalledOffPath(){
		Player p = players.get(1);
		int x=p.x();
		int y=p.y();
		// box in
		Board b = board;
		b.addWallWithoutValidation(new Wall(0,7,"h"));
		b.addWallWithoutValidation(new Wall(0,5,"h"));
		b.addWallWithoutValidation(new Wall(0,3,"h"));
		b.addWallWithoutValidation(new Wall(0,1,"h"));
		b.addWallWithoutValidation(new Wall(1,0,"v"));
		ArrayList<String> expected = new ArrayList<String>();
		
		testResults(b, p, x, y, expected);
	}
	
	/* Test placePawn() */
	
	@Test
	public void checkIfCanMovePawnToEmptySquare(){
		int x=2;
		int y=0;
		boolean expected = true;
		
		testResults(players.get(0), x, y, expected, players.get(0).getPnum());
	}

	@Test
	public void checkIfCanMovePawnToOccupiedSquare(){
		int x=8;
		int y=1;
		boolean expected = false;
	
		testResults(players.get(3), x, y, expected, players.get(3).getPnum());
	}
	
	@Test
	public void checkIfMovePawnOutsideBoardHigh(){
		int x=9;
		int y=1;
		boolean expected = false;
	
		testResults(players.get(2), x, y, expected, players.get(2).getPnum());
	}
	
	@Test
	public void checkIfCanMoveOutsideBoardLow(){
		int x=1;
		int y=-1;
		boolean expected = false;
	
		testResults(players.get(0), x, y, expected, players.get(0).getPnum());
	}

	/* Test placeWall() */
	
	@Test
	public void checkIfCanPlaceVerticalWallInAValidSpace(){
		int x = 1;
		int y = 1;
		String type = "v";
		boolean expected = true;
		
		testResults(players.get(0), x, y, type, expected);
	}
	
	@Test
	public void checkIfCanPlaceHorizontalWallInAValidSpace(){
		int x = 0;
		int y = 6;
		String type = "h";
		boolean expected = true;
		
		testResults(players.get(0), x, y, type, expected);
	}
	
	@Test
	public void checkIfCanPlaceBlockingWall(){
		int x = 0;
		int y = 0;
		String type = "h";
		Board b = board;
		b.addWallWithoutValidation(new Wall(1,1,"v"));
		boolean expected = false;
		
		testResults(b, players.get(0), x, y, type, expected);
	}
	
	@Test
	public void checkIfCanPlaceBoxingWall(){
		int x = 6;
		int y = 0;
		String type = "h";
		Board b = board;
		b.addWallWithoutValidation(new Wall(7,0,"v"));
		b.addWallWithoutValidation(new Wall(7,1,"v"));
		boolean expected = false;
		
		testResults(b, players.get(2), x, y, type, expected);
	}
	
	/* Test isLegalMove() - pawn version */
	/* NEEDS MORE TESTING WHEN LOGIC COMPLETE */
	
	@Test
	public void checkValidPawnMoveLeft(){
		int x=0;
		int y=0;
		boolean expected = true;
	
		testResults(players.get(0), x, y, expected);
	} 
	
	@Test
	public void checkValidPawnMoveRight(){
		int x=2;
		int y=0;
		boolean expected = true;
	
		testResults(players.get(0), x, y, expected);
	} 
	
	@Test
	public void checkValidPawnMoveDown(){
		int x=1;
		int y=1;
		boolean expected = true;
	
		testResults(players.get(0), x, y, expected);
	}
	
	@Test
	public void checkValidPawnMoveUp(){
		int x=2;
		int y=7;
		boolean expected = true;
	
		testResults(players.get(1), x, y, expected);
	}
	
	@Test
	public void checkInvalidPawnMoveUp(){
		int x=1;
		int y=-1;
		boolean expected = false;
	
		testResults(players.get(0), x, y, expected);
	} 
	
	@Test
	public void checkInvalidPawnMoveLeft(){
		int x=0;
		int y=7;
		boolean expected = false;
	
		testResults(players.get(1), x, y, expected);
	}
	
	@Test
	public void checkInvalidPawnMoveRight(){
		int x=0;
		int y=7;
		boolean expected = false;
	
		testResults(players.get(1), x, y, expected);
	}
	
	@Test
	public void checkInvalidPawnMoveDown(){
		int x=0;
		int y=9;
		boolean expected = false;
	
		testResults(players.get(1), x, y, expected);
	}
	
	/* Test isLegalMove() - wall version */
	/* NEEDS MORE TESTING WHEN LOGIC COMPLETE */
	
	@Test
	public void checkInvalidType(){
		Wall w = new Wall(1, 1, "Pizza");
		boolean expected = false;
	
		testResults(players.get(0), w, expected);
	} 
	
	@Test
	public void checkValidHorizontalType(){
		Wall w = new Wall(4, 1, "h");
		boolean expected = true;
	
		testResults(players.get(0), w, expected);
	} 
	
	@Test
	public void checkValidVerticalType(){
		Wall w = new Wall(1, 1, "v");
		boolean expected = true;

		testResults(players.get(0), w, expected);
	}
	
	@Test
	public void checkHorizontalOutOfBoundsLeft(){
		Wall w = new Wall(-1, 1, "h");
		boolean expected = false;
	
		testResults(players.get(0), w, expected);
	}
	
	@Test
	public void checkHorizontalOutOfBoundsRight(){
		Wall w = new Wall(8, 1, "h");
		boolean expected = false;
	
		testResults(players.get(0), w, expected);
	}
	
	@Test
	public void checkVerticalOutOfBoundsTop(){
		Wall w = new Wall(1, -1, "v");
		boolean expected = false;
	
		testResults(players.get(0), w, expected);
	}
	
	@Test
	public void checkVerticalOutOfBoundsBottom(){
		Wall w = new Wall(1, 8, "v");
		boolean expected = false;
	
		testResults(players.get(0), w, expected);
	}
	
	@Test
	public void checkWallOnExistingWall(){
		Wall w = new Wall(3, 3, "v");
		boolean expected = false;
	
		testResults(players.get(0), w, expected);
	}
	
	@Test
	public void checkUprightTH(){
		Wall w = new Wall(2, 3, "h");
		boolean expected = true;
	
		testResults(players.get(0), w, expected);
	}
	
	@Test
	public void checkUpsideDownTH(){
		Wall w = new Wall(4, 3, "h");
		boolean expected = true;
	
		testResults(players.get(0), w, expected);
	}
	
	@Test
	public void checkTOnLeftH(){
		Wall w = new Wall(3, 4, "h");
		boolean expected = true;
	
		testResults(players.get(0), w, expected);
	}
	
	@Test
	public void checkTOnRightH(){
		Wall w = new Wall(3, 2, "h");
		boolean expected = true;
	
		testResults(players.get(0), w, expected);
	}
	
	@Test
	public void checkUprightTV(){
		Wall w = new Wall(6, 1, "v");
		boolean expected = true;
	
		testResults(players.get(0), w, expected);
	}
	
	@Test
	public void checkUpsideDownTV(){
		Wall w = new Wall(4, 1, "v");
		boolean expected = true;
	
		testResults(players.get(0), w, expected);
	}
	
	@Test
	public void checkTOnLeftV(){
		Wall w = new Wall(5, 0, "v");
		boolean expected = true;
	
		testResults(players.get(0), w, expected);
	}
	
	@Test
	public void checkTOnRightV(){
		Wall w = new Wall(5, 2, "v");
		boolean expected = true;
	
		testResults(players.get(0), w, expected);
	}
	
	
}


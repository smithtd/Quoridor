/**
 * 
 */
package players;

import static org.junit.Assert.*;

import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

/**
 * @author marc dean jr. 
 *
 */
public class PlayerTest extends Player {

	public PlayerTest(){
	   super("TestPlayer", 5);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {	
		
	}

	/**
	 * Test method for {@link players.Player#Player(java.lang.String, int)}.
	 */
	@Test
	public void testPlayer() {
		// Test that we can get the move
		testGetMove();
		// Get the number of walls
		testGetWalls();
		
	}

	/**
	 * Test method for {@link players.Player#getMove()}.
	 * Right now just checks that system.in is working,
	 * will build when getMove gets more complex and checks 
	 * if certain moves are legal
	 */
	@Test
	public void testGetMove() {
		
		String input = "";
		Scanner sc;
		
		// Set System.in to input instead of using keyboard
		System.setIn(DebugTools.setKeyBoardInput("e8"));
		sc = new Scanner(System.in);
		
		input = sc.nextLine();
		
		assertEquals("e8", input);
		
		sc.close();

	}

	/**
	 * Test method for {@link players.Player#getWalls()}.
	 */
	@Test
	public void testGetWalls() {
		PlayerTest p = new PlayerTest();
		int walls = p.getWalls();
		assertEquals(5, walls);
	
	}

}

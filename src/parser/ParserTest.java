package parser;


/**
 * ParserTest.java
 * @author marc dean jr.
 * Team 511-tactical
 */
import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import parser.Parser;


public class ParserTest {
	
	static Parser p;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		p = new Parser();
	}

	/**
	 * Should show that certain strings fit into
	 * the protocol of what a move is or not
	 * 
	 * Example : b4, e2, c6
	 * Not moves: z11, m8, j99
	 * 
	 */
	@Test
	public void testMovementString() {
		// Test all movement possibilities that are valid
		String s = "";
		for(int i = 'a'; i <='i'; i++){
			s += (char)i;
			for(int j = 1; j < 10; j++) {
				assertTrue(p.isMove(s + j));
			}
			s = "";
		}
		
		// Test example moves that aren't valid
		for(int i = 'j'; i <= 'z'; i++) {
			s += (char)i;
			for(int j = 1; j < 20; j++) {
				assertFalse(p.isMove(s + j));
			}
			s = "";
		}
	}
	
	@Ignore
	public void testIsWallPlacementString() {
		fail("");
	}
	
	/** Should not accept empty strings **/
	@Ignore
	public void testEmptyString() {
		String s = "";
		for(int i = 0; i <= 100; i++) {
			assertFalse(p.isMove(s));
			assertFalse(p.isWall(s));
			s += " ";
		}
		
	}

}

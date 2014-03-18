package parser;


/**
 * ParserTest.java
 * @author marc dean jr.
 * Team 511-tactical
 */
import static org.junit.Assert.*;

import org.junit.BeforeClass;
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
	
	/** 
	 * Test the wall placement regex with what the protocol should be
	 * 
	 * Ex. a4v, b6h,
	 * 
	 * 
	 */
	
	@Test
	public void testIsWallPlacementString() {
		// Test all wall placement possibilities
		String s = "";
		for(int i = 'a'; i<='i'; i++) {
			s += (char)i;
			for(int j = 1; j < 10; j++) {
				assertTrue(p.isWall(s + j + 'h'));
				assertTrue(p.isWall(s + j + 'v'));
			}
			s = "";
		}
		
		// Wall placements that are 3 characters but don't fit protocol
		for(int i = 'a'; i <= 'z'; i++) {
			s += (char)i;
			for(int j = 11; j < 1000; j++) {
				assertFalse(p.isWall(s + j + 'h'));
				assertFalse(p.isWall(s + j + 'v'));
				assertFalse(p.isWall(s + j + "hv"));
				assertFalse(p.isWall("a4v" + s + j + 'v'));
			}
			s = "";
		}
		
	}
	
	/** Should not accept empty strings **/
	@Test
	public void testEmptyString() {
		String s = "";
		for(int i = 0; i <= 100; i++) {
			assertFalse(p.isMove(s));
			assertFalse(p.isWall(s));
			s += " ";
		}
		
	}
	
	/** Tests the translation of moves to what we must get
	 * for our gui implementation.
	 */
	@Test
	public void testMoveTranslation() {
		
		String cOne = "a4"; // should return "14"
		assertEquals("14", p.moveTranslate(cOne));
		
		cOne = "b9" ; // should return "29"
		assertEquals("29", p.moveTranslate(cOne));
		
		cOne = "i9"; // should return "99"
		assertEquals("99", p.moveTranslate(cOne));
		
		cOne = "z8"; // should return ""
		assertEquals("", p.moveTranslate(cOne));
		
		
	}

}

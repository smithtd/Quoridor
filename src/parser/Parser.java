/**
 * Quoridor String Parser
 * @author marc dean jr
 * Team - 511 tactical
 */

package parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *  The parser will verify moves are input correctly and send them off
 *  to be verified that they are legal. It will do the same for wall
 *  placements.
 *
 */
public class Parser {
	
	/** Regex for pawn movements */
	private Pattern moves;
	
	/** Regex for wall placements */
	private Pattern walls;
	
	/** Regex for IPP pairs */
	private Pattern pairs;
	
	/** Regex for IP address : port */
	private Pattern ipAddress;
	
	/** 
	 *  Creates an instance of our move/wall parser.
	 *  
	 *  Moves should be in the form of (a-i)(1-9), so therefore
	 *  this will provide some low level validity checking. After
	 *  which our logic must make sure the person could actually move
	 *  to this location.
	 *  
	 *  Walls can be set (a-i)(1-9)(v || h). This provides similar low
	 *  level validity checking.
	 */
	public Parser() {
		this.moves = Pattern.compile("[a-i][1-9]");
		this.walls = Pattern.compile("[a-i][1-9][vh]");
		this.pairs = Pattern.compile("[\\w*\\W*]+:[0-9]+");
		this.ipAddress = Pattern.compile("[0-9\\W]+[0-9]+:[0-9]+");
	}
	
	/**
	 * Test if an input is a valid IPP pair representation.
	 * 
	 * @param s - String representation of a possible IPP pair
	 * @return - True if IPP pair, else false.
	 */
	public boolean isPair(String s) {
		Matcher isP = this.pairs.matcher(s);
		Matcher ipIs = this.ipAddress.matcher(s);
		
		return isP.matches() || ipIs.matches();
	}
	
	/** 
	 * Test if an input is a valid move representation
	 * 
	 * @param s - string representation of a possible move
	 * @return - if the string represents a move as per protocol 
	 *  		 returns true, else false
	 */
	public boolean isMove(String s) {
		Matcher isM = moves.matcher(s);
		return isM.matches();
	} // end isMove(String s)
	
	/**
	 * Tests if an input is a valid wall placement representation
	 * 
	 * @param s - string representation of a possible wall placement
	 * @return - if the string represents a wall placement as per
	 * 		     our protocol returns true, else false
	 * 
	 */
	public boolean isWall(String s) {
		Matcher isW = walls.matcher(s);
		return isW.matches();
		
	} // end isWall(String s)
	
	/**
	 * Translates our input from the protocol to a usable form
	 * for our GUI and game logic for pawn movements
	 * 
	 * @param s - representation of the move
	 * @return xy string for the gui/logic to use
	 */
	public String moveTranslate(String s) {
		if(this.isMove(s)) {
			int x = (int) s.charAt(0) - 'a';
			int y = Integer.parseInt(s.charAt(1)+"")-1;
			return y + "" + x; 
		} else
			return "";
		
	} // end moveTranslate(String s)
	
	/**
	 * Translates our input from the protocol to a usable form
	 * for our GUI and game logic for wall placements
	 * 
	 * @param s - representation of the wall placement
	 * 
	 * @return - xy(v || h) string for the gui/logic to use
	 */
	public String wallTranslate(String s) {
		
		if(this.isWall(s)) {
			return moveTranslate(s.substring(0, 2)) + s.charAt(2);
		} else
			return "";
		
	} // end wallTranslate(String s)
	
} // end Parser { }

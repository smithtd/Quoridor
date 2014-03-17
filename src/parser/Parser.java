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
	
	public Parser() {
		this.moves = Pattern.compile("[a-i][1-9]");
	}
	
	public boolean isMove(String s) {
		Matcher isM = moves.matcher(s);
		return isM.matches();
	}
}

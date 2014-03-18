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
		this.walls = Pattern.compile("[a-i][1-9][vh]");
	}
	
	public boolean isMove(String s) {
		Matcher isM = moves.matcher(s);
		return isM.matches();
	}
	
	public boolean isWall(String s) {
		Matcher isW = walls.matcher(s);
		return isW.matches();
		
	}
	
	public String moveTranslate(String s) {
		
		if(this.isMove(s)) {
			int x = (int) s.charAt(0) - 'a';
			String tmp = ""+s.charAt(1);
			int y = Integer.parseInt(tmp);
			y--;
			return x + "" + y;
		} else
			return "";
		
	}
	
}

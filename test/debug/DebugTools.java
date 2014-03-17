package debug;
import java.io.ByteArrayInputStream;

/*
 * author: marc dean jr.
 * 
 */

/*
 * Purpose: Miscellaneous tools for speeding up writing tests, other functions
 * 
 */

public class DebugTools {
	
	// Purpose: When writing tests that involve keyboard input, (Ex. Prompts,
	// menus, etc) you can select the input rather than typing it in for 
	// the tests, speeding up writing and running tests requiring user input
	// Precondition: the user will select strings that user's should input
	// Postcondition: System.in will be set as the string passed in here
	
	public static ByteArrayInputStream setKeyBoardInput(String userInput) {
	
		/*
		 * To use: In a test function that requires user input use the
		 * command System.setIn(setKeyBoardInput("StringToTest"));
		 * to change from listening for keyboard input to something
		 * you specify.
		 */
		ByteArrayInputStream in = new ByteArrayInputStream(userInput.getBytes());
		return in;
		
	}
	
}

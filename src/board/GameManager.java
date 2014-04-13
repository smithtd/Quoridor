/**
 * 	Software Engineering
 * 	Quoridor - Team 511 Tactical
 * 	@author m. dean, d. woythal
 * 
 */

/** 
 * The purpose of this class is to verify legal moves and wall placements and 
 * make sure people can have proper jumping mechanisms and walls that aren't overlapping
 * 
 */

package board;

import java.util.ArrayList;

import javax.swing.JFrame;

import main.Game;
import players.Player;
import ui.GameBoard;
import ui.PlayerButton;
import ui.WallButton;
import walls.Wall;

public class GameManager {
	
	private static Wall[][] vertWalls;
	private static Wall[][] horzWalls;
	private static PlayerButton[][] playerButtons;
	private static WallButton[][] 	wallButtons;
	private static Player[] players;
	private static int[] wallsRemaining;
	private static Game g; 
	private static GameBoard gameBoard;
	private static int numPlayers;
	private static int currentIndex;
	private static ArrayList<String> testedSpaces;
	private static ArrayList<PlayerButton> buttonsToChange;
	private JFrame tempFrame;

}

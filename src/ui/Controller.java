package ui;

import java.awt.Color;
import java.util.ArrayList;

import players.Player;

/*
 * The purpose of this class is to hold all variables of the game state as 
 * static this class is added as a static object to every other object so that 
 * any other object can access it and get a hold of the variables that should be 
 * shared by the entire game
 * 
 * Not sure if any other comments are needed at this point since the methods are
 * all one liners and self explanatory
 */

public class Controller {

	private static Wall[][] vertWalls;
	private static Wall[][] horzWalls;
	public static PlayerButton[][] pbAry;
	private static WallButton[][] wbAry;
	private static Player[] plyrAry;
	private static Game g;
	private static GameBoard gBoard;
	private static int numPlayers;
	private static int currentIndex;
	private static ArrayList<String> testedSpaces;
	private static ArrayList<PlayerButton> btnsToChange;
	
	
	//Constructor
	public Controller( int numPlayers){
		Controller.numPlayers = numPlayers;
		currentIndex = 0;
	}
	
	//Player turn controller
	public void showPlyrMoves(){
		resetMoveableSpaces();
		System.out.println("HERE");
		Player p = plyrAry[currentIndex];
		System.out.println(p.x() + "" + p.y() );
		PlayerButton pBtn = pbAry[ p.x() ][ p.y() ];
		testedSpaces = new ArrayList<String>();
		btnsToChange = new ArrayList<PlayerButton>();
		testSpace( pBtn );
		for(PlayerButton btn : btnsToChange ){
			btn.setBackground( Color.MAGENTA );
		}
	}
	
	public void testSpace( PlayerButton currBtn ){
		if( !testedSpaces.contains("" + currBtn.x() + currBtn.y() ) ){
			testedSpaces.add( "" + currBtn.x() + currBtn.y() );
			if(currBtn.getBackground() == Color.black){
				btnsToChange.add( currBtn );
			} else{
				try{ testSpace(pbAry[currBtn.x()-1][currBtn.y()]); } catch(Exception e){}
				try{ testSpace(pbAry[currBtn.x()+1][currBtn.y()]); } catch(Exception e){}
				try{ testSpace(pbAry[currBtn.x()][currBtn.y()-1]); } catch(Exception e){}
				try{ testSpace(pbAry[currBtn.x()][currBtn.y()+1]); } catch(Exception e){}
			}
		} else{
			return;
		}
	}		

	public void resetMoveableSpaces(){
		for( int x=0; x<9; x++ )
			for( int y=0; y<9; y++ )
				if(pbAry[x][y].getBackground() == Color.MAGENTA)
					pbAry[x][y].setBackground(Color.BLACK);
	}
	
	public void nextPlayerMove(){
		currentIndex++;
		if( currentIndex > plyrAry.length-1 )
			currentIndex = 0;
		showPlyrMoves();
	}
	
	
	
	
	
	
	
	
	
	
	/*
	 * Getters and setters
	 */
	
	public void addPlyrAry(Player[] plyrAry){
		Controller.plyrAry = plyrAry;
	}
	
	public void addGame( Game g ){
		Controller.g = g;
	}
	
	public void addGame( GameBoard gBoard ){
		Controller.gBoard = gBoard;
	}
	
	public void addVertWalls( Wall[][] vertWalls ){
		Controller.vertWalls = vertWalls;
	}

	public void addHorzWalls( Wall[][] horzWalls ){
		Controller.horzWalls = horzWalls;
	}
	
	public void addPlayerButtons( PlayerButton[][] pbAry ){
		Controller.pbAry = pbAry;
	}
	
	public void addWallButtons( WallButton[][] wbAry ){
		Controller.wbAry = wbAry;
	}
	
	public Player[] addPlyrAry(){
		return Controller.plyrAry;
	}
	
	public Game getGame(){
		return Controller.g;
	}
	
	public GameBoard getGameBoard(){
		return Controller.gBoard;
	}
	
	public Wall[][] getVertWalls(){
		return Controller.vertWalls;
	}
	
	public Wall[][] getHorzWalls(){
		return Controller.horzWalls;
	}
	
	public PlayerButton[][] getPlayerButtons(){
		return Controller.pbAry;
	}
	
	public WallButton[][] getWallButtons(){
		return Controller.wbAry;
	}
}

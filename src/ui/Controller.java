package ui;

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
	private static PlayerButton[][] pbAry;
	private static WallButton[][] wbAry;
	
	public Controller(){
		;
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

package ui;

public class Controller {

	private static Wall[][] vertWalls;
	private static Wall[][] horzWalls;
	private static PlayerButton[][] pbAry;
	private static WallButton[][] wbAry;
	
	public Controller(){
		;
	}
	
	public void addVertWalls( Wall[][] vertWalls ){
		this.vertWalls = vertWalls;
	}

	public void addHorzWalls( Wall[][] horzWalls ){
		this.horzWalls = horzWalls;
	}
	
	public void addPlayerButtons( PlayerButton[][] pbAry ){
		this.pbAry = pbAry;
	}
	
	public void addWallButtons( WallButton[][] wbAry ){
		this.wbAry = wbAry;
	}
	
	public Wall[][] getVertWalls(){
		return this.vertWalls;
	}
	
	public Wall[][] getHorzWalls(){
		return this.horzWalls;
	}
	
	public PlayerButton[][] getPlayerButtons(){
		return this.pbAry;
	}
	
	public WallButton[][] getWallButtons(){
		return this.wbAry;
	}
}

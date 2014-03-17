package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import main.Game;
import players.Player;
import walls.Wall;

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
	private static int [] wallsRem;
	private static Game g;
	private static GameBoard gBoard;
	private static int numPlayers;
	private static int currentIndex;
	private static ArrayList<String> testedSpaces;
	private static ArrayList<PlayerButton> btnsToChange;
	private JFrame tempFrame;
	
	
	//Constructor
	public Controller( int numPlayers){
		Controller.numPlayers = numPlayers;
		currentIndex = 0;
	}
	
	//Player turn controller
	public void showPlyrMoves(){
		resetMoveableSpaces();
		printPlayerTurn();
		//System.out.println("HERE");
		Player p = plyrAry[currentIndex];
		//System.out.println(p.x() + "" + p.y() );
		PlayerButton pBtn = pbAry[ p.x() ][ p.y() ];
		testedSpaces = new ArrayList<String>();
		btnsToChange = new ArrayList<PlayerButton>();
		testSpace( pBtn, 'n' );
		for(PlayerButton btn : btnsToChange ){
			btn.setBackground( Color.MAGENTA );
		}
	}
	
	public void testSpace( PlayerButton currBtn, char dir ){

		boolean wallUp = false;
		boolean wallDown = false;
		boolean wallLeft = false;
		boolean wallRight = false;
		try{ wallUp = (horzWalls[currBtn.x()-1][currBtn.y()].getBackground() != Color.GRAY); } catch(Exception e){}
		try{ wallDown = (horzWalls[currBtn.x()][currBtn.y()].getBackground() != Color.GRAY); } catch(Exception e){}
		try{ wallLeft = (vertWalls[currBtn.x()][currBtn.y()-1].getBackground() != Color.GRAY); } catch(Exception e){}
		try{ wallRight = (vertWalls[currBtn.x()][currBtn.y()].getBackground() != Color.GRAY); } catch(Exception e){}

		if( !testedSpaces.contains("" + currBtn.x() + currBtn.y() ) ){
			testedSpaces.add( "" + currBtn.x() + currBtn.y() );
			if( (dir=='l' && !wallLeft) || (dir=='r' && !wallRight) || (dir=='u' && !wallUp) || (dir=='d' && !wallDown) || (dir=='n') )
				if(currBtn.getBackground() == Color.black){
						btnsToChange.add( currBtn );
				}else{
					/* 
					 * The character passed is the direction from which the player's 
					 * old location would be in relation to the button tested 
					 */
					try{ testSpace(pbAry[currBtn.x()-1][currBtn.y()], 'd'); } catch(Exception e){}
					try{ testSpace(pbAry[currBtn.x()+1][currBtn.y()], 'u'); } catch(Exception e){}
					try{ testSpace(pbAry[currBtn.x()][currBtn.y()-1], 'r'); } catch(Exception e){}
					try{ testSpace(pbAry[currBtn.x()][currBtn.y()+1], 'l'); } catch(Exception e){}
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
		if( currentIndex > numPlayers-1)
			currentIndex = 0;
		showPlyrMoves();
	}
	
	public void movePiece(PlayerButton b){
		b.setBackground(this.getPlyrAry()[ this.getPlyrIndex() ].getColor());
		Player p = plyrAry[currentIndex];
		pbAry[p.x()][p.y()].setBackground(Color.black);
		p.setPos(b.x(), b.y());
		//here down is the win state
		if(p.getPnum()==1&&p.x()==8){
			winState(p);
		}
		if(p.getPnum()==2&&p.getStartx()==8&&p.x()==0){
			winState(p);
		}else if(p.getPnum()==2&&p.getStartx()==4&&p.y()==0){
			winState(p);
		}
		if(p.getPnum()==3&&p.x()==0){
			winState(p);
		}
		if(p.getPnum()==4&&p.y()==8){
			winState(p);
		}
		this.nextPlayerMove();
	}
	
	public void printPlayerTurn(){
		if( currentIndex == 0 )
			System.out.println( "Blue Turn" );
		if( currentIndex == 1 )
			System.out.println( "Red Turn" );
		if( currentIndex == 2 )
			System.out.println( "Green Turn" );
		if( currentIndex == 3 )
			System.out.println( "Yellow Turn" );
	}
	
	public void winState(Player p){
		currentIndex = 0;
		JPanel jp1 = new JPanel();
		jp1.setLayout( new FlowLayout() ) ;
		jp1.setPreferredSize( new Dimension( 300, 75 ) );
		tempFrame = new JFrame( "Player " + p.getPnum() + " wins!" );
		tempFrame.setLayout( new BorderLayout() );
		ButtonGroup btngrp = new ButtonGroup();
		final JRadioButton b1 = new JRadioButton("New 2 player game",false);
		final JRadioButton b2 = new JRadioButton("New 4 player game",false);
		final JRadioButton b3 = new JRadioButton("Quit",false);
		b1.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ){
				GameBoard.g.new2PlayerGame();
				tempFrame.dispose();
			}
		});
		b2.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ){
				GameBoard.g.new4PlayerGame();
				tempFrame.dispose();
			}
		});
		b3.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ){
				System.exit(0);
			}
		});
		btngrp.add(b1);
	    btngrp.add(b2);
	    btngrp.add(b3);
	    jp1.add(b3, FlowLayout.LEFT);
	    jp1.add(b2, FlowLayout.LEFT);
		jp1.add(b1, FlowLayout.LEFT);
		tempFrame.add( jp1, BorderLayout.NORTH );
		tempFrame.setLocation( 350, 300 );
		tempFrame.setSize( 300, 300 );
		tempFrame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		tempFrame.setVisible( true );
		tempFrame.pack();
	}
	
	
	
	
	
	
	/*
	 * Getters and setters
	 */
	//Setters
	public void addWallsRem( int [] wallsRem ){
		Controller.wallsRem = wallsRem;
	}
	
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
	
	//Getters
	public int[] getWallsRem(){
		return Controller.wallsRem;
	}
	
	public Player[] getPlyrAry(){
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
	
	public int getPlyrIndex(){
		return currentIndex;
	}
	
	public int getNumPlayers(){
		return Controller.plyrAry.length;
	}

	
}

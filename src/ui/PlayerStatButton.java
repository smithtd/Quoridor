package ui;

import java.awt.Dimension;
import javax.swing.JButton;

import main.Game;

@SuppressWarnings("serial")
public class PlayerStatButton extends JButton {
	
	public int playerNum;
	
	public PlayerStatButton(int pNum){
		super();
		this.playerNum = pNum;
		if(Game.getNumPlayers()==2)
			this.setPreferredSize( new Dimension( GameBoard.getStatsBarDim().width, GameBoard.getStatsBarDim().height/2 ) );
		else
			this.setPreferredSize( new Dimension( GameBoard.getStatsBarDim().width, GameBoard.getStatsBarDim().height/4 ) );
		this.setBackground( Game.getPlayerAry()[playerNum].getColor() );
		this.setText("Walls remaining: " + Game.getCurrPlayer().getWalls());
	}
	
	public void updateWalls(){
		this.setText("Walls remaining: " + Game.getCurrPlayer().getWalls());
	}
}

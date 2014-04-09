package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JButton;

import main.Game;

@SuppressWarnings("serial")
public class StatButton extends JButton {
	
	public int playerNum;
	public Color c;
	
	public StatButton(int pNum){
		super();
		this.setOpaque(true);
		this.setBorderPainted(false);
		this.playerNum = pNum;
		c = Game.getPlayerAry()[playerNum].getColor();
		this.setBackground( c );
		
		this.setForeground( ( c==Color.red || c== Color.blue ?  Color.WHITE : Color.BLACK ) );
		this.setText("Walls remaining: " + Game.getCurrPlayer().getWalls());

		if(Game.getNumPlayers()==2)
			this.setPreferredSize( new Dimension( GameBoard.getStatsBarDim().width, GameBoard.getStatsBarDim().height/2 ) );
		else
			this.setPreferredSize( new Dimension( GameBoard.getStatsBarDim().width, GameBoard.getStatsBarDim().height/4 ) );
	}
	
	public void updateWalls(){
		this.setText("Walls remaining: " + Game.getCurrPlayer().getWalls());
	}
	
	public void paint( Graphics g ){
		//Color block of player
		g.setColor( c );
		g.fillRect( 0, 0, this.getWidth(), this.getHeight() );
		
		//Black bar on side
		g.setColor( Color.black );
		g.fillRect( 0, 0, 1, this.getHeight() );

		//Box around text
		int increment = 150;
		int red = (c.getRed() + increment>256 ? c.getRed() : c.getRed() + increment );
		int green = (c.getGreen() + increment>256 ? c.getGreen() : c.getGreen() + increment );
		int blue = (c.getBlue() + increment>256 ? c.getBlue() : c.getBlue() + increment );
		g.setColor( new Color( red, green, blue ) );
		g.fillRect( 20, this.getHeight()/2-10, this.getWidth()-30, 20 );
		
		//Text
		g.setFont( getFont() );
		g.setColor( Color.BLACK );
		g.drawString( ("Walls remaining: " + Game.getPlayerAry()[playerNum].getWalls()), 30, this.getHeight()/2+5 );
	}
}
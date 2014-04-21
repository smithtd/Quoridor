package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
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
		c = Game.getPlayerAry().get(playerNum).getColor();
		this.setBackground( c );

		this.setForeground( ( c==Color.red || c== Color.blue ?  Color.WHITE : Color.BLACK ) );
		this.setText("Walls remaining: " + Game.getCurrPlayer().getWalls());

		if(Game.getNumPlayers()==2)
			this.setPreferredSize( new Dimension( GameBoard.getRightBarDim().width, GameBoard.getRightBarDim().height/2 ) );
		else
			this.setPreferredSize( new Dimension( GameBoard.getRightBarDim().width, GameBoard.getRightBarDim().height/4 ) );
	}

	public void updateWalls(){
		this.setText("Walls remaining: " + Game.getCurrPlayer().getWalls());
	}

	public void paint( Graphics g ){
		try {
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
			g.fillRect( Game.WallGap, Game.WallGap, this.getWidth()-Game.WallGap*2, this.getHeight()-Game.WallGap*2 );

			//Text
			g.setFont( new Font( "" , Font.BOLD, (GameBoard.getRightBarDim().width==180 ? 10 : (int)(Game.PlayerWidth*(12.0/50.0)) ) ) );
			FontMetrics fm = g.getFontMetrics( g.getFont() );
			String s = "Walls remaining: " + Game.getPlayerAry().get(playerNum).getWalls();
			g.setColor( Color.BLACK );
			g.drawString( s, (this.getWidth()/2 - fm.stringWidth( s )/2), (this.getHeight()/2+fm.getHeight()/2) );
		} catch (Exception e) {

		}

		//		g.drawString( s, 0, 0 );
	}
}

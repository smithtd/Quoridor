package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JButton;

import main.Game;
import ui.GameBoard;

@SuppressWarnings("serial")
public class StatButton extends JButton {

	public int playerNum;
	public Color c;
	private Game game;
	private GameBoard gb;

	public StatButton(int pNum, Game game, GameBoard gb){
		super();
		this.game=game;
		this.gb = gb;
		this.setOpaque(true);
		this.setBorderPainted(false);
		this.playerNum = pNum;
		c = game.getPlayerAry().get(playerNum).getColor();
		this.setBackground( c );

		this.setForeground( ( c==Color.red || c== Color.blue ?  Color.WHITE : Color.BLACK ) );
		this.setText("Walls remaining: " + game.getCurrPlayer().getWalls());

		if(game.getNumPlayers()==2)
			this.setPreferredSize( new Dimension( gb.getRightBarDim().width, gb.getRightBarDim().height/2 ) );
		else
			this.setPreferredSize( new Dimension( gb.getRightBarDim().width, gb.getRightBarDim().height/4 ) );
	}

	public void updateWalls(){
		this.setText("Walls remaining: " + game.getCurrPlayer().getWalls());
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
			g.fillRect( game.getWallGap(), game.getWallGap(), this.getWidth()-game.getWallGap()*2, this.getHeight()-game.getWallGap()*2 );

			//Text
			g.setFont( new Font( "" , Font.BOLD, (gb.getRightBarDim().width==180 ? 10 : (int)(game.getPlayerWidth()*(12.0/50.0)) ) ) );
			FontMetrics fm = g.getFontMetrics( g.getFont() );
			String s = "Walls remaining: " + game.getPlayerAry().get(playerNum).getWalls();
			g.setColor( Color.BLACK );
			g.drawString( s, (this.getWidth()/2 - fm.stringWidth( s )/2), (this.getHeight()/2+fm.getHeight()/4) );
		} catch (Exception e) {

		}

		//		g.drawString( s, 0, 0 );
	}
}

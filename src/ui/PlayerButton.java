package ui;

import javax.swing.*;

import main.Game;

import players.Player;
// using JButton
import java.awt.*;

/**
 * Class provides buttons with action listeners.
 * 
 * @author Tyler Smith
 */
@SuppressWarnings("serial")
public class PlayerButton extends JButton {

	/* Instance variables */
	public int buttonSize;
	private int x;
	private int y;
	private Player plyr;
	private Game game;
	
	/* Constructor	*/
	
	/**
	 * Constructs a PlayerButton with x and y as coordinates.
	 * 
	 * @param x integer x coordinate
	 * @param y integer y coordinate
	 */
	public PlayerButton( int x, int y, Game game ){
		super();
		this.game = game;
		this.x = x;
		this.y = y;
		this.setPreferredSize( new Dimension( game.getHWall().width, game.getVWall().height ) );
		this.setBackground( Color.BLACK );
		this.setOpaque(true);
		this.setBorderPainted(false);
	}
	
	/* Get Methods */
	
	/**
	 * Gets this PlayerButton's x value.
	 * 
	 * @return integer x coordinate
	 */
	public int x(){
		return this.x;
	}
	
	/**
	 * Gets this PlayerButton's y value.
	 * 
	 * @return integer y coordinate
	 */
	public int y(){
		return this.y;
	}
	
	/**
	 * Checks if a PlayerButton has a Player.
	 * 
	 * @return boolean, whether or not this has a Player
	 */
	public boolean hasPlayer(){
		return plyr!=null;
	}
	
	/* Set Methods */
	
	/**
	 * Adds a Player to this PlayerButton. 
	 * This also changes the color of the button to the Player's color.
	 * 
	 * @param plyr Player to add
	 */
	public void addPlayer( Player plyr ){
		this.plyr = plyr; 
		this.setBackground(plyr.getColor());
		this.setOpaque(true);
	}
	
	/**
	 * Removes a Player from this PlayerButton and changes color back to black.
	 */
	public void removePlayer(){
		this.plyr = null;
		this.setBackground(Color.BLACK);
		this.setOpaque(true);
	}
	
	public void paint( Graphics g ){
		if( this.getBackground() == Color.MAGENTA ){
			int increment = game.getColorIncrement();
			Color c = game.getCurrPlayer().getColor();
			int red = (c.getRed() + increment > 256 ? c.getRed() : c.getRed() + increment );
			int green = (c.getGreen() + increment > 256 ? c.getGreen() : c.getGreen() + increment );
			int blue = (c.getBlue() + increment > 256 ? c.getBlue() : c.getBlue() + increment );
			
			g.setColor( new Color( red, green, blue ) );
			g.fillRect( 0, 0, game.getPlayerSize().width, game.getPlayerSize().height );
			
			g.setFont( getFont() );
			FontMetrics fm = g.getFontMetrics( g.getFont() );
			String s = "" + (char)(this.y+65) + (this.x+1) ;
			g.setColor( Color.BLACK );
			g.drawString( s, (this.getWidth()/2 - fm.stringWidth( s )/2), (this.getHeight()/2 + fm.getHeight()/4 ) );
			
		}else{
			g.setColor( this.getBackground() );
			g.fillRect( 0, 0, game.getPlayerSize().width, game.getPlayerSize().height );
		}
		
	}
}

package ui;

import javax.swing.*;

import main.Game;

import java.awt.*;

/**
 * Small square button at center of each wall.
 * 
 * @author Dylan Woythal, Tyler Smith
 */
@SuppressWarnings("serial")
public class WallButton extends JButton {
	
	/* Instance Variables */
	public int x;
	public int y;
	
	/* Constructor */
	/**
	 * Constructs a WallButton at x and y coordinates given
	 * 
	 * @param x integer x coordinate
	 * @param y integer y coordinate
	 */
	public WallButton( int x, int y, Game game ){
		super();
		this.setBorder(null);
		this.x = x;
		this.y = y;
		this.setPreferredSize( game.getIntersection() );
		this.setBackground( Color.DARK_GRAY );
		this.setOpaque(true);
	}
	
	public void paint( Graphics g ){
		g.setColor( this.getBackground() );
		g.fillRect(0, 0, this.getWidth(), this.getHeight() );
	}
}

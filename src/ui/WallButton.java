package ui;

import javax.swing.*;
import java.awt.*;

/**
 * Small square button at center of each wall.
 * 
 * @author Dylan Woythol, Tyler Smith
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
	public WallButton( int x, int y ){
		super();
		this.setBorder(null);
		this.x = x;
		this.y = y;
		this.setPreferredSize( new Dimension( 10, 10 ) );
		this.setBackground( Color.BLACK );
		this.setOpaque(true);
	}
}

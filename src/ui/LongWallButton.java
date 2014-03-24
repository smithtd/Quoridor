package ui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.*;

/**
 * Class defines settings for LongWallButtons, or the buttons that display 
 * actual walls.
 * 
 * @author Dylan Woythol, Tyler Smith
 */
@SuppressWarnings("serial")
public class LongWallButton extends JButton {
	
	/* Instance Variables */
	
	public int x;
	public int y;
       
	/* Constructor */
	
	/**
	 * Constructs a LongWallButton based on parameters.
	 * 
	 * @param x integer x coordinate
	 * @param y integer y coordinate
	 * @param type String wall orientation
	 */
	public LongWallButton(int x, int y, String type){
		super();
        this.setBorder(null);
        this.x = x;
        this.y = y;
        /* set the dimensions upon string identifier */
        if( type.equals( "h" ) )
        	this.setPreferredSize( new Dimension( 50, 10 ) );
        else
        	this.setPreferredSize( new Dimension( 10, 50 ) );
        this.setBackground( Color.gray);
        this.setOpaque(true);	// needed to display on macs   
   }
}
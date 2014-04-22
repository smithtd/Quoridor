package ui;

import java.awt.Color;
//import java.awt.Graphics;

import javax.swing.JButton;

import main.Game;

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
       	this.setPreferredSize( type.equals( "h" ) ? Game.HWall : Game.VWall );
        this.setBackground( Color.DARK_GRAY );
        this.setOpaque(true);	// needed to display on macs   
   }
	
/*	public void paint( Graphics g ){
		g.setColor( Color.DARK_GRAY );
		g.fillRect(0, 0, this.getWidth(), this.getHeight() );
	} */
}
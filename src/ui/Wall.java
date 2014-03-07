package ui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.*;

@SuppressWarnings("serial")
public class Wall extends JButton {
	public int x;
	public int y;
	
	@SuppressWarnings("unused") //For now
	private static Controller cont;
	
	public Wall(int x, int y, String type, Controller cont){
		super();
		this.setBorder(null);
		this.x = x;
		this.y = y;
		Wall.cont = cont;
		/*
		 * if string identifier is a horizontal wall set the dimensions based 
		 * upon that. Same goes for vertical walls
		 */
		if( type.equals( "horz" ) )
			this.setPreferredSize( new Dimension( 50, 10 ) );
		else
			this.setPreferredSize( new Dimension( 10, 50 ) );
		this.setBackground( new Color(255,0,255));
		
	}
}

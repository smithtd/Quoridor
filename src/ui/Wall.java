package ui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.*;

@SuppressWarnings("serial")
public class Wall extends JButton {
	public int x;
	public int y;
	private static Controller cont;
	
	public Wall(int x, int y, String type, Controller cont){
		super();
		this.x = x;
		this.y = y;
		if( type.equals( "horz" ) )
			this.setPreferredSize( new Dimension( 50, 10 ) );
		else
			this.setPreferredSize( new Dimension( 10, 50 ) );
		this.setBackground( Color.BLACK );
	}
}

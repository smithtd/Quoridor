package ui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;

public class SpacerButton extends JButton{

	public SpacerButton( int w, int h, Color c ){
		super();
		this.setPreferredSize( new Dimension( w, h ) );
		this.setBorder( null );
		this.setBackground( c );
	}

	public SpacerButton(int w, int h, Color c, char label) {
		super();
		this.setPreferredSize( new Dimension( w, h ) );
		this.setBorder( null );
		this.setBackground( c );
		this.setText( ""+label );
	}
}

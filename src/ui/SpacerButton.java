package ui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;

public class SpacerButton extends JButton{

	// width, height, and color
	public SpacerButton( int w, int h, Color c ){
		super();
		this.setPreferredSize( new Dimension( w, h ) );
		this.setBorder( null );
		this.setBackground( c );
	}

	//width, height, color, and text
	public SpacerButton(int w, int h, Color c, String label) {
		super();
		this.setPreferredSize( new Dimension( w, h ) );
		this.setBorder( null );
		this.setBackground( c );
		this.setText( label );
	}
}

package ui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class SpacerButton extends JButton{

	// width, height, and color
	public SpacerButton( int w, int h, Color c ){
		super();
		this.setPreferredSize( new Dimension( w, h ) );
		this.setOpaque(true);
		this.setBorderPainted(false);
		this.setBorder( null );
		this.setBackground( c );
	}
	
	// width, height, and color
	public SpacerButton( Dimension d, Color c ){
		super();
		this.setPreferredSize( d );
		this.setOpaque(true);
		this.setBorderPainted(false);
		this.setBorder( null );
		this.setBackground( c );
	}

	//width, height, color, and text
	public SpacerButton(int w, int h, Color c, String label) {
		super();
		this.setPreferredSize( new Dimension( w, h ) );
		this.setOpaque(true);
		this.setBorderPainted(false);
		this.setBorder( null );
		this.setBackground( c );
		this.setText( label );
		this.setForeground( c == Color.DARK_GRAY || c == Color.BLACK ? Color.WHITE : Color.BLACK );
	}
	
	//width, height, color, and text
	public SpacerButton( Dimension d, Color c, String label) {
		super();
		this.setPreferredSize( d );
		this.setOpaque(true);
		this.setBorderPainted(false);
		this.setBorder( null );
		this.setBackground( c );
		this.setText( label );
		this.setForeground( c == Color.DARK_GRAY || c == Color.BLACK ? Color.WHITE : Color.BLACK );
	}
}

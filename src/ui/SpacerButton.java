package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;

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
	
	public void paint( Graphics g ){
		Color c = this.getBackground();
		g.setColor( c );
		g.fillRect(0, 0, this.getWidth(), this.getHeight() );
		if(!this.getText().equals("") ){
			FontMetrics fm = g.getFontMetrics( g.getFont() );
			String s = this.getText();
			g.setColor( (c == Color.DARK_GRAY || c == Color.BLACK) ? Color.WHITE : Color.BLACK );
			g.drawString( s, (this.getWidth()/2 - fm.stringWidth( s )/2), (this.getHeight()/2+fm.getHeight()/4) );
			
		}
	}
			
}

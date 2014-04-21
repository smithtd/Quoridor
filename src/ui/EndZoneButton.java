package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JButton;

import main.Game;

@SuppressWarnings("serial")
public class EndZoneButton extends JButton{
	
	String position;
	public EndZoneButton( int numPlayers, String position ){
		super();
		this.setBorder(null);
		this.position = position;
		
		if( position.equals("U") || position.equals("D") )
			this.setPreferredSize( new Dimension( GameBoard.getButtonHolderDim().width, Game.Intersection.height ) );
		else
			this.setPreferredSize( new Dimension( Game.Intersection.width, GameBoard.getButtonHolderDim().height ) );
		
		if( numPlayers == 2 ){
			if( position.equals( "U" ) )
				this.setBackground( Color.RED );
			else if( position.equals( "D" ) )
				this.setBackground( Color.BLUE );
			else
				this.setBackground( Color.DARK_GRAY );
		}else if( numPlayers == 4 ){
			if( position.equals( "U" ) )
				this.setBackground( Color.GREEN );
			else if( position.equals( "D" ) )
				this.setBackground( Color.BLUE );
			else if( position.equals( "L" ) )
				this.setBackground( Color.RED );
			else if( position.equals( "R" ) )
				this.setBackground( Color.YELLOW );

		}
		this.setOpaque(true);
	}

	public void paint( Graphics g ){
		g.setColor( this.getBackground() );
		g.fillRect(0, 0, this.getWidth(), this.getHeight() );
		g.setColor( Color.BLACK );
		g.drawRect(0, 0, this.getWidth()-1, this.getHeight()-1 );
	}
}

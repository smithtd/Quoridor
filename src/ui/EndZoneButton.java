package ui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class EndZoneButton extends JButton{
	
	String position;
	public EndZoneButton( int numPlayers, String position ){
		super();
		this.setBorder(null);
		this.position = position;
		
		if( position.equals("U") || position.equals("D") )
			this.setPreferredSize( new Dimension( 530, 10 ) );
		else
			this.setPreferredSize( new Dimension( 10, 530 ) );
		
		if( numPlayers == 2 ){
			if( position.equals( "U" ) )
				this.setBackground( Color.RED );
			else if( position.equals( "D" ) )
				this.setBackground( Color.BLUE );
			else
				this.setBackground( Color.BLACK );
		}else if( numPlayers == 4 ){
			if( position.equals( "U" ) )
				this.setBackground( Color.YELLOW );
			else if( position.equals( "D" ) )
				this.setBackground( Color.RED );
			else if( position.equals( "L" ) )
				this.setBackground( Color.BLUE );
			else if( position.equals( "R" ) )
				this.setBackground( Color.GREEN );

		}
		
	}
}

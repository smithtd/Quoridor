package ui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class EndZoneSpacer extends JButton{

	public EndZoneSpacer(){
		super();
		this.setBackground( Color.black);
		this.setBorder(null);
		this.setPreferredSize( new Dimension ( 10, 10 ) );
	}
}

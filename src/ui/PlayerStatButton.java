package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Random;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class PlayerStatButton extends JButton {

	public PlayerStatButton(){
		super();
		this.setPreferredSize( new Dimension( GameBoard.getStatsBarDim().width, 137 ) );
		Random rand = new Random();
		this.setBackground( new Color( rand.nextInt(256), rand.nextInt(256), rand.nextInt(256) ) );
	}
}

/* Author: Tyler Smith
 * Class provides buttons with action listeners
 */

package ui;

import javax.swing.*;

import players.Player;
// using ImageIcon(?), JButton, JOptionPane, JPanel, etc.
import java.awt.*;
import java.awt.event.*;
// using ActionEvent, ActionListener, GridLayout

@SuppressWarnings("serial")
public class PlayerButton extends JButton {

	// Instance variables
	public static Icon Square_Red = new ImageIcon("../Images/Square_Red.png");	// what is this???
	public int buttonSize;

	private int x;
	private int y;
	private Player plyr;
	
	// Constructors	
	public PlayerButton( int x, int y ){
		super();
		this.x = x;
		this.y = y;
		this.setPreferredSize( new Dimension( 50, 50 ) );
		this.setBackground( Color.BLACK );
		this.setOpaque(true);
		this.setBorderPainted(false);
		addButtonListener();
	}
	
	public void addPlayer( Player plyr ){
		this.plyr = plyr; 
		this.setBackground(plyr.getColor());
		this.setOpaque(true);
	}
	
	public void removePlayer(){
		this.plyr = null;
		this.setBackground(Color.BLACK);
		this.setOpaque(true);
	}
	
	public boolean hasPlayer(){
		return plyr!=null;
	}
	
	public int x(){
		return this.x;
	}
	
	public int y(){
		return this.y;
	}
	
	// adds an action listener to a ClickButton
	public void addButtonListener(){
		final PlayerButton b = this;
		this.addActionListener( new ActionListener(){
			// users pawn moves to position that they click assuming it's highlighted
			public void actionPerformed( ActionEvent e ) {
				//if(b.getBackground() == Color.MAGENTA){
					b.clicked();
				//}
			}
		});
	}
	public void clicked(){
		System.err.println("Clicked "+x+" "+y);
//		cont.movePiece(this);
	}
}

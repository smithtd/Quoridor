/* Author: Tyler Smith and Eli Donahue
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

	private static Controller cont;
	
	// Constructors
	public PlayerButton( int x, int y, Controller cont ){
		super();
		this.x = x;
		this.y = y;
		PlayerButton.cont = cont;
		this.setPreferredSize( new Dimension( 50, 50 ) );
		this.setBackground( Color.BLACK );
		addButtonListener();
	}
	
	public void addPlayer( Player plyr ){
		this.plyr = plyr; 
	}
	
	public void removePlayer(){
		this.plyr = null;
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
		addActionListener( new ActionListener(){
			// we want the user to choose an action when they click a grid button
			public void actionPerformed( ActionEvent e ) {
				//int answer = 0;
				if(b.getBackground() == Color.MAGENTA){
					cont.movePiece(b);
				}
/*
				 // swapped out message reporting pos for dialog with options
				JPanel p = new JPanel();
				p.setLayout( new FlowLayout() ) ;
				p.setPreferredSize( new Dimension( 100, 50 ) );
				JRadioButton rb = new JRadioButton("Move your pawn here.");
			    // add the buttons to the panel
				p.add(rb);
			    // call a JOptionPane to display the radio buttons
			    JOptionPane.showMessageDialog(null,p);
			    
			    // perform action based on user's selection
			    if(rb.isSelected()){
			    	JOptionPane.showMessageDialog( null, "Selected move pawn to (" + x() + "," + y() + ")." );
			    } 
*/

				//b.clicked();

			}
		});
	}
	/*public void clicked(){
		int answer = 0;
		if(this.getBackground() == Color.MAGENTA){
			answer = JOptionPane.showConfirmDialog(null, "Move here?", "Pawn Placement", JOptionPane.YES_NO_OPTION);
			if( answer == 0 ){
				//System.out.println("YES");
				cont.movePiece(this);
			}
		}
	}*/
}

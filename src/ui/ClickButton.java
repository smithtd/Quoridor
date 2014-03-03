/* Author: Tyler Smith and Eli Donahue
 * Class provides buttons with action listeners
 */

package ui;

import javax.swing.*;
// using ImageIcon(?), JButton, JOptionPane, JPanel, etc.
import java.awt.*;
import java.awt.event.*;
// using ActionEvent, ActionListener, GridLayout

@SuppressWarnings("serial")
public class ClickButton extends JButton {

	// Instance variables
	public static Icon Square_Red = new ImageIcon("../Images/Square_Red.png");	// what is this???
	public int buttonSize;
	public Point pos;
	
	// Constructors
	public ClickButton( int buttonSize , Point p){
		super(Square_Red);
		this.pos = p;
		this.buttonSize = buttonSize;
	}
	
	public ClickButton( String label, int buttonSize ){
		super(label, Square_Red );
		this.buttonSize = buttonSize;
	}
	
	// adds an action listener to a ClickButton
	public void addButtonListener(){
		addActionListener( new ActionListener(){
			// we want the user to choose an action when they click a grid button
			public void actionPerformed( ActionEvent e ) {
				// swapped out message reporting pos for dialog with options
				JPanel p = new JPanel(new GridLayout(3,5));
				// create an array of radio button options
				JRadioButton[] rb = {new JRadioButton("Move your pawn here."), new JRadioButton("Place a horizontal wall starting here."), new JRadioButton("Place a vertical wall starting here.")};
			    // add the buttons to the panel
				p.add(rb[0]);
			    p.add(rb[1]);
			    p.add(rb[2]);
			    // call a JOptionPane to display the radio buttons
			    JOptionPane.showMessageDialog(null,p);
			    
			    // perform action based on user's selection
			    if(rb[0].isSelected()){
			    	JOptionPane.showMessageDialog(null,"Selected move pawn to ("+pos.getX()+","+pos.getY()+").");
			    } else if(rb[1].isSelected()){
			    	JOptionPane.showMessageDialog(null,"Selected place horizontal wall at ("+pos.getX()+","+pos.getY()+").");
			    } else if(rb[2].isSelected()){
			    	JOptionPane.showMessageDialog(null,"Selected place vertical wall at ("+pos.getX()+","+pos.getY()+").");
			    }
			}
		});
	}
}

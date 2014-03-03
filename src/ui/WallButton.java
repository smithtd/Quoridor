package ui;

import javax.swing.*;
//using ImageIcon(?), JButton, JOptionPane, JPanel, etc.
import java.awt.*;
import java.awt.event.*;
//using ActionEvent, ActionListener, GridLayout

@SuppressWarnings("serial")
public class WallButton extends JButton {
	
	//instance variables
	public static int clicks = 0;
	public int buttonSize;
	public Point pos;
	//keep track of height for the walls
	public int x;
	public int y;
	
	//Constructor
	public WallButton(int x, int y, int buttonSize , Point p){
		this.x = x;
		this.y = y;
		this.pos = p;
		this.buttonSize = buttonSize;
	}
	
	//More basic Constructor
	public WallButton( String label, int buttonSize ){
		this.buttonSize = buttonSize;
	}
	
	//makes walls around wall buttons based on amount of times clicked
	//Having trouble getting walls to work :/
	public void addButtonListener(){
		addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ) {
				//creates walls vertically
				// swapped out message reporting pos for dialog with options
				JPanel p = new JPanel(new GridLayout(3,5));
				// create an array of radio button options
				JRadioButton[] rb = {new JRadioButton("Place a horizontal wall starting here."), 
						new JRadioButton("Place a vertical wall starting here.")};
			    // add the buttons to the panel
				p.add(rb[0]);
			    p.add(rb[1]);
			    // call a JOptionPane to display the radio buttons
			    JOptionPane.showMessageDialog(null,p);
			    
			    // perform action based on user's selection
			    //still not working    >:(
			    if(rb[0].isSelected()){
			    	Wall w = new Wall(x,y);
					w.setBounds(x+11,y,8,100);
					w.setBackground(new Color(0,0,255));
					GameBoard.frame.add(w,FlowLayout.LEFT);
			    } else if(rb[1].isSelected()){
			    	Wall w = new Wall(x,y);
					w.setBounds(x,y,8,100);
					w.setBackground(new Color(0,0,255));
					GameBoard.frame.add(w,FlowLayout.LEFT);
			    }
			}
		});
	}
	
}

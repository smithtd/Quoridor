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
				clicks++;
				if(clicks%3==1){
					JButton wall = new JButton();
					wall.setBounds(x-11,y-101,8,100);
					wall.setBackground(new Color(51,235,51));
					GameBoard.frame.add(wall);
					JButton wall2 = new JButton();
					wall.setBounds(x-11,y+11,8,100);
					wall.setBackground(new Color(51,235,51));
					GameBoard.frame.add(wall);
					GameBoard.frame.add(wall2);
					GameBoard.frame.repaint();
				}
				//creates walls horizontally
				else if(clicks%3==2){
					JButton wall = new JButton();
					wall.setBounds(x-101,y+11,8,100);
					wall.setBackground(new Color(51,235,51));
					GameBoard.frame.add(wall);
					JButton wall2 = new JButton();
					wall.setBounds(x+11,y+11,8,100);
					wall.setBackground(new Color(51,235,51));
					GameBoard.frame.add(wall);	
					GameBoard.frame.add(wall2);
					GameBoard.frame.repaint();
				}
			}
		});
	}
}

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
	private static Controller cont;
	
	//Constructor
	public WallButton( int x, int y, Controller cont ){
		super();
		this.x = x;
		this.y = y;
		WallButton.cont = cont;
		this.setPreferredSize( new Dimension( 10, 10 ) );
		this.setBackground( Color.BLUE );
		addButtonListener();
	}
	
	//makes walls around wall buttons based on amount of times clicked
	//Having trouble getting walls to work :/
	public void addButtonListener(){
		addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ) {
				//creates walls vertically
				
				// swapped out message reporting pos for dialog with options
				JFrame tempFrame = new JFrame( " Wall Options" );
				JPanel p1 = new JPanel(new GridLayout(3,5));
				JPanel p2 = new JPanel(); 
				
				// create an array of radio button options
				JRadioButton rb1 = new JRadioButton("Place a horizontal wall starting here.");
				JRadioButton rb2 = new JRadioButton("Place a vertical wall starting here.");
				JRadioButton rb3 = new JRadioButton("Neither.");
				
				// setSelected = false
				rb1.setSelected(false);
				rb2.setSelected(false);
				rb3.setSelected(false);
				
				// add the buttons to the panel and buttongroup
				ButtonGroup btnGrp = new ButtonGroup();
				p1.add(rb1);
			    p1.add(rb2);
			    p1.add(rb3);
			    btnGrp.add(rb1);
			    btnGrp.add(rb2);
			    btnGrp.add(rb3);
			   
			    // call a JOptionPane to display the radio buttons
			    JOptionPane.showMessageDialog(null,p1);
			    
			    JRadioButton ready = new JRadioButton("Okay!", false);
				p2.add( ready ); 
				
				/*
				 * FIXME
				 * 
				 * While none of the button group radio buttons are selected you 
				 * cannot select the okay button
				 */
				while( ready.isSelected() == false ){
					if( ready.isSelected() && !(rb1.isSelected() || rb2.isSelected() || rb3.isSelected() ) ){
				    	ready.setSelected( false );
					}else if( ready.isSelected() && (rb1.isSelected() || rb2.isSelected()) || !rb3.isSelected() ){
						/*
						 * 
						 * THE SWITCHING OF THE X AND Y COORDINATES IS NOT 
						 * ACCIDENTAL DUE TO HOW MATRICIES WORK IN THIS CASE!!
						 * THE FIRST VALUE IS ACTUALLY THE VERTICAL AND SECOND 
						 * IS THE HORIZONTAL
						 * 
						 * It doesn't make sense but thats how this
						 *
						 *		cont.getHorzWalls()[x][y+1];
						 *		cont.getHorzWalls()[x][y];
						 *
						 * changes the horizontal walls even though it looks 
						 * like the vertical should be changed
						 * 
						 */
						if( rb1.isSelected() ){
							try{
								cont.getHorzWalls()[x][y+1].setBackground( Color.GREEN );
								cont.getHorzWalls()[x][y+1].repaint();
								cont.getHorzWalls()[x][y].setBackground(  Color.GREEN );
								cont.getHorzWalls()[x][y].repaint();
							}catch(Exception ex){
								System.out.println("ERROR IN HORZ");
							}
						}else if( rb2.isSelected() ){
							try{
								cont.getVertWalls()[x][y].setBackground( Color.GREEN );
								cont.getVertWalls()[x][y].repaint();
								cont.getVertWalls()[x+1][y].setBackground( Color.GREEN );
								cont.getVertWalls()[x+1][y].repaint();
							}catch(Exception ex){
								System.out.println("ERROR IN VERT");
							}
						}else if( rb3.isSelected() ){
							System.out.println("NONE SELECTED");
						}
						
						//FIXME Stuck in never ending loop
						System.out.println("DEBUG 1");
						tempFrame.dispose();
						
					}else{
						try{
							/*
							 * Just to stop the system from lagging. The pauses 
							 * stop it from working hard to do nothing while 
							 * waiting for a selection to be made
							 */
							Thread.sleep(100);
						}catch(Exception e2){}
					}
				}
			}
		});
	}
	
}

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
	public static Wall[][] vertWalls;
	public static Wall[][] horzWalls;
	
	
	//Constructor
	public WallButton(int x, int y, int buttonSize , Point p, Wall[][] vertWalls, Wall[][] horzWalls){
		this.x = x;
		this.y = y;
		this.pos = p;
		this.buttonSize = buttonSize;
		WallButton.vertWalls = vertWalls;
		WallButton.horzWalls = horzWalls;
	}
	
	public WallButton( int x, int y ){
		super();
		this.x = x;
		this.y = y;
		this.setPreferredSize( new Dimension( 10, 10 ) );
		this.setBackground( Color.BLUE );
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
				JFrame tempFrame = new JFrame( " Wall Options" );
				JPanel p1 = new JPanel(new GridLayout(3,5));
				JPanel p2 = new JPanel(); 
				// create an array of radio button options
				JRadioButton rb1 = new JRadioButton("Place a horizontal wall starting here.");
				JRadioButton rb2 = new JRadioButton("Place a vertical wall starting here.");
				JRadioButton rb3 = new JRadioButton("Neither.");
				// add the buttons to the panel
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
				
				while(true){
					if( ready.isSelected() && !rb1.isSelected() && !rb2.isSelected() && !rb3.isSelected() )
				    	ready.setSelected( false );
					else if( ready.isSelected() && (rb1.isSelected() || rb2.isSelected()) || !rb3.isSelected() ){
						if( rb1.isSelected() ){
							try{
								horzWalls[x][y].setBackground( Color.GREEN );
								horzWalls[x][y].repaint();
								horzWalls[x+1][y].setBackground(  Color.GREEN );
								horzWalls[x+1][y].repaint();
							}catch(Exception ex){
								System.out.println("ERROR IN HORZ");
							}
						}else if( rb2.isSelected() ){
							try{
								vertWalls[x][y].setBackground( Color.GREEN );
								vertWalls[x][y].repaint();
								vertWalls[x][y+1].setBackground( Color.GREEN );
								vertWalls[x][y+1].repaint();
							}catch(Exception ex){
								System.out.println("ERROR IN VERT");
							}
						}else if( rb3.isSelected() ){
							System.out.println("NONE SELECTED");
						}
						tempFrame.dispose();
						return;
					}else
						try{
							Thread.sleep(100);
						}catch(Exception e2){}
				}
			    
			    
			    // perform action based on user's selection
			   /* 
			    if(rb1.isSelected()){
			    	Wall w = new Wall(x,y);
					w.setBounds(x+11,y,8,100);
					w.setBackground(new Color(0,0,255));
					GameBoard.frame.add(w,FlowLayout.LEFT);
			    } else if(rb2.isSelected()){
			    	Wall w = new Wall(x,y);
					w.setBounds(x,y,8,100);
					w.setBackground(new Color(0,0,255));
					GameBoard.frame.add(w,FlowLayout.LEFT);
			    }
			    */
			}
		});
	}
	
}

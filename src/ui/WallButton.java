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
	private JFrame tempFrame;
	
	//Constructor
	public WallButton( int x, int y, Controller cont ){
		super();
		this.setBorder(null);
		this.x = x;
		this.y = y;
		WallButton.cont = cont;
		this.setPreferredSize( new Dimension( 10, 10 ) );
		this.setBackground( Color.BLACK );
		addButtonListener();
	}
	
	//makes walls around wall buttons based on amount of times clicked
	//Having trouble getting walls to work :/
	public void addButtonListener(){
		final WallButton b = this;
		addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ) {
				b.clicked();
			}
		});
	}
	
	public void clicked(){
		//creates walls vertically
		final WallButton b = this;
		final Color upC = cont.getVertWalls()[x][y].getBackground();
		final Color downC = cont.getVertWalls()[x+1][y].getBackground();
		final Color leftC = cont.getVertWalls()[x][y].getBackground();
		final Color rightC = cont.getVertWalls()[x][y+1].getBackground();
		
		int boardW = 600;
		int boardH = 400;

		JPanel jp1 = new JPanel();
		jp1.setSize(boardW, boardH/2);
		JPanel jp2 = new JPanel();
		jp2.setSize(boardW, boardH/2);
		
		tempFrame = new JFrame( "Wall Options" );
		tempFrame.setLayout( new GridLayout(1,2) );

		ButtonGroup btnGrp = new ButtonGroup();
		JRadioButton b1 = new JRadioButton("Place a horizontal wall starting here.", false );
		JRadioButton b2 = new JRadioButton("Place a vertical wall starting here.", false );
		JRadioButton b3 = new JRadioButton("Neither.", false );
		b1.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ){

					Wall right = cont.getHorzWalls()[x][y+1];
					Wall left = cont.getHorzWalls()[x][y];
					Wall up = cont.getHorzWalls()[x][y];
					Wall down = cont.getHorzWalls()[x+1][y];
					right.setBackground(rightC);
					left.setBackground(leftC);
					down.setBackground(downC);
					up.setBackground(upC);
					
					
					if(right.getBackground() != Color.green && left.getBackground() != Color.green && b.getBackground()!= Color.green ){
						right.setBackground( Color.GREEN);
						left.setBackground( Color.green );
						b.setBackground( Color.green );
					}
			}
		});
		
		b2.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ){

				Wall right = cont.getHorzWalls()[x][y+1];
				Wall left = cont.getHorzWalls()[x][y];
				Wall up = cont.getHorzWalls()[x][y];
				Wall down = cont.getHorzWalls()[x+1][y];
				right.setBackground(rightC);
				left.setBackground(leftC);
				down.setBackground(downC);
				up.setBackground(upC);
				
				if(down.getBackground() != Color.green && up.getBackground() != Color.green && b.getBackground()!= Color.green ){
					down.setBackground( Color.GREEN);
					up.setBackground( Color.green );
					b.setBackground( Color.green );
				}
			}
		});
		b3.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ){
				Wall right = cont.getHorzWalls()[x][y+1];
				Wall left = cont.getHorzWalls()[x][y];
				Wall up = cont.getHorzWalls()[x][y];
				Wall down = cont.getHorzWalls()[x+1][y];
				right.setBackground(rightC);
				left.setBackground(leftC);
				down.setBackground(downC);
				up.setBackground(upC);
			}
		});
		
		// add the buttons to the panel and buttongroup
	    btnGrp.add(b1);
	    btnGrp.add(b2);
	    btnGrp.add(b3);
		jp1.add(b1);
	    jp1.add(b2);
	    jp1.add(b3);
	   
	    JRadioButton ready = new JRadioButton("Okay!", false);
		jp2.add( ready ); 
	    
		tempFrame.add( jp1, BorderLayout.NORTH );
		tempFrame.add( jp2, BorderLayout.SOUTH );
		tempFrame.setLocation( 350, 300 );
		tempFrame.setSize( 250, 100 );
		tempFrame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		tempFrame.setVisible( true );
		
		
		/*
		 * FIXME
		 * 
		 * While none of the button group radio buttons are selected you 
		 * cannot select the okay button
		 */
		
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
		while(true)
			if( ready.isSelected()  && 
			    !b1.isSelected()    && 
			    !b2.isSelected()    && 
			    !b3.isSelected() )
		    	ready.setSelected( false );
			else if( ready.isSelected() && 
			        (b1.isSelected()    || 
			         b2.isSelected()    || 
			         b3.isSelected()) ){
				tempFrame.dispose();
				return;
			}else
				try{
					Thread.sleep(100);
					jp1.repaint();
					jp2.repaint();
				}catch(Exception e2){}
	}
	
}

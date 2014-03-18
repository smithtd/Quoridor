package ui;

import javax.swing.*;

import players.Player;
import walls.Wall;

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
	private JFrame tempFrame;
	private Color yesWallColor = new Color(154,97,41);
	private Color noWallColor = Color.gray;
	private Color upC;
	private Color downC;
	private Color leftC;
	private Color rightC;
	private Color thisC;
	
	
	//Constructor
	public WallButton( int x, int y, Controller cont ){
		super();
		this.setBorder(null);
		this.x = x;
		this.y = y;
		this.setPreferredSize( new Dimension( 10, 10 ) );
		this.setBackground( Color.BLACK );
		addButtonListener();
	}
	
	//Constructor
		public WallButton( int x, int y ){
			super();
			this.setBorder(null);
			this.x = x;
			this.y = y;
			this.setPreferredSize( new Dimension( 10, 10 ) );
			this.setBackground( Color.BLACK );
			this.setOpaque(true);
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
	
	// when a button is clicked, we need to call back end
	public void clicked(){
		final WallButton b = this;
//		upC = cont.getVertWalls()[x][y].getBackground();
//		downC = cont.getVertWalls()[x+1][y].getBackground();
//		leftC = cont.getHorzWalls()[x][y].getBackground();
//		rightC = cont.getHorzWalls()[x][y+1].getBackground();
		thisC = b.getBackground();
		
		if( thisC != Color.black )
			return;
		
		// creating a dialog box?
		JPanel jp1 = new JPanel();
		jp1.setLayout( new FlowLayout() ) ;
		jp1.setPreferredSize( new Dimension( 300, 50 ) );
		JPanel jp2 = new JPanel();
		jp2.setPreferredSize( new Dimension( 300, 50 ) );
		
		tempFrame = new JFrame( "Wall Options" );
		tempFrame.setLayout( new BorderLayout() );

		ButtonGroup btnGrp = new ButtonGroup();
		//if player's piece can get to the other side we give them an option to
		//add a wall, otherwise no option comes up at all in order to not block 
		//anyones way
		//if(canGetAcross()){
		final JRadioButton b1 = new JRadioButton("Horizontal", false );
		final JRadioButton b2 = new JRadioButton("Vertical", false );
		final JRadioButton b3 = new JRadioButton("Neither", false );
		b1.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ){
				System.out.println( b.placeHorzWall() );
			}
		});
		
		b2.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ){
				System.out.println( b.placeVertWall() );
			}
		});
		
		b3.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ){
				System.out.println(  b.resetWall() );
			}
		});
		//}
		
		// add the buttons to the panel and button group
	    btnGrp.add(b1);
	    btnGrp.add(b2);
	    btnGrp.add(b3);
	    jp1.add(b3, FlowLayout.LEFT);
	    jp1.add(b2, FlowLayout.LEFT);
		jp1.add(b1, FlowLayout.LEFT);
	   
	    JButton ready = new JButton("Okay!");
	    ready.addActionListener( new ActionListener(){
	    	public void actionPerformed( ActionEvent e ){
	    		if(!(b1.isSelected() || b2.isSelected() || b3.isSelected() ) )
	    			;
	    		else {
//	    			if( (b1.isSelected() || b2.isSelected()) && cont.getWallsRem()[cont.getPlyrIndex()]>0 ){
//						cont.getWallsRem()[cont.getPlyrIndex()]--;
//	    				cont.nextPlayerMove();
//	    				Player.usedWall();
//	    			}
	    			tempFrame.dispose();
	    		}
	    	}
	    });
		jp2.add( ready ); 
	    
		tempFrame.add( jp1, BorderLayout.NORTH );
		tempFrame.add( jp2, BorderLayout.SOUTH );
		tempFrame.setLocation( 350, 300 );
		tempFrame.setSize( 250, 100 );
		tempFrame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		tempFrame.setVisible( true );
		tempFrame.pack();

	}
	
	public int placeHorzWall(){
//		Wall right = cont.getHorzWalls()[x][y+1];
//		Wall left = cont.getHorzWalls()[x][y];
//		Wall up = cont.getVertWalls()[x][y];
//		Wall down = cont.getVertWalls()[x+1][y];
//		right.setBackground(rightC);
//		left.setBackground(leftC);
//		down.setBackground(downC);
//		up.setBackground(upC);
//		this.setBackground( thisC );
		
//		if(right.getBackground()==noWallColor && left.getBackground()==noWallColor && this.getBackground()==Color.BLACK && cont.getWallsRem()[cont.getPlyrIndex()]>0 ){
//			right.setBackground( yesWallColor );
//			left.setBackground( yesWallColor );
//			this.setBackground( yesWallColor );
//			return 1;
//		}
		System.err.println("Place horz wall at "+x+" "+y);
		return 0;
		
		
	}
	
	public int placeVertWall(){
//		Wall right = cont.getHorzWalls()[x][y+1];
//		Wall left = cont.getHorzWalls()[x][y];
//		Wall up = cont.getVertWalls()[x][y];
//		Wall down = cont.getVertWalls()[x+1][y];
//		right.setBackground(rightC);
//		left.setBackground(leftC);
//		down.setBackground(downC);
//		up.setBackground(upC);
//		this.setBackground( thisC );

//		if(down.getBackground()==noWallColor && up.getBackground()==noWallColor && this.getBackground()==Color.BLACK && cont.getWallsRem()[cont.getPlyrIndex()]>0 ){
//			down.setBackground( yesWallColor );
//			up.setBackground( yesWallColor );
//			this.setBackground( yesWallColor );
//			return 1;
//		}
		System.err.println("Place vert wall at "+x+" "+y);
		return 0;
	}
	
	public int resetWall(){
//		Wall right = cont.getHorzWalls()[x][y+1];
//		Wall left = cont.getHorzWalls()[x][y];
//		Wall up = cont.getVertWalls()[x][y];
//		Wall down = cont.getVertWalls()[x+1][y];
//		right.setBackground(rightC);
//		left.setBackground(leftC);
//		down.setBackground(downC);
//		up.setBackground(upC);				
//		this.setBackground( thisC );
		return 0;
	}
}

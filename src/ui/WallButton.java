package ui;

import javax.swing.*;

import players.Player;
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
		upC = cont.getVertWalls()[x][y].getBackground();
		downC = cont.getVertWalls()[x+1][y].getBackground();
		leftC = cont.getHorzWalls()[x][y].getBackground();
		rightC = cont.getHorzWalls()[x][y+1].getBackground();
		thisC = b.getBackground();
		
		if( thisC != Color.black )
			return;
		
		JPanel jp1 = new JPanel();
		jp1.setLayout( new FlowLayout() ) ;
		jp1.setPreferredSize( new Dimension( 300, 50 ) );
		JPanel jp2 = new JPanel();
		jp2.setPreferredSize( new Dimension( 300, 50 ) );
		
		tempFrame = new JFrame( "Wall Options" );
		tempFrame.setLayout( new BorderLayout() );

		ButtonGroup btnGrp = new ButtonGroup();
		final JRadioButton b1 = new JRadioButton("Horizontal", false );
		final JRadioButton b2 = new JRadioButton("Vertical", false );
		final JRadioButton b3 = new JRadioButton("Neither", false );
		b1.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ){
				b.placeHorzWall();
			}
		});
		
		b2.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ){
				b.placeVertWall();
			}
		});
		
		b3.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ){
				b.resetWall();
			}
		});
		
		// add the buttons to the panel and buttongroup
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
	    			if(b1.isSelected() || b2.isSelected()){
	    				cont.nextPlayerMove();
	    				Player.usedWall();
	    			}
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

		if( ready.isSelected() && !b1.isSelected() && !b2.isSelected() && !b3.isSelected() ){
		    	ready.setSelected( false );
		    	System.out.println( "DEBUG WallButton 1" );
			}else if( ready.isSelected() && (b1.isSelected() || b2.isSelected() || b3.isSelected()) ){
				tempFrame.dispose();
		    	System.out.println( "DEBUG WallButton 2" );
				return;
			}else{
				try{
					Thread.sleep(100);
				}catch(Exception e2){}
				if( b1.isSelected() )
			    	System.out.println( "DEBUG WallButton 3" );
				if( b2.isSelected() )
			    	System.out.println( "DEBUG WallButton 4" );
				if( b3.isSelected() )
			    	System.out.println( "DEBUG WallButton 5" );
				if( ready.isSelected() )
			    	System.out.println( "DEBUG WallButton 6" );
					
		    	//System.out.println( "DEBUG WallButton " + b1.isSelected() + ( b2.isSelected() )  + ( b3.isSelected() ) + ( ready.isSelected() ));
				System.out.println( "DEBUG WallButton " + jp1 );
				System.out.println( "DEBUG WallButton " + jp2 );
				System.out.println( "DEBUG WallButton " + tempFrame );
				System.out.println();
				System.out.println();
			}
	}
	
	public void placeHorzWall(){
		Wall right = cont.getHorzWalls()[x][y+1];
		Wall left = cont.getHorzWalls()[x][y];
		Wall up = cont.getVertWalls()[x][y];
		Wall down = cont.getVertWalls()[x+1][y];
		right.setBackground(rightC);
		left.setBackground(leftC);
		down.setBackground(downC);
		up.setBackground(upC);
		this.setBackground( thisC );
		
		if(right.getBackground()==noWallColor && left.getBackground()==noWallColor && this.getBackground()==Color.BLACK ){
			System.out.println("NONE BROWN");
			right.setBackground( yesWallColor );
			left.setBackground( yesWallColor );
			this.setBackground( yesWallColor );
		}
	}
	
	public void placeVertWall(){
		Wall right = cont.getHorzWalls()[x][y+1];
		Wall left = cont.getHorzWalls()[x][y];
		Wall up = cont.getVertWalls()[x][y];
		Wall down = cont.getVertWalls()[x+1][y];
		right.setBackground(rightC);
		left.setBackground(leftC);
		down.setBackground(downC);
		up.setBackground(upC);
		this.setBackground( thisC );

		if(down.getBackground()==noWallColor && up.getBackground()==noWallColor && this.getBackground()==Color.BLACK ){
			down.setBackground( yesWallColor );
			up.setBackground( yesWallColor );
			this.setBackground( yesWallColor );
		}
	}
	
	public void resetWall(){
		Wall right = cont.getHorzWalls()[x][y+1];
		Wall left = cont.getHorzWalls()[x][y];
		Wall up = cont.getVertWalls()[x][y];
		Wall down = cont.getVertWalls()[x+1][y];
		right.setBackground(rightC);
		left.setBackground(leftC);
		down.setBackground(downC);
		up.setBackground(upC);				
		this.setBackground( thisC );
	}
}

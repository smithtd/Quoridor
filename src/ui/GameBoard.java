package ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import players.Player;

@SuppressWarnings("serial")
public class GameBoard extends JPanel {

	public JFrame frame;
	
	/*
	 * Basic details of this Panel like the panel dimensions and what layout to 
	 * use to add JObjects 
	 */
	public GameBoard(){
		this.setPreferredSize( new Dimension( 500,500 ) );	//Makes the JPanel 500px * 500px
		this.setLayout( null );
		setFrameStats();
	}
	
	/*
	 * The JFrame is what holds JPanel, which is what class type this file is,
	 * so we need to set it up internally so that it can hold a GameBoard object
	 * and suport it
	 */
	public void setFrameStats(){
		frame = new JFrame( "Quoridor" ); 		//Title of JFrame window is "Quoridor"

		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		frame.setLayout( gridbag );
		addPanels( frame, gridbag, c );
		addOtherJObjects();
		
		frame.add( this );						//Adds this JPanel to JFrame
		frame.setVisible( true );				//Sets Frame to visible
		frame.setResizable( false );			//Doesn't allow resizing of frame
		frame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );	//when frame is closed, the program terminates
		frame.setLocation( 150, 150 );
		frame.pack();	//collapses frame to minimum size around all JObjects inside it
	}
	
	/*
	 * Sets up the GridBag Layout with JFrame and JPanel
	 */
	public void addPanels( JFrame frame, 
			   GridBagLayout gridbag,
			   GridBagConstraints c ){
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;			//1.0
		c.gridwidth = GridBagConstraints.REMAINDER;
		JMenuBar menus = getMenus();
		gridbag.setConstraints( menus, c );
		frame.add( menus );
		c.weighty = 1.0;
		c.gridheight = GridBagConstraints.REMAINDER;
		gridbag.setConstraints( this, c );
		frame.add( this );
	}
	
	
	/*
	 * A MenuBar holds a Menu which in turn hold a MenuItem. That MenuItem 
	 * is what has the changeable consequence if clicked. The Menu just drops 
	 * down the Items in the menu 
	 */
	public JMenuBar getMenus(){
		JMenuBar menuBar = new JMenuBar();		
		JMenu fileMenu = new JMenu( "File" );
		JMenuItem quitOpt = new JMenuItem( "Quit" );
		quitOpt.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ){
				System.exit( 0 );	//When quit option is selected the program terminates and closes the window
			}
		});
		fileMenu.add( quitOpt );
		menuBar.add( fileMenu );
		return menuBar;
	}
	
	public void addOtherJObjects(){
		addClickButton();
	}
	/*
	 * Click-able buttons that displays dialog of its position when pressed. No other
	 * functionality 
	 * 
	 * Each button name corresponds to its position on the gameboard. For example, 
	 * cba1 refers to the first button in the top left corner. 
	 */
	public void addClickButton(){
		ClickButton cba1 = new ClickButton( 100, "a1");
		cba1.setBounds( 5, 5, 50, 50 );
		cba1.addButtonListener();
		this.add( cba1, FlowLayout.LEFT );
		ClickButton cba2 = new ClickButton( 100 , "a2");
		cba2.setBounds( 60, 5, 50, 50 );
		cba2.addButtonListener();
		this.add( cba2, FlowLayout.LEFT );
		ClickButton cba3 = new ClickButton( 100 , "a3");
		cba3.setBounds( 115, 5, 50, 50 );
		cba3.addButtonListener();
		this.add( cba3, FlowLayout.LEFT );
		ClickButton cba4 = new ClickButton( 100 , "a4");
		cba4.setBounds( 170, 5, 50, 50 );
		cba4.addButtonListener();
		this.add( cba4, FlowLayout.LEFT );
		ClickButton cba5 = new ClickButton( 100, "a5" );
		cba5.setBounds( 225, 5, 50, 50 );
		cba5.addButtonListener();
		this.add( cba5, FlowLayout.LEFT );
		ClickButton cba6 = new ClickButton( 100 , "a6");
		cba6.setBounds( 280, 5, 50,50 );
		cba6.addButtonListener();
		this.add( cba6, FlowLayout.LEFT );
		ClickButton cba7 = new ClickButton( 100 , "a7");
		cba7.setBounds( 335, 5, 50, 50 );
		cba7.addButtonListener();
		this.add( cba7, FlowLayout.LEFT );
		ClickButton cba8 = new ClickButton( 100 , "a8");
		cba8.setBounds( 390, 5, 50, 50 );
		cba8.addButtonListener();
		this.add( cba8, FlowLayout.LEFT );
		ClickButton cba9 = new ClickButton( 100 , "a9");
		cba9.setBounds( 445, 5, 50, 50 );
		cba9.addButtonListener();
		this.add( cba9, FlowLayout.LEFT );
		////////////////////////////////////////////////
		ClickButton cbb1 = new ClickButton( 100 , "b1");
		cbb1.setBounds( 5, 60, 50, 50 );
		cbb1.addButtonListener();
		this.add( cbb1, FlowLayout.LEFT );
		ClickButton cbb2 = new ClickButton( 100 , "b2");
		cbb2.setBounds( 60, 60, 50, 50 );
		cbb2.addButtonListener();
		this.add( cbb2, FlowLayout.LEFT );
		ClickButton cbb3 = new ClickButton( 100 , "b3");
		cbb3.setBounds( 115, 60, 50, 50 );
		cbb3.addButtonListener();
		this.add( cbb3, FlowLayout.LEFT );
		ClickButton cbb4 = new ClickButton( 100 , "b4");
		cbb4.setBounds( 170, 60, 50, 50 );
		cbb4.addButtonListener();
		this.add( cbb4, FlowLayout.LEFT );
		ClickButton cbb5 = new ClickButton( 100, "b5" );
		cbb5.setBounds( 225, 60, 50, 50 );
		cbb5.addButtonListener();
		this.add( cbb5, FlowLayout.LEFT );
		ClickButton cbb6 = new ClickButton( 100 , "b6");
		cbb6.setBounds( 280, 60, 50, 50 );
		cbb6.addButtonListener();
		this.add( cbb6, FlowLayout.LEFT );
		ClickButton cbb7 = new ClickButton( 100 , "b7");
		cbb7.setBounds( 335, 60, 50, 50 );
		cbb7.addButtonListener();
		this.add( cbb7, FlowLayout.LEFT );
		ClickButton cbb8 = new ClickButton( 100, "b8" );
		cbb8.setBounds( 390, 60, 50, 50 );
		cbb8.addButtonListener();
		this.add( cbb8, FlowLayout.LEFT );
		ClickButton cbb9 = new ClickButton( 100 , "b9");
		cbb9.setBounds( 445, 60, 50, 50 );
		cbb9.addButtonListener();
		this.add( cbb9, FlowLayout.LEFT );
		//////////////////////////////////////////////////
		ClickButton cbc1 = new ClickButton( 100 , "c1");
		cbc1.setBounds( 5, 115, 50, 50 );
		cbc1.addButtonListener();
		this.add( cbc1, FlowLayout.LEFT );
		ClickButton cbc2 = new ClickButton( 100 , "c2");
		cbc2.setBounds( 60, 115, 50, 50 );
		cbc2.addButtonListener();
		this.add( cbc2, FlowLayout.LEFT );
		ClickButton cbc3 = new ClickButton( 100 , "c3");
		cbc3.setBounds( 115, 115, 50, 50 );
		cbc3.addButtonListener();
		this.add( cbc3, FlowLayout.LEFT );
		ClickButton cbc4 = new ClickButton( 100, "c4" );
		cbc4.setBounds( 170, 115, 50, 50 );
		cbc4.addButtonListener();
		this.add( cbc4, FlowLayout.LEFT );
		ClickButton cbc5 = new ClickButton( 100 , "c5");
		cbc5.setBounds( 225, 115, 50, 50 );
		cbc5.addButtonListener();
		this.add( cbc5, FlowLayout.LEFT );
		ClickButton cbc6 = new ClickButton( 100 , "c6");
		cbc6.setBounds( 280, 115, 50, 50 );
		cbc6.addButtonListener();
		this.add( cbc6, FlowLayout.LEFT );
		ClickButton cbc7 = new ClickButton( 100 , "c7");
		cbc7.setBounds( 335, 115, 50, 50 );
		cbc7.addButtonListener();
		this.add( cbc7, FlowLayout.LEFT );
		ClickButton cbc8 = new ClickButton( 100 , "c8");
		cbc8.setBounds( 390, 115, 50, 50 );
		cbc8.addButtonListener();
		this.add( cbc8, FlowLayout.LEFT );
		ClickButton cbc9 = new ClickButton( 100 , "c9");
		cbc9.setBounds( 445, 115, 50, 50 );
		cbc9.addButtonListener();
		this.add( cbc9, FlowLayout.LEFT );
		/////////////////////////////////////////////////
		ClickButton cbd1 = new ClickButton( 100, "d1" );
		cbd1.setBounds( 5, 170, 50, 50 );
		cbd1.addButtonListener();
		this.add( cbd1, FlowLayout.LEFT );
		ClickButton cbd2 = new ClickButton( 100, "d2" );
		cbd2.setBounds( 60, 170, 50, 50 );
		cbd2.addButtonListener();
		this.add( cbd2, FlowLayout.LEFT );
		ClickButton cbd3 = new ClickButton( 100 , "d3");
		cbd3.setBounds( 115, 170, 50, 50 );
		cbd3.addButtonListener();
		this.add( cbd3, FlowLayout.LEFT );
		ClickButton cbd4 = new ClickButton( 100 , "d4");
		cbd4.setBounds( 170, 170, 50, 50 );
		cbd4.addButtonListener();
		this.add( cbd4, FlowLayout.LEFT );
		ClickButton cbd5 = new ClickButton( 100, "d5" );
		cbd5.setBounds( 225, 170, 50, 50 );
		cbd5.addButtonListener();
		this.add( cbd5, FlowLayout.LEFT );
		ClickButton cbd6 = new ClickButton( 100 , "d6");
		cbd6.setBounds( 280, 170, 50, 50 );
		cbd6.addButtonListener();
		this.add( cbd6, FlowLayout.LEFT );
		ClickButton cbd7 = new ClickButton( 100 , "d7");
		cbd7.setBounds( 335, 170, 50, 50 );
		cbd7.addButtonListener();
		this.add( cbd7, FlowLayout.LEFT );
		ClickButton cbd8 = new ClickButton( 100 , "d8");
		cbd8.setBounds( 390, 170, 50, 50 );
		cbd8.addButtonListener();
		this.add( cbd8, FlowLayout.LEFT );
		ClickButton cbd9 = new ClickButton( 100, "d9" );
		cbd9.setBounds( 445, 170, 50, 50 );
		cbd9.addButtonListener();
		this.add( cbd9, FlowLayout.LEFT );
		/////////////////////////////////////////////////
		ClickButton cbe1 = new ClickButton( 100 , "e1");
		cbe1.setBounds( 5, 225, 50, 50 );
		cbe1.addButtonListener();
		this.add( cbe1, FlowLayout.LEFT );
		/////////////////////////////////////////////////
		ClickButton cbf1 = new ClickButton( 100 , "f1");
		cbf1.setBounds( 5, 280, 50, 50 );
		cbf1.addButtonListener();
		this.add( cbf1, FlowLayout.LEFT );
		//////////////////////////////////////////////////
		ClickButton cbg1 = new ClickButton( 100, "g1" );
		cbg1.setBounds( 5, 335, 50, 50 );
		cbg1.addButtonListener();
		this.add( cbg1, FlowLayout.LEFT );
		////////////////////////////////////////////////////
		ClickButton cbh1 = new ClickButton( 100 , "h1");
		cbh1.setBounds( 5, 390, 50, 50 );
		cbh1.addButtonListener();
		this.add( cbh1, FlowLayout.LEFT );
		///////////////////////////////////////////////////
		ClickButton cbi1 = new ClickButton( 100 , "i1");
		cbi1.setBounds( 5, 445, 50, 50 );
		cbi1.addButtonListener();
		this.add( cbi1, FlowLayout.LEFT );
	}
	
	//OverRidden for JPanel graphics
	/*public void paint( Graphics g ){
		g.setColor( Color.black );
		g.fillRect( 50, 50, 400, 400 );
		g.setColor( Color.white );
		g.fillRect( 100, 100, 300, 300);
		g.setColor( Color.black );
		g.fillRect( 150, 150, 200, 200 );
		g.setColor( Color.white );
		g.fillRect( 200, 200, 100, 100);
		
	}	*/
	
	public static void main( String[] args ) {
		// Auto-generated method stub
		new GameBoard();
		Player p = new Player( "Player_1", 10 );
		p.getMove();

	}
	

}
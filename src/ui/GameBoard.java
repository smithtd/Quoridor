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
		this.setLayout( new FlowLayout() );
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
		c.weightx = 1.0;
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
		JMenu fileMenu = new JMenu( "FILE" );
		JMenuItem quitOpt = new JMenuItem( "QUIT" );
		quitOpt.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ){
				System.exit( 0 );
			}
		});
		fileMenu.add( quitOpt );
		menuBar.add( fileMenu );
		return menuBar;
	}
	
	/*
	 * Clickable button that displays dialog of "HELLO" when pressed. No other
	 * functionality 
	 */
	public void addClickButton(){
		JButton clickMe = new JButton();
		clickMe.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ) {
				JOptionPane.showMessageDialog(null,  "HELLO", "THIS MESSAGE", JOptionPane.PLAIN_MESSAGE);
			}
		});
		clickMe.setPreferredSize( new Dimension( 250, 250 ) );
		this.add( clickMe, FlowLayout.LEFT );
	}
	
	
	
	public static void main( String[] args ) {
		// Auto-generated method stub
		new GameBoard();
		Player p = new Player( "Dylan", 10 );
		

	}
	
	//OverRidden for JPanel graphics
	public void paint( Graphics g ){
		g.setColor( Color.black );
		g.fillRect( 0, 0, 500, 500 );
	}
}
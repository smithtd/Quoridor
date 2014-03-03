package ui;

import game.Game;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Point;

@SuppressWarnings("serial")
public class GameBoard extends JPanel {

	public static JFrame frame;
	public static Game g;
	private static Wall[][] vertWalls;
	private static Wall[][] horzWalls;
	private static ClickButton [][] cbAry;
	
	
	/*
	 * Basic details of this Panel like the panel dimensions and what layout to 
	 * use to add JObjects 
	 */
	public GameBoard( Game g ){
		this.setPreferredSize( new Dimension( 1000,1000 ) );	//Makes the JPanel 500px * 500px
		this.setLayout( null );
		setFrameStats();
		GameBoard.g = g;
	}
	
	/*
	 * The JFrame is what holds JPanel, which is what class type this file is,
	 * so we need to set it up internally so that it can hold a GameBoard object
	 * and support it
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
	
	public JFrame getFrame(){
		return GameBoard.frame;
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
		/*
		 JMenuItem undoOpt = new JMenuItem ("undo");
		undoOpt.addActionListener(new ActionListener()){
			public void actionPerformed(ActionEvent e){
				//decrement the wall placing int so players 
				//can take back wall placements if need be
			}
		}
		*/
		
		JMenuItem newGameOpt = new JMenuItem( "New Game" );
		newGameOpt.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ){
				g.newGame();
			}
		});
		fileMenu.add(newGameOpt);
		
		JMenuItem quitOpt = new JMenuItem( "Quit" );
		quitOpt.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ){
				//When quit option is selected the program terminates
				// closes the window
				System.exit( 0 );	
			}
		});
		fileMenu.add( quitOpt );
		//creates "help" in menubar
		JMenu helpMenu = new JMenu("Help");
		menuBar.add(helpMenu);
		helpMenu.setName("Help");
		
		//the rules tab in the help menu
		JMenuItem rulesOpt = new JMenuItem("Rules");
		rulesOpt.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String temp = "";
				temp += "Quoridor is played on a game board of 81 square spaces (9x9). \n" +
						"Each player is represented by a pawn which begins at the center \n" +
						"space of one edge of the board (in a two-player game, the pawns \n" +
						"begin opposite each other). The object is to be the first player \n" +
						"to move their pawn to any space on the opposite side of the \n" +
						"gameboard from which it begins.\n\n";
				temp += "The distinguishing characteristic of Quoridor is its twenty walls. \n" +
						"Walls are flat two-space-wide pieces which can be placed in the \n" +
						"groove that runs between the spaces. Walls block the path of all \n" +
						"pawns, which must go around them. The walls are divided equally \n" +
						"among the players at the start of the game, and once placed, cannot \n" +
						"be moved or removed. On a turn, a player may either move their \n" +
						"pawn, or, if possible, place a wall. \n\n";
				temp += "Pawns can be moved to an adjacent space (not diagonally), or, if \n" +
						"adjacent to another pawn, jump over that pawn. If an adjacent pawn \n" +
						"has a third pawn or a wall on the other side of it, the player may \n" +
						"move to any space that is immediately adjacent to other adjacent pawns.\n" +
						"The official rules are ambiguous concerning the edge of the board. \n\n";
				temp += "Walls can be placed directly between two spaces, in any groove not \n" +
						"already occupied by a wall. However, a wall may not be placed which \n" +
						"cuts off the only remaining path of any pawn to the side of the board \n" +
						"it must reach.\n";
				JOptionPane.showMessageDialog( frame , temp, "Rules of Quoridor",JOptionPane.PLAIN_MESSAGE);
			}
		});
		helpMenu.add( rulesOpt );
		
		//the About tab in the Help menu
		JMenuItem aboutOpt = new JMenuItem( "About" );
		aboutOpt.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String temp = "";
				temp += "Authors: \n";
				temp += "Dylan Woythal \n";
				temp += "Eli Donahue \n";
				temp += "Marc Dean \n";
				temp += "Tyler Smith ";
				JOptionPane.showMessageDialog( frame , temp, "About",JOptionPane.PLAIN_MESSAGE);
			}
		});
		helpMenu.add( aboutOpt );
		
		menuBar.add( fileMenu );
		menuBar.add(helpMenu);
		return menuBar;
	}
	
	// what is this??
	public void addOtherJObjects(){
		addClickButton();
		addWalls();
		addWallButtons();
	}
	
	/*
	 * Click-able buttons that displays options dialog to gather action from user
	 */
	public void addClickButton(){
		cbAry = new ClickButton[9][9];
		// start top left at (5,5)
		int x = 10;
		int y = 10;
		// outer loop adds rows
		for(int i=0; i<9; i++){
			// inner loop adds columns
			for(int j = 0; j<9; j++){
				ClickButton cb = new ClickButton(500, new Point(i, j));
				cb.setBounds(x,y,100,100);
				cb.addButtonListener();
				cb.setBackground(new Color(179,150,70));
				this.add(cb, FlowLayout.LEFT);
				// increment x to create next column over
				cbAry[i][j] = cb;
				x+=110;
			}
			// reset x, increment y
			x = 10;
			y += 110;
		}
	}
	
	//Builds walls out of un-click-able buttons
	public void addWalls(){
		vertWalls = new Wall[9][9];
		horzWalls = new Wall[9][8];
		
		int x = 111;
		int y = 10;
		// Vertical Walls
		for(int i = 0;i<9;i++){
			for(int j = 0;j<8;j++){
				Wall vWall = new Wall(x,y);
				vWall.setBounds(x,y,8,100);
				vWall.setBackground(new Color(255,0,255));
				this.add(vWall,FlowLayout.LEFT);
				vertWalls[i][j] = vWall;
				x+=110;
			}
			x = 111;
			y+=110;
		}
		x = 10;
		y = 111;
		
		//Horizontal Walls
		for(int i = 0;i<9;i++){
			for(int j = 0;j<8;j++){
				Wall hWall = new Wall(x,y);
				hWall.setBounds(x,y,100,8);
				hWall.setBackground(new Color(255,0,255));
				this.add(hWall, FlowLayout.LEFT);
				horzWalls[i][j] = hWall;
				y+=110;
			}
			x+=110;
			y = 111;
		}
		
	}
	
	/*
	 * click-able buttons that build walls
	 */
	public void addWallButtons(){
		int x = 110;
		int y = 110;
		//adds rows
		for(int i = 0;i<8; i++){
			//adds columns
			for(int j = 0;j<8; j++){
				WallButton wb = new WallButton(x,y,10, new Point(i, j));
				wb.setBounds(x,y,10,10);
				wb.addButtonListener();
				wb.setBackground(new Color(255,0,255));
				this.add(wb, FlowLayout.LEFT);
				x+=110;
			}
			//resets x, and increments y
			x = 110;
			y += 110;
		}
	}
}
package ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class GameBoard extends JPanel {

	public static JFrame frame;
	public static Game g;
	public static Controller cont;
	public static PlayerButton[][] pbAry;
	
	/*
	 * Basic details of this Panel like the panel dimensions and what layout to 
	 * use to add JObjects 
	 */
	public GameBoard( Game g ){
		super();
		cont = new Controller();
		FlowLayout flow = new FlowLayout();
		flow.setHgap( 0 );
		flow.setVgap( 0 );
		this.setLayout( flow );
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
		addOtherJObjectsToThis();
		addPanelsToJFrame( frame, gridbag, c );
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
	public void addPanelsToJFrame( JFrame frame, 
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
		this.setPreferredSize( new Dimension( 50*9 + 10*8, 50*9 + 10*8 ) );
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
	public void addOtherJObjectsToThis(){
		addJButtons();
		repaint();
	}

	public void addJButtons(){
		pbAry = new PlayerButton[9][9];
		Wall[][] vertWalls = new Wall[9][8];
		Wall[][] horzWalls = new Wall[8][9];
		WallButton[][] wbAry = new WallButton[8][8];
		//9x9 board
		for( int x = 0; x<9; x++ ){
			for( int layer=0; layer<2; layer++ ){
				for( int y=0; y<9; y++ ){
				//layer 0 is PlayerButton && Vert Walls
				//Layer 1 is HorzWalls && WallButton
					if( layer == 0 ){
						PlayerButton plyrBtn = new PlayerButton( x, y, cont );
						this.add( plyrBtn );
						pbAry[x][y] = plyrBtn;
						if( y!= 8 ){
							Wall w = new Wall( x, y, "vert", cont );
							this.add( w );
							vertWalls[x][y] = w;
						}
					} else if( x!= 8 ) {
						Wall w = new Wall( x, y, "horz", cont );
						this.add( w );
						horzWalls[x][y] = w;
						if( y!= 8 && x!=8 ){
							WallButton wb = new WallButton( x, y, cont );
							this.add( wb );
							wbAry[x][y] = wb;
						}
					}
				}
			}
		}
		cont.addHorzWalls( horzWalls );
		cont.addVertWalls( vertWalls );
		cont.addPlayerButtons( pbAry );
		cont.addWallButtons( wbAry );
	}
	
}
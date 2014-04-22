package ui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

// use to implement Subject/Observer design pattern
import java.util.ArrayList;
import java.util.Observable;  
import java.util.Observer;  

import terminal.Terminal;
import main.Game;
import board.Board;
import walls.Wall;
import players.Player;

/**
 * The GameBoard serves as the UI for the Quoridor g. It updates from the 
 * back end via a Subject/Observer design pattern. The g object is the 
 * subject and the GameBoard object is the observer.
 * 
 * @author Dylan Woythol, Tyler Smith, Eli Donahue
 *
 */
@SuppressWarnings("serial")
public class GameBoard extends JPanel implements Observer {

	/* Instance Variables */

	private JFrame frame;
	private PlayerButton[][] pbAry;
	private WallButton[][] wbAry;
	private LongWallButton[][] vertWalls;
	private LongWallButton[][] horzWalls;
	public StatButton[] statAry;
	private JPanel buttonHolder;
	private JPanel BHBorder;
	private JPanel rightBarPanel;
	private JPanel topBarPanel;
	public Terminal bottomBarPanel;
	private JPanel leftBarPanel;
	private JPanel holder1;
	private JPanel holder2;
	private JPanel holder3;
	private boolean closeFromX;
	private Game g;
	/* Constructor */

	/**
	 * Constructs this Panel with panel dimensions and what layout to 
	 * use to add JObjects. 
	 * 
	 */
	public GameBoard(Game g){
		super();
		this.g=g;
		closeFromX = true;
		FlowLayout flow = new FlowLayout();
		flow.setHgap( 0 );
		flow.setVgap( 0 );

		buttonHolder = new JPanel();
		BHBorder = new JPanel();
		rightBarPanel = new JPanel();
		topBarPanel = new JPanel();
		bottomBarPanel = new Terminal( getBottomBarDim().height/15-2, getBottomBarDim().width/11-2, this );
		leftBarPanel = new JPanel();
		holder1 = new JPanel();
		holder2 = new JPanel();
		holder3 = new JPanel();


		buttonHolder.setLayout( flow );
		BHBorder.setLayout( flow );
		rightBarPanel.setLayout( flow );
		topBarPanel.setLayout( flow );
		bottomBarPanel.setLayout( flow );
		leftBarPanel.setLayout( flow );
		holder1.setLayout( flow );
		holder2.setLayout( flow );
		holder3.setLayout( flow );
		this.setLayout( flow );

		buttonHolder.setPreferredSize( getButtonHolderDim() );
		BHBorder.setPreferredSize( getButtonHolderBorderDim() );
		rightBarPanel.setPreferredSize( getRightBarDim() );
		topBarPanel.setPreferredSize( getTopBarDim() );
		bottomBarPanel.setPreferredSize( getBottomBarDim() );
		leftBarPanel.setPreferredSize( getLeftBarDim() );
		holder1.setPreferredSize( getHolder1Dim() );
		holder2.setPreferredSize( getHolder2Dim() );
		holder3.setPreferredSize( getHolder3Dim() );
		this.setPreferredSize( getThisDim() );


		setFrameStats();
	}
	
	public void closeFrame(){
		closeFromX = false;
		frame.dispose();
	}
	/* Get Methods */

	/**
	 * Gets GameBoard's frame.
	 * 
	 * @return JFrame
	 */
	public JFrame getFrame(){
		return this.frame;
	}

	/**
	 * Gets possible moves from back end.
	 * 
	 * @param p Player
	 * @param b Board
	 * @return ArrayList of PlayerButtons to highlight
	 */
	public ArrayList<PlayerButton> possibleMoves(Player p, Board b){
		ArrayList<String> positions = p.getAvailableMoves();
		ArrayList<PlayerButton> buttons = new ArrayList<PlayerButton>();

		for(String pos : positions){
			int x = Integer.parseInt(pos.substring(0, 1));
			int y = Integer.parseInt(pos.substring(1, 2));
			buttons.add(pbAry[x][y]);
		}

		return buttons;
	}

	/* Set Methods */

	/**
	 * Updates the UI by clearing previous Players and movable spaces
	 * and then adding current Players, Walls, and movable spaces.
	 * 
	 * @param g Observable g object
	 * @param board Object Board object
	 */
	public void update(Observable game, Object board) {  
		// convert from observable and object
		Board b = (Board) board;
		this.g = (Game) game;

		// clear board
		this.clearPlayers();
		this.resetMoveableSpaces();

		// show current pieces
		this.addPlayerButtons(b);
		this.addWallButtons(b);
		this.showPlyrMoves(g.getCurrPlayer(),b);
		repaint();

		if(g.gameWon())
			this.winState(g);
	}  

	/**
	 * Handles display in the event a Player wins. 
	 * Dialog box appears with options for new games or quit.
	 * 
	 * @param g g object
	 */
	public void winState(final Game g){
		// display a win dialog
		JPanel jp1 = new JPanel();
		jp1.setLayout( new FlowLayout() ) ;
		jp1.setPreferredSize( new Dimension( 300, 75 ) );

		// set win dialog background color to player color, light
		int increment = g.getColorIncrement();
		Color c = g.getCurrPlayer().getColor();
		int red = (c.getRed() + increment > 256 ? c.getRed() : c.getRed() + increment );
		int green = (c.getGreen() + increment > 256 ? c.getGreen() : c.getGreen() + increment );
		int blue = (c.getBlue() + increment > 256 ? c.getBlue() : c.getBlue() + increment );

		jp1.setBackground(new Color( red, green, blue ));

		final JFrame WinMessageFrame = new JFrame( "Player " + g.getCurrPlayer().getPnum() + " wins!" );
		WinMessageFrame.setLayout( new BorderLayout() );

		// display buttons in dialog
		ButtonGroup btngrp = new ButtonGroup();
		final JRadioButton b1 = new JRadioButton("New 2 player g",false);
		final JRadioButton b2 = new JRadioButton("New 4 player g",false);
		final JRadioButton b3 = new JRadioButton("Quit",false);

		// add action listeners to dialog buttons
		b1.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ){
				WinMessageFrame.dispose();
				g.newGame( 2 );
			}
		});
		b2.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ){
				WinMessageFrame.dispose();
				g.newGame( 4 );
			}
		});
		b3.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ){
				System.exit(0);
			}
		});
		btngrp.add(b1);
		btngrp.add(b2);
		btngrp.add(b3);
		jp1.add(b3, FlowLayout.LEFT);
		jp1.add(b2, FlowLayout.LEFT);
		jp1.add(b1, FlowLayout.LEFT);
		WinMessageFrame.add( jp1, BorderLayout.NORTH );
		WinMessageFrame.setLocation( 350, 300 );
		WinMessageFrame.setSize( 300, 300 );
		WinMessageFrame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		WinMessageFrame.setVisible( true );
		WinMessageFrame.pack();
	}

	/**
	 * The JFrame is what holds JPanel, which is what class type this file is,
	 * so we need to set it up internally so that it can hold a GameBoard object
	 * and support it
	 */
	public void setFrameStats(){
		frame = new JFrame( "Quoridor" ); 		//Title of JFrame window is "Quoridor"
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		frame.setLayout( gridbag );
		addOtherJObjectsToGameBoard();
		addPanelsToJFrame( frame, gridbag, c );
		frame.add( this );						//Adds this JPanel to JFrame
		frame.setVisible( true );				//Sets Frame to visible
		frame.setResizable( false );			//Doesn't allow resizing of frame
		frame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );	//when frame is closed, the program terminates
		frame.setLocation( 150, 50 );
		frame.pack();	//collapses frame to minimum size around all JObjects inside it
		frame.addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent windowEvent) {
		    	if(closeFromX)
		    		System.exit( 0 );
		    	frame.dispose();
		    }
		});
	}

	/**
	 * Sets up the GridBag Layout with JFrame and JPanel
	 * 
	 * @param frame JFrame
	 * @param gridbag GridBagLayout
	 * @param c GridBagConstraints
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
		setUpThisPanel();
		gridbag.setConstraints( this, c );
		frame.add( this );
	}

	private Dimension getThisDim(){
		return (new Dimension( getLeftBarDim().width + getButtonHolderBorderDim().width + getRightBarDim().width, getTopBarDim().height + getButtonHolderBorderDim().height + getBottomBarDim().height ) );
	}

	private Dimension getLeftBarDim(){
		return (new Dimension( g.getHWall().width, getButtonHolderBorderDim().height+ getTopBarDim().height ) );
	}

	private Dimension getButtonHolderBorderDim(){
		return (new Dimension(getButtonHolderDim().width + g.getIntersection().width*2, getButtonHolderDim().height + g.getIntersection().height*2) );
	}

	public Dimension getButtonHolderDim(){
		return (new Dimension( (g.getHWall().width*9 + g.getVWall().width*8), (g.getVWall().height*9 + g.getHWall().height*8) ));
	}

	private Dimension getHolder1Dim(){
		return (new Dimension( getButtonHolderBorderDim().width + getRightBarDim().width, getButtonHolderBorderDim().height ) );
	}

	private Dimension getHolder2Dim(){
		return (new Dimension( getTopBarDim().width, getTopBarDim().height + getRightBarDim().height ) );
	}

	private Dimension getHolder3Dim(){
		return (new Dimension( getRightBarDim().width + getButtonHolderBorderDim().width + getLeftBarDim().width, getTopBarDim().height + getButtonHolderBorderDim().height ) );
	}

	private Dimension getTopBarDim(){
		return (new Dimension( getButtonHolderBorderDim().width + getRightBarDim().width, g.getVWall().height));
	}

	public Dimension getBottomBarDim(){
		int width = getHolder3Dim().width;
		int height = g.getPlayerHeight()*3;

		return (height < 50 ? new Dimension( width, 50 ) : new Dimension( width, height ) );
	}

	public Dimension getRightBarDim(){
		int width = getButtonHolderBorderDim().width/3+10;
		int height = getButtonHolderBorderDim().height;
		return (width<180 ? new Dimension( 180, height ) : new Dimension( width , height ));
	}

	/**
	 * Adds JButtons to the GameBoard.
	 */
	public void addOtherJObjectsToGameBoard(){
		addJButtons();
		this.repaint();
	}

	/**
	 * Adds PlayerButtons, WallButtons, and LongWallButtons to the GameBoard.
	 */
	public void addJButtons(){
		pbAry = new PlayerButton[9][9];
		wbAry = new WallButton[8][8];
		vertWalls = new LongWallButton[9][8];
		horzWalls = new LongWallButton[8][9];

		// 9 rows
		for( int x = 0; x<9; x++ ){
			// two layers?
			for( int layer=0; layer<2; layer++ ){
				// 9 cols
				for( int y=0; y<9; y++ ){
					//layer 0 is PlayerButton && Vert Walls
					//Layer 1 is HorzWalls && WallButton
					if( layer == 0 ){
						PlayerButton plyrBtn = new PlayerButton( x, y, g );
						buttonHolder.add( plyrBtn );
						pbAry[x][y] = plyrBtn;
						if( y!= 8 ){
							LongWallButton w = new LongWallButton( x, y, "v", g, this);
							buttonHolder.add( w );
							vertWalls[x][y] = w;
						}
					} else if(layer == 1 && x != 8) {
						LongWallButton w = new LongWallButton( x, y, "h", g, this );
						buttonHolder.add( w );
						horzWalls[x][y] = w;
						if( y!= 8 && x!=8 ){
							WallButton wb = new WallButton( x, y, g );
							buttonHolder.add( wb );
							wbAry[x][y] = wb;
						}
					}
				}//end y
			}//end layer
		}//end x	
	}

	private void setUpThisPanel(){
		EndZoneButton up = new EndZoneButton( g.getNumPlayers(), "U", g, this );
		EndZoneButton down = new EndZoneButton( g.getNumPlayers(), "D", g, this );
		EndZoneButton left = new EndZoneButton( g.getNumPlayers(), "L", g, this );
		EndZoneButton right = new EndZoneButton( g.getNumPlayers(), "R", g, this );
		BHBorder.add( new SpacerButton( g.getIntersection(), Color.DARK_GRAY ) );
		BHBorder.add( up );
		BHBorder.add( new SpacerButton( g.getIntersection(), Color.DARK_GRAY ) );
		BHBorder.add( left );
		BHBorder.add( buttonHolder );
		BHBorder.add( right );
		BHBorder.add( new SpacerButton( g.getIntersection(), Color.DARK_GRAY ) );
		BHBorder.add( down );
		BHBorder.add( new SpacerButton( g.getIntersection(), Color.DARK_GRAY ) );
		holder1.add( BHBorder );
		holder1.add( rightBarPanel );
		if(g.getNumPlayers()==2){
			this.statAry = new StatButton[2];
			for(int i=0; i<2; i++){
				StatButton b = new StatButton(i, g, this);
				rightBarPanel.add( b );
				statAry[i] = b;
			}
		}else{
			this.statAry = new StatButton[4];
			for(int i=0; i<4; i++){
				StatButton b = new StatButton(i, g, this);
				rightBarPanel.add( b );
				statAry[i] = b;
			}
		}	

		topBarPanel.add( new SpacerButton( g.getVWall().width, getTopBarDim().height, Color.DARK_GRAY ) );
		topBarPanel.add( new SpacerButton( g.getHWall().width, getTopBarDim().height, Color.LIGHT_GRAY, "" + (char)('A') ) );
		for(int i=0; i<8; i++){
			topBarPanel.add( new SpacerButton( g.getVWall().width, getTopBarDim().height, Color.DARK_GRAY , "" + (char)('A'+i) ) );
			topBarPanel.add( new SpacerButton( g.getHWall().width, getTopBarDim().height, Color.LIGHT_GRAY, "" + (char)('B'+i) ) );
		}
		topBarPanel.add( new SpacerButton( g.getVWall().width, getTopBarDim().height, Color.DARK_GRAY ) );
		topBarPanel.add( new SpacerButton( getRightBarDim().width, getTopBarDim().height, Color.LIGHT_GRAY, "STATS" ) );

		holder2.add( topBarPanel );
		holder2.add( holder1 );

		leftBarPanel.add( new SpacerButton( getLeftBarDim().width, getTopBarDim().height, Color.LIGHT_GRAY) );
		leftBarPanel.add( new SpacerButton( getLeftBarDim().width, g.getHWall().height, Color.DARK_GRAY ) );
		leftBarPanel.add( new SpacerButton( getLeftBarDim().width, g.getVWall().height, Color.LIGHT_GRAY, "" + (char)('1')) );
		for(int i=0; i<8; i++){
			leftBarPanel.add( new SpacerButton( getLeftBarDim().width,g.getHWall().height, Color.DARK_GRAY , "" + (char)('1'+i)) );
			leftBarPanel.add( new SpacerButton( getLeftBarDim().width, g.getVWall().height, Color.LIGHT_GRAY, "" + (char)('2'+i)) );
		}
		leftBarPanel.add( new SpacerButton( getLeftBarDim().width,g.getHWall().height, Color.DARK_GRAY ) );

		holder3.add(leftBarPanel);
		holder3.add(holder2);

		this.add(holder3);
		this.add(bottomBarPanel);
	}

	/**
	 * Adds Players to the GameBoard according to back end Board.
	 * @param b
	 */
	public void addPlayerButtons(Board b){
		ArrayList<Player> players = b.players();
		for(Player p : players)
			pbAry[p.x()][p.y()].addPlayer(p);	
	}

	/**
	 * Adds Walls to the GameBoard according to back end Board.
	 * @param b
	 */
	public void addWallButtons(Board b){
		Wall[] walls = b.walls();
		for(int i=0; i<b.numWalls(); i++){
			Wall w = walls[i];
			if(w.type().equals("v"))
				this.placeVertWall(w.getX(), w.getY());
			if(w.type().equals("h"))
				this.placeHorzWall(w.getX(), w.getY());
		}	
	}

	/**
	 * Adds horizontal walls to the GameBoard according to back end Board.
	 * 
	 * @param x integer x coordinate
	 * @param y integer y coordinate
	 */
	public void placeHorzWall(int x, int y){
		LongWallButton right = horzWalls[x][y];
		LongWallButton left = horzWalls[x][y+1];

		Color yesWallColor = new Color(154,97,41);

		right.setBackground( yesWallColor );
		left.setBackground( yesWallColor );
		wbAry[x][y].setBackground( yesWallColor );

	}

	/**
	 * Adds vertical walls to the GameBoard according to back end Board.
	 * 
	 * @param x integer x coordinate
	 * @param y integer y coordinate
	 */
	public void placeVertWall(int x, int y){
		LongWallButton up = vertWalls[x][y];
		LongWallButton down = vertWalls[x+1][y];

		Color yesWallColor = new Color(154,97,41);

		up.setBackground( yesWallColor );
		down.setBackground( yesWallColor );		
		wbAry[x][y].setBackground( yesWallColor );
	}

	/**
	 * Removes all Players from the GameBoard. 
	 */
	public void clearPlayers(){
		for(int y=0; y<9; y++)
			for(int x=0; x<9; x++)
				if(pbAry[x][y].hasPlayer())
					pbAry[x][y].removePlayer();
	}

	/**
	 * Highlights all possible squares a Player can move.
	 * 
	 * @param p Player
	 * @param b Board
	 */
	public void showPlyrMoves(Player p, Board b){
		// get available spaces
		ArrayList<PlayerButton> btnsToChange = this.possibleMoves(p, b);
		// set available spaces to pink
		for(PlayerButton btn : btnsToChange )
			btn.setBackground( Color.MAGENTA );
	}	

	/**
	 * Set all highlighted buttons back to black.
	 */
	public void resetMoveableSpaces(){
		for( int x=0; x<9; x++ )
			for( int y=0; y<9; y++ )
				if(pbAry[x][y].getBackground() == Color.MAGENTA)
					pbAry[x][y].setBackground(Color.BLACK);
	}
	/**
	 * A MenuBar holds a Menu which in turn hold a MenuItem. That MenuItem 
	 * is what has the changeable consequence if clicked. The Menu just drops 
	 * down the Items in the menu.
	 * 
	 * @return JMenuBar
	 */	
	public JMenuBar getMenus(){
		JMenuBar menuBar = new JMenuBar();	
		menuBar.setBorder( null );
		menuBar.setForeground( Color.WHITE );
		menuBar.setBackground( Color.BLACK );

		JMenu fileMenu = new JMenu( "File" );
		fileMenu.setBorder( null );
		fileMenu.setForeground( Color.WHITE );
		fileMenu.setBackground( Color.BLACK );
			JMenuItem new2PlyrGameOpt = new JMenuItem( "New 2 Player g" );
			new2PlyrGameOpt.setForeground( Color.WHITE );
			new2PlyrGameOpt.setBackground( Color.BLACK );
			new2PlyrGameOpt.addActionListener( new ActionListener(){
				public void actionPerformed( ActionEvent e ){
					g.newGame( 2 );
				}
			});
			fileMenu.add(new2PlyrGameOpt);
			JMenuItem new4PlyrGameOpt = new JMenuItem( "New 4 Player g" );
			new4PlyrGameOpt.setBorder( null );
			new4PlyrGameOpt.setForeground( Color.WHITE );
			new4PlyrGameOpt.setBackground( Color.BLACK );
			new4PlyrGameOpt.addActionListener( new ActionListener(){
				public void actionPerformed( ActionEvent e ){
					g.newGame( 4 );
				}
			});
			fileMenu.add(new4PlyrGameOpt);
			JMenuItem quitOpt = new JMenuItem( "Quit" );
			quitOpt.setBorder( null );
			quitOpt.setForeground( Color.WHITE );
			quitOpt.setBackground( Color.BLACK );
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
		helpMenu.setForeground( Color.WHITE );
		helpMenu.setBackground( Color.BLACK );
		JMenuItem rulesOpt = new JMenuItem("Rules");
		rulesOpt.setBorder( null );
		rulesOpt.setForeground( Color.WHITE );
		rulesOpt.setBackground( Color.BLACK );
		rulesOpt.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String LongWallButton = "";
				LongWallButton += "Quoridor is played on a g board of 81 square spaces (9x9). \n" +
						"Each player is represented by a pawn which begins at the center \n" +
						"space of one edge of the board (in a two-player g, the pawns \n" +
						"begin opposite each other). The object is to be the first player \n" +
						"to move their pawn to any space on the opposite side of the \n" +
						"gameboard from which it begins.\n\n";
				LongWallButton += "The distinguishing characteristic of Quoridor is its twenty walls. \n" +
						"Walls are flat two-space-wide pieces which can be placed in the \n" +
						"groove that runs between the spaces. Walls block the path of all \n" +
						"pawns, which must go around them. The walls are divided equally \n" +
						"among the players at the start of the game, and once placed, cannot \n" +
						"be moved or removed. On a turn, a player may either move their \n" +
						"pawn, or, if possible, place a wall. \n\n";
				LongWallButton += "Pawns can be moved to an adjacent space (not diagonally), or, if \n" +
						"adjacent to another pawn, jump over that pawn. If an adjacent pawn \n" +
						"has a third pawn or a wall on the other side of it, the player may \n" +
						"move to any space that is immediately adjacent to other adjacent pawns.\n" +
						"The official rules are ambiguous concerning the edge of the board. \n\n";
				LongWallButton += "Walls can be placed directly between two spaces, in any groove not \n" +
						"already occupied by a wall. However, a wall may not be placed which \n" +
						"cuts off the only remaining path of any pawn to the side of the board \n" +
						"it must reach.\n";
				JOptionPane.showMessageDialog( frame , LongWallButton, "Rules of Quoridor",JOptionPane.PLAIN_MESSAGE);
			}
		});
		helpMenu.add( rulesOpt );
		//the About tab in the Help menu
		JMenuItem aboutOpt = new JMenuItem( "About" );
		aboutOpt.setBorder( null );
		aboutOpt.setForeground( Color.WHITE );
		aboutOpt.setBackground( Color.BLACK );
		aboutOpt.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String LongWallButton = "";
				LongWallButton += "Authors: \n";
				LongWallButton += "Dylan Woythal \n";
				LongWallButton += "Eli Donahue \n";
				LongWallButton += "Marc Dean \n";
				LongWallButton += "Tyler Smith ";
				JOptionPane.showMessageDialog( frame , LongWallButton, "About",JOptionPane.PLAIN_MESSAGE);
			}
		});
		helpMenu.add( aboutOpt );

		menuBar.add( fileMenu );
		menuBar.add( helpMenu );
		return menuBar;
	}
}

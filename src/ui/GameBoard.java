package ui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

// use to implement Subject/Observer design pattern
import java.util.ArrayList;
import java.util.Observable;  
import java.util.Observer;  

import main.Game;
import board.Board;
import walls.Wall;
import players.Player;

/**
 * The GameBoard serves as the UI for the Quoridor game. It updates from the 
 * back end via a Subject/Observer design pattern. The Game object is the 
 * subject and the GameBoard object is the observer.
 * 
 * @author Dylan Woythol, Tyler Smith, Eli Donahue
 *
 */
@SuppressWarnings("serial")
public class GameBoard extends JPanel implements Observer {

	/* Instance Variables */
	
	private static JFrame frame;
	private static PlayerButton[][] pbAry;
	private static WallButton[][] wbAry;
	private static LongWallButton[][] vertWalls;
	private static LongWallButton[][] horzWalls;
	private static JPanel buttonHolder;
	private static JPanel BHBorder;
	private static JPanel statsPanel;
	private static JPanel underBarPanel;
	private static JPanel holder;
	/* Constructor */

	/**
	 * Constructs this Panel with panel dimensions and what layout to 
	 * use to add JObjects. 
	 * 
	 */
	public GameBoard(){
		super();
		
		FlowLayout flow = new FlowLayout();
		flow.setHgap( 0 );
		flow.setVgap( 0 );

		buttonHolder = new JPanel();
		BHBorder = new JPanel();
		statsPanel = new JPanel();
		underBarPanel = new JPanel();
		holder = new JPanel();


		buttonHolder.setLayout( flow );
		BHBorder.setLayout( flow );
		statsPanel.setLayout( flow );
		underBarPanel.setLayout( flow );
		holder.setLayout( flow );
		this.setLayout( flow );
				
		buttonHolder.setPreferredSize( getButtonHolderDim() );
		BHBorder.setPreferredSize( getButtonHolderBorderDim() );
		statsPanel.setPreferredSize( getStatsBarDim() );
		underBarPanel.setPreferredSize( getUnderBarDim() );
		holder.setPreferredSize( getHolderDim() );
		this.setPreferredSize( getThisDim() );
		

		setFrameStats();
	}
	
	/* Get Methods */
	
	/**
	 * Gets GameBoard's frame.
	 * 
	 * @return JFrame
	 */
	public JFrame getFrame(){
		return GameBoard.frame;
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
		JMenu fileMenu = new JMenu( "File" );
			JMenuItem new2PlyrGameOpt = new JMenuItem( "New 2 Player Game" );
			new2PlyrGameOpt.addActionListener( new ActionListener(){
				public void actionPerformed( ActionEvent e ){
					Game.new2PlayerGame();
				}
			});
			fileMenu.add(new2PlyrGameOpt);
			JMenuItem new4PlyrGameOpt = new JMenuItem( "New 4 Player Game" );
			new4PlyrGameOpt.addActionListener( new ActionListener(){
				public void actionPerformed( ActionEvent e ){
					Game.new4PlayerGame();
				}
			});
			fileMenu.add(new4PlyrGameOpt);
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
			JMenuItem rulesOpt = new JMenuItem("Rules");
			rulesOpt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					String LongWallButton = "";
					LongWallButton += "Quoridor is played on a game board of 81 square spaces (9x9). \n" +
							"Each player is represented by a pawn which begins at the center \n" +
							"space of one edge of the board (in a two-player game, the pawns \n" +
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
		JMenu statsMenu = new JMenu("Stats");
			JMenuItem wallsOpt = new JMenuItem("Walls");
			wallsOpt.addActionListener( new ActionListener(){
				public void actionPerformed(ActionEvent e){
					String LongWallButton = "";
					JOptionPane.showMessageDialog( frame , LongWallButton, "About",JOptionPane.PLAIN_MESSAGE);
				}
			});
			statsMenu.add( wallsOpt );
		menuBar.add( fileMenu );
		menuBar.add( helpMenu );
		menuBar.add( statsMenu );
		return menuBar;
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
	 * @param game Observable Game object
	 * @param board Object Board object
	 */
	public void update(Observable game, Object board) {  
		// convert from observable and object
		Board b = (Board) board;
		Game g = (Game) game;
		
		if(g.gameWon()){
			this.winState(g);
		}else{
			// clear board
			this.clearPlayers();
			this.resetMoveableSpaces();
			
			// show current pieces
			this.addPlayerButtons(b);
			this.addWallButtons(b);
			this.showPlyrMoves(g.currPlayer(),b);
	        
			repaint();
		}
    }  
	
	/**
	 * Handles display in the event a Player wins. 
	 * Dialog box appears with options for new games or quit.
	 * 
	 * @param g Game object
	 */
	public void winState(final Game g){
		// display a win dialog
		JPanel jp1 = new JPanel();
		jp1.setLayout( new FlowLayout() ) ;
		jp1.setPreferredSize( new Dimension( 300, 75 ) );
		final JFrame LongWallButtonFrame = new JFrame( "Player " + g.currPlayer().getPnum() + " wins!" );
		LongWallButtonFrame.setLayout( new BorderLayout() );
		
		// display buttons in dialog
		ButtonGroup btngrp = new ButtonGroup();
		final JRadioButton b1 = new JRadioButton("New 2 player game",false);
		final JRadioButton b2 = new JRadioButton("New 4 player game",false);
		final JRadioButton b3 = new JRadioButton("Quit",false);
		
		// add action listeners to dialog buttons
		b1.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ){
				Game.new2PlayerGame();
				LongWallButtonFrame.dispose();
			}
		});
		b2.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ){
				Game.new4PlayerGame();
				LongWallButtonFrame.dispose();
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
		LongWallButtonFrame.add( jp1, BorderLayout.NORTH );
		LongWallButtonFrame.setLocation( 350, 300 );
		LongWallButtonFrame.setSize( 300, 300 );
		LongWallButtonFrame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		LongWallButtonFrame.setVisible( true );
		LongWallButtonFrame.pack();
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
//		frame.setResizable( false );			//Doesn't allow resizing of frame
		frame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );	//when frame is closed, the program terminates
		frame.setLocation( 150, 50 );
		frame.pack();	//collapses frame to minimum size around all JObjects inside it
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
		return (new Dimension( getUnderBarDim().width, getButtonHolderBorderDim().height+ getUnderBarDim().height ) );
	}
	
	public static Dimension getButtonHolderBorderDim(){
		return (new Dimension(getButtonHolderDim().width + 20, getButtonHolderDim().height + 20) );
	}
	
	public static Dimension getButtonHolderDim(){
		return (new Dimension( (50*9 + 10*8), (50*9 + 10*8) ));
	}
	
	private Dimension getHolderDim(){
		return (new Dimension( getButtonHolderBorderDim().width + getStatsBarDim().width, getButtonHolderBorderDim().height ) );
	}
	
	public static Dimension getUnderBarDim(){
		return (new Dimension( getButtonHolderBorderDim().width + getStatsBarDim().width, 50));
	}
	public static Dimension getStatsBarDim(){
		return (new Dimension( getButtonHolderBorderDim().width/3+10 , getButtonHolderBorderDim().height ));
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
						PlayerButton plyrBtn = new PlayerButton( x, y );
						buttonHolder.add( plyrBtn );
						pbAry[x][y] = plyrBtn;
						if( y!= 8 ){
							LongWallButton w = new LongWallButton( x, y, "v");
							buttonHolder.add( w );
							vertWalls[x][y] = w;
						}
					} else if(layer == 1 && x != 8) {
						LongWallButton w = new LongWallButton( x, y, "h" );
						buttonHolder.add( w );
						horzWalls[x][y] = w;
						if( y!= 8 && x!=8 ){
							WallButton wb = new WallButton( x, y );
							buttonHolder.add( wb );
							wbAry[x][y] = wb;
						}
					}
				}//end y
			}//end layer
		}//end x
		
	}
	
	private void setUpThisPanel(){
	
		
		EndZoneButton up = new EndZoneButton( Game.getNumPlayers(), "U" );
		EndZoneButton down = new EndZoneButton( Game.getNumPlayers(), "D" );
		EndZoneButton left = new EndZoneButton( Game.getNumPlayers(), "L" );
		EndZoneButton right = new EndZoneButton( Game.getNumPlayers(), "R" );

		BHBorder.add( new EndZoneSpacer() );
		BHBorder.add( up );
		BHBorder.add( new EndZoneSpacer() );
		BHBorder.add( left );
		BHBorder.add( buttonHolder );
		BHBorder.add( right );
		BHBorder.add( new EndZoneSpacer() );
		BHBorder.add( down );
		BHBorder.add( new EndZoneSpacer() );
		
		holder.add( BHBorder );
		holder.add( statsPanel );
		for(int i=0; i<4; i++)
		statsPanel.add( new PlayerStatButton() );
		
		this.add( holder, FlowLayout.LEFT );
		this.add( underBarPanel );
		
	}
	
	/**
	 * Adds Players to the GameBoard according to back end Board.
	 * @param b
	 */
	public void addPlayerButtons(Board b){
		Player[] players = b.players();
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
}

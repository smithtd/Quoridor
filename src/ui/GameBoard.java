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

	/* Static Variables */
	
	public static int WallGap = 10;
	public static int PlayerWidth = 40;
	public static int PlayerHeight = 40;
	private static int colorIncrement = 200;

	public static Dimension HWall = new Dimension( PlayerWidth, WallGap );
	public static Dimension VWall = new Dimension( WallGap, PlayerHeight );
	public static Dimension Intersection = new Dimension( VWall.width, HWall.height );
	public static Dimension PlayerSize = new Dimension( HWall.width, VWall.height );
	
	/* Instance Variables */
	
	private static JFrame frame;
	private static PlayerButton[][] pbAry;
	private static WallButton[][] wbAry;
	private static LongWallButton[][] vertWalls;
	private static LongWallButton[][] horzWalls;
	public static StatButton[] statAry;
	private static JPanel buttonHolder;
	private static JPanel BHBorder;
	private static JPanel rightBarPanel;
	private static JPanel topBarPanel;
	private static JPanel bottomBarPanel;
	private static JPanel leftBarPanel;
	private static JPanel holder1;
	private static JPanel holder2;
	private static JPanel holder3;
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
		rightBarPanel = new JPanel();
		topBarPanel = new JPanel();
		bottomBarPanel = new JPanel();
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
		
		// clear board
		this.clearPlayers();
		this.resetMoveableSpaces();
		
		// show current pieces
		this.addPlayerButtons(b);
		this.addWallButtons(b);
		this.showPlyrMoves(Game.getCurrPlayer(),b);
		
		repaint();
		
		// update stats
		this.updateStats(Game.getPlayerAry());
		
		if(g.gameWon())
			this.winState(g);
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
		
		// set win dialog background color to player color, light
		int increment = 150;
		Color c = Game.getCurrPlayer().getColor();
		int red = (c.getRed() + increment > 256 ? c.getRed() : c.getRed() + increment );
		int green = (c.getGreen() + increment > 256 ? c.getGreen() : c.getGreen() + increment );
		int blue = (c.getBlue() + increment > 256 ? c.getBlue() : c.getBlue() + increment );
				
		jp1.setBackground(new Color( red, green, blue ));
		
		final JFrame LongWallButtonFrame = new JFrame( "Player " + Game.getCurrPlayer().getPnum() + " wins!" );
		LongWallButtonFrame.setLayout( new BorderLayout() );
		
		// display buttons in dialog
		ButtonGroup btngrp = new ButtonGroup();
		final JRadioButton b3 = new JRadioButton("Quit",false);
		
		// add action listeners to dialog buttons
		b3.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ){
				System.exit(0);
			}
		});
	    btngrp.add(b3);
	    jp1.add(b3, FlowLayout.LEFT);
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
		frame.setResizable( false );			//Doesn't allow resizing of frame
		frame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );	//when frame is closed, the program terminates
		frame.setLocation( 150, 50 );
		frame.pack();	//collapses frame to minimum size around all JObjects inside it
		frame.addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent windowEvent) {
		    	System.exit( 0 );
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
		return (new Dimension( HWall.width, getButtonHolderBorderDim().height+ getTopBarDim().height ) );
	}
	
	private static Dimension getButtonHolderBorderDim(){
		return (new Dimension(getButtonHolderDim().width + Intersection.width*2, getButtonHolderDim().height + Intersection.height*2) );
	}
	
	public static Dimension getButtonHolderDim(){
		return (new Dimension( (HWall.width*9 + VWall.width*8), (VWall.height*9 + HWall.height*8) ));
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
		return (new Dimension( getButtonHolderBorderDim().width + getRightBarDim().width, VWall.height));
	}
	
	private Dimension getBottomBarDim(){
		int width = getHolder3Dim().width;
		int height = PlayerHeight*3;
		
		return (height < 50 ? new Dimension( width, 50 ) : new Dimension( width, height ) );
	}
	
	public static Dimension getRightBarDim(){
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
	
	public void updateStats(ArrayList<Player> players){
		System.out.println("Updating stats");
		for(int i=0; i<statAry.length; i++){
			statAry[i].update(players.get(i));
			statAry[i].repaint();
		}
	}
	
	private void setUpThisPanel(){
		EndZoneButton up = new EndZoneButton( Game.getNumPlayers(), "U", this );
		EndZoneButton down = new EndZoneButton( Game.getNumPlayers(), "D", this );
		EndZoneButton left = new EndZoneButton( Game.getNumPlayers(), "L", this );
		EndZoneButton right = new EndZoneButton( Game.getNumPlayers(), "R", this );
		BHBorder.add( new SpacerButton( getIntersection(), Color.DARK_GRAY ) );
		BHBorder.add( up );
		BHBorder.add( new SpacerButton( getIntersection(), Color.DARK_GRAY ) );
		BHBorder.add( left );
		BHBorder.add( buttonHolder );
		BHBorder.add( right );
		BHBorder.add( new SpacerButton( getIntersection(), Color.DARK_GRAY ) );
		BHBorder.add( down );
		BHBorder.add( new SpacerButton( getIntersection(), Color.DARK_GRAY ) );
		holder1.add( BHBorder );
		holder1.add( rightBarPanel );

		
		// initialize stat buttons
		holder1.add( rightBarPanel );
		statAry = new StatButton[Game.getNumPlayers()];
		for(int i=0; i<Game.getNumPlayers(); i++){
			StatButton b = new StatButton(Game.getPlayerAry().get(i));
			rightBarPanel.add( b );
			statAry[i] = b;
		}
		
		topBarPanel.add( new SpacerButton( getVWall().width, getTopBarDim().height, Color.DARK_GRAY ) );
		topBarPanel.add( new SpacerButton( getHWall().width, getTopBarDim().height, Color.LIGHT_GRAY, "" + (char)('A') ) );
		for(int i=0; i<8; i++){
			topBarPanel.add( new SpacerButton( getVWall().width, getTopBarDim().height, Color.DARK_GRAY , "" + (char)('A'+i) ) );
			topBarPanel.add( new SpacerButton( getHWall().width, getTopBarDim().height, Color.LIGHT_GRAY, "" + (char)('B'+i) ) );
		}
		topBarPanel.add( new SpacerButton( getVWall().width, getTopBarDim().height, Color.DARK_GRAY ) );
		topBarPanel.add( new SpacerButton( getRightBarDim().width, getTopBarDim().height, Color.LIGHT_GRAY, "STATS" ) );

		holder2.add( topBarPanel );
		holder2.add( holder1 );

		leftBarPanel.add( new SpacerButton( getLeftBarDim().width, getTopBarDim().height, Color.LIGHT_GRAY) );
		leftBarPanel.add( new SpacerButton( getLeftBarDim().width, getHWall().height, Color.DARK_GRAY ) );
		leftBarPanel.add( new SpacerButton( getLeftBarDim().width, getVWall().height, Color.LIGHT_GRAY, "" + (char)('1')) );
		for(int i=0; i<8; i++){
			leftBarPanel.add( new SpacerButton( getLeftBarDim().width,getHWall().height, Color.DARK_GRAY , "" + (char)('1'+i)) );
			leftBarPanel.add( new SpacerButton( getLeftBarDim().width, getVWall().height, Color.LIGHT_GRAY, "" + (char)('2'+i)) );
		}
		leftBarPanel.add( new SpacerButton( getLeftBarDim().width,getHWall().height, Color.DARK_GRAY ) );

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
			if(!p.hasBeenKicked())
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
		for(PlayerButton btn : btnsToChange ){
			btn.setBackground( Color.MAGENTA );
			btn.setMovable( true );
		}
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
		return menuBar;
	}
	
	public static PlayerButton [][] getPBAry(){
		return pbAry;
	}
	
	public int get(){
		return WallGap;
	}

	public Dimension getHWall() {
		return HWall;
	}

	public Dimension getIntersection() {
		return Intersection;
	}

	public Dimension getVWall() {
		return VWall;
	}

	public int getPlayerHeight() {
		return PlayerHeight;
	}

	public static int getColorIncrement() {
		return colorIncrement;
	}

	public int getPlayerWidth() {
		return PlayerWidth;
	}

	public static Dimension getPlayerSize() {
		return PlayerSize;
	}
}

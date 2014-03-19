package ui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.util.ArrayList;
import java.util.Observable;  
import java.util.Observer;  

import main.Game;
import board.Board;
import walls.Wall;
import players.Player;

@SuppressWarnings("serial")
public class GameBoard extends JPanel implements Observer {

	private static JFrame frame;
	private static PlayerButton[][] pbAry;
	private static WallButton[][] wbAry;
	private static Temp[][] vertWalls;
	private static Temp[][] horzWalls;
	
	/*
	 * Basic details of this Panel like the panel dimensions and what layout to 
	 * use to add JObjects 
	 */
	public GameBoard(){
		super();
		FlowLayout flow = new FlowLayout();
		flow.setHgap( 0 );
		flow.setVgap( 0 );
		this.setLayout( flow );
		setFrameStats();
	}
	
	// all updating will occur in this method
	public void update(Observable game, Object board) {  
		// convert from observable and object
		Board b = (Board) board;
		Game g = (Game) game;
		
		if(g.gameWon()){
			this.winState(g);
		}else{
			// clear players
			this.clearPlayers();
			// set all pink squares to black
			this.resetMoveableSpaces();
			
			// add players and walls
			this.addPlayerButtons(b);
			this.addWallButtons(b);
			
			// show potential moves
			System.out.println("Showing moves for current player: "+g.currPlayer().getColor());
			this.showPlyrMoves(g.currPlayer(),b);
	        repaint();
		}
    }  
	
	public void winState(final Game g){
		// display a win dialog
		JPanel jp1 = new JPanel();
		jp1.setLayout( new FlowLayout() ) ;
		jp1.setPreferredSize( new Dimension( 300, 75 ) );
		final JFrame tempFrame = new JFrame( "Player " + g.currPlayer().getPnum() + " wins!" );
		tempFrame.setLayout( new BorderLayout() );
		
		// display buttons in dialog
		ButtonGroup btngrp = new ButtonGroup();
		final JRadioButton b1 = new JRadioButton("New 2 player game",false);
		final JRadioButton b2 = new JRadioButton("New 4 player game",false);
		final JRadioButton b3 = new JRadioButton("Quit",false);
		b1.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ){
				g.new2PlayerGame();
				tempFrame.dispose();
			}
		});
		b2.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ){
				g.new4PlayerGame();
				tempFrame.dispose();
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
		tempFrame.add( jp1, BorderLayout.NORTH );
		tempFrame.setLocation( 350, 300 );
		tempFrame.setSize( 300, 300 );
		tempFrame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		tempFrame.setVisible( true );
		tempFrame.pack();
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
		addOtherJObjectsToGameBoard();
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
			JMenuItem new2PlyrGameOpt = new JMenuItem( "New 2 Player Game" );
			new2PlyrGameOpt.addActionListener( new ActionListener(){
				public void actionPerformed( ActionEvent e ){
					//g.new2PlayerGame();
				}
			});
			fileMenu.add(new2PlyrGameOpt);
			JMenuItem new4PlyrGameOpt = new JMenuItem( "New 4 Player Game" );
			new4PlyrGameOpt.addActionListener( new ActionListener(){
				public void actionPerformed( ActionEvent e ){
					//g.new4PlayerGame();
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
		JMenu statsMenu = new JMenu("Stats");
			JMenuItem wallsOpt = new JMenuItem("Walls");
			wallsOpt.addActionListener( new ActionListener(){
				public void actionPerformed(ActionEvent e){
					String temp = "";
//					temp += "Blue : " + cont.getWallsRem()[0] + "\n";
//					temp += "Red : " + cont.getWallsRem()[1] + "\n";
//					if(cont.getNumPlayers() == 4 ){
//						temp += "Green : " + cont.getWallsRem()[2] + "\n";
//						temp += "Yellow : " + cont.getWallsRem()[3] + "\n";
//					}
					JOptionPane.showMessageDialog( frame , temp, "About",JOptionPane.PLAIN_MESSAGE);
				}
			});
			statsMenu.add( wallsOpt );
		menuBar.add( fileMenu );
		menuBar.add( helpMenu );
		menuBar.add( statsMenu );
		return menuBar;
	}
	
	public void addOtherJObjectsToGameBoard(){
		addJButtons();
		this.repaint();
	}
	
	public void addJButtons(){
		pbAry = new PlayerButton[9][9];
		wbAry = new WallButton[8][8];
		
		vertWalls = new Temp[9][8];
		horzWalls = new Temp[8][9];
		
		//9x9 board
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
						this.add( plyrBtn );
						pbAry[x][y] = plyrBtn;
						if( y!= 8 ){
							Temp w = new Temp( x, y, "v");
							this.add( w );
							vertWalls[x][y] = w;
						}
					} else if(layer == 1 && x != 8) {
						Temp w = new Temp( x, y, "h" );
						this.add( w );
						horzWalls[x][y] = w;
						if( y!= 8 && x!=8 ){
							WallButton wb = new WallButton( x, y );
							this.add( wb );
							wbAry[x][y] = wb;
						}
					}
				}
			}
		}
	}
	
	public void addPlayerButtons(Board b){
		
		Player[] players = b.players();
		
		// change button colors for players
		for(Player p : players){
			pbAry[p.x()][p.y()].addPlayer(p);
		}
			
	}
	
	public void addWallButtons(Board b){
		
		Wall[] walls = b.walls();
		
		// display walls
		for(int i=0; i<b.numWalls(); i++){
			Wall w = walls[i];
			if(w.type().equals("v"))
				this.placeVertWall(w.getX(), w.getY());
			if(w.type().equals("h"))
				this.placeHorzWall(w.getX(), w.getY());
		}
			
	}
	
	public void placeHorzWall(int x, int y){
		Temp right = horzWalls[x][y];
		Temp left = horzWalls[x][y+1];
		
		Color yesWallColor = new Color(154,97,41);
		
		right.setBackground( yesWallColor );
		left.setBackground( yesWallColor );
		wbAry[x][y].setBackground( yesWallColor );
		
	}
	
	public void placeVertWall(int x, int y){
		Temp up = vertWalls[x][y];
		Temp down = vertWalls[x+1][y];
		
		Color yesWallColor = new Color(154,97,41);
		
		up.setBackground( yesWallColor );
		down.setBackground( yesWallColor );		
		wbAry[x][y].setBackground( yesWallColor );
		
	}
	
	public void clearPlayers(){
		for(int y=0; y<9; y++){
			for(int x=0; x<9; x++){
				if(pbAry[x][y].getBackground()!=Color.BLACK)
					pbAry[x][y].setBackground(Color.BLACK);
					pbAry[x][y].removePlayer();
			}
		}
	}
	
	//Player turn controller
	public void showPlyrMoves(Player p, Board b){
		// get available spaces
		ArrayList<PlayerButton> btnsToChange = this.possibleMoves(p, b);
		// set available spaces to pink
		for(PlayerButton btn : btnsToChange ){
			btn.setBackground( Color.MAGENTA );
		}
	}	
	
	public ArrayList<PlayerButton> possibleMoves(Player p, Board b){
		ArrayList<String> positions = b.possibleMoves(p);
		ArrayList<PlayerButton> buttons = new ArrayList<PlayerButton>();
		
		for(String pos : positions){
			int x = Integer.parseInt(pos.substring(0, 1));
			int y = Integer.parseInt(pos.substring(1, 2));
			buttons.add(pbAry[x][y]);
		}
		
		return buttons;
	}
	
	// set all pink buttons black
	public void resetMoveableSpaces(){
		for( int x=0; x<9; x++ )
			for( int y=0; y<9; y++ )
				if(pbAry[x][y].getBackground() == Color.MAGENTA)
					pbAry[x][y].setBackground(Color.BLACK);
	}
	
}

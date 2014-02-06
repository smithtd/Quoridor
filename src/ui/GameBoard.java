package ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class GameBoard extends JPanel {

	public JFrame frame;
	
	public GameBoard(){
		this.setPreferredSize( new Dimension( 500,500 ) );	//Makes the JPanel 500px * 500px
		this.setLayout( new FlowLayout() );
		setFrameStats();
	}
	
	public void setFrameStats(){
		frame = new JFrame( "Quoridor" ); 		//Title of JFrame window is "Quoridor"
		frame.add( this );						//Adds this JPanel to JFrame
		frame.setVisible( true );				//Sets Frame to visible
		frame.setResizable( false );			//Doesn't allow resizing of frame
		frame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );	//when frame is closed, the program terminates
		try { Thread.sleep(100); } catch (InterruptedException e) {}
		frame.setLocation( 150, 150 );
		addOtherJObjects();		
		try { Thread.sleep(100); } catch (InterruptedException e) {}
		frame.pack();	//collapses frame to minimum size around all JObjects inside it
	}
	
	public void addOtherJObjects(){
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

	}
	
	//OverRidden for JPanel graphics
	public void paint( Graphics g ){
		g.setColor( Color.black );
		g.fillRect( 0, 0, 500, 500 );
	}
}
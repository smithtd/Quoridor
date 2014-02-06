package ui;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GameBoard extends JPanel {

	public JFrame frame;
	
	public GameBoard(){
		frame = new JFrame("Quoridor");
		this.setPreferredSize(new Dimension(500,500));
		frame.add( this );
		frame.setVisible(true);
		frame.setResizable(false);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		// Auto-generated method stub
		new GameBoard();

	}

}

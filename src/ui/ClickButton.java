package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class ClickButton extends JButton {

	ImageIcon Square_red = new ImageIcon("../Images/Square_Red.png");
	
	public ClickButton( String label ){
		super( label );
		setIcon( Square_red );
		addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ) {
				JOptionPane.showMessageDialog(null,  "HELLO", "THIS MESSAGE..", JOptionPane.PLAIN_MESSAGE);
			}
		});
	}
}

package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class ClickButton extends JButton {

	public ImageIcon Square_Red = new ImageIcon("Square_Red.png");
	public int buttonSize;
	public ClickButton( int buttonSize ){
		super();
		this.buttonSize = buttonSize;
	}
	
	public ClickButton( String label, int buttonSize ){
		super( label );
		this.buttonSize = buttonSize;
	}
	
	public void addButtonListener(){
		System.out.println( Square_Red );
		setIcon( new ButtonIcon( buttonSize ).getIcon( "Square_Red" ) );
		addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ) {
				JOptionPane.showMessageDialog(null,  "HELLO", "THIS MESSAGE..", JOptionPane.PLAIN_MESSAGE);
			}
		});
	}
}
///home/student/woythadc194/Classes/405/team-511Tactical/src/Images/Square_Blue.png
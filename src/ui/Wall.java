package ui;

import javax.swing.*;
//using ImageIcon(?), JButton, JOptionPane, JPanel, etc.
import java.awt.*;
import java.awt.event.*;
//using ActionEvent, ActionListener, GridLayout

@SuppressWarnings("serial")
public class Wall extends JButton {
	public int x;
	public int y;
	
	public Wall(int x, int y){
		this.x = x;
		this.y = y;
	}
}

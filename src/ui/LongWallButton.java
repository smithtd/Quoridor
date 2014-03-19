package ui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.*;

@SuppressWarnings("serial")
public class LongWallButton extends JButton {
       public int x;
       public int y;
       
       public LongWallButton(int x, int y, String type){
               super();
               this.setBorder(null);
               this.x = x;
               this.y = y;
               /*
                * if string identifier is a horizontal wall set the dimensions 
                * upon that. Same goes for vertical walls
                */
               if( type.equals( "h" ) )
                       this.setPreferredSize( new Dimension( 50, 10 ) );
               else
                       this.setPreferredSize( new Dimension( 10, 50 ) );
               this.setBackground( Color.gray);
               this.setOpaque(true);
               
       }
}
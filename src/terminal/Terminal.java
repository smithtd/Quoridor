package terminal;
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import javax.swing.*;
import javax.swing.text.DefaultCaret;

import ui.GameBoard;

@SuppressWarnings("serial")
public class Terminal extends JPanel {
	
   private static final int GAP = 3;
   private JTextArea textarea;
   private JTextField textfield;
   public final static InputStream inStream = System.in;
   public final static PrintStream printStream = System.out; 
   public static PrintStream ps;
   public static InputStreamWorker instreamWorker;
   public static int moves;
   
   
   public Terminal(int rows, int cols) {
	  this.setPreferredSize( GameBoard.getBottomBarDim() );
	  
	  moves = 0;
	  try {
		  ps = new PrintStream( new File( "GameMoves.txt" ) ) ;
	  } catch (FileNotFoundException e) {  }

	  textarea = prepareTextArea(rows, cols, inStream);
      textfield = prepareTextField(cols, printStream, textarea);

      setLayout( new BorderLayout( GAP, GAP ) );
      setBorder( BorderFactory.createEmptyBorder( GAP, GAP, GAP, GAP ) );
      JScrollPane JSP = new JScrollPane( textarea );
      DefaultCaret caret = ( DefaultCaret )textarea.getCaret();
      caret.setUpdatePolicy( DefaultCaret.ALWAYS_UPDATE );
      textarea.setLineWrap(true);
      add( JSP, BorderLayout.CENTER );
      add( textfield, BorderLayout.SOUTH );
   }

   public InputStream getInputStream(){
	   return Terminal.inStream;
   }
   private JTextField prepareTextField(int cols, PrintStream printStream, JTextArea textArea) {
      JTextField textField = new JTextField(cols);
      textField.setBackground( Color.BLACK );
      textField.setForeground( Color.WHITE );
      textField.addActionListener(new TextFieldListener(printStream, textArea));
      return textField;
   }

   private JTextArea prepareTextArea(int rows, int cols, InputStream inStream) {
      JTextArea textArea = new JTextArea(rows, cols);
      textArea.setBackground( Color.BLACK );
      textArea.setForeground( Color.WHITE );
      textArea.setEditable(false);
      textArea.setFocusable(false);
      instreamWorker = new InputStreamWorker(textArea, inStream);
      instreamWorker.execute();
      return textArea;
   }

   public static int getMoves() {
	   return Terminal.moves;
   }
}
   

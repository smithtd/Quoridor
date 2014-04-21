package terminal;
import java.awt.BorderLayout;
import java.io.InputStream;
import java.io.PrintStream;
import javax.swing.*;
import ui.GameBoard;

@SuppressWarnings("serial")
public class Terminal extends JPanel {
	
   private static final int GAP = 3;
   public static final String PRE_TEXT = "User> ";
   private JTextArea textarea;
   private JTextField textfield;

   /**
    * 
    * @param rows integer rows
    * @param cols integer columns
    */
   
   public Terminal(int rows, int cols) {
	  this.setPreferredSize( GameBoard.getBottomBarDim() );
      final InputStream inStream = System.in;
      final PrintStream printStream = System.out;
	   
      textarea = prepareTextArea(rows, cols, inStream);
      textfield = prepareTextField(cols, printStream, textarea);

      setLayout(new BorderLayout(GAP, GAP));
      setBorder(BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP));
      add(new JScrollPane(textarea), BorderLayout.CENTER);
      add(textfield, BorderLayout.SOUTH);
   }

   private JTextField prepareTextField(int cols, PrintStream printStream, JTextArea textArea) {
      JTextField textField = new JTextField(cols);
      textField.addActionListener(new TextFieldListener(printStream, textArea));
      return textField;
   }

   private JTextArea prepareTextArea(int rows, int cols, InputStream inStream) {
      JTextArea textArea = new JTextArea(rows, cols);
      textArea.setEditable(false);
      textArea.setFocusable(false);
      InputStreamWorker instreamWorker = new InputStreamWorker(textArea, inStream);
      instreamWorker.execute();
      return textArea;
   }
}
   

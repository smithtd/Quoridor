package ui;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.text.JTextComponent;

@SuppressWarnings("serial")
public class Terminal extends JPanel {
	
   private static final int GAP = 3;
   public static final String PRE_TEXT = "User> ";
   private JTextArea textarea;
   private JTextField textfield;

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

   private class TextFieldListener implements ActionListener {
      private PrintStream printStream;
      private JTextArea textArea;

      public TextFieldListener(PrintStream printStream, JTextArea textArea) {
         this.printStream = printStream;
         this.textArea = textArea;
      }

      @Override
      public void actionPerformed(ActionEvent evt) {
         JTextComponent textComponent = (JTextComponent) evt.getSource();
         String text = textComponent.getText();
         textComponent.setText("");

         printStream.println(text);
         textArea.append(Terminal.PRE_TEXT + text + "\n");
      }
   }

   private class InputStreamWorker extends SwingWorker<Void, String> {
      private Scanner scanner;
      private JTextArea textArea;

      private InputStreamWorker(JTextArea textArea, InputStream inStream) {
         this.textArea = textArea;
         scanner = new Scanner(inStream);
      }

      @Override
      protected Void doInBackground() throws Exception {
         while (scanner.hasNextLine()) {
            publish(scanner.nextLine());
         }
         return null;
      }

      @Override
      protected void process(List<String> chunks) {
         for (String chunk : chunks) {
            textArea.append(chunk + "\n");
         }
      }
   }
}

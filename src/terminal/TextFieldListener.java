package terminal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;

import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;

public class TextFieldListener implements ActionListener {
    private PrintStream printStream;
    public static JTextArea textArea;
    public static String lastEntered;

    public TextFieldListener(PrintStream printStream, JTextArea textArea) {
       this.printStream = printStream;
       TextFieldListener.textArea = textArea;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
       JTextComponent textComponent = (JTextComponent) evt.getSource();
       lastEntered = textComponent.getText();
       textComponent.setText("");
       
       Terminal.moves++;
       printStream.println(lastEntered);
       Terminal.ps.println(lastEntered);
    }
 }

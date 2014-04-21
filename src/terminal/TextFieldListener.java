package terminal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;

import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;

import main.Game;

public class TextFieldListener implements ActionListener {
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
       textArea.append(Game.getCurrPlayer().getColorName() + ">" + text + "\n");

    }
 }

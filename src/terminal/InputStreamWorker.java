package terminal;

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

import javax.swing.JTextArea;
import javax.swing.SwingWorker;


public class InputStreamWorker extends SwingWorker<Void, String> {
      private Scanner scanner;
      private JTextArea textArea;

      InputStreamWorker(JTextArea textArea, InputStream inStream) {
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
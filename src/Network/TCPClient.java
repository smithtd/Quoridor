package Network;

/*
 * Author: DLYAN WOYTHAL
 */

import java.util.*;
import java.io.*;
import java.net.*;

public class TCPClient{

    public static void main(String argv[]) throws Exception {

        String input;
        String returned;

        Scanner inFromUser = new Scanner( System.in );
        Socket clientSocket = new Socket("localhost", 6789);

        while(true){
            System.out.println( "CLIENT Debug" );
            System.out.print( "CLIENT input: " );
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            input = inFromUser.nextLine();
            outToServer.writeBytes(input + '\n');
            returned = inFromServer.readLine();
            System.out.println("CLIENT Received: " + returned);
            if( returned.equals( "GAMEOVER" ) ){
                System.out.println( "CLIENT Terminating Client" );
                break;
            }
        }
            clientSocket.close();
    }
}
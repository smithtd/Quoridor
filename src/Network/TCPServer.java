package Network;

/*
 * Author: DLYAN WOYTHAL
 */
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class TCPServer{

    public static void main(String argv[]) throws Exception{

        ArrayList<String> acceptableTokens = new ArrayList<String>();
        acceptableTokens.add( "HELLO" );
        acceptableTokens.add( "QUORIDOR" );
        acceptableTokens.add( "READY" );
        acceptableTokens.add( "MOVE?" );
        acceptableTokens.add( "MOVE" );
        acceptableTokens.add( "MOVED" );
        acceptableTokens.add( "REMOVED" );
        acceptableTokens.add( "WINNER" );
        
        String tokenReceived;
        ServerSocket welcomeSocket = new ServerSocket(6789);
        Socket connectionSocket = welcomeSocket.accept();
        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
        
        while(true){
            tokenReceived = inFromClient.readLine();
            System.out.println("SERVER Received: " + tokenReceived);
            if( acceptableTokens.contains( tokenReceived ) ){
                if( tokenReceived.equals( "WINNER" ) ){
                    outToClient.writeBytes( "GAMEOVER" + '\n' );
                    break;
                }else{
                    outToClient.writeBytes( "Acceptable Token" + '\n' );
                }
            } else{
                outToClient.writeBytes( "Token Not Accepted" + '\n' );
            }
        }
    }
}

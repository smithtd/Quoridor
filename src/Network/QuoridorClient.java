/**
 * Quoridor Networking Client
 * @author Marc Dean Jr.
 * Team - 511Tactical
 */
package network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;



/**
 * The client when started, will determine how many people to look for 
 * for this specific instance of the game. This is where the game will spin
 * up and allow servers to connect to it. It will then ask each person for a
 * name and their moves, and process the information.
 */


/**
 *	Log: keep an array of players(servers??? sockets??) to process moves
 *		 wait for all connections from all servers
 *		 Initiate game, start requesting moves/wall placements 
 *
 */

public class QuoridorClient implements Messages {
	
	// DataOutputStream - out to server
	
	// BufferedReader - in from server
	
	/** Store the player information rather than have two arrays */
	private static PlayerInformation[] channels;
	private static Socket[] socks;
	
	/** Max # of players **/
	private static final int MAX_PLAYERS = 4;
	/** Min # of players **/ 
	private static final int MIN_PLAYERS = 2;
	
	
	public QuoridorClient(int players) {
		
		channels = new PlayerInformation[players];
		socks = new Socket[players];
		
	}
	
	/** Handle CLA's, make a new client, start the game asking 
	 *  person to make their moves, handle legal moves, booting,
	 *  etc...
	 * @param args - <hostName:portNumber> X 2 || 4 (Hopefully)
	 */
	public static void main(String[] args) {
		
		int arguments = args.length;
		
		System.out.println(args.length);
		
		if(!(arguments == MAX_PLAYERS || arguments == MIN_PLAYERS)) {

			System.err.println("Illegal # of players instantiated");
			System.exit(1);
			
		}
		
		QuoridorClient newGame = new QuoridorClient(arguments);
		
		/** Make the connections with the servers */
		newGame.makeConnections(args);
		
		/** connections made, get information from move-server */
		
		try {
			run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}
	
	public static void run() throws IOException {
		
		while(true) {
			for(int i = 0; i < channels.length; i++) {
				BufferedReader b = new BufferedReader(new InputStreamReader(socks[i].getInputStream()));
				DataOutputStream dos = new DataOutputStream(socks[i].getOutputStream());
				dos.writeBytes(ASK_FOR_MOVE);
				System.out.println(b.readLine().trim());
			}
		}
		
	}
	
	/** 
	 * 
	 * @param args
	 */
	private void makeConnections(String[] args) {
		
		/** Connect to the socket and store them into the channels queue */
		
		for(int i = 0; i < args.length; i++) {
			
			String host = args[i].substring(0, (args[i].indexOf(':')));
			int portNumber = Integer.parseInt((args[i]).substring(args[i].indexOf(':') + 1));
			
			System.out.println(host + " " + portNumber);
			
			try {
				//QuoridorServer qs = new QuoridorServer(portNumber);
				Socket s = new Socket(host,portNumber);
				//channels[i] = new PlayerInformation("", s/*,qs*/);
				socks[i] = s;
				
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	private class PlayerInformation {
		public String name;
		public Socket connection;
		//public QuoridorServer moveServer;
		public Scanner input;
		public DataOutputStream dos;
		
		public PlayerInformation(String name, Socket s/*, QuoridorServer mv*/) {
			this.name = name; 
			this.connection = s;
			//this.moveServer = mv;
			try {
				this.input = new Scanner (this.connection.getInputStream());
				dos = new DataOutputStream(this.connection.getOutputStream());
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
}

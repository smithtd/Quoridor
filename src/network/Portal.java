/**
 * @author marc
 * Quoridor
 * Team - 511 Tactical
 */
package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


/**
 * The Portal will contain information for the GameClient to talk to the 
 * servers. When the GameClient starts up it will be able to have this 
 * information in order to connect with the moveServers.
 */

public class Portal {

	/** Machine name where moveServer is running */
	private String machineName;
	/** Port where moveServer is listening */
	private int port;
	/** Communication Socket */
	private Socket socket;
	/** Buffered Reader on incoming information from the server */
	private BufferedReader incoming;
	/** Send data out */
	private PrintStream outgoing;
	/** AI Identifier */
	private String aiIdentifier;
	
	/** Tell if still in game or not */
	private boolean inGame;
	
	/**
	 * Create an instance of a portal for communication with a server.
	 * @param name - Machine name of the machine that is running the moveServer
	 * @param p - Port accepting communication. 
	 */
	public Portal(String name, int p){
		this.machineName = name;
		this.port = p;
		
		try {
			this.socket = new Socket(this.machineName, this.port);
			this.incoming = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			this.outgoing = new PrintStream(this.socket.getOutputStream());
		} catch (UnknownHostException e) {
			System.out.println("Could not find " + this.machineName + ":" + this.port);
		} catch (IOException e) {
			System.out.println("Could not find " + this.machineName + ":" + this.port);
		}
		
		this.inGame = true;
		
		String helloMessage = this.getMessage();
		Scanner sc = new Scanner(helloMessage);
		sc.next(); this.aiIdentifier = sc.next();
		sc.close();
		
	}
	
	/**
	 * Get a message from the moveServer.
	 * @return - Message from the moveServer.
	 */
	public String getMessage() {
		
		String message = "";
		
		try {
			message = this.incoming.readLine();
		} catch (IOException e) {
			
		}
		return message;
	}
	
	/**
	 * Send a message to the moveServer
	 * @param message - Message to send to the server
	 */
	public void sendMessage(String message) {
		this.outgoing.println(message);
	}
	
	/**
	 * Set this flag to boot the player.
	 */
	public void bootPlayer() {
		this.inGame = false;
	}
	
	/**
	 * Tell if the player is still in the game.
	 * @return - True if in game, else false
	 */
	public boolean inGame() {
		return this.inGame;
	}
	
	/** 
	 * 
	 * @return - AI Identifier of this Portal.
	 */
	public String getAIIdentifier(){
		return this.aiIdentifier;
	}
	
}

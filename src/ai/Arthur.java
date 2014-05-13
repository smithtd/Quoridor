package ai;

import java.util.ArrayList;
import java.util.Scanner;

import network.Messages;
import players.Player;
import board.Board;

/**
 * Arthur - A grown up version of Artie.
 */



public class Arthur extends Artie { 


	private Board board;


	public Arthur(int port) {
		super(port);


	}

	private String getMove(){ 

		ArrayList<String> path = this.board.players().get(this.playerId).getPath();

		return path.get(0);


	}

	/**
	 * Takes the Quoridor message and sets up the game board. 
	 * 
	 * @param input - Quoridor message
	 */
	private void setupGameState(String input) {
		try { 

			Scanner sc = new Scanner(input);
			sc.next(); // get rid of the Quoridor token

			// get the second token which should be the playerID in the quoridor message
			this.playerId = Integer.parseInt(sc.next()); 

			int playerCount = 1;
			while(sc.hasNext()) { 
				playerCount++;
				sc.next();
			}
			ArrayList<Player> players = new ArrayList<Player>();
			if(playerCount == 2) { 
				// set up the board to have two players
				players.add(new Player("1", 0, 4, 1, 10));
				players.add(new Player("2", 8, 4, 2, 10));


			} else { 
				// the board must have four players 
				players.add(new Player("1", 0, 4, 1, 5));
				players.add(new Player("4", 4, 8, 4, 5));
				players.add(new Player("2", 8, 4, 2, 5));
				players.add(new Player("3", 4, 0, 3, 5));

			}
			this.board = new Board(players, 20);
			System.out.println(this.board);
			

		} catch(Exception e) {
			System.err.println("This is not a valid Quoridor message or other issues");
		}

	}

	public void getResponse(String input) throws InterruptedException {
		Thread.sleep(250); // try to watch what is happening

		// If the client asks for a move get one 
		if(input.equalsIgnoreCase(Messages.ASK_FOR_MOVE)){
			String move = getMove();
			this.currentPosition = move;

			this.clientOutput.println("MOVE " + move);
			// If the client is being told what move is being made
		} else if(input.startsWith(Messages.TELL_MOVE)){
			Scanner info = new Scanner(input);
			info.next(); 
			int playerNumber = Integer.parseInt(info.next());

			String moveMade = info.next();

			if(moveMade.length() == 2) 
				this.board.placePawn(this.board.players().get(playerNumber),Integer.parseInt("" + moveMade.charAt(0)), Integer.parseInt("" + moveMade.charAt(1)) - 1);
			else if(moveMade.length() == 3) 
				// should be a wall placement
				this.board.placeWall(this.board.players().get(playerNumber), Integer.parseInt("" + moveMade.charAt(0)), Integer.parseInt("" + moveMade.charAt(1)) -1, moveMade.charAt(2) + "");
			else
				System.out.println("someone should be kicked");

			System.out.println("Player " + playerNumber + " made move: " + moveMade);
			info.close();
			// Quoridor message
		} else if(input.startsWith(Messages.START_GAME)){
			setupGameState(input);
			Scanner getID = new Scanner(input);
			getID.next();
			// Skip the quoridor token and grab your player number
			this.playerId = Integer.parseInt(getID.next());
			this.clientOutput.println(Messages.READY + " " + this.identifier);
			System.out.println("Playing as player # " + this.playerId);
			this.currentPosition = this.startPos.get(this.playerId);
			getID.close();
			// Booted a player
		} else if(input.startsWith(Messages.REMOVED)){
			Scanner removed = new Scanner(input);
			removed.next();
			int player = Integer.parseInt(removed.next());
			if(player == this.playerId) {
				System.out.println("You have been removed for illegal moves.");
				this.hasWon = false;
			} else {
				System.out.println("Player " + player + " has been removed");
			}
			removed.close();
		} else if(input.startsWith(Messages.WINNER)) {
			Scanner win = new Scanner(input);
			win.next();
			int player = Integer.parseInt(win.next());
			win.close();
			this.hasWon = false;
			if(player == this.playerId){
				System.out.println("You won!");
			} else {
				System.out.println("Player " + player + " won! You lose!");
			}
		}
	}



	/** 
	 * Starts an instance of this AI, handles command line args 
	 * @param args -- Port number to listen on. 
	 */
	public static void main(String[] args) {

		// Should have a port number as a command line argument
		if(args.length != 1) 
			usage();

		// Try and parse the port from the command
		int port = Integer.parseInt(args[0]);

		Arthur aiArtie = new Arthur(port);

		aiArtie.run();
	}

}
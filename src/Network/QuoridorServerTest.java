package Network;

import static org.junit.Assert.*;

import org.junit.Test;

public class QuoridorServerTest {
	
	private QuoridorServer client = new QuoridorServer("localhost", 3939);
	private String move = "e4b";

	
	@Test
	public void testAddMove() throws InterruptedException {
		
		this.client.addMove(this.move);
		assertEquals(this.move, this.client.getMove());
		
	}
	
	/**
	 * Need to multithread this, since it waits for the
	 * getMove() method.
	 * @throws InterruptedException 
	 * 
	 * Right now the getMove() function will check if the queue
	 * is empty, and if so prompts for a move and adds it to the 
	 * queue. 
	 * 
	 */
	@Test
	public void testAddMoveWhenWeWait() throws InterruptedException {
		
		String waitMove = null;
		waitMove = this.client.getMove();
		this.client.addMove(this.move);
		assertEquals(this.move, waitMove);
		
	}
	
	

}

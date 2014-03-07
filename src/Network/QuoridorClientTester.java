package Network;

import static org.junit.Assert.*;

import org.junit.Test;

public class QuoridorClientTester {
	
	private QuoridorClient client = new QuoridorClient("localhost", 3939);
	private String move = "e4b";

	
	@Test
	public void testAddMove() throws InterruptedException {
		
		this.client.addMove(this.move);
		assertEquals(this.move, this.client.getMove());
		
	}
	
	/**
	 * Need to multithread this, since it waits for the
	 * getMove() method.
	 */
	@Test
	public void testAddMoveWhenWeWait() {
		
		String waitMove = null;
		waitMove = this.client.getMove();
		this.client.addMove(this.move);
		assertEquals(this.move, waitMove);
		
	}
	
	

}

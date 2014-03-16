package Network;

import static org.junit.Assert.*;

import org.junit.Test;

public class QuoridorServerTest {
	
	private QuoridorServer client = new QuoridorServer(3939);
	private String move = "e4b";

	
	
	
	@Test
	public void testGetMove() {
		String waitMove = this.client.getMove();
		assertEquals(this.move, waitMove);
	}
	
	

}

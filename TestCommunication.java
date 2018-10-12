package week06;

import static org.junit.Assert.*;

import org.junit.Test;

import test.AbstractJUnitBase;
import week06.io.AtmEndPointServer;

public class TestCommunication extends AbstractJUnitBase
{
	/**
	 * Default constructor
	 */
	public TestCommunication()
	{
		
	}
	
	@Test
	public void runServerTest()
	{
		// Tests
		AtmEndPointServer server = new AtmEndPointServer(8001);

		try
		{
			server.start();
			server.close();
		}
		catch(AtmException ex)
		{
			fail(ex.getMessage());
		}
	}

	private boolean testComms()
	{
		
		return false;
	}
}

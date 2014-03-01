package elevatorController;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import building.Building;

public class PendingRequestProcessorImplTest {

	private static Building theBuilding = null;
	private static PendingRequestProcessorImpl p = null;
	
	@BeforeClass
	public static void setUpBeforeClass()
	{
		theBuilding = Building.getInstance( ( short ) 10, ( short ) 1, ( short ) 10, ( short ) 1000, ( short ) 2500, ( short ) 0);
		p = new PendingRequestProcessorImpl();
		EController.getInstance().pendingRequests.add(new ERequest((short)5, true));
	}

	@AfterClass
	public static void tearDownAfterClass()
	{
		p = null;
		theBuilding = null;
	}

	@Test
	public void test()
	{
		assertTrue(p.processPendingRequests((short)0,(short)0));
	}
}
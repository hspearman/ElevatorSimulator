package elevatorController;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import building.Building;
import elevator.*;

public class CallBoxRequestProcessorSecondImplTest {

	private static Building theBuilding = null;
	private static CallBoxRequestProcessorSecondImpl c = null;
	
	@BeforeClass
	public static void setUpBeforeClass()
	{
		theBuilding = Building.getInstance( ( short ) 10, ( short ) 1, ( short ) 10, ( short ) 1000, ( short ) 2500, ( short ) 0);
		c = new CallBoxRequestProcessorSecondImpl();
	}

	@AfterClass
	public static void tearDownAfterClass()
	{
		c = null;
		theBuilding = null;
	}
	
	@Test
	public void test()
	{
		ERequest r = new ERequest((short) 5, true);
		c.processCallBoxRequest(r);
		Elevator e = theBuilding.getElevatorFromEController((short)0);
		assertTrue(e.isInDestinations((short)5));
		ERequest rr = new ERequest((short) 8, true);
		c.processCallBoxRequest(rr);
		e = theBuilding.getElevatorFromEController((short)0);
		assertTrue(e.isInDestinations((short)8));
	}

}
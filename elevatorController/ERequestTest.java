package elevatorController;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import building.Building;
import building.PersonFacade;

public class ERequestTest {

	private static Building theBuilding = null;
	
	@BeforeClass
	public static void setUpBeforeClass()
	{
		theBuilding = Building.getInstance( ( short ) 10, ( short ) 3, ( short ) 10, ( short ) 1000, ( short ) 2500, ( short ) 0);
	}

	@AfterClass
	public static void tearDownAfterClass()
	{
		theBuilding = null;
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void noNegativeFloor()
	{
		new ERequest((short)-1,true);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testFloorExists()
	{
		new ERequest((short)20,true);
	}
	
	@Test
	public void test()
	{
		ERequest e = new ERequest((short)5, true);
		assertTrue(e.getDirection());
		assertEquals(e.getFloorNum(),(short)5);
	}

}

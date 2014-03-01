package building;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import elevator.Elevator;
import elevatorController.EController;

public class ECallBoxTest {

	private static Building theBuilding = null;
	private static ECallBox e = null;
	
	@BeforeClass
	public static void setUpBeforeClass()
	{
		theBuilding = Building.getInstance( ( short ) 10, ( short ) 1, ( short ) 10, ( short ) 1000, ( short ) 2500, ( short ) 5);
	}

	@AfterClass
	public static void tearDownAfterClass()
	{
		theBuilding = null;
		e = null;
	}
	
	@After
	public void tearDown()
	{
		EController.getInstance().reset();
	}

	@Test (expected = IllegalArgumentException.class)
	public void noNegativeFloor()
	{
		new ECallBox((short)-1);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testFloorExists()
	{
		new ECallBox((short)100);
	}
	
	@Test
	public void testCallForUp()
	{
		e = new ECallBox((short)7);
		e.callForUp();
		Elevator e = EController.getInstance().getElevator((short)0);
		assertTrue(e.isInDestinations((short)7));
	}
	
	@Test
	public void testCallForDown()
	{
		e = new ECallBox((short)3);
		e.callForDown();
		Elevator e = EController.getInstance().getElevator((short)0);
		assertTrue(e.isInDestinations((short)3));
	}
}
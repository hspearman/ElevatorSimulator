package building;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import elevator.Elevator;
import elevatorController.EController;

public class FloorTest {

	private static Building theBuilding = null;
	private static Floor f = null;
	
	@BeforeClass
	public static void setUpBeforeClass()
	{
		theBuilding = Building.getInstance( ( short ) 10, ( short ) 1, ( short ) 10, ( short ) 1000, ( short ) 2500, ( short ) 5);
	}

	@AfterClass
	public static void tearDownAfterClass()
	{
		f = null;
		theBuilding = null;
	}
	
	@After
	public void tearDown()
	{
		EController.getInstance().reset();
	}

	@Test (expected = IllegalArgumentException.class)
	public void noNegativeFloor()
	{
		new Floor((short)-1);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testFloorExists()
	{
		new Floor((short)100);
	}
	
	@Test
	public void testCallForUp()
	{
		f = new Floor((short)7);
		f.pressCallButton(true);
		Elevator e = EController.getInstance().getElevator((short)0);
		assertTrue(e.isInDestinations((short)7));
	}

	@Test
	public void testCallForDown()
	{
		f = new Floor((short)3);
		f.pressCallButton(false);
		Elevator e = EController.getInstance().getElevator((short)0);
		assertTrue(e.isInDestinations((short)3));
	}
}

package elevator;
import elevator.ElevatorImpl;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import building.Building;

public class ElevatorFactoryTest {

	private static Building theBuilding = null;
	
	@BeforeClass
	public static void beforeClassStuff()
	{
		System.out.println("Starting tests...");
		theBuilding = Building.getInstance( ( short ) 10, ( short ) 3, ( short ) 10, ( short ) 1000, ( short ) 2500, ( short ) 0);
	}
	
	@AfterClass
	public static void afterClassStuff()
	{
		theBuilding = null;
		System.out.println("Tests ended.");
	}
	
	@Test
	public void test()
	{
		ElevatorImpl theElevator = (ElevatorImpl) ElevatorFactory.build((short)1, (short)10, (short)1000, (short)2500, (short)0);
		assertEquals(theElevator.getID(),(short)1);
		assertEquals(theElevator.getCapacity(),(short)10);
		assertEquals(theElevator.getTimePerFloor(),(short)1000);
		assertEquals(theElevator.getDoorOperationTime(),(short)2500);
		assertEquals(theElevator.getDefaultFloor(),(short)0);
	}

}

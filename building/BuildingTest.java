package building;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.*;

import elevator.ElevatorImpl;

public class BuildingTest {

	private static Building theBuilding = null;
	
	@BeforeClass
	public static void setUpClass()
	{
		ArrayList<Short> inFloors = new ArrayList<Short>();
		inFloors.add((short)0);
		inFloors.add((short)1);
		inFloors.add((short)2);
		theBuilding = Building.getInstance((short)10 ,(short)3, (short)10, (short)1000, (short)2500, inFloors);
	}
	
	@Test
	public void testGetNumOfFloors()
	{
		assertEquals(theBuilding.getNumberOfFloors(),(short)10);
	}
	
	@Test
	public void testGetElevatorFromEController()
	{
		ElevatorImpl thisElevator = (ElevatorImpl) theBuilding.getElevatorFromEController((short)0);
		assertEquals(thisElevator.getDefaultFloor(),(short)0);
	}
	
	@Test
	public void testGetInstanceBlank()
	{
		Building theOtherBuilding = Building.getInstance();
		assertEquals(theOtherBuilding.getNumberOfFloors(),(short)10);
	}
}

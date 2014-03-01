package elevator;

import static org.junit.Assert.*;
import org.junit.*;
import building.Building;

public class ElevatorImplTest{

	private static Building theBuilding = null;
	private static ElevatorImpl theElevator = null;
	
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
	
	@Before
	public void setUpTests()
	{
		theElevator = (ElevatorImpl) theBuilding.getElevatorFromEController((short)0);
	}
	
	@After
	public void cleanStuffUp()
	{
		theElevator.shutdown();
		theElevator.reset();
		theElevator = null;
	}
	
	@Test
	public void testIsRunning()
	{
		assertTrue(theElevator.isRunning());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void noNegativeID()
	{
		new ElevatorImpl((short)-1,(short)10,(short)1000, (short)2500, (short)0);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void noNegativeCapacity()
	{
		new ElevatorImpl((short)1,(short)-10,(short)1000, (short)2500, (short)0);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void noNegativeFloorToFloorTime()
	{
		new ElevatorImpl((short)1,(short)10,(short)-1000, (short)2500, (short)0);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void noNegativeDoorOperationTime()
	{
		new ElevatorImpl((short)1,(short)10,(short)1000, (short)-2500, (short)0);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void noNegativeDefaultFloor()
	{
		new ElevatorImpl((short)1,(short)10,(short)1000, (short)2500, (short)-1);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void defaultFloorExists()
	{
		short numOfFloors = theBuilding.getNumberOfFloors();
		new ElevatorImpl((short)1,(short)10,(short)1000, (short)2500, (short)(numOfFloors+10));
	}
	
	@Test
	public void testGetID()
	{
		assertEquals(theElevator.getID(),0);
	}
	
	@Test
	public void testGetCapacity()
	{
		assertEquals(theElevator.getCapacity(),10);
	}
	
	@Test
	public void testGetTimePerFloor()
	{
		assertEquals(theElevator.getTimePerFloor(),1000);
	}
	
	@Test
	public void testGetDoorOperationTime()
	{
		assertEquals(theElevator.getDoorOperationTime(),2500);
	}
	
	@Test
	public void testGetDefaultFloor()
	{
		assertEquals(theElevator.getDefaultFloor(),0);
	}
	
	@Test
	public void testGetCurrentFloor()
	{
		assertEquals(theElevator.getCurrentFloor(),0);
	}
	
	@Test
	public void testGetState()
	{
		assertEquals(theElevator.getState(),0);
	}
	
	@Test
	public void testIsDestinationsEmpty()
	{
		assertTrue(theElevator.isDestinationsEmpty());
		theElevator.addDestination((short)5);
		assertFalse(theElevator.isDestinationsEmpty());
	}
	
	@Test
	public void testIsInDestinations()
	{
		assertFalse(theElevator.isInDestinations((short)5));
		theElevator.addDestination((short)5);
		assertTrue(theElevator.isInDestinations((short)5));
	}
	
	@Test
	public void testIsCurrentDirectionUp()
	{
		assertTrue(theElevator.isCurrentDirectionUp());
	}
	
	@Test
	public void testAddDestination()
	{
		try
		{
			theElevator.addDestination((short)-1);
			fail("Did not reject adding an out of bounds floor");
		}
		catch(IllegalArgumentException e)
		{
			//System.out.println("Good! Rejected adding a destination out-of-range (floor > 10).");
		}
		
		try
		{
			theElevator.addDestination((short)100);
			fail("Did not reject adding an out of bounds floor");
		}
		catch(IllegalArgumentException e)
		{
			//System.out.println("Good! Rejected taking a floor request in the opposite direction (floor < 0).");
		}
		
		try
		{
			theElevator.addDestination((short)3);
			assertTrue(theElevator.isCurrentDirectionUp());
			assertTrue(theElevator.isInDestinations((short)3));
		}
		catch(IllegalArgumentException e)
		{
			fail("Failed on adding 3 to the list of destinations.\n"+e.getMessage());
		}
	}
	
	@Test
	public void testTakeRequest()
	{
		try
		{
			theElevator.takeRequest((short)-1, true);
			fail("Did not reject -1 as a floor.");
		}
		catch (IllegalArgumentException e)
		{
			//System.out.println("Good! Rejected taking a floor request out-of-range (floor < 0).");
		}
		
		try
		{
			theElevator.takeRequest((short)100, false);
			fail("Did not reject 100 as a floor.");
		}
		catch (IllegalArgumentException e)
		{
			//System.out.println("Good! Rejected taking a floor request out-of-range (floor > 10).");
		}
	}
	
	@Test
	public void testShutdown()
	{
		theElevator.shutdown();
		assertEquals(theElevator.getState(),(short)0);
		assertFalse(theElevator.isRunning());
	}
}
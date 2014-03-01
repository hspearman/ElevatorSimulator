package building;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PersonTest {

	private static Building theBuilding = null;
	private static Person thePerson = null;
	
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
	public void setUp() throws Exception
	{
		thePerson = new Person((short)0,(short)0,(short)1);
	}

	@After
	public void tearDown() throws Exception
	{
		thePerson = null;
	}

	@Test (expected = IllegalArgumentException.class)
	public void noNegativeID()
	{
		new Person((short)-1,(short)0,(short)1);
	}

	@Test (expected = IllegalArgumentException.class)
	public void noNegativeBeginFloor()
	{
		new Person((short)0,(short)-1,(short)1);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void noNegativeEndFloor()
	{
		new Person((short)0,(short)0,(short)-1);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void beginFloorExists()
	{
		new Person((short)0,(short)20,(short)1);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void endFloorExists()
	{
		new Person((short)0,(short)0,(short)20);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void beginAndEndDifferent()
	{
		new Person((short)0,(short)0,(short)0);
	}
	
	@Test
	public void testGetID()
	{
		assertEquals(thePerson.getID(),0);
	}
	
	@Test
	public void testStats()
	{
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		thePerson.stopWaitTimer();
		thePerson.startRideTimer();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		thePerson.stopRideTimer();
		assertEquals(thePerson.getWaitTime(),1);
		assertEquals(thePerson.getRideTime(),1);
	}
	
	@Test
	public void testHasReachedDestination()
	{
		assertFalse(thePerson.hasReachedDestination());
		thePerson.setHasReachedDestination(true);
		assertTrue(thePerson.hasReachedDestination());
	}
}

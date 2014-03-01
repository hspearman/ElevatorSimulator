package building;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class PersonFacadeTest {

	private static Building theBuilding = null;
	private static PersonFacade p = null;
	
	@BeforeClass
	public static void setUpBeforeClass()
	{
		theBuilding = Building.getInstance( ( short ) 10, ( short ) 3, ( short ) 10, ( short ) 1000, ( short ) 2500, ( short ) 0);
		p = PersonFacade.getInstance();
	}

	@AfterClass
	public static void tearDownAfterClass()
	{
		theBuilding = null;
		p = null;
	}

	@Test
	public void test()
	{
		p.createPerson((short)0, (short)5);
		assertFalse(p.hasReachedDestination((short)0));
		p.setHasReachedDestination((short)0, true);
		assertTrue(p.hasReachedDestination((short)0));
		assertEquals(p.getDestinationFloor((short)0),5);
	}

}

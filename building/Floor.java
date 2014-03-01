package building;
import java.util.ArrayList;

/**
 * This class represents a floor of the building. It houses all the person objects and interacts
 * with the elevators when needed (such as transferring inhabitants).
 * 
 * @author Jeremy Ma
 * @author Hannah Spearman
 * 
 */
public class Floor
{
	/**
	 * the number of the floor
	 */
	private short floorNum;
	
	/**
	 * creates an ECallBox specific to this floor
	 */
	private ECallBox myCallBox;

	/**
	 * array list that holds the person ID's of people currently residing on this floor during the simulation
	 */
	private ArrayList<Short> carrying;

	/**
	 * Creates a new Floor with the specified arguments. It also initializes internal data members that don't
	 * accept parameters (waitTimes, totalTimes, numOfPeople, carrying, myCallBox)
	 * @param myFloorNum the number of the newly created floor
	 */
	public Floor( short myFloorNum )
	{
		if(myFloorNum < 0 || myFloorNum > Building.getNumberOfFloors())
		{
			throw new IllegalArgumentException("Floor out of bounds, expected 0-"+Short.toString(Building.getNumberOfFloors())+", got "+Short.toString(myFloorNum));
		}
		// short data members
		floorNum = myFloorNum;
		
		// short array list data members
		carrying = new ArrayList<Short>();
		
		myCallBox = new ECallBox(myFloorNum);
	}
	
	/**
	* Accepts a person object and adds that person to the floor (i.e. the carrying array list).
	* @param inPerson the person to be added to the floor
	*/
	public void addPerson( short personID )
	{
		carrying.add( personID );
	}
	
	/**
	* Given a direction (up or down), searches for the first person that wants to get on an
	* elevator traveling in that direction and returns their person ID.
	* @param directionIsUp what direction the elevator is traveling in
	* @param isDestinationsEmpty true if there are no more destinations for the elevator to go to, otherwise false
	* @return short the person ID of the person object who wants to get on the elevator
	*/
	public short getNextPassenger( boolean directionIsUp, boolean isDestinationsEmpty )
	{
		// cycles through the floor to find someone who wants to get on the elevator, and returns their ID
		for( short i = 0; i < carrying.size(); i++ )
		{
			short personID = carrying.get( i );
			short destFloorOfPerson = 		PersonFacade.getInstance().getDestinationFloor( personID );
			boolean hasReachedDestination = PersonFacade.getInstance().hasReachedDestination( personID );
			
			// if elevator is empty, it will go in whatever direction that the first passenger requests
			if ( isDestinationsEmpty && !hasReachedDestination)
			{
				return personID;
			}
			else if ( destFloorOfPerson > floorNum && directionIsUp && !hasReachedDestination )
			{
				return personID;
			}
			else if ( destFloorOfPerson < floorNum && !directionIsUp && !hasReachedDestination )
			{
				return personID;
			}
		}
		
		// return -1 if no one wants to get on the elevator
		return -1;
	}
	
	/**
	 * Given a person ID, this method searches for the corresponding personID on the floor and removes them from the floor.
	 * @param personID
	 * @return
	 */
	public void sendPerson( short personID )
	{
		// cycles through persons until it finds the right ID
		// if it does, returns them
		for( int i = 0; i < carrying.size(); i++ )
		{
			if( personID == carrying.get( i ) )
			{
				carrying.remove( i );
			}
		}
	}
	
	/**
	 * Presses the call box button which generates an elevator requests and sends it to the EController
	 * @param goingUp: The desired direction of travel
	 * @throws IllegalArgumentException
	 */
	public void pressCallButton( boolean goingUp ) throws IllegalArgumentException
	{
		if ( goingUp )
		{
			this.myCallBox.callForUp();
		}
		
		else
		{
			this.myCallBox.callForDown();
		}
	}
	/**
	 * Returns all the people that ended up on this floor (needed for statistical processing)
	 * @return an array list contain all the person objects on this floor
	 */
}
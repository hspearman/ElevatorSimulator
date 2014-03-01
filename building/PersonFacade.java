package building;

import java.util.ArrayList;

public class PersonFacade {
	/**
	 * increments by one after adding each person (used to designate the ID of each person object)
	 */
	private static short IDcounter;
	/**
	 * Contains the singular instance of PersonFacade (since it is a singleton)
	 */
	private static PersonFacade instance;
	/**
	 * Array list that holds all the person objects in the simulation
	 */
	private ArrayList<Person> people;
	
	/**
	 * Creates a new person facade (initializes person array list and sets ID counter to zero)
	 */
	private PersonFacade( )
	{
		people = new ArrayList<Person>();
		IDcounter = 0;
	}
	
	/**
	 * Returns IDcounter so the PersonFacade may assign new ID's to newly created persons
	 * @return the current number of IDcounter
	 */
	private short getIDcounter()
	{
		return IDcounter;
	}

	/**
	 * Increments the ID counter (should be called after each time a person object is created)
	 */
	private void incrementIDcounter()
	{
		IDcounter++;
	}
	
	/**
	* Returns the singleton instance of PersonFacade
	* @return the facade for all person objects
	*/
	public static PersonFacade getInstance()
	{
		if( instance == null )
		{
			synchronized( PersonFacade.class )
			{
				if( instance == null )
				{
					instance = new PersonFacade();
				}
			}
		}
		return instance;
	}
	
	/**
	 * Forwards the call to Person to get a person's destination floor
	 * @param personID: the ID of the person desired
	 * @return the person's destination floor
	 */
	public short getDestinationFloor( short personID )
	{
		return people.get( personID ).getDestinationFloor();
	}

	/**
	 * Forwards the call to Person to get the boolean determining if a person has reached their destination floor 
	 * @param personID: the ID of the person desired
	 * @return the boolean determining if the person has reached their destination
	 */
	public boolean hasReachedDestination( short personID )
	{
		return people.get( personID ).hasReachedDestination();
	}

	/**
	 * Forwards the call to Person to set the boolean determining if the person has reached their destination
	 * @param personID: the ID of the person desired
	 * @param status: the boolean (true == reached, false == has not reached) determining if the person has reached their destination
	 */
	public void setHasReachedDestination( short personID, boolean status )
	{
		people.get( personID ).setHasReachedDestination( status );
	}

	/**
	 * Forwards the call to Person to stop the person's ride timer
	 * @param personID: the ID of the person desired
	 */
	public void stopRideTimer( short personID )
	{
		people.get( personID ).stopRideTimer();
	}

	/**
	 * Forwards the call to Person to stop the person's ride timer
	 * @param personID: the ID of the person desired
	 */
	public void stopWaitTimer( short personID )
	{
		people.get( personID ).stopWaitTimer();
	}

	/**
	 * Forwards the call to Person to start the person's ride timer
	 * @param personID: the ID of the person desired
	 */
	public void startRideTimer( short personID )
	{
		people.get( personID ).startRideTimer();
	}
	
	public void createPerson( short startFloor, short destFloor )
	{
		Person generated = new Person( getIDcounter(), startFloor, destFloor );
		people.add( generated );
		incrementIDcounter();
	}
	
	public ArrayList<PersonDTO> getPersonDTOs()
	{
		ArrayList<PersonDTO> peopleInfo = new ArrayList<PersonDTO>();
		for ( Person person : people )
		{
			// only include people who have completed their trip
			if ( person.hasReachedDestination() )
			{
				peopleInfo.add( new PersonDTO(	person.getID(),
												person.getStartFloor(),
												person.getDestinationFloor(), 
												( int ) person.getWaitTime(), 
												( int ) person.getRideTime() ) );
			}
		}
		
		return peopleInfo;
	}
	
	/**
	 * This method resets the person facade so it can run for another simulation loop
	 * (needed because singletons cannot be recreated during a program)
	 */
	public void reset()
	{
		this.people.clear();
		PersonFacade.IDcounter = 0;
	}
}

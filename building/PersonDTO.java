package building;

/**
 * This class represents a DTO object for the Person class. They are generated by the PersonFacade and given to the Simulation for
 * generating statistics.
 * @author Hannah
 *
 */
public class PersonDTO {
	/**
	 * The ID of the person
	 */
	public short personID;
	/**
	 * The floor the person started on
	 */
	public short startFloor;
	/**
	 * The person's destination floor
	 */
	public short destFloor;
	/**
	 * The total time a person waited for an elevator
	 */
	public int waitTime;
	/**
	 * The total time a person rode the elevator
	 */
	public int rideTime;
	/**
	 * Takes the requested Person's object information and creates a data transfer object from them.
	 * @param inID: the ID of the person who's DTO is being created
	 * @param inStartFloor: the floor the Person started on
	 * @param inDestFloor: the floor the Person wanted to reach
	 * @param inWaitTime: the total time the Person waited for an elevator
	 * @param inRideTime: the total time the Person rode the elevator
	 */
	public PersonDTO( short inID, short inStartFloor, short inDestFloor, int inWaitTime, int inRideTime )
	{
		personID = inID;
		startFloor = inStartFloor;
		destFloor = inDestFloor;
		waitTime = inWaitTime;
		rideTime = inRideTime;
	}
	
}

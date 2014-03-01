package building;
//TODO: Call ECallBox.callForUp();

/**
 * This class represents a person, many of which will inhabit the building and make elevator requests.
 * 
 * @author Jeremy Ma
 * @author Hannah Spearman
 * 
 */
public class Person {
	/**
	 * Stores the unique ID of the person
	 */
	private short personID;
	/**
	 * Stores the generated starting floor of the person
	 */
	private short startFloor;
	/**
	 * Stores the generated and desired destination of the person
	 */
	private short destFloor;
	/**
	 * Stores the start time of the person's wait timer.
	 */
	private long startWaitTime;
	/**
	 * Stores the stop time of the person's wait timer.
	 */
	private long stopWaitTime;
	/**
	 * Stores the start time of the person's ride timer
	 */
	private long startRideTime;
	/**
	 * Stores the stop time of the person's ride timer
	 */
	private long stopRideTime;
	/**
	 * Flags whether or not the person has reached their destination (ensures that they won't keep getting on the elevator after they've reached it)
	 */
	private boolean hasReachedDest;
	
	/**
	 * Returns the person's generated start floor
	 * @return returns the person's start floor
	 */
	public short getStartFloor() { return startFloor; }
	
	/**
	 * Presses the call box button which generates an elevator requests and sends it to the EController
	 * @param goingUp: The desired direction of travel
	 * @throws IllegalArgumentException
	 */
	public short getDestinationFloor() { return destFloor; }
	/**
	 * Returns the total wait time (in seconds) until the person got onto an elevator
	 * @return total elevator wait time
	 */
	public long getWaitTime() { return ( stopWaitTime - startWaitTime ) / 1000; }
	/**
	 * Returns the total ride time (in seconds) spent on the elevator until the destination was reached
	 * @return total elevator ride time
	 */
	public long getRideTime() { return ( stopRideTime - startRideTime ) / 1000; }
	/**
	 * Logs the time that this person started waiting for an elevator.
	 */
	public void startWaitTimer()
	{
		startWaitTime = System.currentTimeMillis();
	}
	/**
	 * Logs the time that this person stopped waiting for an elevator.
	 */
	public void stopWaitTimer()
	{
		stopWaitTime = System.currentTimeMillis();
	}
	/**
	 * Logs the time that this person started riding the elevator.
	 */
	public void startRideTimer()
	{
		startRideTime = System.currentTimeMillis();
	}
	/**
	 * Logs the time that this person stopped riding the elevator
	 */
	public void stopRideTimer()
	{
		stopRideTime = System.currentTimeMillis();
	}
	/**
	 * Constructs a person object given an ID, a start floor, and a desired destination (and starts their waiting timer)
	 * @param myID the ID used to identify the person object
	 * @param beginFloor the floor the person starts on
	 * @param endFloor the destination floor the person is trying to reach
	 */
	public Person( short myID, short beginFloor, short endFloor )
	{
		if(myID < 0)
		{
			throw new IllegalArgumentException("Tried to set negative ID.");
		}
		personID = myID;
		
		if(beginFloor == endFloor)
		{
			throw new IllegalArgumentException("Tried to make the person go to the same floor at which they start.");
		}
		if(beginFloor < 0 || beginFloor>Building.getNumberOfFloors())
		{
			throw new IllegalArgumentException("Tried to make the person start at an out-of-bounds floor.\nExpected (0-"+Short.toString(Building.getNumberOfFloors())+"), got"+Short.toString(beginFloor));
		}
		startFloor = beginFloor;
		if(endFloor < 0 || endFloor>Building.getNumberOfFloors())
		{
			throw new IllegalArgumentException("Tried to make the person go to an out-of-bounds floor.\nExpected (0-"+Short.toString(Building.getNumberOfFloors())+"), got"+Short.toString(endFloor));
		}
		destFloor = endFloor;
		
		this.startWaitTimer();
		hasReachedDest = false;
	}
	/**
	 * Returns the person's unique ID
	 * @return the person ID
	 */
	public short getID()
	{
		return personID;
	}	
	/**
	 * Returns the flag that determiens whether or the not the person has reached their destination (ensures they don't keep getting on the elevator)
	 * @return hasReachedDest: true if they'v reached their destination, false otherwise.
	 */
	public boolean hasReachedDestination() { return hasReachedDest; }
	
	/**
	 * Given a boolean, flags whether or the not the person has reached their destination (ensures they don't keep getting on the elevator)
	 * @param status: whether the person has reached their destination (true or false)
	 */
	public void setHasReachedDestination( boolean status ) { hasReachedDest = status; }
}

package elevator;

/**
 * This is an interface for an elevator object. It outlines the basic methods needed by the EController
 * to successfully manage a building's elevators.
 * 
 * @author Jeremy Ma
 * @author Hannah Spearman
 * 
 */
public interface Elevator
{
	/**
	 * Returns the elevator's state
	 * ( 0 = Idle, 1 = Active, but stopped, 2 = Active, and moving, 3 = Going to the default floor. )
	 * @return the elevator's current state
	 */
	public short getState();
	
	/**
	 * Checks the elevator's current direction of travel
	 * @return true if it's going up, false if it's going down
	 */
	public boolean isCurrentDirectionUp();
	
	/**
	 * Given a destination, the elevator will check if it's in its destination list and return true if it is and false if it isn't
	 * @param floorNum: the floor to be checked for in the destination list
	 * @return true or false, depending on if it's in the destination list or not
	 */
	public boolean isInDestinations(short floorNum);
	
	/**
	 * Adds a destination to the list of pending floors to arrive at.
	 * (used for JUnit testing)
	 * @param floorNum The floor to add to the list.
	 * @throws IllegalArgumentException Thrown when the requested floor is not between 1 and the number of floors in the building, the elevator is currently at the requested floor, or when the elevator has already passed the requested floor.
	 */
	public void addDestination(short floorNum) throws IllegalArgumentException;
	
	/**
	 * Checks whether a certain destination is "on the way."
	 * If the request is valid, returns true.
	 * @param destFloor The requested floor.
	 * @param directionIsUp The requested direction.
	 * @return true if the elevator can accept the request, or false when it cannot.
	 * @throws IllegalArgumentException 
	 */
	public boolean takeRequest(short destFloor, boolean directionIsUp) throws IllegalArgumentException;
	
	/**
	 * Shuts down the elevator.
	 * @throws IllegalArgumentException 
	 */
	public void shutdown() throws IllegalArgumentException;
	
	/**
	 * used to reset the elevator for JUnit testing
	 * @throws IllegalArgumentException 
	 */
	public void reset() throws IllegalArgumentException;
}
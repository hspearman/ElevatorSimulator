package elevator;

/**
 * This is a factory class for the elevator interface, so the building does not need to hard-code a specific implemenation.
 * 
 * @author Jeremy Ma
 * @author Hannah Spearman
 * 
 */
public class ElevatorFactory {

	/**
	 * Creates a new elevator with the specified arguments.
	 * @param inID The ID of the new elevator.
	 * @param inCapacity The maximum number of people the new elevator can carry.
	 * @param inSpeed The amount of time it takes for the new elevator to get from one floor to the next.
	 * @param inDoorSpeed The amount of time it takes for the new elevator to allow passengers to get on and off the elevator.
	 * @return An Elevator object with the specified arguments.
	 * @throws IllegalArgumentException 
	 */
	public static Elevator build( short inID, short inCapacity, short inSpeed, short inDoorSpeed, short inDefaultFloor ) throws IllegalArgumentException
	{
		return new ElevatorImpl( inID, inCapacity, inSpeed, inDoorSpeed, inDefaultFloor );
	}
}

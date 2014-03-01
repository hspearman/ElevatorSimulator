package elevatorController;

import building.Building;

/**
 * This class represents an elevator request, containing information such as the floor the request originated from, and the desired direction.
 * ERequests are processed by the EController class.
 * 
 * @author Jeremy Ma
 * @author Hannah Spearman
 * 
 */
public class ERequest {

	/**
	 * Holds the desired direction of travel for the request
	 */
	private boolean directionIsUp;
	
	/**
	 * the floor number that the ERequest was generated on
	 */
	private short floorNum;
	
	/**
	 * Creates an ERequest given the specified parameters. These ERequests created by the ECallBox and sent
	 * to the EController for processing
	 * @param inFloorNum the floor the request was made on
	 * @param inDirectionIsUp what direction is desired
	 */
	public ERequest(short inFloorNum, boolean inDirectionIsUp) throws IllegalArgumentException
	{
		if(inFloorNum < 0 || inFloorNum > Building.getNumberOfFloors())
		{
			throw new IllegalArgumentException("Floor request out of bounds, expected 0-"+Short.toString(Building.getNumberOfFloors())+", got "+Short.toString(inFloorNum));
		}
		floorNum = 		inFloorNum;
		directionIsUp =	inDirectionIsUp;
	}
	
	/**
	 * Creates an ERequest given the specified parameters. These ERequests created by the ECallBox and sent
	 * to the EController for processing. This constructor is temporarily used for the initial implementation
	 * so we can supply an exact elevator to request.
	 * @param inFloorNum the floor the request was made on
	 * @param inDirectionIsUp what direction is desired
	 * @param elevatorRequest the elevator number we want to serve us
	 */
	public ERequest(short inFloorNum, boolean inDirectionIsUp, short elevatorRequest)
	{
		if(inFloorNum < 0 || inFloorNum > Building.getNumberOfFloors())
		{
			throw new IllegalArgumentException("Floor request out of bounds, expected 0-"+Short.toString(Building.getNumberOfFloors())+", got "+Short.toString(inFloorNum));
		}
		floorNum = 		inFloorNum;
		directionIsUp = inDirectionIsUp;
	}
	
	// accessors
	/**
	 * Gives the desired direction of the request
	 * @return returns the request's direction of travel
	 */
	public boolean getDirection() { return directionIsUp; }
	/**
	 * Gives the floor number that this request originated from
	 * @return the request's floor number
	 */
	public short getFloorNum() { return floorNum; }
}

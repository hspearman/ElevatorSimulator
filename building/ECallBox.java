package building;
import elevatorController.EController;
import elevatorController.ERequest;

/**
 * This class represents a call-box, one of which is found on each floor.
 * It takes creates elevator requests upon being used and sends them to the EController.
 * 
 * @author Jeremy Ma
 * @author Hannah Spearman
 * 
 */
public class ECallBox
{
	/**
	 * Stores the floor number that this callbox resides on
	 */
	private short floorNum;
	
	/**
	 * Creates a new ECallBox with the specified arguments.
	 * @param inFloorNum:		the floor number that this call box exists on
	 */
	public ECallBox(short inFloorNum) throws IllegalArgumentException
	{
		if(inFloorNum < 0 || inFloorNum > Building.getNumberOfFloors())
		{
			throw new IllegalArgumentException("Floor out of bounds, expected 0-"+Short.toString(Building.getNumberOfFloors())+", got "+Short.toString(inFloorNum));
		}
		floorNum = inFloorNum;
	}
	
	/**
	* Creates an UP ERequest from the floor that the call-box was pressed,
	* and sends it to EController for processing.
	* @throws IllegalArgumentException 
	* @see EController.processCallBoxRequest()
	*/
	public void callForUp() throws IllegalArgumentException
	{
		EController.getInstance().processCallBoxRequest( new ERequest( floorNum, true ) );
	}
	
	/**
	* Creates a DOWN ERequest from the floor that the call-box was pressed,
	* and sends it to EController for processing.
	* @throws IllegalArgumentException 
	* @see EController.processCallBoxRequest()
	*/
	public void callForDown() throws IllegalArgumentException
	{
		EController.getInstance().processCallBoxRequest( new ERequest( floorNum, false ) );
	}
	
}

package elevatorController;

/**
 * This class functions as the interface for pending request processor algorithms
 * @author Hannah
 *
 */
public interface PendingRequestProcessor
{
	/**
	 * Given an elevator number and a floor number that it resides on, this algorithm will assign viable pending requests to the inputted idle elevator.
	 * Returns true if the elevator successfully processed pending requests, and false if there were none to serve.
	 * @param elevatorNum the number of the elevator that wants to process pending requests
	 * @param floorNum the floor number that the elevator resides on 
	 * @return true if it successfully served pending requests, and false otherwise
	 * @throws IllegalArgumentException
	 */
	public boolean processPendingRequests( short elevatorNum, short floorNum ) throws IllegalArgumentException;
}

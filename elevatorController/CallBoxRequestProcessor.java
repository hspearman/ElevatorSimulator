package elevatorController;

/**
 * This class creates the interface for a callbox request processor algorithm that can be used inside the EController
 * @author Hannah and Jeremy
 *
 */
public interface CallBoxRequestProcessor
{
	/**
	 * Attempts to process a received callbox request. If the request can't be served at this time, it's added to the pending requests list.
	 * @param request: the request to be served
	 * @throws IllegalArgumentException
	 */
	public void processCallBoxRequest( ERequest request ) throws IllegalArgumentException;
}

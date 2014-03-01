package elevatorController;
import java.lang.Math;

import elevator.Elevator;

/**
 * This class serves as the initial implementation of the pending request algorithm
 * @author Hannah Spearman
 * @author Jeremy Ma
 *
 */
public class PendingRequestProcessorImpl implements PendingRequestProcessor
{
	/** If there's pending requests, process the viable ones (by adding them to their destination list) and return true
	 * otherwise, return false
	 * @see elevatorController.PendingRequestsProcessor#processPendingRequests()
	 */
	public boolean processPendingRequests( short elevatorNum, short floorNum ) throws IllegalArgumentException
	{
		if ( false == EController.getInstance().pendingRequests.isEmpty() )
		{
			// first, find the farthest requested floor from the current elevator
			ERequest farthestRequest = EController.getInstance().pendingRequests.get( 0 );
			int farthest = 0;
			for ( ERequest request : EController.getInstance().pendingRequests )
			{
				int difference = Math.abs( ( int ) request.getFloorNum() - ( int ) floorNum );
				if ( difference > farthest )
				{
					farthest = difference;
					farthestRequest = request;
				}
			}
			
			// remove the processed request from the pending list
			EController.getInstance().pendingRequests.remove( farthestRequest );
			
			// correctly sets the elevator's state and direction, then adds the destination
			Elevator elevator = EController.getInstance().getElevator( elevatorNum );
			elevator.takeRequest( ( short ) farthest, true );
			elevator.addDestination( ( short ) farthest );
			
			// cycles through the requests to process any other viable ones
			for ( ERequest request : EController.getInstance().pendingRequests )
			{
				if ( elevator.takeRequest( request.getFloorNum(), request.getDirection() ) )
				{
					elevator.addDestination( request.getFloorNum() );
				}
			}
			
			return true;
		}
		
		/* we only return false if there's no pending requests to process
		 * (boolean is needed so elevators asking about pending requests will know
		 * either to moveToDestination() or go back to waiting)
		 */
		return false;
	}
}

/**
 * This class is the implementation of the second algorithm for processing ECallBox requests.
 * 
 * @author Hannah Spearman
 * @author Jeremy Ma
 * 
 */

package elevatorController;

public class CallBoxRequestProcessorSecondImpl implements CallBoxRequestProcessor
{
	/**
	 * Given an elevator request, this algorithm attempts to assign the request to a specific elevator.
	 * This algorithm differs in that it will try to assign the request to an IDLE elevator first.
	 * If there is no IDLE elevator, it will try to find an ACTIVE elevator to take the request.
	 * If there is neither an IDLE or ACTIVE elevator to take the request, it gets added to the pending requests lists.
	 * This algorithm's attempt at better elevator efficiency exists in getting all elevators to participate as fast as possible,
	 * instead of having only a few elevators do all the work and slowly making IDLE elevators join the workload
	 * @throws IllegalArgumentException if EController is given bad parameters
	 */
	public void processCallBoxRequest( ERequest request ) throws IllegalArgumentException
	{
		short dest = 			request.getFloorNum();
		boolean direction = 	request.getDirection();
		
		// checks if there's already an elevator headed to that floor
		for ( short i = 0; i < EController.getInstance().myElevators.size(); i++ )
		{
			if ( EController.getInstance().myElevators.get( i ).isInDestinations( dest ) )
			{
				if ( EController.getInstance().myElevators.get( i ).isCurrentDirectionUp() == direction )
				{
					// return since there's already an elevator on its way
					return;
				}
			}
		}
		
		// checks if there's an IDLE elevator that can take the request
		for ( short i = 0; i < EController.getInstance().myElevators.size(); i++ )
		{
			if ( EController.getInstance().myElevators.get( i ).getState() == 0
				 && EController.getInstance().myElevators.get( i ).takeRequest( dest, direction ) )
			{
				EController.getInstance().myElevators.get( i ).addDestination( dest );
				return;
			}
		}
		
		/* If there's not already an elevator on the way to this destination floor,
		 * and there's no IDLE elevator available, then check if there's an available
		 * ACTIVE elevator that can take the request.
		 */
		for ( short i = 0; i < EController.getInstance().myElevators.size(); i++ )
		{
			if ( EController.getInstance().myElevators.get( i ).takeRequest( dest, direction )
				 && ( EController.getInstance().myElevators.get( i ).getState() == 2
					  || EController.getInstance().myElevators.get( i ).getState() == 3 ) )
			{
				EController.getInstance().myElevators.get( i ).addDestination( dest );
				return;
			}	
		}
		
		// if none of the elevators could accept the request, add it to the pending list
		EController.getInstance().pendingRequests.add( request );
	}
}

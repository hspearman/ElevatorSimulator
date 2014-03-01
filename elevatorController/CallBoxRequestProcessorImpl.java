package elevatorController;

import building.Building;

/**
 * This class is the initial implementation of the call box processor algorithm
 * @author Jeremy Ma
 * @author Hannah Spearman
 *
 */
public class CallBoxRequestProcessorImpl implements CallBoxRequestProcessor
{
	/**
	 * Given an elevator request, this algorithm attempts to assign the request to a specific elevator.
	 * It first checks if there is an elevator already headed to that floor, and if there isn't, it finds the first elevator that can serve the request.
	 * If there are no available elevators, the request is added to the pending requests list
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
		
		// checks if there's an elevator heading in that direction that can take the request
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
		
		/* if there is no elevator on its way already
		 * and no active elevator can take the request,
		 * then find and send an IDLE elevator to this floor if possible
		 */
		for ( short i = 0; i < EController.getInstance().myElevators.size(); i++ )
		{
			if ( EController.getInstance().myElevators.get( i ).takeRequest( dest, direction ) 
				 && EController.getInstance().myElevators.get( i ).getState() == 0 )
			{
				EController.getInstance().myElevators.get( i ).addDestination( dest );
				return;
			}
		}
		
		// if none of the elevators could accept the request, add it to the pending list
		EController.getInstance().pendingRequests.add( request );
	}
}

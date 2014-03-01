/**
 * This class is a second algorithm for processing ECallBox requests.
 * 
 * @author Hannah Spearman
 * @author Jeremy Ma
 * 
 */

package elevatorController;

import java.util.ArrayList;

public class PendingRequestProcessorSecondImpl implements PendingRequestProcessor
{
	/** If there's pending requests, process the viable ones (by adding them to their destination list) and return true
	 * otherwise, return false
	 * This algorithm implementation differs in that it tests which category of requests has the higher
	 * density of pending requests, and travels in the direction of the higher density.
	 * Categories:	Above the elevator's floor and want to go DOWN
	 * 				Above the elevator's floor and want to go UP
	 * 				Below the elevator's floor and want to go DOWN
	 * 				Below the elevator's floor and want to go UP
	 * 
	 * @see elevatorControllerLogic.PendingRequestsProcessor#processPendingRequests()
	 */
	public boolean processPendingRequests( short elevatorNum, short floorNum ) throws IllegalArgumentException
	{		
		if ( false == EController.getInstance().pendingRequests.isEmpty() )
		{
			// first, calculate which direction has a higher density of requests
			int UPcount = 0;
			int DOWNcount = 0;
			for ( ERequest request : EController.getInstance().pendingRequests )
			{
				if ( floorNum < request.getFloorNum() )
				{
					UPcount++;
				}
				else if ( floorNum > request.getFloorNum() )
				{
					DOWNcount++;
				}
				// requests that are on the same floor as the elevator won't count towards either density
			}
			
			// if there's a higher density of UP requests, then add them
			if ( UPcount > DOWNcount)
			{
				for ( int i = 0; i<EController.getInstance().pendingRequests.size(); i++)
				{
					ERequest request = EController.getInstance().pendingRequests.get(i);
					if ( floorNum <= request.getFloorNum()
						 && EController.getInstance().getElevator( elevatorNum ).takeRequest( request.getFloorNum(), request.getDirection() ) )
					{
						EController.getInstance().getElevator( elevatorNum ).addDestination( request.getFloorNum() );
						synchronized(EController.getInstance().pendingRequests)
						{
							EController.getInstance().pendingRequests.remove( request );
						}
					}
				}
				return true;
			}
		
			// if there's a higher density of DOWN requests, then add them
			else
			{
				for ( int i = 0; i<EController.getInstance().pendingRequests.size(); i++)
				{
					ERequest request = EController.getInstance().pendingRequests.get(i);
					if ( floorNum >= request.getFloorNum()
						 && EController.getInstance().getElevator( elevatorNum ).takeRequest( request.getFloorNum(), request.getDirection() ) )
					{
						EController.getInstance().getElevator( elevatorNum ).addDestination( request.getFloorNum() );
						synchronized(EController.getInstance().pendingRequests)
						{
							EController.getInstance().pendingRequests.remove( request );
						}
					}
				}
				return true;
			}
		}
		
		/* we only return false if there's no pending requests to process
		 * (boolean is needed so elevators asking about pending requests will know
		 * either to moveToDestination() or go back to waiting)
		 */
		return false;
	}
}

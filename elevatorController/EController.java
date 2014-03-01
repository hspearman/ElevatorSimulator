package elevatorController;

import java.util.ArrayList;

import elevator.Elevator;
import elevator.ElevatorFactory;

/**
 * This is the elevator control class which processes elevator requests and directs the buildings elevators accordingly.
 * It acts as a facade for the elevators.
 * 
 * @author Jeremy Ma
 * @author Hannah Spearman
 * 
 */
public class EController implements CallBoxRequestProcessor, PendingRequestProcessor
{
	/**
	 * Holds the entirety of all elevator requests (for statistical use)
	 */
	ArrayList<ERequest> requests;
	/**
	 * Holds all pending requests so they may be served later
	 */
	ArrayList<ERequest> pendingRequests;
	/**
	 * Holds all elevators in the building that the EController maintains
	 */
	ArrayList<Elevator> myElevators;
	
	/**
	 * This holds the specific algorithm implementation for processing call box requests
	 */
	CallBoxRequestProcessor requestProcessor;
	/**
	 * This holds the specific algorithm implementation for processing pending requests
	 */
	PendingRequestProcessor pendingProcessor;
	
	private volatile static EController instance;
	
	/**
	 * Creates a new EController with the specified arguments. It also initializes internal data members that don't
	 * accept parameters (requests, myElevators) and starts running the elevator threads.
	 * @param numberOfElevators:	number of elevators that EController will maintain
	 */
	private EController( short numberOfElevators, short inCapacity, short inSpeed, short inDoorSpeed, short inDefaultFloor )
	{
		// array lists
		requests = 			new ArrayList<ERequest>();
		pendingRequests = 	new ArrayList<ERequest>();
		myElevators = 		new ArrayList<Elevator>();
		
		// delegates
		requestProcessor = 	CallBoxRequestProcessorFactory.build();
		pendingProcessor = 	PendingRequestProcessorFactory.build();
		
		// instantiates elevators and stores them
		for(short i = 0; i < numberOfElevators; i++)
		{
			myElevators.add( ElevatorFactory.build(	( short ) i, 
													( short ) inCapacity,
													( short ) inSpeed, 
													( short ) inDoorSpeed,
													( short ) inDefaultFloor ) );
		}
		
		// gets the elevators running
		for (Elevator e: myElevators)
		{
			new Thread( ( Runnable ) e).start();
		}
	}
	
	/**
	 * Creates a new EController with the specified arguments. It also initializes internal data members that don't
	 * accept parameters (requests, myElevators) and starts running the elevator threads. Alternative constructor that accepts an array list
	 * of default floors instead of a single short.
	 * @param numberOfElevators:	number of elevators that EController will maintain
	 */
	private EController( short numberOfElevators, short inCapacity, short inSpeed, short inDoorSpeed, ArrayList<Short> inDefaultFloors )
	{
		// array lists
		requests = 			new ArrayList<ERequest>();
		pendingRequests = 	new ArrayList<ERequest>();
		myElevators = 		new ArrayList<Elevator>();
		
		new CallBoxRequestProcessorFactory();
		// delegates
		requestProcessor = 	CallBoxRequestProcessorFactory.build();
		pendingProcessor = 	PendingRequestProcessorFactory.build();
		
		// instantiates elevators and stores them
		for(short i = 0; i < numberOfElevators; i++)
		{
			myElevators.add( ElevatorFactory.build(	( short ) i, 
													( short ) inCapacity,
													( short ) inSpeed, 
													( short ) inDoorSpeed,
													( short ) inDefaultFloors.get(i) ) );
		}
		
		// gets the elevators running
		for (Elevator e: myElevators)
		{
			new Thread((Runnable) e).start();
		}
	}
	
	/**
	* Returns the singleton instance of EController.
	* This version of getInstance doesn't need a parameter,
	* so it automatically forwards the call to getInstance(short numOfElevators) with a parameter of 0.
	* @return EController
	* @see EController getInstance(short numOfElevators)
	*/
	public static EController getInstance()
	{
		return EController.getInstance( ( short ) 0, ( short ) 0, ( short ) 0, ( short ) 0, ( short ) 0 );
	}
	
	/**
	* Returns the singleton instance of EController.
	* If the singleton EController hasn't been initialized yet, it initializes it with the parameter value.
	* Otherwise, it returns the pre-existing instance.
	* @param short numOfElevators
	* @return EController
	* @see private EController(numberOfElevators)
	*/
	public static EController getInstance( short numberOfElevators, short inCapacity, short inSpeed, short inDoorSpeed, short inDefaultFloor )
	{
		if(instance == null)
		{
			synchronized(EController.class)
			{
				if(instance == null)
				{
					instance = new EController( numberOfElevators, inCapacity, inSpeed, inDoorSpeed, inDefaultFloor );
				}
			}
		}
		return instance;
	}
	
	public static EController getInstance( short numberOfElevators, short inCapacity, short inSpeed, short inDoorSpeed, ArrayList<Short> inDefaultFloors )
	{
		if(instance == null)
		{
			synchronized(EController.class)
			{
				if(instance == null)
				{
					instance = new EController( numberOfElevators, inCapacity, inSpeed, inDoorSpeed, inDefaultFloors );
				}
			}
		}
		return instance;
	}
	
	/**
	* Receives an elevator request and adds the requested destination to a designated elevator (decided via an algorithm).
	* @param ERequest request:	the request to be processed
	*/
	// TODO: improve this method
	@Override
	public void processCallBoxRequest( ERequest request )
	{
		// sends it to the algorithm delegate
		requestProcessor.processCallBoxRequest( request );
	}
	
	/**
	 * Receives a specific elevator ID and a floor number, and processes and assigns requests to this elevator if they can process them.
	 * Returns true if the elevator successfully processed requests, and returns false if there were none.
	 */
	@Override
	public boolean processPendingRequests( short elevatorNum, short floorNum ) {
		// sends it to the algorithm delegate
		return pendingProcessor.processPendingRequests( elevatorNum, floorNum );
	}
	
	/**
	* Individually shuts down all running elevators.
	*/
	public void shutdownAllElevators()
	{
		for ( Elevator e: myElevators )
		{
			e.shutdown();
		}
	}
	
	/**
	 * Method used for JUnit testing (given an elevator ID, returns the specific elevator
	 * @param elevatorNum
	 * @return the elevator that was requested
	 */
	public Elevator getElevator( short elevatorNum )
	{
		return myElevators.get( elevatorNum );
	}
	
	/**
	 * Needed to reset the EController for an additional simulation (because singletons cannot be recreated)
	 */
	public void reset()
	{
		// array lists
		requests = 			new ArrayList<ERequest>();
		pendingRequests = 	new ArrayList<ERequest>();
		
		// delegates
		requestProcessor = 	CallBoxRequestProcessorFactory.build();
		pendingProcessor = 	PendingRequestProcessorFactory.build();
		
		// reset the elevators
		for ( Elevator e : myElevators)
		{
			e.reset();
		}
		
		// gets the elevators running
		for (Elevator e: myElevators)
		{
			new Thread( ( Runnable ) e ).start();
		}
	}
}

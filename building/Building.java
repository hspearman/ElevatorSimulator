package building;
import java.util.ArrayList;

import elevator.Elevator;
import elevatorController.EController;

/**
 * This class represents a building, which houses many floors and acts as a facade for said floors.
 * 
 * @author Jeremy Ma
 * @author Hannah Spearman
 * 
 */
public class Building {
	
	/**
	 * Stores the number of floors inside the building
	 */
	private static short numOfFloors;
	
	/**
	 * A list of the floors contained in this building.
	 */
	private ArrayList<Floor> myFloors;
	
	/**
	 * The elevator controller that is contained by this building.
	 */
	private EController myEController;
	
	/**
	 * The single instance of the Building object.
	 */
	private volatile static Building instance;
	
	/**
	 * Creates a new Building with the specified arguments. Building also constructs the EController in it's constructor,
	 * supporting it with the numberOfElevators parameter. It also instantiates all floors based on the number inputed.
	 * @param numberOfFloors:	number of floors the building will maintain
	 * @param numberOfElevators:	number of elevators that EController will maintain
	 * @param inCapacity:	The maximum number of people the new elevator can carry.
	 * @param inSpeed:	The amount of time it takes for the new elevator to get from one floor to the next/
	 * @param inDoorSpeed:	The amount of time it takes for the new elevator to open it doors, exchange people, and close its doors.
	 * @param inDefaultFloor:	The default floor of the new elevator.
	 * @throws IllegalArgumentException 
	 */
	private Building( short numberOfFloors, short numberOfElevators, short inCapacity, short inSpeed, short inDoorSpeed, short inDefaultFloor ) throws IllegalArgumentException
	{
		numOfFloors = numberOfFloors;
		
		// create floors
		myFloors = new ArrayList< Floor >();
		for( short i = 0; i < numberOfFloors; i++ )
		{
			Floor tempFloor = new Floor( ( short ) i );
			myFloors.add( tempFloor );
		}
		
		// create EController
		myEController = EController.getInstance( numberOfElevators, inCapacity, inSpeed, inDoorSpeed, inDefaultFloor );
	}
	
	/**
	 * Creates a new Building with the specified arguments. Building also constructs the EController in it's constructor,
	 * supporting it with the numberOfElevators parameter. It also instantiates all floors based on the number inputed.
	 * This version of the building constructor accepts a default floors array list instead of a universal default floor.
	 * @param numberOfFloors:	number of floors the building will maintain
	 * @param numberOfElevators:	number of elevators that EController will maintain
	 * @param inCapacity:	The maximum number of people the new elevator can carry.
	 * @param inSpeed:	The amount of time it takes for the new elevator to get from one floor to the next/
	 * @param inDoorSpeed:	The amount of time it takes for the new elevator to open it doors, exchange people, and close its doors.
	 * @param inDefaultFloor:	The default floor of the new elevator.
	 * @throws IllegalArgumentException 
	 */
	private Building( short numberOfFloors, short numberOfElevators, short inCapacity, short inSpeed, short inDoorSpeed, ArrayList<Short> inDefaultFloors ) throws IllegalArgumentException
	{	
		numOfFloors = numberOfFloors;
		
		// create floors
		myFloors = new ArrayList< Floor >();
		for( short i = 0; i < numberOfFloors; i++ )
		{
			Floor tempFloor = new Floor( ( short ) i );
			myFloors.add( tempFloor );
		}
		
		// create EController
		myEController = EController.getInstance( numberOfElevators, inCapacity, inSpeed, inDoorSpeed, inDefaultFloors );
	}
	
	/**
	* Returns the singleton instance of Building.
	* This version of getInstance doesn't need a parameter,
	* so it automatically forwards the call to getInstance(short numOfElevators) with a parameter of 0.
	* @return Building
	* @see Building getInstance(short numberOfFloors, short numberOfElevators)
	*/
	public static Building getInstance()
	{
		return Building.getInstance( ( short ) 0, ( short ) 0, ( short ) 0, ( short ) 0, ( short ) 0, ( short ) 0 );
	}
	
	/**
	 * Returns the singleton instance of Building.
	 * If the singleton Building hasn't been initialized yet, it initializes it with the parameter value.
	 * Otherwise, it returns the pre-existing instance.
	 * @param numberOfFloors:	number of floors the building will maintain
	 * @param numberOfElevators:	number of elevators that EController will maintain
	 * @param inCapacity:	The maximum number of people the new elevator can carry.
	 * @param inSpeed:	The amount of time it takes for the new elevator to get from one floor to the next/
	 * @param inDoorSpeed:	The amount of time it takes for the new elevator to open it doors, exchange people, and close its doors.
	 * @param inDefaultFloor:	The default floor of the new elevator.
	 * @throws IllegalArgumentException 
	 */
	public static Building getInstance( short numberOfFloors, short numberOfElevators, short inCapacity, short inSpeed, short inDoorSpeed, short inDefaultFloor ) throws IllegalArgumentException
	{
		if(instance == null)
		{
			synchronized(Building.class)
			{
				if(instance == null)
				{
					instance = new Building( numberOfFloors, numberOfElevators, inCapacity, inSpeed, inDoorSpeed, inDefaultFloor );
				}
			}
		}
		return instance;
	}
	
	/**
	 * Returns the singleton instance of Building.
	 * If the singleton Building hasn't been initialized yet, it initializes it with the parameter value.
	 * This version accepts a default floor array list instead of a universal default floor.
	 * Otherwise, it returns the pre-existing instance.
	 * @param numberOfFloors:	number of floors the building will maintain
	 * @param numberOfElevators:	number of elevators that EController will maintain
	 * @param inCapacity:	The maximum number of people the new elevator can carry.
	 * @param inSpeed:	The amount of time it takes for the new elevator to get from one floor to the next/
	 * @param inDoorSpeed:	The amount of time it takes for the new elevator to open it doors, exchange people, and close its doors.
	 * @param inDefaultFloor:	The default floor of the new elevator.
	 * @throws IllegalArgumentException 
	 */
	public static Building getInstance( short numberOfFloors, short numberOfElevators, short inCapacity, short inSpeed, short inDoorSpeed, ArrayList<Short> inDefaultFloor ) throws IllegalArgumentException
	{
		if(instance == null)
		{
			synchronized(Building.class)
			{
				if(instance == null)
				{
					instance = new Building( numberOfFloors, numberOfElevators, inCapacity, inSpeed, inDoorSpeed, inDefaultFloor );
				}
			}
		}
		return instance;
	}

	
	/**
	 * Sends a person to a certain floor. Building is the facade for floor, so this method is a middle-man
	 * between the elevator and the floor.
	 * @param floorNum The floor to send the person to.
	 * @param person The person to send.
	 */
	public void addPersonToFloor( short floorNum, short personID )
	{
		myFloors.get( floorNum ).addPerson( personID );
	}
	
	/**
	 * Sends a person to a certain elevator. Building is the facade for floor, so this method is a middle-man
	 * between the elevator and the floor. Removes the personID from the floor to avoid duplication when adding to the elevator.
	 * @param floorNum The floor to get the person from.
	 * @param personID The ID of the person
	 */
	public void sendPersonToElevator( short floorNum, short personID )
	{
		myFloors.get( floorNum ).sendPerson( personID );
	}
	
	/**
	 * Retrieves another person to get on the elevator; only picks up people that are going in the same way as the elevator is.
	 * Building is the facade for floor, so this method is a middle-man between the elevator and the floor.
	 * @param floorNum The floor to get passengers from.
	 * @param directionIsUp The direction that the elevator is going in.
	 * @return
	 */
	public short getNextPassengerFromFloor( short floorNum, boolean directionIsUp, boolean isDestinationsEmpty )
	{
		return myFloors.get( floorNum ).getNextPassenger( directionIsUp, isDestinationsEmpty );
	}
	
	/**
	 * Returns the number of floors inside the building.
	 * Number of floors must be statically accessed. Otherwise, it may be null if certain set methods within the elevator's
	 * constructor call this method because the Building constructor hasn't finished yet.
	 * @return The number of floors the building has
	 */
	public static short getNumberOfFloors()
	{
		return numOfFloors;
	}
	
	/**
	 * Given an elevator ID, this method will return an elevator from the EController
	 * @param elevatorNum
	 * @return the elevator that was requested
	 */
	public Elevator getElevatorFromEController(short elevatorNum)
	{
		return myEController.getElevator( elevatorNum );
	}
	
	/**
	 * Forwards a call box button press to the floor which generates an elevator requests and sends it to the EController (since building is a facade for the floors)
	 * @param goingUp: The desired direction of travel
	 * @throws IllegalArgumentException
	 */
	public void pressCallBox( short startFloor, boolean goingUp ) throws IllegalArgumentException
	{
		// TODO: universalize how floors are stored (as 0+ or 1+?)
		this.myFloors.get( startFloor ).pressCallButton( goingUp );
	}
	
	/**
	 * This resets all elements of the simulation to default state.
	 * This allows the simulation to be run again
	 * (needed because singletons cannot be recreated during the program)
	 */
	public void resetSimulation()
	{
		myFloors = new ArrayList< Floor >();
		for( short i = 0; i < numOfFloors; i++ )
		{
			Floor tempFloor = new Floor( ( short ) i );
			myFloors.add( tempFloor );
		}
		PersonFacade.getInstance().reset();
		EController.getInstance().reset();
	}
}

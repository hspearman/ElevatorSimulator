package elevator;

import java.util.ArrayList;
import java.util.Collections;

import elevatorController.EController;

import building.Building;
import building.PersonFacade;
import java.util.Calendar;

/**
 * This is an implementation class for the Elevator interface. 
 * It performs the default operations as deemed by the Elevator interface,
 * as well as having additional private methods to expand internal functionality.
 * 
 * @author Jeremy Ma
 * @author Hannah Spearman
 * 
 */
public class ElevatorImpl implements Elevator, Runnable
{
	/**
	 * The identifier of the elevator.
	 */
	private short elevatorID;
	
	/**
	 * The maximum number of people this elevator can carry.
	 */
	private short capacity;
	
	/**
	 * Indicates what the elevator is doing.
	 * 0 = Idle
	 * 1 = Active, but stopped
	 * 2 = Active, and moving
	 */
	private short state; 
	
	/**
	 * The amount of time, in milliseconds, it takes for the elevator to get from one floor to the next.
	 */
	private short timePerFloor; 		// milliseconds
	
	/**
	 * The amount of time, in milliseconds, it takes for the elevator to open the doors, allow passengers to get on and off, and close the doors.
	 */
	private short doorOperationTime; 	// milliseconds
	
	/**
	 * The floor that the elevator goes to when it has been idle for a certain amount of time.
	 */
	private short defaultFloor;
	
	/**
	 * The floor that the elevator is currently on.
	 */
	private short currentFloor;
	
	/**
	 * Indicates the direction the elevator is moving in.
	 * true = Up
	 * false = Down
	 */
	private boolean currentDirectionIsUp;
	
	/**
	 * Holds all the pending destinations for this elevator.
	 */
	private boolean running;
	
	/**
	 * Shows which people are on this elevator.
	 */
	private ArrayList<Short> destinations; // stores the assigned destinations
	
	/**
	 * The building that this elevator is in.
	 */
	private ArrayList<Short> carrying;
	
	/**
	 * Determines whether or not to print output
	 */
	private boolean DEBUG = true;

	/**
	 * Creates a new ElevatorImpl with the specified arguments.
	 * @param ID The ID of the new elevator.
	 * @param inCapacity The maximum number of people the new elevator can carry.
	 * @param inSpeed The amount of time it takes for the new elevator to get from one floor to the next/
	 * @param inDoorSpeed The amount of time it takes for the new elevator to open it doors, exchange people, and close its doors.
	 * @param inDefaultFloor The default floor of the new elevator.
	 * @throws IllegalArgumentException if one of the set methods fail
	 */
	public ElevatorImpl( short ID, short inCapacity, short inSpeed, short inDoorSpeed, short inDefaultFloor ) throws IllegalArgumentException
	{
		// short data members
		setID( ID );
		setCapacity( inCapacity );
		setState( ( short ) 0 );
		setTimePerFloor( inSpeed );
		setDoorOperationTime( inDoorSpeed );
		setDefaultFloor( inDefaultFloor );
		setCurrentFloor( inDefaultFloor );
		
		// array list data members
		destinations =			new ArrayList<Short>();
		carrying =				new ArrayList<Short>();
		
		// boolean data members
		setCurrentDirectionUp( true );
		setRunning( true );
	}
	
	/**
	 * Indicates whether there are any pending destinations.
	 * (used for JUnit testing)
	 * @return true if it is empty, false if there are pending destinations.
	 */
	public boolean isDestinationsEmpty()
	{
		return destinations.isEmpty();
	}
	
	/**
	 * Indicates whether the elevator is operational.
	 * (used for JUnit testing)
	 * @return true if the elevator is operational, otherwise false.
	 */
	public boolean isInDestinations(short floorNum)
	{
		return destinations.contains( floorNum );
	}
	
	/**
	 * Indicates which direction the elevator is moving in.
	 * @return true = up, false = down.
	 */
	public boolean isCurrentDirectionUp() { return currentDirectionIsUp; }
	
	/**
	 * Indicates whether the elevator is operational.
	 * (used for JUnit testing)
	 * @return true if the elevator is operational, otherwise false.
	 */
	public boolean isRunning() { return running; }
	
	/**
	 * Indicates what state the elevator is in.
	 * (used for JUnit testing)
	 * @return 0 = Idle, 1 = Active, but stopped, 2 = Active, and moving, 3 = Going to the default floor.
	 */
	public short getState() { return state; }
	
	/**
	 * Indicates what floor the elevator is currently on
	 * @return 1 to max floor of building.
	 */
	public short getCurrentFloor() { return currentFloor; }
	
	/**
	 * Returns the ID of the elevator
	 * @return elevator ID number
	 */
	public short getID() { return elevatorID; }
	
	/**
	 * Returns the max capacity of the elevator
	 * @return the max capacity
	 */
	public short getCapacity() { return capacity; }
	
	/**
	 * Returns the time taken to pass a floor
	 * @return the time per floor in milliseconds
	 */
	public short getTimePerFloor() { return timePerFloor; }
	
	/**
	 * Returns the time taken to open the elevator doors
	 * @return the the door operation time in milliseconds
	 */
	public short getDoorOperationTime() { return doorOperationTime; }
	
	/**
	 * Returns the default floor of the elevator
	 * @return the default floor
	 */
	public short getDefaultFloor() { return defaultFloor; }
	
	/**
	 * Sets the current direction of the elevator (true == up, false == down)
	 */
	private void setCurrentDirectionUp( boolean inDirection ) { currentDirectionIsUp = inDirection; }
	
	/**
	 * Sets the running status of the elevator (true == running, false == shutoff )
	 */
	private void setRunning( boolean inRunning ) { running = inRunning; }
	
	/**
	 * Sets the state of the elevator between 0 and 2
	 * @throws IllegalArgumentException
	 */
	private void setState( short inState ) throws IllegalArgumentException
	{ 
		if ( -1 < inState && inState < 3)
		{
			state = inState;
		}
		
		else throw new IllegalArgumentException( "The state you tried to set is out of bounds (must be in the range of 0-2).\n" );
	}
	
	/**
	 * Sets the current floor of the elevator between 0 and the max floor of the building
	 * @throws IllegalArgumentException
	 */
	private void setCurrentFloor( short inFloor ) throws IllegalArgumentException
	{ 
		if ( -1 < inFloor || inFloor > ( int ) ( Building.getNumberOfFloors() - 1 ) )
		{
			currentFloor = inFloor; 
		}
		
		else throw new IllegalArgumentException( "The current floor you tried to set is out of bounds (must be in the range of [0-maxFloor])" );	
	}
	
	/**
	 * Sets the ID of the elevator (must be non-negative)
	 * @throws IllegalArgumentException
	 */
	private void setID( short inID ) throws IllegalArgumentException
	{
		if ( inID > -1 )
		{
			elevatorID = inID;
		}
		
		else throw new IllegalArgumentException( "The ID you tried to set is out of bounds (must be a non-negative number).\n" );
	}
	
	/**
	 * Sets the capacity of the elevator (must be non-negative)
	 * @throws IllegalArgumentException
	 */
	private void setCapacity( short inCapacity ) throws IllegalArgumentException
	{
		if ( inCapacity > 0 )
		{
			capacity = inCapacity;
		}
		
		else throw new IllegalArgumentException( "The capacity you tried to set is out of bounds (must be greater than 0).\n" );
	}
	
	/**
	 * Sets the time spent for the elevator to pass a floor (must be non-negative)
	 * @throws IllegalArgumentException
	 */
	private void setTimePerFloor(short inTime) throws IllegalArgumentException
	{
		if ( inTime > -1 )
		{
			timePerFloor = inTime;
		}
		
		else throw new IllegalArgumentException( "The time-per-floor you tried to set is out of bounds (must be a non-negative number)\n" );
	}
	
	/**
	 * Sets the time the elevator keeps it's doors open when stopped at a floor (must be non-negative)
	 * @throws IllegalArgumentException
	 */
	private void setDoorOperationTime( short inTime ) throws IllegalArgumentException
	{
		if ( inTime > -1 )
		{
			doorOperationTime = inTime;
		}
		
		else throw new IllegalArgumentException( "The door operation time you tried to set is out of bounds (must be a non-negative number)\n" );
	}
	
	/**
	 * Sets the default floor of the elevator (must be between 0 and the max floor of the building)
	 * @throws IllegalArgumentException
	 * 
	 */
	private void setDefaultFloor(short inFloor) throws IllegalArgumentException
	{
		if ( -1 < inFloor && inFloor < ( int ) ( Building.getNumberOfFloors() - 1 ) )
		{
			defaultFloor = inFloor;
		}
		
		else throw new IllegalArgumentException( "The default floor you tried to set is out of bounds (must be between [0-maxFloor])\n" );
	}
	
	/**
	 * This method overrides the Runnable interface's run method in order to support multi-threading.
	 * This run method will make the elevator wait until it's given a destination, and then it will move to those pending destinations.
	 * If the wait time passes a certain thresh-hold, then the elevator will return to its default floor (if it isn't there already).
	 */
	@Override
	public void run() {
		while (running)
		{
			short currFloor = getCurrentFloor();
			short defFloor = getDefaultFloor();
			
			try {
				
				long waitTime = System.currentTimeMillis();
				
				synchronized( destinations )
				{
					destinations.wait(10000); // initially wait until there's something to do
				}
			  
				/* Thread wakes up!
				 * 
				 * If we have a destination:
				 * 		- Move that destination
				 * 
				 * Else if there's pending requests:
				 * 		- Process them and add viable requests to your destinations list
				 * 
				 * Else if we've waited long enough:
				 * 		- Check if we're at the default floor, and if we're not, move to the default floor
				 * 
				 * Else:
				 * 		- Go back to waiting
				 */
				if ( destinations.isEmpty() )
				{
					long timeWaited = System.currentTimeMillis() - waitTime;
					
					if (timeWaited >= 10000)
					{
						// if we're at the default floor, change to idle
						if( currFloor == defFloor)
						{
							setState( ( short ) 0 );
						}
						
						// else if we're not at the default floor, go there
						else
						{
							if (defFloor > currFloor)
							{
								setCurrentDirectionUp( true );
							}
							
							else
							{
								setCurrentDirectionUp( false );
							}
							
							synchronized( destinations )
							{
								addDestination(defFloor);
							}
							
							if (DEBUG)
							{
								String string = String.format( "%2d:%2d:%2d   ", 	Calendar.getInstance().get( Calendar.HOUR ), 
																					Calendar.getInstance().get( Calendar.MINUTE ),
																					Calendar.getInstance().get( Calendar.SECOND ) );
								string += String.format( "Elevator %d returning to default floor %d...\n", elevatorID + 1, defaultFloor + 1 );
								System.out.print( string );
							}
							
							moveToDestination();
						}
						
						continue; // keep waiting
					}
				}
				
				else
				{
					// else, move to all current destinations
					if (DEBUG)
					{
						String string = String.format( "%2d:%2d:%2d   ", 	Calendar.getInstance().get( Calendar.HOUR ), 
																			Calendar.getInstance().get( Calendar.MINUTE ),
																			Calendar.getInstance().get( Calendar.SECOND ) );
						string += String.format( "Elevator %d changed from IDLE to ", elevatorID + 1 );
						
						if ( isCurrentDirectionUp() == true )
						{
							string += "UP.";
						}
						else
						{
							string += "DOWN.";
						}
						
						System.out.println( string );
					}
					
					moveToDestination();
					
					/* before returning to sleep, see if you can serve any pending requests
					 * and keep serving until there are none
					 */
					while ( EController.getInstance().processPendingRequests( getID(), getCurrentFloor() ) )
					{
						moveToDestination();
						
						if (DEBUG)
						{
							String string = String.format( "%2d:%2d:%2d   ", 	Calendar.getInstance().get( Calendar.HOUR ), 
																				Calendar.getInstance().get( Calendar.MINUTE ),
																				Calendar.getInstance().get( Calendar.SECOND ) );
							string += String.format( "Elevator %d recieved destination.\n", elevatorID + 1 );
							System.out.print( string );
						}
					}
				}
			} catch (Exception e) {}
		}		
	}

	/**
	* Adds a destination to an elevator's destination list.
	* The method first checks if the destination is valid (i.e. checks that the floor is in the current direction of travel and that the elevator hasn't passed it yet).
	* If the destination passes these checks, the destination is added to the destination list and the list is then sorted accordingly.
	* @param floorNum number of the destination floor
	* @throws IllegalArgumentException if a given floor is out of range or not in the direction of travel
	*/
	@Override
	public void addDestination( short floorNum ) throws IllegalArgumentException
	{
		short currFloor = getCurrentFloor();
		boolean goingUp = isCurrentDirectionUp();
		
		// if we're already headed to the requested destination, don't add it again
		if ( destinations.contains( floorNum ) )
		{
			return;
		}
		
		else if ( getState() == 0 )
		{
			if ( floorNum > currFloor )
			{
				setCurrentDirectionUp( true );
			}
			
			else
			{
				setCurrentDirectionUp( false );
			} 
		}
		
		// if outside of range, error
		if ( floorNum < 0 || floorNum > Building.getNumberOfFloors() - 1 )
		{
			throw new IllegalArgumentException("Expected a floor number between 1 and " + Building.getNumberOfFloors() + 1 + ", got " + floorNum + 1 + ".");
		}
		
		/* If it is NOT going in the same direction, 
		 * then do nothing
		 */
		if ( goingUp == false && floorNum > currFloor )
		{
			throw new IllegalArgumentException( "Elevator is going down and is at a floor below the requested floor, and cannot serve this floor at this time." );
		}
		else if ( goingUp == true && floorNum < currFloor )
		{
			throw new IllegalArgumentException( "Elevator is going up and is at a floor above the requested floor, and cannot serve this floor at this time." ); 
		}

		synchronized( destinations )
		{
			// set state to travelling
			setState( ( short ) 2 );
			destinations.add( floorNum );
		
			// after receiving a destination, re-order the destinations correctly
			if ( currentDirectionIsUp ) 
			{
				Collections.sort( destinations );
			}
			else
			{
				Collections.sort( destinations );
				Collections.reverse( destinations );
			}
		
				destinations.notifyAll();
		}
			
		return;
	}

	/**
	* Opens the doors and performs needed duties when an elevator reaches a floor.
	* More specifically, this method:
	* 	- Opens elevator doors for designated operation time
	* 	- Keeps elevator doors open for designated operation time
	* 	- Removes people from the elevator (who want to get off) and adds them to the floor
	* 	- Adds people from the floor (who want to get on) and adds them to the elevator
	* 
	* @throws RuntimeException Thrown when the elevator is currently moving and cannot open the doors.
	*/
	private void openDoor() throws RuntimeException {
		
		short currFloor = getCurrentFloor();
		short waitTime = getDoorOperationTime();
		boolean goingUp = isCurrentDirectionUp();
		
		// if active and traveling, do nothing (ERROR)
		if ( getState() == 2 )
		{
			throw new RuntimeException( "Elevator is currently in motion and cannot open doors." );
		}
		
		// otherwise, set state as active and stopped
		else
		{
			setState( ( short ) 1 );
		}
		
		if ( DEBUG )
		{ 
			String string = String.format( "%2d:%2d:%2d   ", 	Calendar.getInstance().get( Calendar.HOUR ), 
																Calendar.getInstance().get( Calendar.MINUTE ),
																Calendar.getInstance().get( Calendar.SECOND ) );
			string += String.format( "Elevator %d's doors opened.\n", elevatorID + 1 );
			System.out.print( string );
		}
		
		// keep the doors open for a set amount of time
		try {
			Thread.sleep( waitTime );
		} catch ( InterruptedException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/* when stopped at a floor:
		 * 		-	remove people who want to get off the elevator
		 *		 	and add them to the current floor
		 *		-	remove people who want to get on the elevator
		 *			and add them to current elevator
		 */
		
		/* ----- START people bailing out ----- */
		for( int i = 0; i < carrying.size(); i++ )
		{
			short personID = carrying.get( i );
			
			// if this is their destination, remove them from the elevator and add them to the floor
			if ( PersonFacade.getInstance().getDestinationFloor( personID ) == currFloor )
			{
				Building.getInstance().addPersonToFloor( currFloor, personID );
				
				// this prevents persons from getting on the elevator again after they've reached their destination
				PersonFacade.getInstance().setHasReachedDestination( personID, true );
				PersonFacade.getInstance().stopRideTimer( personID );
				
				carrying.remove( i );
			}
		}
		/* ----- END people bailing out ----- */
			
		/* ----- START people getting on ----- */
		short passengerID = Building.getInstance().getNextPassengerFromFloor( currFloor, goingUp, isDestinationsEmpty() );
		while( passengerID != -1 )
		{
			// removes the person from the floor so they may be added to the elevator without duplication
			Building.getInstance().sendPersonToElevator( currFloor, passengerID );
			short destFloor = PersonFacade.getInstance().getDestinationFloor( passengerID );
			
			/* if we have no more destinations after reaching this current one, this ensures
			 * that we set our elevator's direction correctly (since it can legally go either way)
			 */
			if ( isDestinationsEmpty() )
			{
				// Only print out the button that the first person who enters the elevator presses (to avoid clutter)
				String string = String.format( "%2d:%2d:%2d   ", 	Calendar.getInstance().get( Calendar.HOUR ), 
																	Calendar.getInstance().get( Calendar.MINUTE ),
																	Calendar.getInstance().get( Calendar.SECOND ) );
				string += String.format( 	"Person %d enters elevator %d and presses button %d\n", 
											passengerID + 1, this.getID() + 1, destFloor + 1 );
				System.out.print( string );
				
				if ( destFloor > currFloor )
				{
					setCurrentDirectionUp( true );
				}
				
				else
				{
					setCurrentDirectionUp( false );
				} 
			}
			
			// if the elevator isn't full, add the passenger and their destination
			if ( carrying.size() < capacity )
			{
				addDestination( destFloor );
				carrying.add( passengerID );
				PersonFacade.getInstance().stopWaitTimer( passengerID );
				PersonFacade.getInstance().startRideTimer( passengerID );
			}
			
			// if we're at max capacity but there's still people who want to get on the elevator, re-queue a request from this floor
			else
			{
				Building.getInstance().pressCallBox( currFloor, goingUp );
				break;
			}
			
			passengerID = Building.getInstance().getNextPassengerFromFloor( currFloor, goingUp, isDestinationsEmpty() );
		}
		/* ----- END people getting on ----- */
		
		// if no one got on the elevator and added a destination, switch to IDLE
		if ( isDestinationsEmpty() )
		{
			setState( ( short ) 0 );
		}
		
		return;
	}


	/**
	 * Moves the elevator to all pending destinations until the destination list is empty.
	 * @throws RuntimeException if there are no destinations to move to
	 */
	private void moveToDestination() throws RuntimeException
	{
		short currFloor = getCurrentFloor();
		
		if ( destinations.size() == 0 )
		{
			throw new RuntimeException("No destinations to go to.");
		}
		
		while( destinations.size() != 0 )
		{	
			/* while the elevator has not reached its next destination,
			 * travel past a floor
			 */
			while( destinations.size() != 0 && currFloor != destinations.get( 0 ) )
			{
				// active and traveling
				setState( ( short ) 2 );
				
				if ( DEBUG )
				{
					String string = String.format( "%2d:%2d:%2d   ", 	Calendar.getInstance().get( Calendar.HOUR ), 
																		Calendar.getInstance().get( Calendar.MINUTE ),
																		Calendar.getInstance().get( Calendar.SECOND ) );
					string += String.format( "Elevator %d passing floor %d... ", elevatorID + 1, currentFloor + 1 );
					
					// print out the elevator's direction
					if ( currentDirectionIsUp )
					{
						string += String.format( "Moving UP.\n" );
					}
					else
					{
						string += String.format( "Moving DOWN.\n" );
					}
					
					// print out the destination list if it's non-empty
					string += String.format("\t\tDestination list: [");
					
					for ( int i = 0; i < destinations.size(); i++ )
					{
						if ( i != destinations.size() - 1 )
						{
							string += String.format( "%d, ", destinations.get( i ) + 1 );
						}
						else
						{
							string += String.format( "%d", destinations.get( i ) + 1 );
						}
					}
					
					string += String.format( "]\n");
			
					// print out the carrying list if it's non-empty
					string += String.format( "\t\tCarrying list: [");
					
					for ( int i = 0; i < carrying.size(); i++ )
					{
						if ( i != carrying.size() - 1 )
						{
							string += String.format( "%d, ", carrying.get( i ) + 1 );
						}
						else
						{
							string += String.format( "%d", carrying.get( i ) + 1 );
						}
					}

					string += String.format( "]" );
					
					System.out.println( string );		
				}
				
				/* record that the elevator has passed one floor 
				 * (increment or decrement depends on elevator's direction-of-travel
				 */
				if ( currentDirectionIsUp )
				{
					setCurrentFloor( ( short ) ( currFloor + 1 ) );
				}
				else
				{
					setCurrentFloor( ( short ) ( currFloor - 1 ) );
				}
				
				currFloor = getCurrentFloor();
				
				// sleep until you've passed one floor
				try {
					Thread.sleep( timePerFloor );
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
			if ( DEBUG )
			{
				String string = String.format( "%2d:%2d:%2d   ", 	Calendar.getInstance().get( Calendar.HOUR ), 
																	Calendar.getInstance().get( Calendar.MINUTE ),
																	Calendar.getInstance().get( Calendar.SECOND ) );
				string += String.format( "Elevator %d arrived at floor %d.\n", elevatorID + 1, currentFloor + 1 );
				System.out.print( string );
			}
			
			// remove reached destination
			if ( destinations.isEmpty() == false )
			{
				destinations.remove( 0 );
			}
			
			// flag elevator as active and stopped and open the doors
			setState( ( short ) 1 );
			openDoor();
		}
		
		/* if we're at the bottom floor or the top floor,
		 * set our elevator's direction accordingly
		 * (so we don't go down from the first floor or up from the top floor)
		 */
		if ( getCurrentFloor() == 0 )
		{
			setCurrentDirectionUp( true );
		}
		else if ( getCurrentFloor() == Building.getInstance().getNumberOfFloors() - 1 )
		{
			setCurrentDirectionUp( false );
		}
		
		 // set back to idle
		setState( ( short ) 0 );
		
		return;
	}
	
	/**
	* Accepts a destination floor and a direction and checks that the request can be successfully accepted.
	* Specifically, it checks that the elevator is heading in the direction of the requested floor, and that it hasn't passed it yet.
	* If these checks succeed, the method returns true.
	* @param destFloor destination floor
	* @param directionIsUp the elevator's current direction
	* @return boolean whether the request was accepted successfully or not
	*/
	public boolean takeRequest( short destFloor, boolean directionIsUp ) throws IllegalArgumentException
	{	
		short currState = getState();
		short currFloor = getCurrentFloor();
		boolean goingUp = isCurrentDirectionUp();
		
		// if floor out of range
		if ( destFloor < 0 || destFloor > Building.getInstance().getNumberOfFloors() )
		{
			throw new IllegalArgumentException("Floor is out of bounds.");
		}
		
		// if idle, then direction doesn't matter
		else if ( currState == 0 )
		{
			if ( destFloor > currFloor )
			{
				setCurrentDirectionUp( true );
			}
			
			else
			{
				setCurrentDirectionUp( false );
			} 
			
			setState( ( short ) 2 );
			return true;
		}
		
		// if the elevator's going up and it hasn't passed the destination floor yet
		else if ( goingUp && directionIsUp && destFloor > currFloor )
		{
			return true;
		}
		
		// if the elevator's going down and it hasn't passed the destination floor yet
		else if ( !goingUp && !directionIsUp && destFloor < currFloor )
		{
			return true;
		}
		
		return false;
	}
	
	/**
	* Shuts down all the elevators by setting their running state to false and emptying their destinations
	* @throws IllegalArgumentException 
	*/
	public void shutdown() throws IllegalArgumentException
	{
		setRunning( false );
		setState( ( short ) 0);
		
		synchronized( destinations )
		{
			destinations.notifyAll();
		}
	}
	
	/**
	* Used by JUnit testing to reset the elevator to default state.
	* Also used by EController to reset the elevators for another simulation run.
	* @throws IllegalArgumentException 
	*/
	public void reset() throws IllegalArgumentException
	{
		destinations.clear();
		setCurrentFloor( this.getDefaultFloor() );
		setState( ( short ) 0 );
		setCurrentDirectionUp( true );
		setRunning( true );
	}
}

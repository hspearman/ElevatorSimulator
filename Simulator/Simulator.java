package Simulator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.Scanner;

import building.Building;
import building.PersonDTO;
import building.PersonFacade;

import elevatorController.EController;

/**
 * This class makes a new building with elevators, and directs those elevators via elevator requests and outputs the narrative.
 * 
 * @author Jeremy Ma
 * @author Hannah Spearman
 * 
 */
public class Simulator
{
	/**
	 * File that the program's output will be written to.
	 */
	private static File output;
	/**
	 * Enables and disables whether or not to print simulator's activity
	 */
	private static boolean DEBUG = true;
	/**
	 * Stores the duration of the simulation (how long it's supposed to run)
	 */
	private static int duration;
	
	/**
	 * Stores the time-scale of the simulation (modifies how fast the elevator runs through its duration)
	 */
	private static int timeScale;
	/**
	 * Stores the number of persons generated per minute that will generate elevator requests
	 */
	private static int peoplePerMin;
	/**
	 * Stores the number of floors in the building
	 */
	private static int numOfFloors;
	
	/**
	 * Stores the percentages of how likely each starting floor is for a generated person
	 */
	private static ArrayList<Short> startPercentages;
	/**
	 * Stores the ranges of the starting floor probabilities (used in the generateFloor method)
	 */
	private static ArrayList<Short> cumulativeStartPercentages;
	
	/**
	 * Stores the percentages of how likely each destination floor is for a generated person
	 */
	private static ArrayList<Short> destPercentages;
	/**
	 * Stores the ranges of the destination floor probabilities (used in the generateFloor method)
	 */
	private static ArrayList<Short> cumulativeDestPercentages;
	/**
	 * Stores the Person DTO's generated from PersonFacade (used for generating statistics)
	 */
	private static ArrayList<PersonDTO> peopleStats;
	/**
	 * A two dimensional array list that stores an array of Person DTO's per floor (used for generating statistics).
	 * Each index of the array list represents a floor (ex. index 7 stores all of floor 7's DTO's)
	 */
	private static ArrayList<ArrayList<PersonDTO>> floorStats;
	/**
	 * Stores the singleton instance of the Building
	 */
	private static Building myBuilding;
	/**
	 * Stores the singleton instance of the EController
	 */
	private static EController myEController;
	
	/**
	 * This class makes a new building with elevators, and directs those elevators via elevator requests and outputs the narrative.
	 * 
	 * @author Jeremy Ma
	 * @author Hannah Spearman
	 * 
	 */
	public static void main( String args[] )
	{
		// reads in input data from input file for the simulation
		readSimInput( args[0] );
		
		// run with the initial algorithm implementations
		runSimulation();
		
		// wait for the threads to finish
		try {
			Thread.sleep( 5000 );
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// generate initial algorithm implementations output
		generateStatistics();
		
		Building.getInstance().resetSimulation();
		
		// run with the secondary algorithm implementations (a static variable will switch the implementations automatically)
		runSimulation();
		
		// wait for the threads to finish
		try {
			Thread.sleep( 5000 );
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// generate secondary algorithm implementations output
		generateStatistics();
	}

	/**
	 * Called from within the main method to run an entire simulation using the input file as parameters.
	 * This runs the main simulation loop and subsequently shuts down after the simulation time has expired.
	 * This is called twice: first for the initial algorithm implementations, and second for the second algorithm implementations.
	 */
	public static void runSimulation()
	{
		int interval = ( 60000 / peoplePerMin ) / timeScale;	// this gives how many milliseconds to wait until generating another person
		int msDuration = ( duration * 60000 ) / timeScale;						// simulation duration in milliseconds
		int timeSpent = 0;										// tracks how many seconds have passed			
		int difference;				// difference between the total duration time AND time passed
		int personID = 0;										// generated person ID (used for debug information)							
		
		/* ----- START Simulation loop ----- */
		//  keep generating people as long as sleeping for "interval" milliseconds won't exceed our duration
		while ( msDuration > timeSpent )
		{
			try {
				Thread.sleep( interval );
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// generate the start and destination floors for the person
			short startFloor = generateFloor( cumulativeStartPercentages );
			short destFloor;
			do
			{
				destFloor = generateFloor( cumulativeDestPercentages );
			}
			while( destFloor == startFloor ); // Make sure that the destination floor is not the same as the starting floor
			
			// based on generated start and end floors, determine if the person wants to go up or down
			boolean isGoingUp = destFloor > startFloor;
			
			// generate person, add them to a floor, and presses the callbox
			PersonFacade.getInstance().createPerson( ( short ) startFloor, ( short ) destFloor );
			Building.getInstance().addPersonToFloor( ( short ) startFloor, ( short ) personID );
			
			Building.getInstance().pressCallBox( ( short ) startFloor, isGoingUp );
			
			if ( DEBUG )
			{
				String string = String.format( "%2d:%2d:%2d   ", 	Calendar.getInstance().get( Calendar.HOUR ), 
																	Calendar.getInstance().get( Calendar.MINUTE ),
																	Calendar.getInstance().get( Calendar.SECOND ) );
				string += String.format( "Person %d on floor %d makes a request for floor %d\n", personID + 1, startFloor + 1, destFloor + 1 );
				System.out.print( string );
			}
			
			personID++;
			timeSpent += interval;
		}
		/* ----- END Simulation loop ----- */
		
		difference = msDuration - timeSpent;
		// otherwise, sleep for the remaining time then end the simulation
		try {
			Thread.sleep( difference );
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// end the simulation
		System.out.println( "SHUTTING DOWN ELEVATORS." );
		EController.getInstance().shutdownAllElevators();
	}
	/**
	 * Given a text file, this method will read the Simulation input and store it in the appropriate variables needed to run the simulation 
	 * @throws IllegalArgumentException 
	 */
	public static void readSimInput( String filename ) throws IllegalArgumentException
	{
		// The File object that points to the actual file (in CSV format)
		File inFile = new File( filename ); 
		Scanner fileScanner = null;
		Scanner lineScanner = null;
		
		try
		{
			fileScanner = new Scanner( inFile ).useDelimiter( "," ); // Creates a scanner to read from the file
		}
		catch( FileNotFoundException e )
		{
			e.printStackTrace();
		}
		
		// Duration of entire simulation (stores in private global variable so the main method can use it)
		String inDuration = fileScanner.nextLine();	
		duration = Integer.decode( inDuration );
		
		// Time scale
		String inTimeScale = fileScanner.nextLine();
		timeScale = Short.decode( inTimeScale );
		
		// Number of floors in the building
		String inNumOfFloors = fileScanner.nextLine(); 	
		numOfFloors = Short.decode( inNumOfFloors );
		
		// Number of elevators in the building
		String inNumOfElevators = fileScanner.nextLine();		
		short numOfElevators = Short.decode( inNumOfElevators );
		
		// Maximum number of people that can be in each elevator at once
		String inMaxCapacity = fileScanner.nextLine(); 	 
		short maxCapacity = Short.decode( inMaxCapacity );
		
		// The time it takes to get from one floor to the next
		String inTimePerFloor = fileScanner.nextLine();							
		short timePerFloor = ( short )( Short.decode( inTimePerFloor ) / timeScale );
		
		// The time it takes to open the doors, exchange people, and close the doors
		String inDoorTime = fileScanner.nextLine();						
		short doorTime = ( short )( Short.decode(inDoorTime) / timeScale );
		
		// Figuring out the default floor numbers
		String inDefaultFloors = fileScanner.nextLine(); 	// fileScanner reads one line from the file
		lineScanner = new Scanner( inDefaultFloors ); 		// lineScanner reads the line that fileScanner generated
		lineScanner.useDelimiter(","); 				// delimits the string using commas

		short defaultFloorOne = lineScanner.nextShort();
		
		/* ----- START figuring out default floors ----- */
		
		// If there are multiple values for default floors
		if( lineScanner.hasNextShort() ) 
		{
			// The ArrayList containing the default floors for each of the elevators
			ArrayList<Short> defaultFloors = new ArrayList<Short>( numOfElevators ); 
			
			defaultFloors.add( defaultFloorOne ); 
			
			// This reads input and puts the data into the defaultFloors ArrayList
			for(short i = 1; i < numOfElevators; i++)
			{												
				defaultFloors.add( lineScanner.nextShort() );
			}
			
			myBuilding = Building.getInstance(	( short ) numOfFloors, 
												numOfElevators,
												maxCapacity,
												timePerFloor,
												doorTime,
												defaultFloors );
		}
		else // Otherwise, make all the default floors the same value
		{
			myBuilding = Building.getInstance(	( short ) numOfFloors,
												numOfElevators,
												maxCapacity,
												timePerFloor,
												doorTime,
												defaultFloorOne );
		}
		
		lineScanner.close();
		
		/* ----- END Figuring out the default floor numbers ----- */
		
		String inPeoplePerMin = fileScanner.nextLine();		// The frequency of people being generated per minute
		peoplePerMin = Integer.decode(inPeoplePerMin);
		
		/* ----- START figuring out start floor probabilities ----- */
		
		startPercentages = new ArrayList<Short>( ( int ) numOfFloors ); 			// The ArrayList containing how probable the person will be generated at each floor.
		cumulativeStartPercentages = new ArrayList<Short>( ( int ) numOfFloors ); 	// Cumulative percentages (used later)
		
		String inStartProb = fileScanner.nextLine();
		lineScanner = new Scanner( inStartProb );
		lineScanner.useDelimiter(",");
		
		short previousNum = 0;
		short thisNum;
		
		// This reads input and puts the data into startPercentages
		for( short i = 0; i < numOfFloors; i++ )								
		{																	
			thisNum = lineScanner.nextShort();						
			startPercentages.add( thisNum );									
			cumulativeStartPercentages.add( ( short ) ( thisNum + previousNum ) );	
			previousNum += thisNum;											
		}																	
		if( previousNum != 100 )
		{
			lineScanner.close();
			fileScanner.close();
			System.err.println( "Total percentage for starting floor probability is not 100%" );
			return;
		}
		if( lineScanner.hasNext() ) //Check for extraneous data in this line
		{
			lineScanner.close();
			fileScanner.close();
			System.err.println( "Invalid data. Too many values for start percentages." );
			return;
		}
		
		lineScanner.close();
		
		/* ----- END Figuring out the probabilities for where people start ----- */
		
		/* ----- START figuring out the probabilities for where people end ----- */
		destPercentages = new ArrayList<Short>( ( int ) numOfFloors ); 			// The ArrayList containing how probable the person will be generated at each floor.
		cumulativeDestPercentages = new ArrayList<Short>( ( int ) numOfFloors ); 	// Cumulative percentages (used later)
		
		String inDestProb = fileScanner.nextLine();
		lineScanner = new Scanner( inDestProb );
		lineScanner.useDelimiter(",");
		
		previousNum = 0;
		
		// This reads input and puts the data into startPercentages
		for( short i = 0; i < numOfFloors; i++ )								
		{																	
			thisNum = lineScanner.nextShort();						
			destPercentages.add( thisNum );									
			cumulativeDestPercentages.add( ( short ) ( thisNum + previousNum ) );	
			previousNum += thisNum;											
		}																	
		if( previousNum != 100 )
		{
			lineScanner.close();
			fileScanner.close();
			System.err.println( "Total percentage for destination floor probability is not 100%" );
			return;
		}
		if( lineScanner.hasNext() ) // Check for extraneous data in this line
		{
			lineScanner.close();
			fileScanner.close();
			System.err.println( "Invalid data. Too many values for start percentages." );
			return;
		}
		
		lineScanner.close();
		
		// Checks to make sure that there is no extraneous input
		if( fileScanner.hasNext() )										
		{																
			fileScanner.close();										
			System.err.println( "Invalid data. Too many lines of data." );
			return;														
		}																
		
		fileScanner.close();
	}

	/**
	 * Given a cumulative array of floor probabilities, this method randomly generates a floor according to these probabilities and returns it
	 * @returns the randomly generated floor between 0 and the max floor of the building
	 */
	public static short generateFloor( ArrayList<Short> probabilities )
	{
		/* Taking probabilities into account, assign start and destination floor numbers to Person objects
		 *
		 * Visual representation of the algorithm that assigns floor numbers to Person objects:
		 * Suppose the following:
		 * - Building has 5 floors
		 * - Floor probabilities are as follows: [50,10,20,10,10]
		 * - random.nextInt(100) returns 62
		 * 
		 * Floor:		|         1        | 2 |   3   | 4 | 5 |
		 * Percentages: 0%                50% 60%     80% 90% 100%
		 *                                      ^
		 *                                     62%
		 * The person will then be assigned floor 3.
		 */
		Random random = new Random();
		
		short generatedFloor = -1; 	// floor to be returned		
		short floorProb = ( short ) random.nextInt( 100 );
		short lowerBoundOfRange = 0;
		short higherBoundOfRange = 0;
		
		// searches the floor probabilities for the matching floor (based on the randomly generated number from 1 to 100)
		for( short i = 0; i < numOfFloors; i++ )
		{
			higherBoundOfRange = probabilities.get( i );
			
			// Checks if generated number is within the current floor probability range (ex. 60% - 70%)
			if( lowerBoundOfRange <= floorProb  && floorProb < higherBoundOfRange )
			{
				// and if it is, sets generated floor to the floor of this probability range and stops the loop
				generatedFloor = i;
				break;
			}
			
			// moves on to the next probability range (ex. lower bound changes from 60% to 70%)
			lowerBoundOfRange = higherBoundOfRange;
		}
		if( generatedFloor == -1 )
		{
			System.err.println( "Starting floor not found." );
			return -1;
		}
		
		return generatedFloor;
	}

	/**
	 * This method generates and outputs the three desired output tables
	 * by calculating statistics gathered during the simulation.
	 */
	public static void generateStatistics()
	{
		// initialize floorStats
		floorStats = new ArrayList<ArrayList<PersonDTO>>();
		
		// get all person DTO's from person facade
		peopleStats = PersonFacade.getInstance().getPersonDTOs();
		
		generateFloorStats();
		generateRideStats();
		generatePersonStats();
	}

	/**
	 * This method is called within generateStats().
	 * It calculates and outputs the average ride time from each floor to each other floor in table format.
	 * (Ex. from floor 1 to floor 2, floor 1 to floor 3 ... floor 1 to floor n)
	 */
	private static void generateRideStats()
	{	
		// print out the table label
		String string = String.format( "%9s", "Floor" );
		
		for ( int floor = 0; floor < numOfFloors; floor++ )
		{
			string += String.format( "%9s", floor + 1 );
		}
		
		System.out.println( string );

		// for each floor ...
		for( int fromFloor = 0; fromFloor < numOfFloors; fromFloor++ )
		{
			string = String.format( "%9d", fromFloor + 1 );
			
			// gets the DTO array list for this floor (for readability)
			ArrayList<PersonDTO> thisFloor = floorStats.get( fromFloor );
			
			/* ... get the average ride time to every other floor.
			 * (ex. from floor 1 to floor 2, from floor 1 to floor 3 ... from floor 1 to floor n)
			 */
			for ( int toFloor = 0; toFloor < numOfFloors; toFloor++ )
			{
				int avgRide = 0;
				int counter = 0;
				
				// we're comparing the floor to itself, void out the field (ex. from floor 1 to floor 1)
				if ( fromFloor == toFloor )
				{
					string += String.format( "%9s", "X" );
					continue;
				}
			
				/* if the current DTO matches the current dest floor we're looking at, 
				 * then include it in our calculations
				 */
				for ( PersonDTO dto : thisFloor )
				{
					if ( dto.destFloor == toFloor )
					{
						avgRide += dto.rideTime;
						counter++;
					}
				}
				
				// if we actually had stats for fromFloor and toFloor, then calculate them
				if ( counter != 0 )
				{
					avgRide = avgRide / counter;
					string += String.format( "%9d", avgRide );
				}
				// otherwise, void out these field
				else
				{
					string += String.format( "%9s", "X" );
				}			
			}
			
			// newline for the next floor's stats
			System.out.println( string );
		}
		
		System.out.println();
	}

	/**
	 * This method is called within generateStats().
	 * It calculates the average wait time, the minimum wait time, and maximum wait time for each floor.
	 * The statistics are gathered from Persons who were generated on this floor during the simulation.
	 */
	private static void generateFloorStats()
	{
		String string = String.format(	"%18s	%18s	%18s	%18s", " ", 
														"Average Wait Time", 
														"Minimum Wait Time", 
														"Maximum Wait Time" );
		
		System.out.println( string );
				
		/* For every floor, populate the array list with the DTO's of persons generated there.
		 * Print out the relevant information from these DTO's (avg wait time, min wait time, max wait time),
		 * then clear the list and rinse and repeat for the next floor
		 */
		for ( int floor = 0; floor < numOfFloors; floor++ )
		{
			ArrayList<PersonDTO> thisFloor = new ArrayList<PersonDTO>();
			
			// adds all corresponding DTO's to this current floor's array list
			for ( PersonDTO dto : peopleStats )
			{
				if ( dto.startFloor == floor )
				{
					thisFloor.add( dto );
				}
			}
			
			// if there were any DTO's for this floor, print out the floor's statistics
			if ( !thisFloor.isEmpty() )
			{			
				int counter = 0;
				int avgWait = 0;
				int minWait = thisFloor.get( 0 ).waitTime;
				int maxWait = thisFloor.get( 0 ).waitTime;
				
				for ( PersonDTO dto : thisFloor )
				{
					if ( dto.waitTime < minWait )
					{
						minWait = dto.waitTime;
					}
					if ( dto.waitTime > maxWait )
					{
						maxWait = dto.waitTime;
					}
					
					avgWait += dto.waitTime;
					counter++;
				}
			
				avgWait = avgWait / counter;

				string = String.format(	"Floor %3d:	%10d seconds	%10d seconds	%10d seconds", 
										floor + 1, avgWait, minWait, maxWait );
				
				System.out.println( string );
			}
			// otherwise, void out the fields
			else
			{
				string = String.format(	"Floor %3d:	%10s seconds	%10s seconds	%10s seconds", 
										floor + 1, "---", "---", "---" );
				
				System.out.println( string );
			}
			
			// adds this floor's DTO list to the two-dimensional array
			floorStats.add( thisFloor );
		}
		
		System.out.println();
	}
	
	/**
	 * This method is called within generateStats().
	 * It calculates and outputs the statistics for every individual person.
	 */
	private static void generatePersonStats()
	{
		String string = String.format(	"%20s %20s %20s %20s %20s\n", 
										"Person", "Wait Time", "Start Floor", "Destination Floor", "Ride Time" );
		
		System.out.println( string );
		
		for ( PersonDTO dto : peopleStats )
		{
			string = String.format(	"%20d %12d seconds %20d %20d %12d seconds\n", 
									dto.personID + 1, dto.waitTime, dto.startFloor + 1, dto.destFloor + 1, dto.rideTime );
			System.out.print( string );
		}
		
		System.out.println();
	}
}

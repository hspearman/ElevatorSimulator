package elevatorController;

/**
 * This class is a factory for all pending request processor algorithms
 * @author Hannah
 *
 */
public class PendingRequestProcessorFactory
{
	/**
	 * This static variable will make sure the initial algorithm implementation is used for the first simulation,
	 * and that the second algorithm implementation is used for the second simulation
	 */
	private static int algorithmImplementation = 0;
	
	/**
	 * Returns an algorithm implementation for the pending request processor
	 * @return the algorithm implementation
	 */
	public static PendingRequestProcessor build()
	{
		if ( algorithmImplementation == 0 )
		{
			algorithmImplementation++;
			return new PendingRequestProcessorImpl();
		}
		else
		{
			return new PendingRequestProcessorSecondImpl();
		}
	}
}

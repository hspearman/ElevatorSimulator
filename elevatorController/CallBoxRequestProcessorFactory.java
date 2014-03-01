package elevatorController;

/**
 * This is a factory class for the call-box request processor algorithms.
 * @author Hannah Spearman
 * @author Jeremy Ma
 *
 */
public class CallBoxRequestProcessorFactory
{
	/**
	 * This static variable will make sure the initial algorithm implementation is used for the first simulation,
	 * and that the second algorithm implementation is used for the second simulation
	 */
	private static int algorithmImplementation = 0;
	/**
	 * Returns the appropriate call-box request algorithm implementation
	 * @return the appropriate algorithm object
	 */
	public static CallBoxRequestProcessor build()
	{
		if ( algorithmImplementation == 0 )
		{
			algorithmImplementation++;
			return new CallBoxRequestProcessorImpl();
		}
		else
		{
			return new CallBoxRequestProcessorSecondImpl();
		}
	}
}

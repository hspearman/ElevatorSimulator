package elevatorController;

import static org.junit.Assert.*;

import org.junit.Test;

public class CallBoxRequestProcessorFactoryTest {

	@Test
	public void test()
	{
		CallBoxRequestProcessor c = CallBoxRequestProcessorFactory.build();
		assertEquals(c.getClass(),CallBoxRequestProcessorImpl.class);
		c = CallBoxRequestProcessorFactory.build();
		assertEquals(c.getClass(),CallBoxRequestProcessorSecondImpl.class);
	}

}

package elevatorController;

import static org.junit.Assert.*;

import org.junit.Test;

public class PendingRequestProcessorFactoryTest {

	@Test
	public void test()
	{
		PendingRequestProcessor p = PendingRequestProcessorFactory.build();
		assertEquals(p.getClass(),PendingRequestProcessorImpl.class);
		p = PendingRequestProcessorFactory.build();
		assertEquals(p.getClass(),PendingRequestProcessorSecondImpl.class);
	}

}

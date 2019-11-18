package com.perrone.pittard;

import junit.framework.TestCase;

public class CmdTest extends TestCase {
	
	public void testExtraListeners() throws Exception {
		String numListeners = "10";
		String feedIntervalMillis = "1";
		String demoLifetimeMillis = "100";
		String[] args = {numListeners, feedIntervalMillis, demoLifetimeMillis};
		Cmd.main(args);
		// Queue should reach max size.
		assertTrue(Integer.parseInt(feedIntervalMillis) < (Integer.parseInt(numListeners) * QueueListener.SLEEP_TIME));
	}

	public void testFewListeners() throws Exception {
		String numListeners = "2";
		String feedIntervalMillis = "100";
		String demoLifetimeMillis = "500";
		String[] args = {numListeners, feedIntervalMillis, demoLifetimeMillis};
		Cmd.main(args);
		// Listeners should be waiting for values
		assertTrue(Integer.parseInt(feedIntervalMillis) > (Integer.parseInt(numListeners) * QueueListener.SLEEP_TIME));
	}


}

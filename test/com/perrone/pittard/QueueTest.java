package com.perrone.pittard;

import junit.framework.TestCase;

public class QueueTest extends TestCase {
	
	public void testAddAndPop() throws InterruptedException {
	    SynchedQueue q = new SynchedQueue();
	    q.add(0);
	    q.add(Integer.MIN_VALUE);
	    q.add(Integer.MAX_VALUE);
	    assertEquals(3, q.getLength());
	    
		assertEquals(0, q.remove().intValue());
		assertEquals(Integer.MIN_VALUE, q.remove().intValue());
		assertEquals(Integer.MAX_VALUE, q.remove().intValue());
		assertEquals(0, q.getLength());
	}
	

}

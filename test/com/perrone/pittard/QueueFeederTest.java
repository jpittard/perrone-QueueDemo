package com.perrone.pittard;

import junit.framework.TestCase;
import com.perrone.pittard.SynchedQueue;

public class QueueFeederTest extends TestCase {
	
	public void testRun() throws InterruptedException {
		SynchedQueue q = new SynchedQueue();
		QueueFeeder feeder = new QueueFeeder(10, q);
		Thread f = new Thread(feeder);
		f.start();
		Thread.currentThread().sleep(25);
		feeder.shutdown(1);
		assertEquals(4, q.getLength());
		assertFalse(q.remove()==q.remove());
		q.remove();
		assertEquals(SynchedQueue.POISON, q.remove());
	}

	public void testMaxSizeWait() throws InterruptedException {
		SynchedQueue q = new SynchedQueue();
		for (int i = 0; i < SynchedQueue.MAX_SIZE; i++) {
			q.add(1);
		}
		assertEquals(SynchedQueue.MAX_SIZE, q.getLength());
		QueueFeeder feeder = new QueueFeeder(1, q);
		Thread f = new Thread(feeder);
		f.start();
		
		try {
			synchronized(q) {
				q.remove();
				assertEquals(SynchedQueue.MAX_SIZE - 1, q.getLength());
				q.notify();
			}
			Thread.currentThread().sleep(25);
			// Feeder should have added a random number to the queue
			assertEquals(SynchedQueue.MAX_SIZE, q.getLength());
			for (int i = 0; i < SynchedQueue.MAX_SIZE - 1; i++) {
				q.remove();
			}
			assertFalse(1 == q.remove().intValue());
		} finally {
			feeder.shutdown(0);
		}
		
	}


}

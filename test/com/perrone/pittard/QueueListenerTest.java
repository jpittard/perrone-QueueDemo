package com.perrone.pittard;

import junit.framework.TestCase;

public class QueueListenerTest extends TestCase {

	public void testRun() throws InterruptedException {
		SynchedQueue q = new SynchedQueue();
		QueueListener listener = new QueueListener(q);
		Thread f = new Thread(listener);
		f.setDaemon(true);
		f.start();
		
		try {
			// Listener should be waiting
			assertEquals(0, q.getLength());
			synchronized (q) {
				q.add(1);
				assertEquals(1, q.getLength());
				q.notify();
			}
			Thread.sleep(QueueListener.SLEEP_TIME);
			// Now the listener should have taken the value
			synchronized (q) {
				assertEquals(0, q.getLength());
			}

			
		} finally {
			listener.shutdown();
		}
	}
	
	public void testPoison() throws InterruptedException {
		SynchedQueue q = new SynchedQueue();
		QueueListener listener = new QueueListener(q);
		Thread f = new Thread(listener);
		f.setDaemon(true);
		f.start();
		
		synchronized (q) {
			q.add(SynchedQueue.POISON.intValue());
			assertEquals(1, q.getLength());
			q.notify();
		}
		Thread.sleep(QueueListener.SLEEP_TIME);
		// Now the listener should have taken the value
		synchronized (q) {
			assertEquals(0, q.getLength());
		}
		Thread.sleep(QueueListener.SLEEP_TIME);
	}

}

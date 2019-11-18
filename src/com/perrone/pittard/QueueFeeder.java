package com.perrone.pittard;

import com.perrone.pittard.SynchedQueue;
import java.util.Random;

/**
 * A producer to implement the following requirement:
 * 'Create another thread that puts random values as “Integer” Java Objects onto the queue
 * at an interval specified via a Java command line parameter. '
 */
public class QueueFeeder implements Runnable {
	
	private int interval;
	private SynchedQueue queue;
	private Random rand;
	private boolean active = true;
	
	/**
	 * @param interval - number of millis to wait between adding values to queue.
	 * @param queue - shared queue
	 */
	public QueueFeeder(int interval, SynchedQueue queue) {
		this.interval = interval;
		this.queue = queue;
		this.rand = new Random();
	}

	public void run() {
		
		while(this.active) {
			
			int i = rand.nextInt(Integer.MAX_VALUE - 1);
			
			addToQueue(i);
			
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				break;
			}
		}
	}
	
	private void addToQueue(int i) {
		synchronized(this.queue) {
			while (this.queue.getLength() >= SynchedQueue.MAX_SIZE) {
				try {
					printStatus("Max size reached. Waiting to add " + i);
					this.queue.wait();
				} catch (InterruptedException e) {
					break;
				}
			}
			printStatus("Adding " + i + " to queue.");
			this.queue.add(i);
			this.queue.notify();
		}

	}
	
	/**
	 * Shut down the feeder and add poison to the queue.
	 * @param numListeners - number of listeners needing to be poisoned
	 */
	public void shutdown(int numListeners) {
		printStatus("shutting down");
		this.active = false;
		for (int i = 0; i < numListeners; i++) {
			addToQueue(SynchedQueue.POISON.intValue());
		}
	}
	
	private void printStatus(String status) {
		System.out.println(Thread.currentThread().getName() + " " + status);
	}


}

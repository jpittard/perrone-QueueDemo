package com.perrone.pittard;

import com.perrone.pittard.SynchedQueue;

/**
 * A listener to implement the following requirement:
 * When a listener thread grabs an Integer off the Queue, have it print out the value, 
 * along with its Thread name, and sleeps for at least 20ms to simulate some form of necessary processing.
 */
public class QueueListener implements Runnable {
	
	public static final long SLEEP_TIME = 20L;
	private SynchedQueue queue;
	private boolean active = true;
	
	public QueueListener(SynchedQueue queue) {
		this.queue = queue;
	}
	

	public void run() {
		
		while(this.active) {
			Integer i = null;
			synchronized(this.queue) {
				while (this.queue.getLength() == 0) {
					try {
						printStatus("waiting for value.");
						this.queue.wait();
					} catch (InterruptedException e) {
						break;
					}
				}
				i = queue.remove();
				printStatus("read " + i);
				queue.notify();
			}
			
			if (i.equals(SynchedQueue.POISON)) {
				printStatus("poisoned");
				break;
			}
			
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				printStatus("interrupted");
				break;
			}
		}
	}
	
	private void printStatus(String status) {
		System.out.println(Thread.currentThread().getName() + " " + status);
	}
	
	public void shutdown() {
		printStatus("shutting down");
		this.active = false;
	}
	

}

package com.perrone.pittard;

import java.util.LinkedList;


/**
 * I could have used Collections.SynchronizedList for this. It seemed
 * better for the demo to make my own class.
 */
public class SynchedQueue {
	
	public static final Integer POISON = new Integer(Integer.MAX_VALUE);
	public static final int MAX_SIZE = 10;
	private LinkedList q;

	public SynchedQueue() {
		q = new LinkedList();
	}
	
	/**
	 * Add an integer to the back of the queue.
	 * @param i - integer to add
	 */
	protected synchronized void add(int i) {
		q.add(new Integer(i));
	}
	
	/**
	 * Pop an integer off the front of the queue.
	 * @return Integer
	 */
	protected synchronized Integer remove() {
		return (Integer)q.removeFirst();
	}
	
	/**
	 * @return number of integers in the queue
	 */
	public synchronized int getLength() {
		return q.size();
	}
	
}

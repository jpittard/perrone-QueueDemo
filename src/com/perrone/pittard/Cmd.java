package com.perrone.pittard;

import com.perrone.pittard.SynchedQueue;
import com.perrone.pittard.QueueFeeder;

/**
 * Demo for job application from James Pittard
 * This class rocesses command-line arguments and manages threads.
 */
public class Cmd {
	
	/**
	 * Entry point for the program.
	 * @param numListeners - number of listener threads to deploy
	 * @param feedIntervalMillis - milliseconds between adding integers to the queue
	 * @param demoLifetimeMillis - milliseconds before the demo automatically starts shutting down
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		if (args.length != 3) {
			printUsage();
			System.exit(1);
		}
		
		// Not addressing parsing errors since just making a weekend demo
		int numListeners = Integer.parseInt(args[0]);
		int feedIntervalMillis = Integer.parseInt(args[1]);
		int demoLifetimeMillis = Integer.parseInt(args[2]);
				
		SynchedQueue queue = new SynchedQueue();
		
		QueueFeeder feeder = startThreads(queue, feedIntervalMillis, numListeners);
		
		Thread.sleep(demoLifetimeMillis);
		System.out.println("Demo lifetime reached.");
		
		shutdownGracefully(feeder, numListeners);
		Thread.sleep(queue.getLength() * QueueListener.SLEEP_TIME / numListeners + 1);
		System.out.println("Finished.");
	}
	
	private static void printUsage() {
		System.out.println("Usage:");
		System.out.println("  java -jar <jar_filename> numListeners feedIntervalMillis demoLifetimeMillis");
	}
	
	/**
	 * Create a pool of “listener” threads (the number of them passed as a parameter via the Java command line) 
	 * that wait on a Queue. Create another thread that puts random values as 
	 * “Integer” Java Objects onto the queue at an interval specified via a Java command line parameter. 
	 * 
	 * @return QueueFeeder reference to use in shutdown.
	 */
	private static QueueFeeder startThreads(SynchedQueue queue, int interval, int numListeners) {
		for (int i = 0; i < numListeners; i++) {
			Thread listener = new Thread(new QueueListener(queue));
			listener.setName("Listener " + i);
			listener.setDaemon(true);
			listener.start();
		}
		
		QueueFeeder feeder = new QueueFeeder(interval, queue);
		Thread thread = new Thread(feeder);
		thread.setName("Feeder");
		thread.setDaemon(true);
		thread.start();
		return feeder;
	}
	
	/**
	 * We could keep an array of threads to shut them down, but I am opting for 
	 * the method of adding "poison" to each thread object, delegated to the feeder 
	 * since the synchronized add method is already there.
	 */
	private static void shutdownGracefully(QueueFeeder feeder, int numListeners) {
		System.out.println("Starting shutdown...");
		feeder.shutdown(numListeners);
	}
	
}

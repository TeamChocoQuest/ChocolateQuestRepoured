package de.DerToaster.SimpleThreading;

import java.util.Queue;
// DONE: Replace current queue with a queue that is thread safe!!! This will make the whole thing faster as it must not wait for the SimpleThread to unlock its queue
import java.util.concurrent.ConcurrentLinkedQueue;

class SimpleThread extends Thread {

	private Thread mainThread;

	private Queue<Runnable> tasks = new ConcurrentLinkedQueue<>();

	private boolean queueLocked = true;

	public SimpleThread(boolean async) {
		this.mainThread = Thread.currentThread();

		this.setDaemon(async);

		this.queueLocked = false;
	}

	public void addTask(Runnable task) {
		this.lockQueue();

		boolean runAfterAdd = false;
		if (this.tasks.isEmpty()) {
			runAfterAdd = true;
		}

		this.tasks.add(task);

		this.unlockQueue();

		if (runAfterAdd) {
			this.run();
		}
	}

	@Override
	public synchronized void start() {
		if (!this.tasks.isEmpty()) {
			this.run();
		}
	}

	@Override
	public void run() {
		if (!this.tasks.isEmpty()) {
			this.lockQueue();

			this.tasks.remove().run();

			this.unlockQueue();

			/*
			 * try { sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
			 */
			this.run();
		}
	}

	public boolean isQueueLocked() {
		return this.queueLocked;
	}

	private void lockQueue() {
		this.queueLocked = true;
	}

	private void unlockQueue() {
		this.queueLocked = false;
	}

	public Thread getMainThread() {
		return this.mainThread;
	}

}

package de.DerToaster.SimpleThreading;

import java.util.Queue;
//DONE: Replace current queue with a queue that is thread safe!!! This will make the whole thing faster as it must not wait for the SimpleThread to unlock its queue
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
		lockQueue();
		
		/*boolean runAfterAdd = false;
		if(this.tasks.isEmpty()) {
			runAfterAdd = true;
		}*/
		
		tasks.add(task);
		
		unlockQueue();
		
		/*if(runAfterAdd) {
			this.run();
		}*/
	}
	
	@Override
	public synchronized void start() {
		if(!this.tasks.isEmpty()) {
			run();
		}
	}
	
	@Override
	public void run() {
		if(!this.tasks.isEmpty()) {
			lockQueue();
			
			this.tasks.remove().run();
			
			unlockQueue();
			
			/*try {
				sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
			this.run();
		} else {
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

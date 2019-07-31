package de.DerToaster.SimpleThreading;

import java.util.Queue;
//DONE: Replace current queue with a queue that is thread safe!!! This will make the whole thing faster as it must not wait for the SimpleThread to unlock its queue
import java.util.concurrent.ConcurrentLinkedQueue;

class SimpleThread extends Thread {
	
	private int rerunsTillStop = 20;
	private boolean running = false;

	private Thread mainThread;
	
	private Queue<Runnable> tasks = new ConcurrentLinkedQueue<>();
	
	private boolean queueLocked = true;
	
	public SimpleThread(boolean async) {
		this.mainThread = Thread.currentThread();
		
		this.setDaemon(async);
		
		this.queueLocked = false;
	}
	
	public void addTask(Runnable task) {
		//System.out.println("Adding task to CQ-Thread: " + this.getName() + "...");
		
		lockQueue();
		
		/*boolean runAfterAdd = false;
		if(this.tasks.isEmpty()) {
			runAfterAdd = true;
		}*/
		
		tasks.add(task);
		
		unlockQueue();
		
		if(!this.running) {
			this.start();
		}
		
		/*if(runAfterAdd) {
			this.run();
		}*/
	}
	
	@Override
	public void start() {
		if(!this.tasks.isEmpty()) {
			this.running = true;
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
			try {
				sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.rerunsTillStop--;
			if(this.rerunsTillStop <= 0) {
				this.running = false;
				this.rerunsTillStop = 20;
			} else {
				//System.out.println("CQ Thread: " + this.getName() + ": Remaing tasks: " + this.tasks.size());
				this.run();
			}
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

package de.DerToaster.SimpleThreading;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class MultiThreadController {
	
	private List<SimpleThread> threads = new ArrayList<SimpleThread>();
	
	private Queue<Runnable> tasksToAdd;

	public MultiThreadController(int threadCount) {
		for(int i = 0; i < (Math.abs(threadCount) > 0 ? Math.abs(threadCount):1); i++) {
			this.threads.add(new SimpleThread(true));
		}
	}
	
	public void resetThreads(int newThreadCount) {
		this.threads.clear();
		for(int i = 0; i < (Math.abs(newThreadCount) > 0 ? Math.abs(newThreadCount):1); i++) {
			this.threads.add(new SimpleThread(true));
		}
	}
	
	public void addTask(Runnable task) {
		this.tasksToAdd.add(task);
		
		boolean taskNotAdded = true;
		while(taskNotAdded) {
			for(SimpleThread st : this.threads) {
				if(!st.isQueueLocked()) {
					st.addTask(task);
					taskNotAdded = false;
					break;
				}
			}
		}
	}

}

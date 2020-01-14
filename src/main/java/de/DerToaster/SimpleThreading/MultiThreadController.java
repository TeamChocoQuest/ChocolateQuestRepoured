package de.DerToaster.SimpleThreading;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MultiThreadController {

	private List<SimpleThread> threads = new ArrayList<SimpleThread>();

	private Queue<Runnable> tasksToAdd = new ConcurrentLinkedQueue<>();

	private int nextThreadID = 0;

	public MultiThreadController(int threadCount) {
		for (int i = 0; i < (Math.abs(threadCount) > 0 ? Math.abs(threadCount) : 1); i++) {
			SimpleThread fred = new SimpleThread(true);

			fred.setName("CQ_Async_Block_Placer_Thread#" + (i + 1));
			fred.start();

			this.threads.add(new SimpleThread(true));
		}
	}

	public void resetThreads(int newThreadCount) {
		this.threads.clear();
		for (int i = 0; i < (Math.abs(newThreadCount) > 0 ? Math.abs(newThreadCount) : 1); i++) {
			SimpleThread fred = new SimpleThread(true);

			fred.setName("CQ_Async_Block_Placer_Thread#" + (i + 1));
			fred.start();

			this.threads.add(fred);
		}
	}

	public void addTask(Runnable task) {
		this.tasksToAdd.add(task);

		/*if (this.nextThreadID >= this.threads.size()) {
			this.nextThreadID = 0;
		}

		SimpleThread st = this.threads.get(this.nextThreadID);
		st.addTask(task);

		this.nextThreadID++;*/

		/*
		 * boolean taskNotAdded = true;
		 * while(taskNotAdded) {
		 * for(SimpleThread st : this.threads) {
		 * //if(!st.isQueueLocked()) {
		 * st.addTask(task);
		 * taskNotAdded = false;
		 * System.out.println("Task added to thread " + st.getName());
		 * break;
		 * //}
		 * }
		 * }
		 */
		// if(this.tasksToAdd.size() >= this.threads.size()) {
		/*
		 * int threadIndex = 0;
		 * while(!this.tasksToAdd.isEmpty()) {
		 * if(threadIndex >= this.threads.size()) threadIndex = 0;
		 * 
		 * Runnable taskToAdd = this.tasksToAdd.remove();
		 * 
		 * SimpleThread st = this.threads.get(threadIndex);
		 * st.addTask(taskToAdd);
		 * System.out.println("Task added to thread " + st.getName());
		 * 
		 * threadIndex++;
		 * }
		 */
		// }
	}
	
	public Runnable getNextTask() {
		return this.tasksToAdd.poll();
	}

	public int getRemainingTasks() {
		if(this.tasksToAdd.isEmpty()) {
			return 0;
		}
		return this.tasksToAdd.size();
	}

}

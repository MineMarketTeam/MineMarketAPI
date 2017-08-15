package com.minemarket.api;

public interface BaseTaskScheduler {

	public abstract void runTaskAsynchronously(Runnable task);

	public abstract void runTaskSynchronously(Runnable task);
	
	public abstract void scheduleAsyncDelayedTask(Runnable task, int delay);
	
	public abstract void scheduleAsyncRepeatingTask(Runnable task, int delay, int repeatIn);

	public abstract void scheduleSyncDelayedTask(Runnable task, int delay);
	
	public abstract void scheduleSyncRepeatingTask(Runnable task, int delay, int repeatIn);
	
	public abstract void cancelTasks();
	
}

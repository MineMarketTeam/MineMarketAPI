package com.minemarket.api;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BukkitBaseScheduler implements BaseTaskScheduler {

	private final Plugin main;
	private Set<BukkitRunnable> tasks =  new HashSet<>();
	
	public BukkitRunnable createTask(Runnable task) {
		BukkitRunnable r = new BukkitRunnable() {
			
			public void run() {
				task.run();
			}
		};
		tasks.add(r);
		return r;
	}
	
	public void runTaskAsynchronously(Runnable task) {
		createTask(task).runTaskAsynchronously(main);
	}

	public void runTaskSynchronously(Runnable task) {
		createTask(task).runTask(main);
	}

	public void scheduleAsyncDelayedTask(Runnable task, int delay) {
		createTask(task).runTaskLaterAsynchronously(main, 20 * delay);
		
	}

	public void scheduleAsyncRepeatingTask(Runnable task, int delay, int repeatIn) {
		createTask(task).runTaskTimerAsynchronously(main, delay * 20, repeatIn * 20);
	}

	public void scheduleSyncDelayedTask(Runnable task, int delay) {
		createTask(task).runTaskLater(main, delay * 20);
		
	}

	public void scheduleSyncRepeatingTask(Runnable task, int delay, int repeatIn) {
		createTask(task).runTaskTimer(main, delay * 20, repeatIn * 20);
	}
	
	@Override
	public void cancelTasks() {
		for (BukkitRunnable r : tasks){
			r.cancel();
		}
		tasks.clear();
	}
}

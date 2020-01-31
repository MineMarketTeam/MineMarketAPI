package com.minemarket.api;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.api.scheduler.TaskScheduler;

public class BungeeBaseScheduler implements BaseTaskScheduler{

	private final Plugin main;
	private final TaskScheduler ts;
	private Set<ScheduledTask> tasks;
	
	// Esta classe � uma implementa��o de scheduler para a vers�o BungeeCord
	// O task scheduler do BungeeCord n�o possui execu��o de tarefas sincronizadas...
	// ent�o, usamos uma tarefa async (fora do Thread principal) em qualquer caso..
	// TODO: Implementar tarefas em sincronia.
	
	public BungeeBaseScheduler(){
		this.main = MineMarketBungee.getInstance();
		this.ts = main.getProxy().getScheduler();
		this.tasks = new HashSet<>();
	}
	
	private void onSchedule(ScheduledTask task){
		tasks.add(task);
	}
	
	@Override
	public void runTaskAsynchronously(Runnable task) {
		onSchedule(ts.runAsync(main, task));
	}

	@Override
	public void runTaskSynchronously(Runnable task) {
		onSchedule(ts.runAsync(main, task));
	}

	@Override
	public void scheduleAsyncDelayedTask(Runnable task, int delay) {
		onSchedule(ts.schedule(main, task, delay, TimeUnit.SECONDS));
	}

	@Override
	public void scheduleAsyncRepeatingTask(Runnable task, int delay, int repeatIn) {
		onSchedule(ts.schedule(main, task, delay, repeatIn, TimeUnit.SECONDS));		
	}

	@Override
	public void scheduleSyncDelayedTask(Runnable task, int delay) {
		onSchedule(ts.schedule(main, task, delay, TimeUnit.SECONDS));
	}

	@Override
	public void scheduleSyncRepeatingTask(Runnable task, int delay, int repeatIn) {
		onSchedule(ts.schedule(main, task, delay, repeatIn, TimeUnit.SECONDS));
	}

	@Override
	public void cancelTasks() {
		for (ScheduledTask task : tasks){
			try {
				task.cancel();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		tasks.clear();
	}

}

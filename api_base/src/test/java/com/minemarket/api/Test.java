package com.minemarket.api;

import java.util.UUID;

import com.minemarket.api.BaseCommandExecutor;
import com.minemarket.api.BaseTaskScheduler;
import com.minemarket.api.MineMarketBaseAPI;
import com.minemarket.api.types.ConnectionStatus;
import com.minemarket.api.types.PendingCommand;
import org.json.JSONObject;

public class Test implements BaseCommandExecutor, BaseTaskScheduler{

	@Override
	public void runTaskAsynchronously(Runnable task) {
		
	}

	@Override
	public void runTaskSynchronously(Runnable task) {
		
	}

	@Override
	public void scheduleAsyncDelayedTask(Runnable task, int delay) {
		
	}

	@Override
	public void scheduleAsyncRepeatingTask(Runnable task, int delay, int repeatIn) {
		
	}

	@Override
	public void scheduleSyncDelayedTask(Runnable task, int delay) {
		
	}

	@Override
	public void scheduleSyncRepeatingTask(Runnable task, int delay, int repeatIn) {
		
	}

	@Override
	public boolean executeCommand(PendingCommand command) {
		//System.out.println(command.getCommandLine().replaceAll("%nick%", command.getPlayerName()));
			
		return false;
	}
	
	public void testar(){
		MineMarketBaseAPI api = new MineMarketBaseAPI("TEST-KEY", "2.2", "BUKKIT", this, this, new BaseUpdater() {
			@Override
			public void update() {
				System.out.println("uma tentativa de update foi realizada.");
			}
		});

		if (api.initialize() == ConnectionStatus.OK){
			System.out.println("yay-");
			System.out.println(api.getStoreName());
			System.out.println(api.getStatus());
			api.loadPendingCommands();
		} else {
			
		}
	}
	
	public static void main(String[] args){
		new Test().testar();
	}

	@Override
	public void cancelTasks() {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean isPlayerOnline(UUID uuid) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPlayerOnline(String name) {
		// TODO Auto-generated method stub
		return false;
	}

}

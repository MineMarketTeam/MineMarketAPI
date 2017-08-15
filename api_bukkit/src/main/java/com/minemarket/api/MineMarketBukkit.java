package com.minemarket.api;

import javax.xml.transform.OutputKeys;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;
import com.minemarket.api.MineMarketBaseAPI.APIStatus;
import com.minemarket.api.commands.MineMarketCommand;
import com.minemarket.api.exceptions.MineMarketException;
import com.minemarket.api.types.CommandType;
import com.minemarket.api.types.PendingCommand;

@Getter
public class MineMarketBukkit extends JavaPlugin implements BaseCommandExecutor, Listener{
	
	@Getter
	private static MineMarketBukkit instance;
	private MineMarketBaseAPI api;
	private BukkitBaseScheduler scheduler;
	
	public void onEnable() {
		instance = this;
		scheduler = new BukkitBaseScheduler(instance);
		loadAPI();
		getCommand("MineMarket").setExecutor(new MineMarketCommand());
		Bukkit.getPluginManager().registerEvents(instance, instance);
	}
	
	public void loadAPI(){
		scheduler.cancelTasks();
		FileConfiguration config = getConfig();
		validateConfig(config);
		api = new MineMarketBaseAPI("http://api.minemarket.com.br/v2/", config.getString("key"), this.getDescription().getVersion(), scheduler, instance, new BukkitUpdater());
		api.initialize();
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		if (api.getStatus() == APIStatus.OK){
			api.onPlayerJoin(event.getPlayer().getUniqueId(), event.getPlayer().getName());
		}
		if (event.getPlayer().hasPermission("minemarket.configure")){
				new BukkitRunnable() {
					
					@Override
					public void run() {
						if (api.getStatus() != APIStatus.OK){
							event.getPlayer().sendMessage("====================");
							event.getPlayer().sendMessage(ChatColor.DARK_AQUA + "[!] Por favor verifique as seguintes informacoes:");
							getServer().dispatchCommand(event.getPlayer(), "minemarket info");			
							event.getPlayer().sendMessage("====================");
						}
						if (api.isUpdateAvailable()){
							event.getPlayer().sendMessage("====================");
							event.getPlayer().sendMessage(ChatColor.GOLD + "[MineMarket] " + ChatColor.GREEN + "Existe uma nova vers�o da API dispon�vel para download!");
							event.getPlayer().sendMessage(ChatColor.BLUE + "N�s recomendamos que atualize o mais rapidamente o poss�vel, para " + ChatColor.RED + "manter a seguran�a.");
							event.getPlayer().sendMessage("====================");
						}
					}
				}.runTaskLater(instance, 20L);
		}
	}
	
	private void validateConfig(FileConfiguration config){
		saveDefaultConfig();
		if (!config.isString("key"))
			config.set("key", "");
	}

	public boolean executeCommand(PendingCommand command) {
		String cmd = getCommandLine(command);
		CommandSender sender = command.getCommandType() == CommandType.CONSOLE ? Bukkit.getConsoleSender() : (command.getPlayerUUID() == null ? Bukkit.getPlayer(command.getPlayerName()) : Bukkit.getPlayer(command.getPlayerUUID()));
		if (command.getCommandType() == CommandType.OP){
			boolean op = sender.isOp();
			sender.setOp(true);
			Bukkit.dispatchCommand(sender, cmd);
			sender.setOp(op);
		} else {
			Bukkit.dispatchCommand(sender, cmd);
		}
		return true;
	}
	
}

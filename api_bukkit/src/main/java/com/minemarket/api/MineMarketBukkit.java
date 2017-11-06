package com.minemarket.api;

import java.util.UUID;

import javax.xml.transform.OutputKeys;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;
import com.minemarket.api.commands.MineMarketCommand;
import com.minemarket.api.exceptions.MineMarketException;
import com.minemarket.api.gui.MenuListener;
import com.minemarket.api.gui.PageManager;
import com.minemarket.api.types.CommandType;
import com.minemarket.api.types.ConnectionStatus;
import com.minemarket.api.types.PendingCommand;

@Getter
public class MineMarketBukkit extends JavaPlugin implements BaseCommandExecutor, Listener{
	
	@Getter
	private static MineMarketBukkit instance;
	private MineMarketBaseAPI api;
	private BukkitBaseScheduler scheduler;
	private PageManager pageManager;
	private MenuListener menuListener;
	
	public void onEnable() {
		instance = this;
		scheduler = new BukkitBaseScheduler(instance);
		loadAPI();
		getCommand("MineMarket").setExecutor(new MineMarketCommand());
		registerListener(instance);
	}
	
	private void registerListener(Listener listener){
		Bukkit.getPluginManager().registerEvents(listener, instance);
	}
	
	public void loadAPI(){
		scheduler.cancelTasks();
		FileConfiguration config = getConfig();
		validateConfig(config);
		api = new MineMarketBaseAPI("http://api.minemarket.com.br/snapshot/", config.getString("key"), this.getDescription().getVersion(), "BUKKIT", scheduler, instance, new BukkitUpdater());
		api.initialize();

		//TODO: Verify if menu pages are enabled before loading pageManager and the menuListener
		pageManager = new PageManager(instance);
		pageManager.loadPages();
		registerListener(menuListener = new MenuListener());
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		if (api.getStatus() == ConnectionStatus.OK){
			api.onPlayerJoin(event.getPlayer().getUniqueId(), event.getPlayer().getName());
		}
		if (event.getPlayer().hasPermission("minemarket.configure")){
				new BukkitRunnable() {
					
					@Override
					public void run() {
						if (api.getStatus() != ConnectionStatus.OK){
							event.getPlayer().sendMessage("====================");
							event.getPlayer().sendMessage(ChatColor.DARK_AQUA + "[!] Por favor verifique as seguintes informacoes:");
							getServer().dispatchCommand(event.getPlayer(), "minemarket info");			
							event.getPlayer().sendMessage("====================");
						}
						if (api.isUpdateAvailable()){
							event.getPlayer().sendMessage("====================");
							event.getPlayer().sendMessage(ChatColor.GOLD + "[MineMarket] " + ChatColor.GREEN + "Existe uma nova versão da API disponível para download!");
							event.getPlayer().sendMessage(ChatColor.BLUE + "Nós recomendamos que atualize o mais rapidamente o possível, para " + ChatColor.RED + "manter a segurança.");
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

	@Override
	public boolean executeCommand(PendingCommand command) {
		String cmd = getCommandLine(command);
		CommandSender sender = command.getCommandType() == CommandType.CONSOLE || !command.isRequireOnline() ? Bukkit.getConsoleSender() : (command.getPlayerUUID() == null ? Bukkit.getPlayer(command.getPlayerName()) : Bukkit.getPlayer(command.getPlayerUUID()));
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

	@Override
	public boolean isPlayerOnline(UUID uuid) {
		Player player;
		return (player = Bukkit.getPlayer(uuid)) != null && player.isOnline();
	}

	@Override
	public boolean isPlayerOnline(String name) {
		Player player;
		return (player = Bukkit.getPlayer(name)) != null && player.isOnline();
	}
	
}

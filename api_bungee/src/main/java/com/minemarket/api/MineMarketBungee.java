package com.minemarket.api;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.UUID;

import com.minemarket.api.commands.MineMarketCommand;
import com.minemarket.api.types.CommandType;
import com.minemarket.api.types.PendingCommand;

import lombok.AccessLevel;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

@Getter
public class MineMarketBungee extends Plugin implements BaseCommandExecutor, Listener {

	@Getter
	private static MineMarketBungee instance;
	private MineMarketBaseAPI api;
	private BungeeBaseScheduler scheduler;
	private Configuration configuration;
	
	public void onEnable() {
		instance = this;
		scheduler = new BungeeBaseScheduler();
		loadAPI();
		getProxy().getPluginManager().registerListener(instance, instance);
		getProxy().getPluginManager().registerCommand(instance, new MineMarketCommand());
	}
	
	public boolean loadAPI(){
		scheduler.cancelTasks();
		if (loadConfig() && validateConfig()){
			System.out.println(configuration.getString("key"));
			api = new MineMarketBaseAPI(configuration.getString("key"),  "1.2", "BUNGEE", scheduler, instance, new BungeeUpdater());
			api.initialize();
			return true;
		}
		return false;
	}
	
	public boolean loadConfig(){
		try {
	        if (!getDataFolder().exists())
	            getDataFolder().mkdir();
	
	        File file = new File(getDataFolder(), "config.yml");
	
	        if (!file.exists()) {
	            try (InputStream in = getResourceAsStream("config.yml")) {
	                Files.copy(in, file.toPath());
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
			configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
			return true;
		} catch (IOException e) {
			System.out.println("[MineMarketBungee] Um erro ocorreu ao carregar configura��es do seu plugin: ");
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean saveConfig(){
		try{
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, new File(getDataFolder(), "config.yml"));
			return true;
		} catch (IOException e) {
			System.out.println("[MineMarketBungee] Um erro ocorreu ao carregar configura��es do seu plugin: ");
			e.printStackTrace();
			return false;
		}
	}

	private boolean validateConfig(){
		if (configuration.getString("key") == null){
			configuration.set("key", "");
			return saveConfig();
		} 
		return true;
	}

	public boolean executeCommand(PendingCommand command) {
		String cmd = getCommandLine(command);
		CommandSender sender = command.getCommandType() == CommandType.CONSOLE || !command.isRequireOnline() ? getProxy().getConsole() : (command.getPlayerUUID() == null ? getProxy().getPlayer(command.getPlayerName()) : getProxy().getPlayer(command.getPlayerUUID()));
		return getProxy().getPluginManager().dispatchCommand(sender, cmd);
	}

	@Override
	public boolean isPlayerOnline(UUID uuid) {
		ProxiedPlayer player;
		return (player = getProxy().getPlayer(uuid)) != null && player.isConnected();
	}

	@Override
	public boolean isPlayerOnline(String name){
		ProxiedPlayer player;
		return (player = getProxy().getPlayer(name)) != null && player.isConnected();
	}
	
}

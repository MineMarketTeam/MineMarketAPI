package com.minemarket.api.commands;

import javax.xml.transform.OutputKeys;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.minemarket.api.MineMarketBaseAPI;
import com.minemarket.api.MineMarketBukkit;

public class MineMarketCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("MineMarket")){
			if (sender.hasPermission("MineMarket.configure")){
				
				String sbcmd;
				if (args.length != 0 && !(sbcmd = args[0]).equalsIgnoreCase("help")){
					if (sbcmd.equalsIgnoreCase("reload")){
						MineMarketBukkit.getInstance().reloadConfig();
						MineMarketBukkit.getInstance().loadAPI();
						sender.sendMessage(ChatColor.GREEN + "API recarregada.");
						onCommand(sender, cmd, label, new String[]{"info"});
						return true;
					} else if (sbcmd.equalsIgnoreCase("setkey")){
						if (args.length < 2)
							sender.sendMessage(ChatColor.RED + "Use: /" + label + " setkey <key>");
						else {
							String key = args[1];
							MineMarketBukkit.getInstance().getConfig().set("key", key);
							MineMarketBukkit.getInstance().saveConfig();
							sender.sendMessage(ChatColor.GOLD + "Key atualizada para: " + ChatColor.GREEN + key);
							sender.sendMessage(ChatColor.YELLOW + "Digite " + ChatColor.GOLD + "/MineMarket reload" + ChatColor.YELLOW + " para carregar as alterações.");
						}
						return true;
					} else if (sbcmd.equalsIgnoreCase("info")){
						String message;
						MineMarketBaseAPI api = MineMarketBukkit.getInstance().getApi();
						
						if (api == null){
							message = ChatColor.RED + "Com ERRO -> Verifique o console.";
						} else switch (api.getStatus()) {
						case OK:
							message = ChatColor.GREEN + "Funcionando corretamente";
							//message += "\n" + ChatColor.WHITE + "Nome da store: " + ChatColor.AQUA + api.getInfo().getStoreName();
							message += "\n" + ChatColor.WHITE + "KEY da API: " + ChatColor.AQUA + api.getKey();
							message += "\n" + ChatColor.WHITE + "Commandos pendentes: " + ChatColor.GOLD + api.getPendingCommands().size();
							break;
						case INVALID_KEY:
							message = ChatColor.RED + "Key Invalida";
							message += "\n" + ChatColor.WHITE + "Mude sua key utilizando: " + ChatColor.GOLD + "/" + label + " setkey";
							break;
						case BLOCKED_IP:
							message = ChatColor.RED + "Key Bloqueada";
							message += "\n" + ChatColor.WHITE + "Acesse o painel e desbloqueie o acesso deste servidor com a API.";
							break;
						case UNCONFIRMED_IP:
							message = ChatColor.YELLOW + "Aguardando confirmacao da key";
							message += "\n" + ChatColor.WHITE + "Acesse o painel e libere o acesso deste servidor com a API.";
							break;
						case CONNECTION_ERROR:
							message = ChatColor.RED + "Desconectado";
							message += "\n" + ChatColor.RED + "Nao foi possivel se conectar a API.";
							message += "\n" + ChatColor.WHITE + "Verifique se a conexao do seu servidor com a internet esta funcionando.";
							break;
						default:
							message = "";
							break;
						}
						if (api.isUpdateAvailable()){
							message += "\n" + ChatColor.GOLD + "Existe uma nova versão disponível! Por favor atualize assim que possível, fazendo o download no nosso site.";
						}
						sender.sendMessage(ChatColor.WHITE + "Status do sistema: " + message);
						return true;
					} 
				} 
				
				sender.sendMessage("===========================");
				sender.sendMessage(ChatColor.GOLD + "Ajuda - MineMarket");
				sender.sendMessage(ChatColor.AQUA + "Subcomandos disponíveis:");
				sender.sendMessage("");
				sender.sendMessage(ChatColor.GREEN + "/MineMarket setkey <key>" + ChatColor.WHITE + " - Use para configurar a KEY de acesso à API.");
				sender.sendMessage(ChatColor.GREEN + "/MineMarket info" + ChatColor.WHITE + " - Veja o status do sistema.");
				sender.sendMessage(ChatColor.GREEN + "/MineMarket reload" + ChatColor.WHITE + " - Recarrega as configurações do sistema.");
				sender.sendMessage("===========================");
				
			} else {
				sender.sendMessage(ChatColor.RED + "You don't have permission to configure the store.");
			}
			return true;
		}		
		
		return false;
	}
	
}

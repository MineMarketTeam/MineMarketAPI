package com.minemarket.api.commands;

import com.minemarket.api.MineMarketBaseAPI;
import com.minemarket.api.MineMarketBungee;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class MineMarketCommand extends Command {

	public MineMarketCommand() {
		// Utilizamos o g antes de minemarket para não ocorrer confusões em servidores que utilizam ambas versões da API: Bukkit + Bungee
		super("gminemarket");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (sender.hasPermission("MineMarket.configure")) {

			String sbcmd;
			if (args.length != 0 && !(sbcmd = args[0]).equalsIgnoreCase("help")) {
				if (sbcmd.equalsIgnoreCase("reload")) {
					if (MineMarketBungee.getInstance().loadAPI()){
						sender.sendMessage(ChatColor.GREEN + "API recarregada.");
						execute(sender, new String[] { "info" });
					} else {
						sender.sendMessage(ChatColor.RED + "Não foi possível recarregar a API (Verifique o Console).");
					}
					return;
				} else if (sbcmd.equalsIgnoreCase("setkey")) {
					if (args.length < 2)
						sender.sendMessage(ChatColor.RED + "Use: /" + getName() + " setkey <key>");
					else {
						String key = args[1];
						MineMarketBungee.getInstance().getConfiguration().set("key", key);
						MineMarketBungee.getInstance().saveConfig();
						sender.sendMessage(ChatColor.GOLD + "Key atualizada para: " + ChatColor.GREEN + key);
						sender.sendMessage(ChatColor.YELLOW + "Digite " + ChatColor.GOLD + "/MineMarket reload"
								+ ChatColor.YELLOW + " para carregar as alterações.");
					}
					return;
				} else if (sbcmd.equalsIgnoreCase("info")) {
					String message;
					MineMarketBaseAPI api = MineMarketBungee.getInstance().getApi();

					if (api == null) {
						message = ChatColor.RED + "Com ERRO -> Verifique o console imediatamente.";
					} else
						switch (api.getStatus()) {
						case OK:
							message = ChatColor.GREEN + "Funcionando corretamente";
							// message += "\n" + ChatColor.WHITE + "Nome da
							// store: " + ChatColor.AQUA +
							// api.getInfo().getStoreName();
							message += "\n" + ChatColor.WHITE + "KEY da API: " + ChatColor.AQUA + api.getKey();
							message += "\n" + ChatColor.WHITE + "Commandos pendentes: " + ChatColor.GOLD
									+ api.getPendingCommands().size();
							break;
						case INVALID_KEY:
							message = ChatColor.RED + "Key Invalida";
							message += "\n" + ChatColor.WHITE + "Mude sua key utilizando: " + ChatColor.GOLD + "/"
									+ getName() + " setkey";
							break;
						case BLOCKED_IP:
							message = ChatColor.RED + "Key Bloqueada";
							message += "\n" + ChatColor.WHITE
									+ "Acesse o painel e desbloqueie o acesso deste servidor com a API.";
							break;
						case UNCONFIRMED_IP:
							message = ChatColor.YELLOW + "Aguardando confirmacao da key";
							message += "\n" + ChatColor.WHITE
									+ "Acesse o painel e libere o acesso deste servidor com a API.";
							break;
						case CONNECTION_ERROR:
							message = ChatColor.RED + "Desconectado";
							message += "\n" + ChatColor.RED + "Nao foi possivel se conectar a API.";
							message += "\n" + ChatColor.WHITE
									+ "Verifique se a conexao do seu servidor com a internet esta funcionando.";
							break;
						default:
							message = "";
							break;
						}
					if (api.isUpdateAvailable()) {
						message += "\n" + ChatColor.GOLD
								+ "Existe uma nova versão disponível! Por favor atualize assim que possível, fazendo o download no nosso site.";
					}
					sender.sendMessage(ChatColor.WHITE + "Status do sistema: " + message);
					return;
				}
			}

			sender.sendMessage("===========================");
			sender.sendMessage(ChatColor.GOLD + "Ajuda - MineMarket");
			sender.sendMessage(ChatColor.AQUA + "Subcomandos disponíveis:");
			sender.sendMessage("");
			sender.sendMessage(ChatColor.GREEN + "/MineMarket setkey <key>" + ChatColor.WHITE
					+ " - Use para configurar a KEY de acesso à API.");
			sender.sendMessage(ChatColor.GREEN + "/MineMarket info" + ChatColor.WHITE + " - Veja o status do sistema.");
			sender.sendMessage(ChatColor.GREEN + "/MineMarket reload" + ChatColor.WHITE
					+ " - Recarrega as configurações do sistema.");
			sender.sendMessage("===========================");

		} else {
			sender.sendMessage(ChatColor.RED + "You don't have permission to configure the store.");
		}
	}

}

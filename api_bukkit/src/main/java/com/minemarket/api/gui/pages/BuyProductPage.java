package com.minemarket.api.gui.pages;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONException;

import com.minemarket.api.MineMarketBaseAPI;
import com.minemarket.api.MineMarketBukkit;
import com.minemarket.api.credits.CreditsManager;
import com.minemarket.api.credits.PlayerCredits;
import com.minemarket.api.credits.Product;
import com.minemarket.api.gui.PageLoader;
import com.minemarket.api.types.CommandType;
import com.minemarket.api.types.PendingCommand;

public class BuyProductPage implements PageLoader{

	@Override
	public String getPrefix() {
		return "_buyProduct";
	}

	@Override
	public void loadPage(Player player, String data) {
		int productID = Integer.valueOf(data);
		MineMarketBaseAPI api = MineMarketBukkit.getInstance().getApi();
		CreditsManager creditsManager = api.getCreditsManager();
		Product product = api.getProductManager().getProduct(productID);
		player.closeInventory();
		player.sendMessage(ChatColor.YELLOW + "Estamos processando seu pedido...");
		new BukkitRunnable() {
			
			@Override
			public void run() {
				try {
					if (creditsManager.loadCredits(player.getName(), player.getUniqueId())){
						PlayerCredits playerCredits = api.getCreditsManager().getPlayerCredits(player.getUniqueId());
						int credits = playerCredits == null ? 0 : playerCredits.getCredits();
						
						if (credits >= product.getPrice()){
							
							boolean success = creditsManager.updateCredits(playerCredits, credits - product.getPrice(), "Buying product " + product.getId() + "/" + product.getName());
							
							if (success){
								for (int x = 0; x < product.getCommands().length; x++){
									try {
										api.getCommandExecutor().executeCommand(new PendingCommand(-1, player.getUniqueId(), player.getName(), CommandType.CONSOLE, product.getCommands()[x], "", true));
									} catch (Exception e) {
										e.printStackTrace();
										// TODO: Command error logging on database
										player.sendMessage(ChatColor.RED + "Ocorreu um erro ao rodar o comando de ativação do produto " + ChatColor.GRAY + "(PRODUCT_ID=" + product.getId() + " , COMMAND_ID=" + x + ").");
										player.sendMessage(ChatColor.RED + "Entre em contato com um administrador do servidor e informe o problema.");
									}
								}
								player.sendMessage(ChatColor.GREEN + "Parabéns, compra bem-sucedida.");
							}
							
						} else {
							// TODO: load webstore url to buy credits
							player.sendMessage(ChatColor.RED + "Você não possui créditos suficientes."); // Compre mais em: http://URL");
						}
					} 
				} catch (JSONException | IOException e) {
					System.out.println("[MineMarket] Erro ao realizar compra do jogador " + player.getName());
					e.printStackTrace();
					player.sendMessage(ChatColor.RED + "Ocorreu um erro durante a compra, por favor entre em contato com os administradores do servidor e informe o problema.");
					
				}
			}
		}.runTaskAsynchronously(MineMarketBukkit.getInstance());

		
	}

}

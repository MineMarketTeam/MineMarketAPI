package com.minemarket.api.gui;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.Inventory;

import com.minemarket.api.MineMarketBaseAPI;
import com.minemarket.api.MineMarketBukkit;

/**
 * @author SAM_XPS
 * This class is a simple listener for the custom menu features.
 */
public class MenuListener implements Listener{
	
	@EventHandler
	public void onClick(InventoryClickEvent event){
		Inventory inv = event.getInventory();
		if (inv.getHolder() instanceof MenuHolder){
			event.setCancelled(true);
			if (event.getWhoClicked() instanceof Player){
				Player player = (Player) event.getWhoClicked();
				MenuItem clickedItem = ((MenuHolder) inv.getHolder()).getPage().getItem(event.getSlot());
				ItemAction action = clickedItem == null ? ItemAction.DO_NOTHING : clickedItem.getAction();
				if (action != null){
					switch (action) {
					case DO_NOTHING:
						break;
					case CLOSE_MENU:
						event.getWhoClicked().closeInventory();
						break;
					case CHANGE_PAGE:
						try {
							MineMarketBukkit.getInstance().getPageManager().openPage(clickedItem.getActionData(), player);
						} catch (NullPointerException e) {
							player.closeInventory();
							player.sendMessage(ChatColor.RED + "Página não encontrada.");
						}
						break;
					case OPEN_URL:
						player.sendMessage(ChatColor.GOLD + "Para prosseguir, acesse o link:");
						player.sendMessage(clickedItem.getActionData());
						break;
					default:
						break;
					}
				}
			}
		}
	}
	
	// TODO: Remove this event and use a CommandFramework to override Bukkit command configuration and register a custom command named as configured on the database.
	// This event is temporary and should be removed on future versions.
	@EventHandler
	public void onChat(PlayerCommandPreprocessEvent event){
		MineMarketBukkit mm = MineMarketBukkit.getInstance();
		if (event.getMessage().toLowerCase().startsWith("/" + mm.getApi().getMenuCommand())) {
			event.setCancelled(true);
			mm.getPageManager().openPage("_productList", event.getPlayer());
		}
	}

}

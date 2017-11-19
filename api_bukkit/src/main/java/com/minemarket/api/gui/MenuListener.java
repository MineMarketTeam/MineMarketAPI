package com.minemarket.api.gui;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.inventory.Inventory;

import com.minemarket.api.MineMarketBaseAPI;
import com.minemarket.api.MineMarketBukkit;

public class MenuListener implements Listener{
	
	@EventHandler
	public void onClick(InventoryClickEvent event){
		Inventory inv = event.getInventory();
		if (inv.getHolder() instanceof MenuHolder){
			event.setCancelled(true);
			if (event.getWhoClicked() instanceof Player){
				Player player = (Player) event.getWhoClicked();
				MenuHolder holder = (MenuHolder) inv.getHolder();
				MenuPage page = holder.getPage();
				MenuItem clickedItem = page.getItem(event.getSlot());
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
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event){
		if (event.getMessage().startsWith("menu")) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED + "adsasdsa");
			MineMarketBukkit.getInstance().getPageManager().openPage("_productList", event.getPlayer());
		}
	}

}

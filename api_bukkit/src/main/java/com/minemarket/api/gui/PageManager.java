package com.minemarket.api.gui;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.minemarket.api.MineMarketBaseAPI;
import com.minemarket.api.MineMarketBukkit;
import com.minemarket.api.credits.Product;
import com.minemarket.api.util.ItemUtils;

public class PageManager {

	private MineMarketBukkit main;
	private MineMarketBaseAPI api;
	private HashMap<String, MenuPage> pages = new HashMap<>();
	
	public PageManager(){
		this.main = MineMarketBukkit.getInstance();
		this.api = main.getApi();
	}
	
	public boolean addPage(MenuPage page) {
		if (!pages.keySet().contains(page.getPageName())) {
			pages.put(page.getPageName(), page);
			return true;
		}
		return false;
	}
	
	public void loadPages(){
		// TODO: Carregar informações de menu configurável através do painel
		
		Set<Product> products = api.getProductManager().getPluginProducts();
		MenuItem[] items = new MenuItem[products.size()];
		
		int x = 0;
		for (Product product : products) {
			items[x] = new MenuItem(
					ItemAction.CHANGE_PAGE, 
					"_buyproduct{" + product.getId() + "}", 
					ItemUtils.createItem(
							product.getDisplayItem(), 
							ChatColor.AQUA + product.getName(),
							ChatColor.GOLD + "Descrição: " + ChatColor.WHITE + product.getDescription(),
							ChatColor.GOLD + "Preço: " + ChatColor.WHITE + product.getPrice() + " creditos"
					)
			);
			x++;
		}
		
		// Adding default product page
		addPage(new MenuPage("_products", ChatColor.GOLD + "Loja de Produtos", 3, items));
	}
	
	public void openPage(MenuPage page, Player player) {
		// TODO: Abrir menu para jogador
	}
	
}

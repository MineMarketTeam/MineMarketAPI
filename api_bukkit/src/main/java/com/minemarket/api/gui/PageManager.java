package com.minemarket.api.gui;

import java.util.HashMap;
import java.util.Set;

import javax.swing.text.Utilities;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.minemarket.api.MineMarketBaseAPI;
import com.minemarket.api.MineMarketBukkit;
import com.minemarket.api.credits.PlayerCredits;
import com.minemarket.api.credits.Product;
import com.minemarket.api.util.ItemUtils;

import lombok.Getter;

public class PageManager {

	@Getter
	private MineMarketBukkit main;
	private MineMarketBaseAPI api;
	private HashMap<String, MenuPage> pages = new HashMap<>();
	
	public PageManager(MineMarketBukkit main){
		this.main = main;
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
		addPage(new MenuPage("$products", ChatColor.GOLD + "Loja de Produtos", 3, items));
	}
	
	public void openPage(String page, Player player) throws NullPointerException{
		if (page.startsWith("_")){
			// Handling custom automatic generated pages
			if (page.startsWith("_buyproduct{")){
				int productID = Integer.valueOf(page.substring(12).replace("}", ""));
				
				Product product = api.getProductManager().getProduct(productID);
				PlayerCredits playerCredits = api.getCreditsManager().getPlayerCredits(player.getUniqueId());
				int credits = playerCredits == null ? 0 : playerCredits.getCredits();
			
				MenuItem[] items = new MenuItem[27];

				items[0] = new MenuItem(ItemAction.CHANGE_PAGE, "$products", ItemUtils.createItem(Material.INK_SACK, (byte)8, "Voltar"));
				items[8] = new MenuItem(ItemAction.CLOSE_MENU, null, ItemUtils.createItem(Material.INK_SACK, (byte)1, "Fechar"));
				items[11] = new MenuItem(ItemAction.DO_NOTHING, null, ItemUtils.createItem(Material.PAPER, ChatColor.GOLD + "Descrição", product.getDescription()));
				items[15] = new MenuItem(ItemAction.CHANGE_PAGE, "_confirmbuyproduct{" + productID + "}", ItemUtils.createItem(Material.EMERALD, ChatColor.GREEN + "Comprar", ChatColor.GOLD + "Preço: " + product.getPrice(), ChatColor.GOLD + "Seus créditos: " + credits));
				
				new MenuPage(page, ChatColor.DARK_AQUA + "Comprar produto " + product.getName(), 3, items).openPage(player);
			}
		} else {
			pages.get(page).openPage(player);
		}
	}
	
}

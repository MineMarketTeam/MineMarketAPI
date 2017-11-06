package com.minemarket.api.gui.pages;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.minemarket.api.MineMarketBukkit;
import com.minemarket.api.credits.Product;
import com.minemarket.api.gui.ItemAction;
import com.minemarket.api.gui.MenuItem;
import com.minemarket.api.gui.MenuPage;
import com.minemarket.api.gui.PageLoader;
import com.minemarket.api.util.ItemUtils;

public class ProductListPage implements PageLoader {	
	
	private static final int rows = 4; // Should be greater or equal to 2
	
	@Override
	public String getPrefix() {
		return "_productList";
	}

	@Override
	public void loadPage(Player player, String data) {		
		Set<Product> products = MineMarketBukkit.getInstance().getApi().getProductManager().getPluginProducts();
		MenuItem[] items = new MenuItem[rows * 9];
		
		int pageNumber;
		try { 
			pageNumber = Integer.valueOf(data); 
		} catch (NumberFormatException | NullPointerException e) {
			pageNumber = 0;
		}
		
		int x = 0;
		for (Product product : products) {
			if (x < pageNumber * 9 * (rows - 1)); // Just skipping the first elements
			else if (x < (pageNumber + 1) * 9 * (rows - 1)){
				items[x - 9 * (pageNumber * (rows - 1) - 1)] = new MenuItem(
						ItemAction.CHANGE_PAGE, 
						"_productInfo{" + product.getId() + "}", 
						ItemUtils.createItem(
								product.getDisplayItem(), 
								ChatColor.AQUA + product.getName(),
								ChatColor.GOLD + "Descrição: " + ChatColor.WHITE + product.getDescription(),
								ChatColor.GOLD + "Preço: " + ChatColor.WHITE + product.getPrice() + " creditos"
						)
				);
			} 
			else break;
			x++;
		}
		
		if (pageNumber == 0){
			items[0] = new MenuItem(ItemAction.CLOSE_MENU, null, ItemUtils.createItem(Material.INK_SACK, (byte)1, "Fechar"));
		} else {
			items[0] = new MenuItem(ItemAction.CHANGE_PAGE, getCustomPrefixWithData(String.valueOf(pageNumber - 1)), ItemUtils.createItem(Material.INK_SACK, (byte) 7, "Página Anterior"));
		}
		
		if ((pageNumber + 1) * 9 * (rows - 1) <= products.size()){
			items[8] = new MenuItem(ItemAction.CHANGE_PAGE, getCustomPrefixWithData(String.valueOf(pageNumber + 1)), ItemUtils.createItem(Material.INK_SACK, (byte) 10, "Próxima Página"));
		}
		
		new MenuPage(getCustomPrefixWithData(data), ChatColor.DARK_AQUA + "Loja de Créditos (pg" + pageNumber + ")", 3, items).openPage(player);;
	}
	
	

}

package com.minemarket.api.gui.pages;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.minemarket.api.MineMarketBaseAPI;
import com.minemarket.api.MineMarketBukkit;
import com.minemarket.api.credits.PlayerCredits;
import com.minemarket.api.credits.Product;
import com.minemarket.api.gui.ItemAction;
import com.minemarket.api.gui.MenuItem;
import com.minemarket.api.gui.MenuPage;
import com.minemarket.api.gui.PageLoader;
import com.minemarket.api.util.ItemUtils;

public class ProductInfoPage implements PageLoader {

	@Override
	public String getPrefix() {
		return "_productInfo";
	}

	@Override
	public void loadPage(Player player, String data) {
		int productID = Integer.valueOf(data);
		
		MineMarketBaseAPI api = MineMarketBukkit.getInstance().getApi();
		
		Product product = api.getProductManager().getProduct(productID);
		PlayerCredits playerCredits = api.getCreditsManager().getPlayerCredits(player.getUniqueId());
		int credits = playerCredits == null ? 0 : playerCredits.getCredits();
	
		MenuItem[] items = new MenuItem[27];

		items[0] = new MenuItem(ItemAction.CHANGE_PAGE, "_productList", ItemUtils.createItem(Material.INK_SACK, (byte)8, "Voltar"));
		items[8] = new MenuItem(ItemAction.CLOSE_MENU, null, ItemUtils.createItem(Material.INK_SACK, (byte)1, "Fechar"));
		items[11] = new MenuItem(ItemAction.DO_NOTHING, null, ItemUtils.createItem(Material.PAPER, ChatColor.GOLD + "Descrição", product.getDescription()));
		items[15] = new MenuItem(ItemAction.CHANGE_PAGE, "_buyProduct{" + productID + "}", ItemUtils.createItem(Material.EMERALD, ChatColor.GREEN + "Comprar", ChatColor.GOLD + "Preço: " + product.getPrice(), ChatColor.GOLD + "Seus créditos: " + credits));
		
		new MenuPage(getCustomPrefixWithData(data), ChatColor.DARK_AQUA + "Comprar produto " + product.getName(), 3, items).openPage(player);
		
	}

}

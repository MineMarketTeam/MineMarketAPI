package com.minemarket.api.gui.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.constructor.BaseConstructor;

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
		PlayerCredits playerCredits = api.getCreditsManager().getPlayerCredits(player.getUniqueId(), player.getName());
		int credits = playerCredits == null ? 0 : playerCredits.getCredits();
	
		MenuItem[] items = new MenuItem[27];

		items[0] = new MenuItem(ItemAction.CHANGE_PAGE, "_productList", ItemUtils.createItem(Material.INK_SACK, (byte)8, "Voltar"));
		items[8] = new MenuItem(ItemAction.CLOSE_MENU, null, ItemUtils.createItem(Material.INK_SACK, (byte)1, "Fechar"));
		items[11] = new MenuItem(ItemAction.DO_NOTHING, null, ItemUtils.createItem(Material.PAPER, ChatColor.GOLD + "Descrição", wrapDescription(product.getDescription(), ChatColor.WHITE)));
		items[15] = new MenuItem(ItemAction.CHANGE_PAGE, "_buyProduct{" + productID + "}", ItemUtils.createItem(Material.EMERALD, 
				ChatColor.GREEN + "Comprar Produto", 
				ChatColor.GOLD + "Preço: " + ChatColor.WHITE + product.getPrice(), 
				ChatColor.GOLD + "Seus créditos: " + ChatColor.WHITE + credits, 
				(credits >= product.getPrice()) ? ChatColor.AQUA + "Saldo Final: " + ChatColor.WHITE + (credits - product.getPrice()) : ChatColor.RED + "Você não possui créditos suficientes" ));
		
		new MenuPage(getCustomPrefixWithData(data), ChatColor.DARK_AQUA + "Informações do Produto", 3, items).openPage(player);
		
	}
	
	public static List<String> wrapDescription(String description, ChatColor baseColor){
		StringTokenizer tok = new StringTokenizer(description, " ");
		List<String> lines = new ArrayList<>();
		String currentLine = "";
		while (tok.hasMoreTokens()){
			String word = tok.nextToken();
			currentLine += word + " ";
			if (currentLine.length() >= 16){
				lines.add(baseColor + currentLine.substring(0, currentLine.length() - 1));
				currentLine = "";
			}
		}

		if (currentLine.length() > 0){
			lines.add(baseColor + currentLine);
		}
		
		return lines;
	}
	
}

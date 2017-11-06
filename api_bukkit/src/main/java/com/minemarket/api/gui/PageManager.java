package com.minemarket.api.gui;

import java.util.HashMap;

import org.bukkit.entity.Player;

import com.minemarket.api.MineMarketBaseAPI;
import com.minemarket.api.MineMarketBukkit;
import com.minemarket.api.gui.pages.BuyProductPage;
import com.minemarket.api.gui.pages.ProductInfoPage;
import com.minemarket.api.gui.pages.ProductListPage;

import lombok.Getter;

public class PageManager {

	@Getter
	private MineMarketBukkit main;
	private MineMarketBaseAPI api;
	private HashMap<String, MenuPage> pages = new HashMap<>();
	private HashMap<String, PageLoader> pageLoaders = new HashMap<>();
	
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
	
	public boolean addPageLoader(PageLoader loader){
		if (!pageLoaders.keySet().contains(loader.getPrefix())) {
			pageLoaders.put(PageLoader.truncateData(loader.getPrefix()), loader);
			return true;
		}
		return false;
	}
	
	public void loadPages(){
		// TODO: Carregar informações de menu configurável através do painel
		addPageLoader(new ProductListPage());
		addPageLoader(new ProductInfoPage());
		addPageLoader(new BuyProductPage());
	}
	
	public void openPage(String page, Player player) throws NullPointerException{
		if (page.startsWith("_")){
			// Handling custom automatic generated pages
			String loaderName;
			if (pageLoaders.containsKey(loaderName = PageLoader.truncateData(page))){
				pageLoaders.get(loaderName).loadPage(player, PageLoader.getData(page));
			}
		} else {
			pages.get(page).openPage(player);
		}
	}
	
}

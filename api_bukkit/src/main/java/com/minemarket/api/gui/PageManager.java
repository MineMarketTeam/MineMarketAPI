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
	private HashMap<String, MenuPage> pages = new HashMap<>();
	private HashMap<String, PageLoader> pageLoaders = new HashMap<>();
	
	public PageManager(MineMarketBukkit main){
		this.main = main;
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
	
	public boolean openPage(String page, Player player){
		if (page.startsWith("_")){
			// Handling custom automatic generated pages
			String loaderName;
			if (pageLoaders.containsKey(loaderName = PageLoader.truncateData(page))){
				pageLoaders.get(loaderName).loadPage(player, PageLoader.getData(page));
				return true;
			}
		} else {
			if (pages.containsKey(page)) {
				pages.get(page).openPage(player);
				return true;
			}
		}
		return false;
	}
	
}

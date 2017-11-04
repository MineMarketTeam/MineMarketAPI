package com.minemarket.api.gui;

import java.util.HashMap;

import com.minemarket.api.MineMarketBaseAPI;
import com.minemarket.api.MineMarketBukkit;

public class PageManager {

	private MineMarketBukkit main;
	private MineMarketBaseAPI api;
	private String defaultPage;
	private HashMap<String, MenuPage> pages = new HashMap<>();
	
	public PageManager(){
		this.main = MineMarketBukkit.getInstance();
		this.api = main.getApi();
	}
	
	public void loadPages(){
		// TODO: Carregar informações de menu configurável através do painel
		
	}
	
}

package com.minemarket.api.gui;

import org.bukkit.entity.Player;

public interface PageLoader {
	
	/**
	 * Should return a prefix starting with the character _ (underscore) to be recognized by the PageManager
	 * @return a {@link String} containing its prefix 
	 */
	public String getPrefix();
	
	/**
	 * This method will be called when a page matching this loader's prefix is processed by PageManager.
	 * This method should generate a dynamic PageMenu and open it to the player. 
	 * @param player The {@link Player} that this page will be loaded for. 
	 * @param data The data between {} (brackets) on the requested page name
	 * or {@link null} if bracket were not found in the requested page name.
	 */
	public void loadPage(Player player, String data);
	
	/**
	 * This is the default way that custom data will be passed from and through the page management system.
	 */
	public default String getCustomPrefixWithData(String data){
		return getPrefix() + "{" + data + "}";
	}
	
	public static String getData(String page){
		String str = page;
		int substStart = str.indexOf("{");
		if (substStart > 0)
			return str.substring(substStart + 1, str.length() - 1);
		return "";
	}
	
	public static String truncateData(String pageName){
		String str = pageName.toLowerCase();
		int subsLimit = str.indexOf("{");
		if (subsLimit > 0)
			return str.substring(0, subsLimit);
		return str;
	}
	
}

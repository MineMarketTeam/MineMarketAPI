package com.minemarket.api.gui;

import org.bukkit.entity.Player;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MenuPage {

	private String pageName;
	private String pageTitle;
	private int rows;
	private MenuItem[] items;
	
	public MenuItem getItem(int index){
		if (index <= items.length && index >= 0)
			return items[index];
		return null;
	}
	
	public void openPage(Player player){
		player.openInventory(new MenuHolder(this).getInventory());
	}
	
}

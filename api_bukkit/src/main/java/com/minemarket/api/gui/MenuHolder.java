package com.minemarket.api.gui;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import lombok.Getter;

public class MenuHolder implements InventoryHolder{

	private Inventory inv;
	@Getter
	private MenuPage page;
	
	public MenuHolder(MenuPage page) {
		this.page = page;
		inv = Bukkit.createInventory(this, page.getRows() * 9, page.getPageTitle());
	}
	
	@Override
	public Inventory getInventory() {
		return inv;
	}
	
}

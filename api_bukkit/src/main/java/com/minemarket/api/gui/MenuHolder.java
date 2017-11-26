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
		inv = Bukkit.createInventory(this, page.getRows() * 9, limitTitle());
		
		for (int x = 0; x < Math.min(page.getRows() * 9, page.getItems().length); x++){
			try {
				inv.setItem(x, page.getItem(x).getDisplayItem());
			} catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
				
			}
		}
	}
	
	@Override
	public Inventory getInventory() {
		return inv;
	}
	
	private String limitTitle() {
		if (page.getPageTitle().length() > 32)
			return page.getPageTitle().substring(0, 32);
		return page.getPageTitle();
	}
	
}

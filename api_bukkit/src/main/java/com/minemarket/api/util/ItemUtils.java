package com.minemarket.api.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemUtils {

	public static List<String> wrapLore(String[] lore) {
		List<String> list = new ArrayList<>();
		for (int x = 0; x < lore.length; x++)
			list.add(lore[x]);
		return list;
	}

	public static ItemStack createItem(int type, Byte data, String name, String... lore) {
		return createItem(type, data, name, wrapLore(lore));
	}  
	
	public static ItemStack createItem(int type, String name, String... lore) {
		return createItem(type, name, wrapLore(lore));
	}
	
	public static ItemStack createItem(Material type, String name, String... lore) {
		return createItem(type, name, wrapLore(lore));
	}
	
	public static ItemStack createItem(Material type, Byte data, String name, String... lore) {
		return createItem(type, data, name, wrapLore(lore));
	}  
	
	public static ItemStack createItem(int type, String name, List<String> lore) {
		return updateItemMeta(new ItemStack(type), name, lore);
	}
	
	public static ItemStack createItem(int type, Byte data, String name, List<String> lore) {
		return updateItemMeta(new ItemStack(type, 1, (short)0, data), name, lore);
	}
	
	public static ItemStack createItem(Material type, String name, List<String> lore) {
		return updateItemMeta(new ItemStack(type), name, lore);
	}
	
	public static ItemStack createItem(Material type, Byte data, String name, List<String> lore) {
		return updateItemMeta(new ItemStack(type, 1, (short)0, data), name, lore);
	}
	
	public static ItemStack updateItemMeta(ItemStack item, String name, List<String> lore) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
}

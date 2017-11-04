package com.minemarket.api.gui;

import org.bukkit.inventory.ItemStack;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MenuItem {

	private ItemAction action;
	private String actionData;
	private ItemStack displayItem;
	
}

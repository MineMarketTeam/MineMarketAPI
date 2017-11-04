package com.minemarket.api.gui;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MenuPage {

	private String pageName;
	private String pageTitle;
	private int rows;
	private MenuItem[] items;
	
}

package com.minemarket.api.credits;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Product {

	private int id;
	private String name, description;
	private int price, displayItem;
	private String[] commands;
	
}

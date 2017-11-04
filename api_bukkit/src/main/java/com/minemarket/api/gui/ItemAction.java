package com.minemarket.api.gui;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ItemAction {

	DO_NOTHING	(0),
	CLOSE_MENU	(1),
	CHANGE_PAGE	(2),
	OPEN_URL	(3);
	
	private int id;
	
	public static ItemAction getByID(int id){
		for (ItemAction ia : values())
			if (ia.id == id) 
				return ia;
		return null;
	}
	
}

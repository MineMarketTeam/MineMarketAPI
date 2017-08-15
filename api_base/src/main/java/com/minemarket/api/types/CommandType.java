package com.minemarket.api.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CommandType {
	
	NORMAL(0), CONSOLE(1), OP(2);
	
	private int id;
	
	public static CommandType getByID(int id){
		for (CommandType t : values())
			if (t.getId() == id)
				return t;
		return null;
	}
	
	public static CommandType getByName(String name){
		for (CommandType t : values())
			if (t.name().equalsIgnoreCase(name))
				return t;
		return null;
	}
	
	public static CommandType getDefault(){
		return CommandType.CONSOLE;
	}
	

}

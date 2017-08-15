package com.minemarket.api.types;

import java.util.UUID;
import org.json.JSONObject;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PendingCommand {
	
	private int commandID;
	private UUID playerUUID;
	private String playerName;
	private CommandType commandType;
	private String commandLine;
	private String custom;
	
	private static UUID parseUUID(String input){
		if (input == null || input.length() == 0)
			return null;
		return UUID.fromString(input);
	}
	
	public static PendingCommand fromJSON(JSONObject j){
		return new PendingCommand(j.getInt("ID"), parseUUID(j.getString("UUID")), j.getString("NICK"), CommandType.getByID(j.getInt("TYPE")), j.getString("COMMAND"), j.getString("CUSTOM"));
	}

}

package com.minemarket.api.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.json.JSONObject;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class PendingCommand {

    private int commandID;
    private UUID playerUUID;
    private String playerName;
    private CommandType commandType;
    private String commandLine;
    private String custom;
    private boolean requireOnline;
    private Object credits;

    private static UUID parseUUID(String input) {
        if (input == null || input.length() == 0)
            return null;
        return UUID.fromString(input);
    }

    public static PendingCommand fromJSON(JSONObject j) {
        return new PendingCommand(j.getInt("ID"), parseUUID(j.getString("UUID")), j.getString("NICK"), CommandType.getByID(j.getInt("TYPE")), j.getString("COMMAND"), j.getString("CUSTOM"), j.getBoolean("REQUIRE_ONLINE"), -1);
    }

}

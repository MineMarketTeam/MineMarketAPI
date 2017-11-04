package com.minemarket.api.credits;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.minemarket.api.MineMarketBaseAPI;
import com.minemarket.api.utils.JSONResponse;
import com.minemarket.api.utils.JsonUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreditsManager {

	private final MineMarketBaseAPI api;
	private HashMap<Integer, PlayerCredits> credits = new HashMap<Integer, PlayerCredits>();

	public PlayerCredits getPlayerCredits(String nick){
		for (PlayerCredits plc : credits.values()){
			if (nick.equalsIgnoreCase(plc.nick))
				return plc;
		}
		return null;
	}
	
	public PlayerCredits getPlayerCredits(UUID uuid){
		for (PlayerCredits plc : credits.values()){
			if (plc.uuid == uuid)
				return plc;
		}
		return null;
	}
	
	public boolean loadAllCredits() throws JSONException, IOException{
		return loadCredits("ALL", "");
	}	
	
	public boolean loadCredits(String nick, UUID uuid) throws JSONException, IOException{
		return loadCredits(nick, uuid.toString());
	}
	
	public boolean loadCredits(String nick, String uuid) throws JSONException, IOException{
		
		JSONResponse response;
		if (api.verifyResponse(response = api.loadResponse("player_credits", api.getKeyData() + "&NICK=" + nick + "&UUID=" + uuid))){
			JSONArray jcredits = response.getData().getJSONArray("PLAYER_CREDITS");
			for (int x = 0; x < jcredits.length(); x++){
				
				JSONObject jobj = jcredits.getJSONObject(x);
				
				try {
					Integer credits = jobj.getInt("CREDITS"), id = jobj.getInt("ID");
					String jnick = jobj.getString("NICK"), suid = jobj.getString("UUID");
					UUID juuid = suid.length() == 36 ? juuid = UUID.fromString(suid) : null;
					long time = Timestamp.valueOf(jobj.getString("TIME")).getTime();
					
					if (this.credits.containsKey(id)){
						PlayerCredits pcredits = this.credits.get(id);
						pcredits.credits = credits;
						pcredits.uuid = juuid;
						pcredits.lastUpdate = time;
					} else {
						this.credits.put(id, new PlayerCredits(juuid, jnick, credits, time));
					}
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}					

			}
			return true;
		}
		return false;
	}
	
}

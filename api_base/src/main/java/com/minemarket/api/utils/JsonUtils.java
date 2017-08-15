package com.minemarket.api.utils;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.minemarket.api.types.KeyStatus;
import com.minemarket.api.types.PendingCommand;

public class JsonUtils {
	
	public static ArrayList<String> loadStringList(JSONArray ja) throws JSONException{
		ArrayList<String> list = new ArrayList<>();
		for (int i = 0; i < ja.length(); i++){
			list.add(ja.getString(i));
		}
		return list;
	}
	
	public static ArrayList<String> loadPendingPlayers(JSONResponse jr) throws JSONException{
		JSONArray ja = jr.getData().getJSONArray("PENDING_PLAYERS");
		
		ArrayList<String> list = new ArrayList<>();
		for (int i = 0; i < ja.length(); i++){
			list.add(ja.getString(i));
		}
		return list;
	}
	
	public static ArrayList<PendingCommand> loadPendingCommands(JSONResponse jr) throws JSONException{
		JSONArray ja = jr.getData().getJSONArray("PENDING_COMMANDS");
		
		ArrayList<PendingCommand> list = new ArrayList<>();
		for (int i = 0; i < ja.length(); i++){
			list.add(PendingCommand.fromJSON(ja.getJSONObject(i)));
		}
		return list;
	}
	
	public static JSONResponse loadResponse(JSONObject obj) throws JSONException{
		KeyStatus ks = KeyStatus.getByName(obj.getString("key_status"));		
		return new JSONResponse(ks, obj.optJSONObject("data"), obj.optJSONArray("errors"));
	}
	
	public static JSONResponse createResponse(String source) throws JSONException{
		return loadResponse(createObject(source));
	}
	
	public static JSONObject createObject(String jsonCode) throws JSONException{
		return new JSONObject(jsonCode);
	}
	
}

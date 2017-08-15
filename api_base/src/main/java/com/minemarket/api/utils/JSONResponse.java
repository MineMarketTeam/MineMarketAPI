package com.minemarket.api.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import com.minemarket.api.types.KeyStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JSONResponse {

	private KeyStatus keyStatus;
	private JSONObject data;
	private JSONArray errors;
	
}

package com.minemarket.api.utils;

import com.minemarket.api.types.KeyStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;

@Getter
@AllArgsConstructor
public class JSONResponse {

    private KeyStatus keyStatus;
    private String serverType;
    private JSONObject data;
    private JSONArray errors;

}

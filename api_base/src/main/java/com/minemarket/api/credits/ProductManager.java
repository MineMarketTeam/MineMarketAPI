package com.minemarket.api.credits;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.minemarket.api.MineMarketBaseAPI;
import com.minemarket.api.utils.JSONResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductManager {

	private final MineMarketBaseAPI api;
	private Set<Product> products;

	public Set<Product> getPluginProducts(){
		if (products == null){
			try {
				loadProducts();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return new HashSet<>(products);
	}
	
	public boolean loadProducts() throws JSONException, IOException{
		JSONResponse response;
		if (api.verifyResponse(response = api.loadResponse("plugin_produtos", api.getKeyData()))){
			JSONArray jproducts = response.getData().getJSONArray("PLUGIN_PRODUTOS");
			this.products = new HashSet<>();
			for (int x = 0; x < jproducts.length(); x++){
				
				JSONObject jobj = jproducts.getJSONObject(x);
				
				try {
					
					int id = jobj.getInt("ID");
					String name = jobj.getString("NOME");
					String description = jobj.getString("DESCRICAO");
					int price = jobj.getInt("PRECO");
					int displayItem = jobj.getInt("DISPLAY_ITEM");
					JSONArray jcommands = jobj.getJSONArray("COMANDOS");
					String[] commands = new String[jcommands.length()];
					
					for (int i = 0; i < commands.length; i++)
						commands[i] = jcommands.getJSONObject(i).getString("COMANDO");
					
					this.products.add(new Product(id, name, description, price, displayItem, commands));
					
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

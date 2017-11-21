package com.minemarket.api;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.minemarket.api.credits.CreditsManager;
import com.minemarket.api.credits.ProductManager;
import com.minemarket.api.exceptions.AfterExecutionCommandUpdateFailureException;
import com.minemarket.api.types.ConnectionStatus;
import com.minemarket.api.types.KeyStatus;
import com.minemarket.api.types.PendingCommand;
import com.minemarket.api.utils.HttpUtils;
import com.minemarket.api.utils.JSONResponse;
import com.minemarket.api.utils.JsonUtils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author SAM_XPS
 */
@RequiredArgsConstructor
@Getter
public class MineMarketBaseAPI {

	private static final String prefix = "[MineMarketAPI] ";
	
	private final String apiURL;
	private final String key;
	private final String version;
	private final String serverType;
	private final BaseTaskScheduler scheduler;
	private final BaseCommandExecutor commandExecutor;
	private final BaseUpdater updater;
	
	private ArrayList<PendingCommand> pendingCommands;
	private CreditsManager creditsManager = new CreditsManager(this);
	private ProductManager productManager = new ProductManager(this);

	private boolean updateAvailable = false;
	private ConnectionStatus status;	
	private String storeName;
	private String storeURL;
	
	/**
	 * Metódo que inicializa a conexão com a API, e retorna o status da tentativa.
	 * @return {@link ConnectionStatus} o status da conexão.
	 */
	public ConnectionStatus initialize(){
		JSONResponse response = null;
		try {
			response = loadResponse("api_status", getKeyData());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (response != null){
			if (response.getKeyStatus() == KeyStatus.VALID){
				if (response.getServerType().equalsIgnoreCase(this.serverType)){
					
					// Let's get info about the store, such as its NAME and URL
					loadStoreInfo();
					
					Runnable task;
					
					// Scheduling a task to update our data every minute
					// This is good because we keep a local copy and avoid creating unnecessary connections every time a player joins
					scheduler.scheduleAsyncRepeatingTask(task = new Runnable() {
						
						@Override
						public void run() {
							loadPendingCommands();
							loadPlayerCreditsDatabase();
							loadPluginProducts();
						}
					}, 60, 60);
					
					// Everything looks fine, so let's load our data
					task.run();
					
					System.out.println(prefix + " Sistema iniciado. Versão atual: " + version);
					
					if (!version.equalsIgnoreCase(response.getData().getString("CURRENT_VERSION"))){
						System.out.println(prefix + "Você está usando uma versão desatualizada! por favor baixe a versão " + response.getData().getString("CURRENT_VERSION"));
						updateAvailable = true;
					}

					status = ConnectionStatus.OK;
				} else {
					System.out.println(prefix + "Sistema nao foi inicializado porque sua key não está configurada para servidores " + this.serverType);
					System.out.println(prefix + "Por favor, crie uma nova key " + this.serverType + " no painel.");
					status = ConnectionStatus.WRONG_SERVER_TYPE;
				}
			} else if (response.getKeyStatus() == KeyStatus.BLOCKED){
				System.out.println(prefix + "Sistema nao foi inicializado porque sua key foi bloqueada! "
						+ "Entre no painel e desbloqueie a conexao deste ip com a API.");
				status = ConnectionStatus.BLOCKED_IP;
			} else if (response.getKeyStatus() == KeyStatus.WAITING_VALIDATION){
				System.out.println(prefix + "Sistema nao foi inicializado porque sua key ainda nao foi validada! "
						+ "Entre no painel e permita esse ip se conectar com a API.");
				status = ConnectionStatus.UNCONFIRMED_IP;
			} else if (response.getKeyStatus() == KeyStatus.INVALID || response.getKeyStatus() == KeyStatus.UNKNOWN) {
				System.out.println(prefix + "Sistema nao foi inicializado porque sua key é inválida. "
						+ "Por favor, verifique-a em suas configuracoes.");
				status = ConnectionStatus.INVALID_KEY;
			}
		} else {
			System.out.println(prefix + "Sistema nao foi inicializado devido a falha de conexao.");
			System.out.println(prefix + "Verifique os erros no console e a URL da API nas suas configuracoes.");
			status = ConnectionStatus.CONNECTION_ERROR;
		}
		return status;
	}
	
	public void onPlayerJoin(UUID uuid, String name){
		scheduler.runTaskAsynchronously(new Runnable() {
			@Override
			public void run() {
				try {
					verifyPendingCommands(uuid, name);
				} catch (AfterExecutionCommandUpdateFailureException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	protected boolean updateCommandInfo(int id){
		JSONResponse response;
		if (verifyResponse(response = getResponse("update_command", getKeyData() + "&COMMAND_ID=" + id))){
			return response.getData().getString("COMMAND_UPDATE_RESULT").equalsIgnoreCase("SUCCESS");
		}
		return false;
	}

	protected boolean loadPlayerCreditsDatabase(){
		try {
			return creditsManager.loadAllCredits();
		} catch (JSONException | IOException e) {
			System.out.println(prefix + " Erro ao carregar créditos dos jogadores.");
			e.printStackTrace();
			return false;
		}
	}
	
	protected boolean loadPluginProducts(){
		try {
			return productManager.loadProducts();
		} catch (JSONException | IOException e) {
			System.out.println(prefix + " Erro ao carregar produtos que podem ser comprados com créditos.");
			e.printStackTrace();
			return false;
		}
	}
	
	protected boolean loadStoreInfo(){
		try {
			JSONResponse response;
			if (verifyResponse(response = loadResponse("store_info", getKeyData()))){
				JSONObject info = response.getData().getJSONObject("STORE_INFO");
				
				this.storeName = info.getString("STORE_NAME");
				this.storeURL = info.getString("STORE_URL");
				
				return true;
			};
		} catch (JSONException | IOException e) {
			System.out.println(prefix + " Erro ao carregar informações da loja.");
			e.printStackTrace();
		}
		return false;
	}
	
	public synchronized void loadPendingCommands(){ 
		JSONResponse response;
		if (verifyResponse(response = getResponse("pending_commands", getKeyData()))){
			pendingCommands = JsonUtils.loadPendingCommands(response);
			
			scheduler.runTaskAsynchronously(new Runnable() {
				@Override
				public void run() {
					List<PendingCommand> runCommands = new ArrayList<>();
					
					for (PendingCommand pc : pendingCommands){
						try{
							if (!pc.isRequireOnline() || commandExecutor.isPlayerOnline(pc.getPlayerUUID(), pc.getPlayerName())){
								if (commandExecutor.executeCommand(pc)){
									runCommands.add(pc);
								}
							}
						} catch (Exception e){
							e.printStackTrace();
						}
					}
					
					for (PendingCommand pc : runCommands){
						try {
							handleRunCommand(pc);
						} catch (AfterExecutionCommandUpdateFailureException e) {
							e.printStackTrace();
						}
					}
				}
			});
			
		}
	}
	
	public synchronized void verifyPendingCommands(UUID uuid, String name) throws AfterExecutionCommandUpdateFailureException{
		for (PendingCommand pc : new ArrayList<>(getPendingCommands())){
			if (((pc.getPlayerUUID() == null && pc.getPlayerName().equalsIgnoreCase(name)) || uuid.equals(pc.getPlayerUUID())) && commandExecutor.executeCommand(pc)){
				handleRunCommand(pc);
			}
		}
	}
	
	public synchronized void handleRunCommand(PendingCommand pc) throws AfterExecutionCommandUpdateFailureException{
		if (!updateCommandInfo(pc.getCommandID())){
			throw new AfterExecutionCommandUpdateFailureException(pc);
		}
		pendingCommands.remove(pc);
	}
	
	public boolean verifyResponse(JSONResponse jr){
		if (jr != null)
			if (jr.getKeyStatus() == KeyStatus.VALID)
				if (jr.getErrors().length() == 0)
					return true;
				else for(int i = 0; i < jr.getErrors().length(); i++)
					System.out.println(prefix + "Erro: " + jr.getErrors().getString(i));
			else if (jr.getKeyStatus() == KeyStatus.WAITING_VALIDATION)
				System.out.println(prefix + "Sua key ainda não foi validada.");
			else 
				System.out.println(prefix + "Sua key e invalida.");
		return false;
	}
	
	public JSONResponse getResponse(String page, String data){
		try{ 
			return loadResponse(page, data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public JSONResponse loadResponse(String page, String data) throws IOException, JSONException{
		if (!page.toLowerCase().endsWith(".php"))
			page += ".php";
		HttpURLConnection conn = HttpUtils.createConnection(apiURL + page, HttpUtils.encodeUTFData(data));
		String src = HttpUtils.readSourceCode(conn);
		return JsonUtils.createResponse(src);
	}

	public String getKeyData(){
		return "key=" + this.key;
	}
	
}

package com.minemarket.api;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import com.minemarket.api.exceptions.AfterExecutionCommandUpdateFailureException;
import com.minemarket.api.types.KeyStatus;
import com.minemarket.api.types.PendingCommand;
import com.minemarket.api.utils.HttpUtils;
import com.minemarket.api.utils.JSONResponse;
import com.minemarket.api.utils.JsonUtils;

import lombok.AllArgsConstructor;
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
	private final BaseTaskScheduler scheduler;
	private final BaseCommandExecutor commandExecutor;
	private final BaseUpdater updater;
	private ArrayList<PendingCommand> pendingCommands;
	private APIStatus status;
	private boolean updateAvailable = false;
	
	/**
	 * Met�do que inicializa a conex�o com a API, e retorna o status da tentativa.
	 * @return {@link APIStatus} o status da conex�o.
	 */
	public APIStatus initialize(){
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
				
				loadPendingCommands();
				
				System.out.println(prefix + " Sistema iniciado, voc� est� utilizando a vers�o " + version);
				scheduler.scheduleAsyncRepeatingTask(new Runnable() {
					
					@Override
					public void run() {
						loadPendingCommands();
					}
				}, 60, 60);
				
				if (!version.equalsIgnoreCase(response.getData().getString("CURRENT_VERSION"))){
					System.out.println(prefix + "Voc� est� usando uma vers�o desatualizada! por favor baixe a vers�o " + response.getData().getString("CURRENT_VERSION"));
					updateAvailable = true;
				}
				
				status = APIStatus.OK;
			} else if (response.getKeyStatus() == KeyStatus.BLOCKED){
				System.out.println(prefix + "Sistema nao foi inicializado porque sua key foi bloqueada! "
						+ "Entre no painel e desbloqueie a conexao deste ip com a API.");
				status = APIStatus.BLOCKED_IP;
			} else if (response.getKeyStatus() == KeyStatus.WAITING_VALIDATION){
				System.out.println(prefix + "Sistema nao foi inicializado porque sua key ainda nao foi validada! "
						+ "Entre no painel e permita esse ip se conectar com a API.");
				status = APIStatus.UNCONFIRMED_IP;
			} else if (response.getKeyStatus() == KeyStatus.INVALID || response.getKeyStatus() == KeyStatus.UNKNOWN) {
				System.out.println(prefix + "Sistema nao foi inicializado porque sua key � inv�lida. "
						+ "Por favor, verifique-a em suas configuracoes.");
				status = APIStatus.INVALID_KEY;
			}
		} else {
			System.out.println(prefix + "Sistema nao foi inicializado devido a falha de conexao.");
			System.out.println(prefix + "Verifique os erros no console e a URL da API nas suas configuracoes.");
			status = APIStatus.CONNECTION_ERROR;
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
	
	public synchronized void loadPendingCommands(){ 
		JSONResponse response;
		if (verifyResponse(response = getResponse("pending_commands", getKeyData()))){
			pendingCommands = JsonUtils.loadPendingCommands(response);
		}
	}
	
	public synchronized void verifyPendingCommands(UUID uuid, String name) throws AfterExecutionCommandUpdateFailureException{
		for (PendingCommand pc : new ArrayList<>(getPendingCommands())){
			if (((pc.getPlayerUUID() == null && pc.getPlayerName().equalsIgnoreCase(name)) || uuid.equals(pc.getPlayerUUID())) && commandExecutor.executeCommand(pc)){
				if (!updateCommandInfo(pc.getCommandID())){
					throw new AfterExecutionCommandUpdateFailureException(pc);
				}
				pendingCommands.remove(pc);
			}
		}
	}
	
	private boolean verifyResponse(JSONResponse jr){
		if (jr != null)
			if (jr.getKeyStatus() == KeyStatus.VALID)
				if (jr.getErrors().length() == 0)
					return true;
				else for(int i = 0; i < jr.getErrors().length(); i++)
					System.out.println(prefix + "Erro: " + jr.getErrors().getString(i));
			else if (jr.getKeyStatus() == KeyStatus.WAITING_VALIDATION)
				System.out.println(prefix + "Sua key ainda n�o foi validada.");
			else 
				System.out.println(prefix + "Sua key e invalida.");
		return false;
	}
	
	private JSONResponse getResponse(String page, String data){
		try{ 
			return loadResponse(page, data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private JSONResponse loadResponse(String page, String data) throws IOException, JSONException{
		if (!page.toLowerCase().endsWith(".php"))
			page += ".php";
		HttpURLConnection conn = HttpUtils.createConnection(apiURL + page, HttpUtils.encodeUTFData(data));
		String src = HttpUtils.readSourceCode(conn);
		return JsonUtils.createResponse(src);
	}

	public String getKeyData(){
		return "key=" + this.key;
	}
	
	/**
	 * Todos os STATUS poss�veis para a conex�o com a API.
	 */
	public enum APIStatus {
		/**
		 * A conex�o foi bem sucedida e a key � v�lida.
		 */
		OK,
		/**
		 * A key utilizada � inv�lida.
		 */
		INVALID_KEY,
		/**
		 * O IP utilizado para a conex�o foi bloqueado para esta key.
		 */
		BLOCKED_IP, 
		/**
		 * O IP utilizado ainda n�o foi autorizado.
		 */
		UNCONFIRMED_IP, 
		/**
		 * Um erro ocorreu ao se conectar com a API.
		 */
		CONNECTION_ERROR, 
	}
	
}

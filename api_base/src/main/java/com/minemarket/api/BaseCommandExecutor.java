package com.minemarket.api;

import java.util.UUID;

import com.minemarket.api.types.PendingCommand;

/**
 * Esta interface deve ser implementada para realizar a execu��o dos {@link PendingCommand}s.
 */
public interface BaseCommandExecutor {

	/**
	 * Retorna a linha de comando que deve ser executada, substituindo os devidos valores de %name%, %uuid% ou %custom%
	 * @param command {@link PendingCommand} contendo as informa��es a serem utilizadas na substitui��o.
	 * @return Uma {@link String} onde as express�es vari�veis foram substituidas de acordo com o comando.
	 */
	public default String getCommandLine(PendingCommand command){
		String cmd = command.getCommandLine();
		String[] playerNameAlias = new String[] {"%name%", "%nick%", "%player%", "%nickname%"}; // Lista de aliases para a vari�vel do nome do jogador.
		
		for (String alias : playerNameAlias)
			cmd = cmd.replaceAll(alias, command.getPlayerName());
		
		if (command.getPlayerUUID() != null) 
			cmd = cmd.replaceAll("%uuid%", command.getPlayerUUID().toString());
		
		cmd = cmd.replaceAll("%custom%", command.getCustom());
		
		return cmd;
	}
	
	/**
	 * @param command O comando a ser executado
	 * @return <b>true</b> Se o comando for executado com sucesso <br>
	 * <b>false</b> Caso ocorra alguma falha durante a execu��o
	 */
	public abstract boolean executeCommand(PendingCommand command);
	
	public abstract boolean isPlayerOnline(UUID uuid);

	public abstract boolean isPlayerOnline(String name);
	
	public default boolean isPlayerOnline(UUID uuid, String name){
		return uuid == null ? isPlayerOnline(name) : isPlayerOnline(uuid);
	}
	
}

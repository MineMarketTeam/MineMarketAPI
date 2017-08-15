package com.minemarket.api;

import com.minemarket.api.types.PendingCommand;

public interface BaseCommandExecutor {

	/**
	 * Retorna a linha de comando que deve ser executada, substituindo os devidos valores de %name%, %uuid% ou %custom%
	 * @param command
	 * @return {@link String} 
	 */
	public default String getCommandLine(PendingCommand command){
		String cmd = command.getCommandLine();
		String[] playerNameAlias = new String[] {"%name%", "%nick%", "%player%"}; // Lista de aliases para a variável do nome do jogador.
		
		for (String alias : playerNameAlias)
			cmd = cmd.replaceAll(alias, command.getPlayerName());
		
		if (command.getPlayerUUID() != null) 
			cmd = cmd.replaceAll("%uuid%", command.getPlayerUUID().toString());
		
		cmd = cmd.replaceAll("%custom%", command.getCustom());
		
		return cmd;
	}
	
	public abstract boolean executeCommand(PendingCommand command);
	
}

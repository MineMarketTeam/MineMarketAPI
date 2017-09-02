package com.minemarket.api.types;

/**
 * Todos os STATUS possíveis para a conexão com a API.
 */
public enum ConnectionStatus {
	
	/**
	 * A conexão foi bem sucedida e a key é válida.
	 */
	OK,
	
	/**
	 * O tipo de servidor relacionado com a key está diferente.
	 */
	WRONG_SERVER_TYPE,
	
	/**
	 * A key utilizada é inválida.
	 */
	INVALID_KEY,
	
	/**
	 * O IP utilizado para a conexão foi bloqueado para esta key.
	 */
	BLOCKED_IP, 
	
	/**
	 * O IP utilizado ainda não foi autorizado.
	 */
	UNCONFIRMED_IP, 
	
	/**
	 * Um erro ocorreu ao se conectar com a API.
	 */
	CONNECTION_ERROR, 
}

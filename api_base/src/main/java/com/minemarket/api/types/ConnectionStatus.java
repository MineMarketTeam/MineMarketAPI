package com.minemarket.api.types;

/**
 * Todos os STATUS poss�veis para a conex�o com a API.
 */
public enum ConnectionStatus {

    /**
     * A conex�o foi bem sucedida e a key � v�lida.
     */
    OK,

    /**
     * O tipo de servidor relacionado com a key est� diferente.
     */
    WRONG_SERVER_TYPE,

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

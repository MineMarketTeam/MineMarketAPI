package com.minemarket.api;

import java.util.HashMap;

/**
 * <p>Esta interface � respons�vel em lidar com o agendamento e a repeti��o cont�nua de tarefas.</p>
 * <p>Esta interface foi baseada em um TaskScheduler implementado no projeto Bukkit.</p>
 * <p><b>Nota: </b>O tempo das opera��es realizadas ser�o sempre ser em <b>segundos</b>.
 * <p><b>Nota: </b>A interface deve lidar com o registro e <b>cancelamento</b> de tarefas, para isso deve conter um {@link HashMap} contendo todas as tarefas sendo executadas.</p>
 */
public interface BaseTaskScheduler {

    /**
     * Executa uma tarefa fora do Thread principal.
     *
     * @param task Tarefa a ser executada.
     */
    void runTaskAsynchronously(Runnable task);

    /**
     * Executa uma tarefa dentro do Thread principal da aplica��o.
     *
     * @param task Tarefa a ser executada.
     */
    void runTaskSynchronously(Runnable task);

    /**
     * Programa uma tarefa para ser executada fora do Thread principal depois de um certo per�odo de tempo.
     *
     * @param task  Tarefa a ser executada
     * @param delay Tempo em <b>segundos</b> que se deve esperar antes de realizar a tarefa.
     */
    void scheduleAsyncDelayedTask(Runnable task, int delay);

    /**
     * Programa uma tarefa para ser executada periodicamente fora do Thread principal, ap�s um delay inicial.
     *
     * @param task     Tarefa a ser executada
     * @param delay    Tempo em <b>segundos</b> que se deve esperar antes de realizar a tarefa.
     * @param repeatIn Tempo em <b>segundos</b> que haver� entre cada execu��o.
     */
    void scheduleAsyncRepeatingTask(Runnable task, int delay, int repeatIn);

    /**
     * Programa uma tarefa para ser executada dentro do Thread principal depois de um certo per�odo de tempo.
     *
     * @param task  Tarefa a ser executada
     * @param delay Tempo em <b>segundos</b> que se deve esperar antes de realizar a tarefa.
     */
    void scheduleSyncDelayedTask(Runnable task, int delay);

    /**
     * Programa uma tarefa para ser executada periodicamente dentro do Thread principal, ap�s um delay inicial.
     *
     * @param task     Tarefa a ser executada
     * @param delay    Tempo em <b>segundos</b> que se deve esperar antes de realizar a tarefa.
     * @param repeatIn Tempo em <b>segundos</b> que haver� entre cada execu��o.
     */
    void scheduleSyncRepeatingTask(Runnable task, int delay, int repeatIn);

    /**
     * Esta fun��o deve cancelar todas as tarefas que foram instanciadas dentro da interface,
     * para que n�o haja uso excessivo de mem�ria ou tarefas pendentes mesmo ap�s a finaliza��o de execu��o da instancia da API.
     */
    void cancelTasks();

}

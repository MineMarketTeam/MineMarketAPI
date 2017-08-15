package com.minemarket.api;

import java.util.HashMap;

/**
 * <p>Esta interface é responsável em lidar com o agendamento e a repetição contínua de tarefas.</p>
 * <p>Esta interface foi baseada em um TaskScheduler implementado no projeto Bukkit.</p>
 * <p><b>Nota: </b>O tempo das operações realizadas serão sempre ser em <b>segundos</b>.
 * <p><b>Nota: </b>A interface deve lidar com o registro e <b>cancelamento</b> de tarefas, para isso deve conter um {@link HashMap} contendo todas as tarefas sendo executadas.</p>
 */
public interface BaseTaskScheduler {

	/**
	 * Executa uma tarefa fora do Thread principal.
	 * @param task Tarefa a ser executada.
	 */
	public abstract void runTaskAsynchronously(Runnable task);

	/**
	 * Executa uma tarefa dentro do Thread principal da aplicação.
	 * @param task Tarefa a ser executada.
	 */
	public abstract void runTaskSynchronously(Runnable task);
	
	/**
	 * Programa uma tarefa para ser executada fora do Thread principal depois de um certo período de tempo.
	 * @param task Tarefa a ser executada
	 * @param delay Tempo em <b>segundos</b> que se deve esperar antes de realizar a tarefa.
	 */
	public abstract void scheduleAsyncDelayedTask(Runnable task, int delay);
	
	/**
	 * Programa uma tarefa para ser executada periodicamente fora do Thread principal, após um delay inicial.
	 * @param task Tarefa a ser executada
	 * @param delay Tempo em <b>segundos</b> que se deve esperar antes de realizar a tarefa.
	 * @param repeatIn Tempo em <b>segundos</b> que haverá entre cada execução.
	 */
	public abstract void scheduleAsyncRepeatingTask(Runnable task, int delay, int repeatIn);
	
	/**
	 * Programa uma tarefa para ser executada dentro do Thread principal depois de um certo período de tempo.
	 * @param task Tarefa a ser executada
	 * @param delay Tempo em <b>segundos</b> que se deve esperar antes de realizar a tarefa.
	 */
	public abstract void scheduleSyncDelayedTask(Runnable task, int delay);
	
	/**
	 * Programa uma tarefa para ser executada periodicamente dentro do Thread principal, após um delay inicial.
	 * @param task Tarefa a ser executada
	 * @param delay Tempo em <b>segundos</b> que se deve esperar antes de realizar a tarefa.
	 * @param repeatIn Tempo em <b>segundos</b> que haverá entre cada execução.
	 */
	public abstract void scheduleSyncRepeatingTask(Runnable task, int delay, int repeatIn);
	
	/**
	 * Esta função deve cancelar todas as tarefas que foram instanciadas dentro da interface, 
	 * para que não haja uso excessivo de memória ou tarefas pendentes mesmo após a finalização de execução da instancia da API.
	 */
	public abstract void cancelTasks();
	
}

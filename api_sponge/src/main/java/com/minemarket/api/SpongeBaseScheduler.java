package com.minemarket.api;

import lombok.RequiredArgsConstructor;
import org.spongepowered.api.scheduler.Task;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class SpongeBaseScheduler implements BaseTaskScheduler {

    private final MineMarketSponge plugin;
    private Task.Builder taskBuilder = Task.builder();
    private Set<Task> tasks = new HashSet<>();

    public Runnable createTask(Runnable task) {
        Runnable r = new Runnable() {

            public void run() {
                task.run();
            }
        };
        return r;
    }

    public void runTaskAsynchronously(Runnable task) {
        Task spongeTask = taskBuilder.async().execute(createTask(task)).submit(plugin);
        tasks.add(spongeTask);
    }

    public void runTaskSynchronously(Runnable task) {
        Task spongeTask = taskBuilder.execute(createTask(task)).submit(plugin);
        tasks.add(spongeTask);
    }

    public void scheduleAsyncDelayedTask(Runnable task, int delay) {
        Task spongeTask = taskBuilder.async().delay(delay, TimeUnit.SECONDS).execute(createTask(task)).submit(plugin);
        tasks.add(spongeTask);
    }

    public void scheduleAsyncRepeatingTask(Runnable task, int delay, int repeatIn) {
        Task spongeTask = taskBuilder.async().delay(delay, TimeUnit.SECONDS).interval(repeatIn, TimeUnit.SECONDS).execute(createTask(task)).submit(plugin);
        tasks.add(spongeTask);
    }

    public void scheduleSyncDelayedTask(Runnable task, int delay) {
        Task spongeTask = taskBuilder.delay(delay, TimeUnit.SECONDS).execute(createTask(task)).submit(plugin);
        tasks.add(spongeTask);
    }

    public void scheduleSyncRepeatingTask(Runnable task, int delay, int repeatIn) {
        Task spongeTask = taskBuilder.delay(delay, TimeUnit.SECONDS).interval(repeatIn, TimeUnit.SECONDS).execute(createTask(task)).submit(plugin);
        tasks.add(spongeTask);
    }

    @Override
    public void cancelTasks() {
        for (Task r : tasks) {
            r.cancel();
        }
        tasks.clear();
    }
}
package com.minemarket.api;

import com.minemarket.api.types.CommandType;
import com.minemarket.api.types.PendingCommand;
import lombok.RequiredArgsConstructor;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class SpongeCommandExecutor implements BaseCommandExecutor {
    private final MineMarketSponge plugin;
    private Task.Builder taskBuilder = Task.builder();
    ArrayList<PendingCommand> executedCommands = new ArrayList<>();

    @Override
    public boolean executeCommand(PendingCommand pendingCommand) {
        Server server = Sponge.getServer();
        String cmd = getCommandLine(pendingCommand);
        CommandSource source = pendingCommand.getCommandType() == CommandType.CONSOLE || !pendingCommand.isRequireOnline() ? server.getConsole() : (pendingCommand.getPlayerUUID() == null ? server.getPlayer(pendingCommand.getPlayerName()).get() : server.getPlayer(pendingCommand.getPlayerUUID()).get());

        if (executedCommands.contains(pendingCommand)){ return true; }
        if (source == null){ return false; }
        if (pendingCommand.isRequireOnline() && !isPlayerOnline(pendingCommand.getPlayerName())) { return false; }

        if (pendingCommand.getCommandType() == CommandType.OP) {
            boolean op = source.hasPermission("thisisafake.permission.to.check.like.a.op");
            taskBuilder.execute(new Runnable() {
                @Override
                public void run() {
                    Sponge.getCommandManager().process(server.getConsole(), "/op "+source.getName());
                }
            }).submit(plugin);

            taskBuilder.execute(new Runnable() {
                @Override
                public void run() {
                    Sponge.getCommandManager().process(source, cmd);
                }
            }).submit(plugin);
            if (!op)
                taskBuilder.execute(new Runnable() {
                    @Override
                    public void run() {
                        Sponge.getCommandManager().process(server.getConsole(), "/deop "+source.getName());
                    }
                }).submit(plugin);
        } else {
            taskBuilder.execute(new Runnable() {
                @Override
                public void run() {
                    Sponge.getCommandManager().process(source, cmd);
                }
            }).submit(plugin);
        }
        executedCommands.add(pendingCommand);
        return true;
    }

    @Override
    public boolean isPlayerOnline(UUID uuid) {
        Player player = null;
        Optional<Player> maybePlayer = Sponge.getServer().getPlayer(uuid);
        if (maybePlayer.isPresent())
            player = maybePlayer.get();
        return (player != null && player.isOnline());
    }

    @Override
    public boolean isPlayerOnline(String name) {
        Player player = null;
        Optional<Player> maybePlayer = Sponge.getServer().getPlayer(name);
        if (maybePlayer.isPresent())
            player = maybePlayer.get();
        return (player != null && player.isOnline());
    }

}

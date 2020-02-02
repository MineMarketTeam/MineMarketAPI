package com.minemarket.api;

import com.minemarket.api.command.MineMarketCommand;
import com.minemarket.api.types.ConnectionStatus;
import lombok.Getter;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

import java.io.IOException;

@Plugin(id = "minemarketsponge", name = "MineMarketSponge", version = "2.2", authors = "vitorblog")
public class MineMarketSponge {

    @Getter
    private static MineMarketSponge instance;
    @Getter
    private MineMarketBaseAPI mineMarketAPI;
    private SpongeBaseScheduler scheduler;
    private SpongeCommandExecutor baseCommandExecutor;
    @Getter
    private Config config;

    @Listener
    public void onServerStart(GameStartedServerEvent event) throws IOException {
        instance = this;

        /* config */
        config = new Config();

        /* API */
        scheduler = new SpongeBaseScheduler(this);
        baseCommandExecutor = new SpongeCommandExecutor(this);
        mineMarketAPI = new MineMarketBaseAPI(config.getString("key"), "2.2", "BUKKIT", scheduler, baseCommandExecutor, new SpongeUpdater());
        mineMarketAPI.initialize();
        mineMarketAPI.loadPendingCommands();

        /* Commands */
        CommandSpec mineMarketCommand = CommandSpec.builder()
                .permission("MineMarket.configure")
                .arguments(GenericArguments.remainingJoinedStrings(Text.of("arguments")))
                .executor(new MineMarketCommand())
                .build();

        Sponge.getCommandManager().register(this, mineMarketCommand, "minemarket", "mm");
    }

    public void reloadPlugin(){
        try {
            scheduler.cancelTasks();
            if (mineMarketAPI.getPendingCommands() != null)
                mineMarketAPI.getPendingCommands().clear();

            config.save();
            config = new Config();

            scheduler = new SpongeBaseScheduler(this);
            baseCommandExecutor = new SpongeCommandExecutor(this);
            mineMarketAPI = new MineMarketBaseAPI(config.getString("key"), "2.2", "BUKKIT", scheduler, baseCommandExecutor, new SpongeUpdater());
            mineMarketAPI.initialize();
        }catch (Exception exp){
            exp.printStackTrace();
        }
    }

    @Listener
    public void onPlayer(ClientConnectionEvent.Join event){
        if (mineMarketAPI.getStatus() == ConnectionStatus.OK) {
            mineMarketAPI.onPlayerJoin(event.getTargetEntity().getUniqueId(), event.getTargetEntity().getName());
        }
        if (event.getTargetEntity().hasPermission("minemarket.configure")) {
            if (mineMarketAPI.getStatus() != ConnectionStatus.OK) {
                event.getTargetEntity().sendMessage(Text.of("===================="));
                event.getTargetEntity().sendMessage(Text.of("§b[!] Por favor verifique as seguintes informacoes:"));
                Sponge.getCommandManager().process(event.getTargetEntity(), "minemarket info");
                event.getTargetEntity().sendMessage(Text.of("===================="));
            }
            if (mineMarketAPI.isUpdateAvailable()) {
                event.getTargetEntity().sendMessage(Text.of("===================="));
                event.getTargetEntity().sendMessage(Text.of("§6[MineMarket] §aExiste uma nova versão da API disponível para download!"));
                event.getTargetEntity().sendMessage(Text.of("§bNós recomendamos que atualize o mais rapidamente o possível, para §cmanter a segurança."));
                event.getTargetEntity().sendMessage(Text.of("===================="));
            }
        }
    }

}

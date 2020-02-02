package com.minemarket.api.command;

import com.minemarket.api.Config;
import com.minemarket.api.MineMarketBaseAPI;
import com.minemarket.api.MineMarketSponge;
import lombok.SneakyThrows;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

public class MineMarketCommand implements CommandExecutor {

    @SneakyThrows
    @Override
    public CommandResult execute(CommandSource src, CommandContext context) {

        if (context.hasAny("arguments")){
            String[] args = context.getOne("arguments").get().toString().split(" ");
            switch (args[0].toLowerCase()) {
                case "setkey":
                    if (args[1] != null){
                        String key = args[1];

                        Config config = MineMarketSponge.getInstance().getConfig();
                        config.setString("key", key);
                        config.save();

                        src.sendMessage(Text.of("§6Key atualizada para: §a" + key));
                        src.sendMessage(Text.of("§eDigite §6/MineMarket reload§e para carregar as alterações."));
                    } else {
                        src.sendMessage(Text.of("§c/minemarket setkey <key>"));
                    }
                    return CommandResult.success();
                case "info":
                    String message;
                    MineMarketBaseAPI api = MineMarketSponge.getInstance().getMineMarketAPI();

                    if (api == null) {
                        message = "§cCom ERRO -> Verifique o console.";
                    } else switch (api.getStatus()) {
                        case OK:
                            message = "§aFuncionando corretamente";
                            //message += "\n§fNome da store: " + ChatColor.AQUA + api.getInfo().getStoreName();
                            message += "\n§fKEY da API: §b" + api.getKey();
                            message += "\n§fCommandos pendentes: §6" + api.getPendingCommands().size();
                            break;
                        case WRONG_SERVER_TYPE:
                            message = "§6Configuracao Necessaria";
                            message += "\n§cAcesse o painel e altere o tipo de servidor para: BUKKIT";
                            message += "\n§c(ou) Crie uma nova key do tipo BUKKIT";
                            break;
                        case INVALID_KEY:
                            message = "§cKey Invalida";
                            message += "\n§fMude sua key utilizando: §6/minemarket setkey";
                            break;
                        case BLOCKED_IP:
                            message = "§cKey Bloqueada";
                            message += "\n§fAcesse o painel e desbloqueie o acesso deste servidor com a API.";
                            break;
                        case UNCONFIRMED_IP:
                            message = "§eAguardando confirmacao da key";
                            message += "\n§fAcesse o painel e libere o acesso deste servidor com a API.";
                            break;
                        case CONNECTION_ERROR:
                            message = "§cDesconectado";
                            message += "\n§cNao foi possivel se conectar a API.";
                            message += "\n§fVerifique se a conexao do seu servidor com a internet esta funcionando.";
                            break;
                        default:
                            message = "";
                            break;
                    }

                    if (api != null && api.isUpdateAvailable()) {
                        message += "\n§6Existe uma nova versão disponível! Por favor atualize assim que possível, fazendo o download no nosso site.";
                    }

                    src.sendMessage(Text.of("§fStatus do sistema: " + message));
                    return CommandResult.success();
                case "reload":
                    MineMarketSponge.getInstance().reloadPlugin();
                    src.sendMessage(Text.of("§aAPI recarregada."));
                    return CommandResult.success();
            }
        }

        src.sendMessage(Text.of("==========================="));
        src.sendMessage(Text.of("§6Ajuda - MineMarket"));
        src.sendMessage(Text.of("§bSubcomandos disponíveis:"));
        src.sendMessage(Text.of(""));
        src.sendMessage(Text.of("§a/MineMarket setkey <key>§f - Use para configurar a KEY de acesso à API."));
        src.sendMessage(Text.of("§a/MineMarket info§f - Veja o status do sistema."));
        src.sendMessage(Text.of("§a/MineMarket reload§f - Recarrega as configurações do sistema."));
        src.sendMessage(Text.of("==========================="));
        return CommandResult.success();
    }

}

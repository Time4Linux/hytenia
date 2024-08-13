package de.pxscxl.bungee.proxysystem.commands;

import de.pxscxl.cloud.CloudServer;
import de.pxscxl.cloud.manager.ServerManager;
import de.pxscxl.origin.bungee.Origin;
import de.pxscxl.origin.bungee.api.OriginPlayer;
import de.pxscxl.origin.bungee.api.manager.OriginManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class HubCommand extends Command {

    public HubCommand() {
        super("hub", null, "lobby", "l");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof ProxiedPlayer)) {
            commandSender.sendMessage("This command is only executable as a player!");
            return;
        }

        OriginPlayer player = OriginManager.getInstance().getPlayer((ProxiedPlayer) commandSender);
        if (args.length == 0) {
            if (!player.getServer().getName().toLowerCase().startsWith("lobby-")) {
                List<CloudServer> servers = ServerManager.getInstance().getServers().stream().filter(cloudServer -> cloudServer.getName().toLowerCase().startsWith("lobby-")).collect(Collectors.toList());
                if (!servers.isEmpty()) {
                    Collections.shuffle(servers);
                    player.connect(ProxyServer.getInstance().getServerInfo(servers.get(0).getName()));
                    player.sendMessage(
                            Origin.getInstance().getPrefix() + "§7Du wirst auf eine §eLobby §7gesendet",
                            Origin.getInstance().getPrefix() + "§7You will be connected to a §elobby"
                    );
                } else {
                    player.sendMessage(
                            Origin.getInstance().getPrefix() + "§7Derzeit wurde §ckeine §7Lobby gefunden",
                            Origin.getInstance().getPrefix() + "§7Currently there is §cno §7lobby available"
                    );
                }
            } else {
                player.sendMessage(
                        Origin.getInstance().getPrefix() + "§7Du bist §ebereits §7auf einer Lobby",
                        Origin.getInstance().getPrefix() + "§7You are §ealready §7connected to a lobby"
                );
            }
        } else {
            player.sendMessage(
                    Origin.getInstance().getPrefix() + "§7Bitte nutze: §f/hub",
                    Origin.getInstance().getPrefix() + "§7Please use: §f/hub"
            );
        }
    }
}

package de.pxscxl.bungee.proxysystem.commands;

import de.pxscxl.bungee.proxysystem.ProxySystem;
import de.pxscxl.origin.bungee.Origin;
import de.pxscxl.origin.bungee.api.OriginPlayer;
import de.pxscxl.origin.bungee.api.manager.OriginManager;
import de.pxscxl.origin.bungee.api.manager.PingManager;
import de.pxscxl.origin.utils.enums.Rank;
import de.pxscxl.origin.utils.objects.origin.OriginServerPing;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.stream.IntStream;

public class MotdCommand extends Command {

    public MotdCommand() {
        super("messageoftheday", null, "motd");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof ProxiedPlayer)) {
            commandSender.sendMessage("This command is only executable as a player!");
            return;
        }

        OriginPlayer player = OriginManager.getInstance().getPlayer((ProxiedPlayer) commandSender);
        if (player.hasPriorityAccess(Rank.ADMINISTRATOR.getPriority())) {
            if (args.length >= 2) {
                if (args[0].equals("1")) {
                    StringBuilder message = new StringBuilder();
                    IntStream.range(1, args.length).forEach(value -> message.append(value == 1 ? "" : " ").append(args[value]));

                    player.sendMessage(
                            Origin.getInstance().getPrefix() + "§7Du hast die Header Motd §aaktualisiert",
                            Origin.getInstance().getPrefix() + "§7You have §aactualized §7the header motd"
                    );
                    player.sendMessage(Origin.getInstance().getPrefix() + " §f- " + message.toString().replace("&", "§"));

                    OriginServerPing serverPing = PingManager.getInstance().getOriginServerPing();
                    serverPing.setHeaderMotd(message.toString().replace("&", "§"));
                    ProxyServer.getInstance().getScheduler().runAsync(ProxySystem.getInstance(), () -> PingManager.getInstance().setOriginServerPing(serverPing));
                } else if (args[0].equals("2")) {
                    StringBuilder message = new StringBuilder();
                    IntStream.range(1, args.length).forEach(value -> message.append(value == 1 ? "" : " ").append(args[value]));

                    player.sendMessage(
                            Origin.getInstance().getPrefix() + "§7Du hast die Footer Motd §aaktualisiert",
                            Origin.getInstance().getPrefix() + "§7You have §aactualized §7the footer motd"
                    );
                    player.sendMessage(Origin.getInstance().getPrefix() + " §f- " + message.toString().replace("&", "§"));

                    OriginServerPing serverPing = PingManager.getInstance().getOriginServerPing();
                    serverPing.setFooterMotd(message.toString().replace("&", "§"));
                    ProxyServer.getInstance().getScheduler().runAsync(ProxySystem.getInstance(), () -> PingManager.getInstance().setOriginServerPing(serverPing));
                } else {
                    player.sendMessage(
                            Origin.getInstance().getPrefix() + "§7Bitte nutze: §f/motd [1/2] [Text]",
                            Origin.getInstance().getPrefix() + "§7Please use: §f/motd [1/2] [Text]"
                    );
                }
            } else {
                player.sendMessage(Origin.getInstance().getPrefix() + "§f/motd [1/2] [Motd]");
            }
        } else {
            player.sendMessage(
                    Origin.getInstance().getPrefix() + "§7Du hast §ckeine §7Rechte diesen Befehl auszuführen!",
                    Origin.getInstance().getPrefix() + "§7You §cdon't §7have permission to perform this command!"
            );
        }
    }
}

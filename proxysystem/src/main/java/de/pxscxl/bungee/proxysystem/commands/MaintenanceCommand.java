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

public class MaintenanceCommand extends Command {

    public MaintenanceCommand() {
        super("maintenance");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof ProxiedPlayer)) {
            commandSender.sendMessage("This command is only executable as a player!");
            return;
        }

        OriginPlayer player = OriginManager.getInstance().getPlayer((ProxiedPlayer) commandSender);
        if (player.hasPriorityAccess(Rank.ADMINISTRATOR.getPriority())) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("true")) {
                    player.sendMessage(
                            Origin.getInstance().getPrefix() + "§7Die Wartungsarbeiten wurden §aaktiviert",
                            Origin.getInstance().getPrefix() + "§7You have §aactivated §7the maintenance mode"
                    );
                    OriginServerPing serverPing = PingManager.getInstance().getOriginServerPing();
                    serverPing.setMaintenance(true);
                    ProxyServer.getInstance().getScheduler().runAsync(ProxySystem.getInstance(), () -> PingManager.getInstance().setOriginServerPing(serverPing));
                } else if (args[0].equalsIgnoreCase("false")) {
                    player.sendMessage(
                            Origin.getInstance().getPrefix() + "§7Die Wartungsarbeiten wurden §cdeaktiviert",
                            Origin.getInstance().getPrefix() + "§7You have §cdeactivated §7the maintenance mode"
                    );
                    OriginServerPing serverPing = PingManager.getInstance().getOriginServerPing();
                    serverPing.setMaintenance(false);
                    ProxyServer.getInstance().getScheduler().runAsync(ProxySystem.getInstance(), () -> PingManager.getInstance().setOriginServerPing(serverPing));
                } else {
                    player.sendMessage(
                            Origin.getInstance().getPrefix() + "§7Bitte nutze: §f/maintenance [true/false]",
                            Origin.getInstance().getPrefix() + "§7Please use: §f/maintenance [true/false]"
                    );
                }
            } else {
                player.sendMessage(
                        Origin.getInstance().getPrefix() + "§7Bitte nutze: §f/maintenance [true/false]",
                        Origin.getInstance().getPrefix() + "§7Please use: §f/maintenance [true/false]"
                );
            }
        } else {
            player.sendMessage(
                    Origin.getInstance().getPrefix() + "§7Du hast §ckeine §7Rechte diesen Befehl auszuführen!",
                    Origin.getInstance().getPrefix() + "§7You §cdon't §7have permission to perform this command!"
            );
        }
    }
}

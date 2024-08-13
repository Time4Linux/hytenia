package de.pxscxl.bungee.proxysystem.commands;

import com.google.gson.JsonObject;
import de.pxscxl.bungee.proxysystem.ProxySystem;
import de.pxscxl.origin.bungee.Origin;
import de.pxscxl.origin.bungee.api.OriginPlayer;
import de.pxscxl.origin.bungee.api.manager.ClanManager;
import de.pxscxl.origin.bungee.api.manager.OriginManager;
import de.pxscxl.origin.bungee.api.manager.PingManager;
import de.pxscxl.origin.utils.enums.Rank;
import de.pxscxl.origin.utils.objects.origin.OriginServerPing;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.SQLException;
import java.util.stream.IntStream;

public class SlotsCommand extends Command {

    public SlotsCommand() {
        super("slots");
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
                int slots;
                try {
                    slots = Integer.parseInt(args[0]);
                } catch (NumberFormatException e) {
                    player.sendMessage(
                            Origin.getInstance().getPrefix() + "§7Bitte gebe eine §cgültige §7Zahl an!",
                            Origin.getInstance().getPrefix() + "§7Please enter a §cvaild §7number!"
                    );
                    return;
                }
                player.sendMessage(
                        Origin.getInstance().getPrefix() + "§7Du hast die Slots §aaktualisiert",
                        Origin.getInstance().getPrefix() + "§7You have §aactualized §7the slots"
                );
                player.sendMessage(Origin.getInstance().getPrefix() + " §f- §e" + slots + " §7Slots");

                OriginServerPing serverPing = PingManager.getInstance().getOriginServerPing();
                serverPing.setSlots(slots);
                ProxyServer.getInstance().getScheduler().runAsync(ProxySystem.getInstance(), () -> PingManager.getInstance().setOriginServerPing(serverPing));
            } else {
                player.sendMessage(
                        Origin.getInstance().getPrefix() + "§7Bitte nutze: §f/slots [Slots]",
                        Origin.getInstance().getPrefix() + "§7Please use: §f/slots [Slots]"
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

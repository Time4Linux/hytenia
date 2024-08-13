package de.pxscxl.bungee.proxysystem.commands;

import de.pxscxl.origin.bungee.Origin;
import de.pxscxl.origin.bungee.api.OriginPlayer;
import de.pxscxl.origin.bungee.api.manager.ClanManager;
import de.pxscxl.origin.bungee.api.manager.OriginManager;
import de.pxscxl.origin.utils.enums.Rank;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.stream.IntStream;

public class StaffchatCommand extends Command {

    public StaffchatCommand() {
        super("staffchat", null, "teamchat", "sc", "tc");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof ProxiedPlayer)) {
            commandSender.sendMessage("This command is only executable as a player!");
            return;
        }

        OriginPlayer player = OriginManager.getInstance().getPlayer((ProxiedPlayer) commandSender);
        if (player.hasPriorityAccess(Rank.BUILDER.getPriority())) {
            if (args.length >= 1) {
                StringBuilder message = new StringBuilder();
                IntStream.range(0, args.length).forEach(value -> message.append(" §3").append(args[value]));
                OriginManager.getInstance().getPlayers().forEach(players -> {
                    if (players.hasPriorityAccess(Rank.BUILDER.getPriority())) {
                        players.sendMessage(Origin.getInstance().getPrefix() + "§8[" + player.getServer().getName() + "§8] " + player.getDisplayName() + "§8:§3" + message);
                    }
                });
            } else {
                player.sendMessage(
                        Origin.getInstance().getPrefix() + "§7Bitte nutze: §f/staffchat [Nachricht]",
                        Origin.getInstance().getPrefix() + "§7Please use: §f/staffchat [Message]"
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

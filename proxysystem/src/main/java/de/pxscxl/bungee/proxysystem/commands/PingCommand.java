package de.pxscxl.bungee.proxysystem.commands;

import de.pxscxl.origin.bungee.Origin;
import de.pxscxl.origin.bungee.api.OriginPlayer;
import de.pxscxl.origin.bungee.api.manager.ClanManager;
import de.pxscxl.origin.bungee.api.manager.OriginManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class PingCommand extends Command {

    public PingCommand() {
        super("ping");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof ProxiedPlayer)) {
            commandSender.sendMessage("This command is only executable as a player!");
            return;
        }

        OriginPlayer player = OriginManager.getInstance().getPlayer((ProxiedPlayer) commandSender);
        switch (args.length) {
            case 0:
                player.sendMessage(
                        Origin.getInstance().getPrefix() + "§7Dein Ping beträgt §e" + player.getPing() + "ms§7.",
                        Origin.getInstance().getPrefix() + "§7You have a ping of §e" + player.getPing() + "ms§7."
                );
                break;
            case 1:
                OriginPlayer target = OriginManager.getInstance().getPlayer(args[0]);
                if (target != null && target.isOnline()) {
                    if (target != player) {
                        player.sendMessage(
                                Origin.getInstance().getPrefix() + target.getDisplayName() + "'s §7Ping beträgt §e" + target.getPing() + "ms§7.",
                                Origin.getInstance().getPrefix() + target.getDisplayName() + "'s §7has a ping of §e" + target.getPing() + "ms§7."
                        );
                    } else {
                        player.sendMessage(
                                Origin.getInstance().getPrefix() + "§7Du darfst §cnicht §7mit dir selbst interagieren!",
                                Origin.getInstance().getPrefix() + "§7You §ccan't §7interact with yourself!"
                        );
                    }
                } else {
                    player.sendMessage(
                            Origin.getInstance().getPrefix() + "§7Dieser Spieler wurde §cnicht §7gefunden!",
                            Origin.getInstance().getPrefix() + "§7This player was §cnot §7found!"
                    );
                }
                break;
            default:
                player.sendMessage(
                        Origin.getInstance().getPrefix() + "§7Bitte nutze: §f/ping [Name]",
                        Origin.getInstance().getPrefix() + "§7Please use: §f/ping [Name]"
                );
                break;
        }
    }
}

package de.pxscxl.bungee.proxysystem.commands;

import de.pxscxl.origin.bungee.Origin;
import de.pxscxl.origin.bungee.api.OriginPlayer;
import de.pxscxl.origin.bungee.api.manager.OriginManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class OnlineTimeCommand extends Command {

    public OnlineTimeCommand() {
        super("onlinetime", null, "playtime", "ot", "pt");
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
                        Origin.getInstance().getPrefix() + "§7Deine aktuelle Spielzeit§8: §e" + formatTime(player),
                        Origin.getInstance().getPrefix() + "§7Your current online time§8: §e" + formatTime(player)
                );
                break;
            case 1:
                OriginPlayer target = OriginManager.getInstance().getPlayer(args[0]);
                if (target != null) {
                    if (target != player) {
                        player.sendMessage(
                                Origin.getInstance().getPrefix() + target.getDisplayName() + "'s §7aktuelle Spielzeit§8: §e" + formatTime(player),
                                Origin.getInstance().getPrefix() + target.getDisplayName() + "'s §7current online time§8: §e" + formatTime(player)
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
                        Origin.getInstance().getPrefix() + "§7Bitte nutze: §f/onlinetime [Name]",
                        Origin.getInstance().getPrefix() + "§7Please use: §f/onlinetime [Name]"
                );
                break;
        }
    }

    private String formatTime(OriginPlayer player) {
        long hours = (long) Math.floor((double) player.getOnlineTime() / 60L);
        return hours == 1L ? hours + player.language(" Stunde", " hour") : hours + player.language(" Stunden", " hours");
    }
}

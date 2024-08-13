package de.pxscxl.bungee.proxysystem.commands;

import de.pxscxl.origin.bungee.Origin;
import de.pxscxl.origin.bungee.api.OriginPlayer;
import de.pxscxl.origin.bungee.api.manager.OriginManager;
import de.pxscxl.origin.utils.enums.Rank;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CheckAccountsCommand extends Command {

    public CheckAccountsCommand() {
        super("checkaccounts", null, "checkaccs");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof ProxiedPlayer)) {
            commandSender.sendMessage("This command is only executable as a player!");
            return;
        }

        OriginPlayer player = OriginManager.getInstance().getPlayer((ProxiedPlayer) commandSender);
        if (player.hasPriorityAccess(Rank.SRMODERATOR.getPriority())) {
            if (args.length == 1) {
                OriginPlayer target = OriginManager.getInstance().getPlayer(args[0]);
                if (target != null) {
                    if (target != player) {
                        ProxyServer.getInstance().getScheduler().runAsync(Origin.getInstance(), () -> {
                            List<OriginPlayer> accounts = OriginManager.getInstance().getDatabasePlayers().stream().filter(players -> Objects.equals(players.getSocketAddress(), target.getSocketAddress())).collect(Collectors.toList());
                            player.sendMessage(
                                    Origin.getInstance().getPrefix() + "§7Folgende Accounts gehören zu der IP von " + target.getDisplayName() + "§8:",
                                    Origin.getInstance().getPrefix() + "§7The following accounts are regarding to the ip of " + target.getDisplayName() + "§8:"
                            );
                            accounts.forEach(players -> player.sendMessage(Origin.getInstance().getPrefix() + " §f- §9" + players.getName() + " §f| §e" + players.getLastLogin()));
                        });
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
            } else {
                player.sendMessage(
                        Origin.getInstance().getPrefix() + "§7Bitte nutze: §f/checkaccounts [Name]",
                        Origin.getInstance().getPrefix() + "§7Please use: §f/checkaccounts [Name]"
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

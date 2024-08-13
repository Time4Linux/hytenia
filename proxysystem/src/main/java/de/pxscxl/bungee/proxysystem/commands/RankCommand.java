package de.pxscxl.bungee.proxysystem.commands;

import com.google.common.base.Joiner;
import de.pxscxl.bungee.proxysystem.ProxySystem;
import de.pxscxl.origin.bungee.Origin;
import de.pxscxl.origin.bungee.api.OriginPlayer;
import de.pxscxl.origin.bungee.api.manager.ClanManager;
import de.pxscxl.origin.bungee.api.manager.OriginManager;
import de.pxscxl.origin.bungee.api.manager.PermissionManager;
import de.pxscxl.origin.utils.enums.Rank;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;
import java.util.stream.Collectors;

public class RankCommand extends Command {

    public RankCommand() {
        super("rank");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof ProxiedPlayer)) {
            commandSender.sendMessage("This command is only executable as a player!");
            return;
        }

        OriginPlayer player = OriginManager.getInstance().getPlayer((ProxiedPlayer) commandSender);
        if (player.hasPriorityAccess(Rank.ADMINISTRATOR.getPriority())) {
            if (args.length == 2) {
                OriginPlayer target = OriginManager.getInstance().getPlayer(args[0]);
                if (target != null) {
                    if (Arrays.stream(Rank.values()).anyMatch(rank -> rank.getName().equalsIgnoreCase(args[1]))) {
                        Rank rank = Arrays.stream(Rank.values()).filter(r -> r.getName().equalsIgnoreCase(args[1])).findFirst().orElse(null);
                        if (rank != null) {
                            player.sendMessage(
                                    Origin.getInstance().getPrefix() + "§7Du hast " + target.getDisplayName() + " §7den Rang " + rank.getColor() + rank.getName() + " §7gesetzt §c[LIFETIME§c]",
                                    Origin.getInstance().getPrefix() + "§7You set the rank of " + target.getDisplayName() + " §7to " + rank.getColor() + rank.getName() + " §c[LIFETIME§c]"
                            );

                            if (target.isOnline()) {
                                target.disconnect(player.language(
                                        "§fDir wurde durch §bVerany §fder Rang " + rank.getColor() + rank.getName() + " §fgesetzt!\n§fBitte verbinde ich erneut zum Netzwerk, um die Rechte nutzen zu können.",
                                        "§fYou got the rank " + rank.getColor() + rank.getName() + " §fthrough §bVerany§f!\n§fPlease reconnect to our network to use your received rights."
                                ));
                            }

                            ProxyServer.getInstance().getScheduler().runAsync(ProxySystem.getInstance(), () -> PermissionManager.getInstance().setRank(target.getUniqueId(), rank));
                        }
                    } else {
                        player.sendMessage(
                                Origin.getInstance().getPrefix() + "§7Dieser Rang wurde §cnicht §7gefunden!",
                                Origin.getInstance().getPrefix() + "§7This rank was §cnot §7found!"
                        );
                        player.sendMessage(Origin.getInstance().getPrefix() + "§7" + player.language("Ränge", "Ranks") + "§8: §f" + Joiner.on(", ").join(Arrays.stream(Rank.values()).map(Rank::getName).collect(Collectors.toList())));
                    }
                } else {
                    player.sendMessage(
                            Origin.getInstance().getPrefix() + "§7Dieser Spieler wurde §cnicht §7gefunden!",
                            Origin.getInstance().getPrefix() + "§7This player was §cnot §7found!"
                    );
                }
            } else {
                player.sendMessage(
                        Origin.getInstance().getPrefix() + "§7Bitte nutze: §f/rank [Name] [Rank]",
                        Origin.getInstance().getPrefix() + "§7Please use: §f/rank [Name] [Rank]"
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

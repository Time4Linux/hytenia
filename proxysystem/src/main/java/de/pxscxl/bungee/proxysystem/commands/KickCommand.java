package de.pxscxl.bungee.proxysystem.commands;

import de.pxscxl.origin.bungee.Origin;
import de.pxscxl.origin.bungee.api.OriginPlayer;
import de.pxscxl.origin.bungee.api.manager.NotifyManager;
import de.pxscxl.origin.bungee.api.manager.OriginManager;
import de.pxscxl.origin.utils.enums.Rank;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.stream.IntStream;

public class KickCommand extends Command {

    public KickCommand() {
        super("kick", null);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof ProxiedPlayer)) {
            commandSender.sendMessage("This command is only executable as a player!");
            return;
        }

        OriginPlayer player = OriginManager.getInstance().getPlayer((ProxiedPlayer) commandSender);
        if (player.hasPriorityAccess(Rank.SUPPORTER.getPriority())) {
            if (args.length >= 2) {
                OriginPlayer target = OriginManager.getInstance().getPlayer(args[0]);
                if (target != null && target.isOnline()) {
                    if (target != player) {
                        if (target.hasPriorityAccess(Rank.BUILDER.getPriority()) && !player.hasPriorityAccess(Rank.ADMINISTRATOR.getPriority())) {
                            player.sendMessage(
                                    Origin.getInstance().getPrefix() + "§7Dieser darfst diesen Spieler §cnicht §7kicken!",
                                    Origin.getInstance().getPrefix() + "§7You §caren't §7allowed to kick this player!"
                            );
                            return;
                        }
                        StringBuilder reason = new StringBuilder();
                        IntStream.range(1, args.length).forEach(value -> reason.append(value == 1 ? "§c" : " §c").append(args[value]));

                        OriginManager.getInstance().getPlayers().forEach(players -> {
                            if (players.hasPriorityAccess(Rank.SUPPORTER.getPriority()) && NotifyManager.getInstance().getObject(players.getUniqueId()).get("kick").getAsBoolean()) {
                                players.sendMessage();
                                players.sendMessage(
                                        Origin.getInstance().getPrefix() + target.getDisplayName() + " §7wurde von " + player.getDisplayName() + " §7gekickt.",
                                        Origin.getInstance().getPrefix() + target.getDisplayName() + " §7was kicked by " + player.getDisplayName() + "§7."
                                );
                                players.sendMessage(Origin.getInstance().getPrefix() + "§c" + reason.toString().toUpperCase());
                                players.sendMessage();
                            }
                        });

                        target.disconnect(target.language(
                                "§fDu wurdest von §bVerany §fgekickt!\n\n§fGrund§8: §c" + reason + "\n\n§fWeitere Informationen können unter §chttps://verany.net §fangefordert werden.",
                                "§fYou have been kicked by §bVerany§f!\n\n§fReason§8: §c" + reason + "\n\n§fFurther information you can receive at §chttps://verany.net§f."
                        ));
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
                        Origin.getInstance().getPrefix() + "§7Bitte nutze: §f/kick [Name] [Grund]",
                        Origin.getInstance().getPrefix() + "§7Please use: §f/kick [Name] [Reason]"
                );            }
        } else {
            player.sendMessage(
                    Origin.getInstance().getPrefix() + "§7Du hast §ckeine §7Rechte diesen Befehl auszuführen!",
                    Origin.getInstance().getPrefix() + "§7You §cdon't §7have permission to perform this command!"
            );
        }
    }
}

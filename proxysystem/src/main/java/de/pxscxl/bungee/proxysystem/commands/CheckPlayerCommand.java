package de.pxscxl.bungee.proxysystem.commands;

import de.pxscxl.origin.bungee.Origin;
import de.pxscxl.origin.bungee.api.OriginPlayer;
import de.pxscxl.origin.bungee.api.manager.OriginManager;
import de.pxscxl.origin.bungee.api.manager.PunishmentManager;
import de.pxscxl.origin.utils.enums.Rank;
import de.pxscxl.origin.utils.objects.origin.OriginPunishment;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Objects;

public class CheckPlayerCommand extends Command {

    public CheckPlayerCommand() {
        super("checkplayer", null, "checkplayer");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof ProxiedPlayer)) {
            commandSender.sendMessage("This command is only executable as a player!");
            return;
        }

        OriginPlayer player = OriginManager.getInstance().getPlayer((ProxiedPlayer) commandSender);
        if (player.hasPriorityAccess(Rank.SUPPORTER.getPriority())) {
            if (args.length == 1) {
                OriginPlayer target = OriginManager.getInstance().getPlayer(args[0]);
                if (target != null) {
                    if (target != player) {
                        OriginPunishment ban = PunishmentManager.getInstance().getPunishments().stream().filter(originPunishment -> Objects.equals(originPunishment.getTarget(), target.getUniqueId())).findFirst().orElse(null);
                        OriginPunishment mute = PunishmentManager.getInstance().getPunishments().stream().filter(originPunishment -> Objects.equals(originPunishment.getTarget(), target.getUniqueId())).findFirst().orElse(null);

                        player.sendMessage(Origin.getInstance().getPrefix() + "§7Player Check§8: " + target.getDisplayName());
                        player.sendMessage("");
                        player.sendMessage(Origin.getInstance().getPrefix() + "§7" + player.language("Gebannt", "Banned") + "§8: " + (ban != null && ban.isActive() ? "§a[✔]" : "§c[✘]"));
                        player.sendMessage(Origin.getInstance().getPrefix() + "§7" + player.language("Gemuted", "Muted") + "§8: " + (mute != null && mute.isActive() ? "§a[✔]" : "§c[✘]"));
                        player.sendMessage("");
                        player.sendMessage(Origin.getInstance().getPrefix() + "§7" + player.language("Rang", "Rank") + "§8: " + target.getRank().getColor() + target.getRank().getName());
                        player.sendMessage(Origin.getInstance().getPrefix() + "§7Coins§8: §e" + player.getCoins());
                        player.sendMessage(Origin.getInstance().getPrefix() + "§7" + player.language("Punkte", "Points") + "§8: §e" + player.getPoints());
                        player.sendMessage(Origin.getInstance().getPrefix() + "§7" + player.language("Sprache", "Language") + "§8: §e" + player.getLanguage().name());
                        if (target.isOnline()) {
                            player.sendMessage(Origin.getInstance().getPrefix() + "§7Status§8: §aOnline §7@ §6" + target.getServer());
                        } else {
                            player.sendMessage(Origin.getInstance().getPrefix() + "§7Status§8: §cOffline");
                        }
                        player.sendMessage("");
                        player.sendMessage(
                                Origin.getInstance().getPrefix() + "§7Um Sub-Accs zu checken mache §f/checkaccs [Name]",
                                Origin.getInstance().getPrefix() + "§7To check sub-accs do §f/checkaccs [Name]"
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
            } else {
                player.sendMessage(
                        Origin.getInstance().getPrefix() + "§7Bitte nutze: §f/checkplayer [Name]",
                        Origin.getInstance().getPrefix() + "§7Please use: §f/checkplayer [Name]"
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

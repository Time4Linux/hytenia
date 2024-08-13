package de.pxscxl.bungee.proxysystem.commands;

import com.google.common.base.Joiner;
import de.pxscxl.origin.bungee.Origin;
import de.pxscxl.origin.bungee.api.OriginPlayer;
import de.pxscxl.origin.bungee.api.manager.ClanManager;
import de.pxscxl.origin.bungee.api.manager.NotifyManager;
import de.pxscxl.origin.bungee.api.manager.OriginManager;
import de.pxscxl.origin.bungee.api.manager.PunishmentManager;
import de.pxscxl.origin.utils.enums.Rank;
import de.pxscxl.origin.utils.objects.origin.OriginPunishment;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class ReducePunishCommand extends Command {

    public ReducePunishCommand() {
        super("reducepunish", null, "rp");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof ProxiedPlayer)) {
            commandSender.sendMessage("This command is only executable as a player!");
            return;
        }

        OriginPlayer player = OriginManager.getInstance().getPlayer((ProxiedPlayer) commandSender);
        if (player.hasPriorityAccess(Rank.MODERATOR.getPriority())) {
            if (args.length == 3) {
                OriginPlayer target = OriginManager.getInstance().getPlayer(args[1]);
                if (target != null) {
                    if (target != player) {
                        OriginPunishment.PunishmentType type = Arrays.stream(OriginPunishment.PunishmentType.values()).filter(punishmentType -> punishmentType.name().equalsIgnoreCase(args[0])).findFirst().orElse(null);
                        if (type != null) {
                            OriginPunishment punishment = target.getActivePunishments().stream().filter(originPunishment -> originPunishment.getType() == type && Objects.equals(originPunishment.getId(), args[2])).findFirst().orElse(null);
                            if (punishment != null && punishment.isActive()) {
                                PunishmentManager.getInstance().executeUnpunish(punishment.getId(), punishment.getTarget(), punishment.getType(), calculateUntil(punishment));

                                OriginManager.getInstance().getPlayers().forEach(players -> {
                                    if (players.hasPriorityAccess(Rank.SUPPORTER.getPriority()) && NotifyManager.getInstance().getObject(players.getUniqueId()).get("ban").getAsBoolean()) {
                                        players.sendMessage();
                                        players.sendMessage(
                                                Origin.getInstance().getPrefix() + target.getDisplayName() + "'s §7Ban wurde von " + player.getDisplayName() + " §7verkürzt.",
                                                Origin.getInstance().getPrefix() + target.getDisplayName() + "'s §7ban was reduced by " + player.getDisplayName() + "§7."
                                        );
                                        players.sendMessage(Origin.getInstance().getPrefix() + "§a[" + player.language("§aENTBANNUNGSANTRAG ANGENOMMEN", "APPEAL ACCEPTED") + "]");
                                        players.sendMessage();
                                    }
                                });
                            } else {
                                player.sendMessage(
                                        Origin.getInstance().getPrefix() + "§7Dieser Spieler ist §cnicht §7gebannt!",
                                        Origin.getInstance().getPrefix() + "§7This player §cisn't §7banned!"
                                );
                            }
                        } else {
                            player.sendMessage(
                                    Origin.getInstance().getPrefix() + "§7Dieser Type wurde §cnicht §7gefunden!",
                                    Origin.getInstance().getPrefix() + "§7This type was §cnot §7found!"
                            );
                            player.sendMessage(Origin.getInstance().getPrefix() + "§7Types§8: §f" + Joiner.on(", ").join(OriginPunishment.PunishmentType.values()));
                        }
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
                        Origin.getInstance().getPrefix() + "§7Bitte nutze: §f/reducepunish [mute/ban] [Name] [ID]",
                        Origin.getInstance().getPrefix() + "§7Please use: §f/reducepunish [mute/ban] [Name] [ID]"
                );
            }
        } else {
            player.sendMessage(
                    Origin.getInstance().getPrefix() + "§7Du hast §ckeine §7Rechte diesen Befehl auszuführen!",
                    Origin.getInstance().getPrefix() + "§7You §cdon't §7have permission to perform this command!"
            );
        }
    }

    private Timestamp calculateUntil(OriginPunishment punishment) {
        if (punishment.getUntil() == null) {
            Instant instant = new Date().toInstant().plus(30, ChronoUnit.DAYS);
            return new Timestamp(Date.from(instant).getTime());
        }
        long days = Duration.between(Instant.now(), punishment.getUntil().toInstant()).toDays() / 2;
        System.out.println(days);
        Instant instant = new Date().toInstant().plus(days, ChronoUnit.DAYS);
        return new Timestamp(Date.from(instant).getTime());
    }
}

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;

public class PunishCommand extends Command {

    public PunishCommand() {
        super("punish", null, "ban");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof ProxiedPlayer)) {
            commandSender.sendMessage("This command is only executable as a player!");
            return;
        }

        OriginPlayer player = OriginManager.getInstance().getPlayer((ProxiedPlayer) commandSender);
        if (player.hasPriorityAccess(Rank.SUPPORTER.getPriority())) {
            if (args.length == 2) {
                OriginPlayer target = OriginManager.getInstance().getPlayer(args[0]);
                if (target != null) {
                    OriginPunishment.PunishmentReason reason = Arrays.stream(OriginPunishment.PunishmentReason.values()).filter(punishmentReason -> punishmentReason.name().equalsIgnoreCase(args[1])).findFirst().orElse(null);
                    if (reason != null) {
                        OriginPunishment punishment = target.getActivePunishments().stream().filter(originPunishment -> originPunishment.isActive() && originPunishment.getReason().equals(reason)).findFirst().orElse(null);
                        if (punishment == null) {
                            if (target.hasPriorityAccess(Rank.BUILDER.getPriority()) && !target.hasPriorityAccess(Rank.ADMINISTRATOR.getPriority())) {
                                player.sendMessage(
                                        Origin.getInstance().getPrefix() + "§7Dieser darfst diesen Spieler §cnicht §7bannen!",
                                        Origin.getInstance().getPrefix() + "§7You §caren't §7allowed to punish this player!"
                                );
                                return;
                            }
                            OriginPunishment executedPunishment = PunishmentManager.getInstance().executePunishment(reason, target.getSocketAddress(), target.getUniqueId(), player.getUniqueId(), calculateUntil(target, reason), reason.getType());
                            OriginManager.getInstance().getPlayers().forEach(players -> {
                                if (players.hasPriorityAccess(Rank.SUPPORTER.getPriority()) && NotifyManager.getInstance().getObject(players.getUniqueId()).get("ban").getAsBoolean()) {
                                    players.sendMessage();
                                    players.sendMessage(
                                            Origin.getInstance().getPrefix() + target.getDisplayName() + " §7wurde von " + player.getDisplayName() + " §7gebannt!",
                                            Origin.getInstance().getPrefix() + target.getDisplayName() + " §7was banned by " + player.getDisplayName() + "§7!"
                                    );
                                    players.sendMessage(Origin.getInstance().getPrefix() + "§e" + executedPunishment.getReason().name() + " §f- §8[§7" + executedPunishment.getId() + "§8]");
                                    players.sendMessage();
                                }
                            });
                            if (target.isOnline()) {
                                if (executedPunishment.getType().equals(OriginPunishment.PunishmentType.BAN)) {
                                    target.disconnect(target.language(
                                            "§fDu wurdest von §bVerany §fgebannt!\n\n§fGrund§8: §c" + executedPunishment.getReason().name() + "\n§fDauer§8: §c" + executedPunishment.getUntilAsDate() + "\n\n§fWeitere Informationen können unter §chttps://verany.net §fangefordert werden.",
                                            "§fYou have been banned by §bVerany§f!\n\n§fReason§8: §c" + executedPunishment.getReason().name() + "\n§fDuration§8: §c" + executedPunishment.getUntilAsDate() + "\n\n§fFurther information you can receive at §chttps://verany.net§f."
                                    ));
                                } else if (executedPunishment.getType().equals(OriginPunishment.PunishmentType.MUTE)) {
                                    target.sendMessage(
                                            Origin.getInstance().getPrefix() + "§7Du wurdest aus dem §fChat §7gebannt",
                                            Origin.getInstance().getPrefix() + "§7You have been banned from the §fchat"
                                    );
                                    target.sendMessage(
                                            Origin.getInstance().getPrefix() + "§7Grund§8: §c" + executedPunishment.getReason(),
                                            Origin.getInstance().getPrefix() + "§7Reason§8: §c" + executedPunishment.getReason()
                                    );
                                    target.sendMessage(
                                            Origin.getInstance().getPrefix() + "§7Dauer§8: §e" + executedPunishment.getUntilAsDate(),
                                            Origin.getInstance().getPrefix() + "§7Duration§8: §e" + executedPunishment.getUntilAsDate()
                                    );
                                }
                            }
                        } else {
                            player.sendMessage(
                                    Origin.getInstance().getPrefix() + "§7Dieser Spieler ist §ebereits §7gebannt!",
                                    Origin.getInstance().getPrefix() + "§7This player §ealready §7banned!"
                            );
                        }
                    } else {
                        player.sendMessage(
                                Origin.getInstance().getPrefix() + "§7Dieser Grund wurde §cnicht §7gefunden!",
                                Origin.getInstance().getPrefix() + "§7This reason was §cnot §7found!"
                        );
                        player.sendMessage(Origin.getInstance().getPrefix() + "§7" + player.language("Gründe", "Reasons") + "§8: §f" + Joiner.on(", ").join(OriginPunishment.PunishmentReason.values()));
                    }
                } else {
                    player.sendMessage(
                            Origin.getInstance().getPrefix() + "§7Dieser Spieler wurde §cnicht §7gefunden!",
                            Origin.getInstance().getPrefix() + "§7This player was §cnot §7found!"
                    );
                }
            } else {
                player.sendMessage(
                        Origin.getInstance().getPrefix() + "§7Bitte nutze: §f/punish [Name] [Grund]",
                        Origin.getInstance().getPrefix() + "§7Please use: §f/punish [Name] [Reason]"
                );            }
        } else {
            player.sendMessage(
                    Origin.getInstance().getPrefix() + "§7Du hast §ckeine §7Rechte diesen Befehl auszuführen!",
                    Origin.getInstance().getPrefix() + "§7You §cdon't §7have permission to perform this command!"
            );
        }
    }

    private Timestamp calculateUntil(OriginPlayer player, OriginPunishment.PunishmentReason reason) {
        int earlierPunishments = (int) PunishmentManager.getInstance().getPunishments(player.getUniqueId()).stream().filter(punishment -> !punishment.isActive() && punishment.getReason() == reason).count() + 1;
        if (earlierPunishments <= 3) {
            int days = (reason.getDynamic() ? reason.getDays() * earlierPunishments : reason.getDays());
            Instant instant = new Date().toInstant().plus(days, ChronoUnit.DAYS);
            return new Timestamp(Date.from(instant).getTime());
        } else {
            return null;
        }
    }
}

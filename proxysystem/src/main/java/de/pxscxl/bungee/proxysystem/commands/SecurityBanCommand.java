package de.pxscxl.bungee.proxysystem.commands;

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

import java.util.Objects;

public class SecurityBanCommand extends Command {

    public SecurityBanCommand() {
        super("securityban", null, "sb");
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
                OriginPlayer target = OriginManager.getInstance().getPlayer(args[0]);
                if (target != null) {
                    OriginPunishment punishment = target.getActivePunishments().stream().filter(originPunishment -> originPunishment.getType().equals(OriginPunishment.PunishmentType.BAN)).findFirst().orElse(null);
                    if (punishment == null) {
                        OriginManager.getInstance().getPlayers().forEach(players -> {
                            if (players.hasPriorityAccess(Rank.ADMINISTRATOR.getPriority()) && NotifyManager.getInstance().getObject(players.getUniqueId()).get("ban").getAsBoolean()) {
                                players.sendMessage();
                                players.sendMessage(
                                        Origin.getInstance().getPrefix() + target.getDisplayName() + " §7hat einen Sicherheitsban von " + player.getDisplayName() + " §7erhalten!",
                                        Origin.getInstance().getPrefix() + target.getDisplayName() + " §7was security banned by " + player.getDisplayName() + "§7!"
                                );
                                players.sendMessage();
                            }
                        });
                        if (OriginManager.getInstance().getPlayers().stream().anyMatch(players -> Objects.equals(players.getSocketAddress(), target.getSocketAddress()))) {
                            OriginManager.getInstance().getPlayers().stream().filter(players -> Objects.equals(players.getSocketAddress(), target.getSocketAddress())).forEach(players -> {
                                if (players.isOnline()) {
                                    players.disconnect(players.language(
                                            "§fDeine Verbindung zu §bVerany §fwurde getrennt!\n\n§fGrund§8: §cSecurity Ban\n§fDauer§8: §4PERMANENT\n\n§fWeitere Informationen können unter §chttps://verany.net §fangefordert werden.",
                                            "§fYour connection to §bVerany §fwas disconnected!\n\n§fReason§8: §cSecurity Ban\n§fDuration§8: §4PERMANENT\n\n§fFurther information you can receive at §chttps://verany.net§f."
                                    ));
                                }
                            });
                        }
                        PunishmentManager.getInstance().executePunishment(OriginPunishment.PunishmentReason.SECURITY_BAN, target.getSocketAddress(), target.getUniqueId(), player.getUniqueId(), null, OriginPunishment.PunishmentType.BAN);
                    } else {
                        OriginManager.getInstance().getPlayers().forEach(players -> {
                            if (players.hasPriorityAccess(Rank.ADMINISTRATOR.getPriority()) && NotifyManager.getInstance().getObject(player.getUniqueId()).get("ban").getAsBoolean()) {
                                players.sendMessage();
                                players.sendMessage(
                                        Origin.getInstance().getPrefix() + args[0] + " §7hat einen Sicherheitsban von " + player.getDisplayName() + " §7erhalten!",
                                        Origin.getInstance().getPrefix() + args[0] + " §7was security banned by " + player.getDisplayName() + "§7!"
                                );
                                players.sendMessage();
                            }
                        });
                        if (OriginManager.getInstance().getPlayers().stream().anyMatch(players -> Objects.equals(players.getSocketAddress(), args[0]))) {
                            OriginManager.getInstance().getPlayers().stream().filter(players -> Objects.equals(players.getSocketAddress(), args[0])).forEach(players -> {
                                if (players.isOnline()) {
                                    players.disconnect(players.language(
                                            "§fDeine Verbindung zu §bVerany §fwurde getrennt!\n\n§fGrund§8: §cSecurity Ban\n§fDauer§8: §4PERMANENT\n\n§fWeitere Informationen können unter §chttps://verany.net §fangefordert werden.",
                                            "§fYour connection to §bVerany §fwas disconnected!\n\n§fReason§8: §cSecurity Ban\n§fDuration§8: §4PERMANENT\n\n§fFurther information you can receive at §chttps://verany.net§f."
                                    ));
                                }
                            });
                        }
                        PunishmentManager.getInstance().executePunishment(OriginPunishment.PunishmentReason.SECURITY_BAN, args[0], null, player.getUniqueId(), null, OriginPunishment.PunishmentType.BAN);
                    }
                } else {
                    player.sendMessage(
                            Origin.getInstance().getPrefix() + "§7Dieser Spieler ist §ebereits §7gebannt!",
                            Origin.getInstance().getPrefix() + "§7This player §ealready §7banned!"
                    );
                }
            } else {
                player.sendMessage(
                        Origin.getInstance().getPrefix() + "§7Bitte nutze: §f/securityban [IP-Address]",
                        Origin.getInstance().getPrefix() + "§7Please use: §f/securityban [IP-Address]"
                );
                player.sendMessage(
                        Origin.getInstance().getPrefix() + "§7Bitte nutze: §f/securityban [Name]",
                        Origin.getInstance().getPrefix() + "§7Please use: §f/securityban [Name]"
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

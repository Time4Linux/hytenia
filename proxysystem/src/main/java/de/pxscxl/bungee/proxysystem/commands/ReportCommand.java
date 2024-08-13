package de.pxscxl.bungee.proxysystem.commands;

import com.google.common.base.Joiner;
import de.pxscxl.origin.bungee.Origin;
import de.pxscxl.origin.bungee.api.OriginPlayer;
import de.pxscxl.origin.bungee.api.manager.ClanManager;
import de.pxscxl.origin.bungee.api.manager.NotifyManager;
import de.pxscxl.origin.bungee.api.manager.OriginManager;
import de.pxscxl.origin.bungee.api.manager.ReportManager;
import de.pxscxl.origin.utils.enums.Rank;
import de.pxscxl.origin.utils.objects.origin.OriginReport;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class ReportCommand extends Command {

    public ReportCommand() {
        super("report");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof ProxiedPlayer)) {
            commandSender.sendMessage("This command is only executable as a player!");
            return;
        }

        OriginPlayer player = OriginManager.getInstance().getPlayer((ProxiedPlayer) commandSender);
        if (args.length == 2) {
            OriginPlayer target = OriginManager.getInstance().getPlayer(args[0]);
            if (target != null && target.isOnline()) {
                if (target != player) {
                    if (Arrays.stream(OriginReport.ReportType.values()).anyMatch(type -> type.name().equalsIgnoreCase(args[1]))) {
                        OriginReport.ReportType reportType = Arrays.stream(OriginReport.ReportType.values()).filter(type -> type.name().equalsIgnoreCase(args[1])).findFirst().orElse(null);
                        if (reportType != null) {
                            player.sendMessage(
                                    Origin.getInstance().getPrefix() + "§7Dein Report wurde §aerfolgreich §7erstellt",
                                    Origin.getInstance().getPrefix() + "§7Your report was §asuccessfully §7created"
                            );
                            OriginManager.getInstance().getPlayers().forEach(players -> {
                                if (players.hasPriorityAccess(Rank.SUPPORTER.getPriority()) && NotifyManager.getInstance().getObject(players.getUniqueId()).get("report").getAsBoolean()) {
                                    players.sendMessage();
                                    players.sendMessage(
                                            Origin.getInstance().getPrefix() + "§7Ein neuer §cReport §7ist eingegangen!",
                                            Origin.getInstance().getPrefix() + "§7A new §creport §7has appeared!");
                                    players.sendMessage();
                                    players.sendMessage(
                                            Origin.getInstance().getPrefix() + target.getDisplayName() + " §7wurde wegen §c" + reportType.name() + " §7gemeldet.",
                                            Origin.getInstance().getPrefix() + target.getDisplayName() + " §7was reported because of §c" + reportType.name() + "§7."
                                    );
                                    players.sendMessage(
                                            Origin.getInstance().getPrefix() + "§7Server§8: §e" + target.getServer().getName() + " §f| §7Uhrzeit§8: §e" + new SimpleDateFormat("HH:mm").format(new Date()),
                                            Origin.getInstance().getPrefix() + "§7Server§8: §e" + target.getServer().getName() + " §f| §7Time§8: §e" + new SimpleDateFormat("HH:mm").format(new Date())
                                    );
                                    players.sendMessage();
                                }
                            });
                            ReportManager.getInstance().executeReport(reportType, target.getUniqueId(), player.getUniqueId(), target.getServer().getName(), OriginReport.ReportState.OPENED);
                        }
                    } else {
                        player.sendMessage(
                                Origin.getInstance().getPrefix() + "§7Dieser Grund wurde §cnicht §7gefunden!",
                                Origin.getInstance().getPrefix() + "§7This reason was §cnot §7found!"
                        );
                        player.sendMessage(Origin.getInstance().getPrefix() + "§7" + player.language("Gründe", "Reasons") + "§8: §f" + Joiner.on(", ").join(OriginReport.ReportType.values()));
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
                    Origin.getInstance().getPrefix() + "§7Bitte nutze: §f/report [Name] [Grund]",
                    Origin.getInstance().getPrefix() + "§7Please use: §f/report [Name] [Reason]"
            );
        }
    }
}

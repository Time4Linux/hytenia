package de.pxscxl.bungee.proxysystem.commands;

import de.pxscxl.bungee.proxysystem.ProxySystem;
import de.pxscxl.origin.bungee.Origin;
import de.pxscxl.origin.bungee.api.OriginPlayer;
import de.pxscxl.origin.bungee.api.manager.OriginManager;
import de.pxscxl.origin.utils.enums.Rank;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class PointsCommand extends Command {

    public PointsCommand() {
        super("points");
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
                int points = player.getPoints();
                player.sendMessage(
                        Origin.getInstance().getPrefix() + "§7Du hast derzeit §e" + points + " §7" + (points == 1 ? "Punkt" : "Punkte"),
                        Origin.getInstance().getPrefix() + "§7You currently have §e" + points + " §7" + (points == 1 ? "point" : "points")
                );
                break;
            case 1:
                OriginPlayer target = OriginManager.getInstance().getPlayer(args[0]);
                if (target != null) {
                    if (target != player) {
                        points = target.getPoints();
                        player.sendMessage(
                                Origin.getInstance().getPrefix() + target.getDisplayName() + " §7hat derzeit §e" + points + " §7" + (points == 1 ? "Punkt" : "Punkte"),
                                Origin.getInstance().getPrefix() + target.getDisplayName() + " §7has currently §e" + points + " §7" + (points == 1 ? "point" : "points")
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
            case 3:
                if (player.hasPriorityAccess(Rank.ADMINISTRATOR.getPriority())) {
                    target = OriginManager.getInstance().getPlayer(args[1]);
                    if (target != null) {
                        points = Integer.parseInt(args[2]);
                        if (args[0].equalsIgnoreCase("add")) {
                            player.sendMessage(
                                    Origin.getInstance().getPrefix() + "§7Du hast " + target.getDisplayName() + " §e" + points + " §7" + (points == 1 ? "Punkt" : "Punkte") + " hinzugefügt.",
                                    Origin.getInstance().getPrefix() + "§7You added " + target.getDisplayName() + " §e" + points + " §7" + (points == 1 ? "Punkt" : "Punkte") + "."
                            );
                            ProxyServer.getInstance().getScheduler().runAsync(ProxySystem.getInstance(), () -> target.addPoints(points));
                        } else if (args[0].equalsIgnoreCase("remove")) {
                            player.sendMessage(
                                    Origin.getInstance().getPrefix() + "§7Du hast " + target.getDisplayName() + " §e" + points + " §7" + (points == 1 ? "Punkt" : "Punkte") + " entfernt.",
                                    Origin.getInstance().getPrefix() + "§7You removed " + target.getDisplayName() + " §e" + points + " §7" + (points == 1 ? "Punkt" : "Punkte") + "."
                            );
                            ProxyServer.getInstance().getScheduler().runAsync(ProxySystem.getInstance(), () -> target.removePoints(points));
                        } else if (args[0].equalsIgnoreCase("set")) {
                            player.sendMessage(
                                    Origin.getInstance().getPrefix() + target.getDisplayName() + " §7hat nun §e" + points + " §7" + (points == 1 ? "Punkt" : "Punkte") + ".",
                                    Origin.getInstance().getPrefix() + target.getDisplayName() + " §7has now §e" + points + " §7" + (points == 1 ? "Punkt" : "Punkte") + "."
                            );
                            ProxyServer.getInstance().getScheduler().runAsync(ProxySystem.getInstance(), () -> target.setPoints(points));
                        } else {
                            player.sendMessage(
                                    Origin.getInstance().getPrefix() + "§7Bitte nutze: §f/points [Name]",
                                    Origin.getInstance().getPrefix() + "§7Please use: §f/points [Name]"
                            );
                            player.sendMessage(
                                    Origin.getInstance().getPrefix() + "§7Bitte nutze: §f/points add [Name] [Punkte]",
                                    Origin.getInstance().getPrefix() + "§7Please use: §f/points add [Name] [Points]"
                            );
                            player.sendMessage(
                                    Origin.getInstance().getPrefix() + "§7Bitte nutze: §f/points set [Name] [Punkte]",
                                    Origin.getInstance().getPrefix() + "§7Please use: §f/points set [Name] [Points]"
                            );
                            player.sendMessage(
                                    Origin.getInstance().getPrefix() + "§7Bitte nutze: §f/points remove [Name] [Punkte]",
                                    Origin.getInstance().getPrefix() + "§7Please use: §f/points remove [Name] [Points]"
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
                            Origin.getInstance().getPrefix() + "§7Du hast §ckeine §7Rechte diesen Befehl auszuführen!",
                            Origin.getInstance().getPrefix() + "§7You §cdon't §7have permission to perform this command!"
                    );
                }
                break;
            default:
                player.sendMessage(
                        Origin.getInstance().getPrefix() + "§7Bitte nutze: §f/points [Name]",
                        Origin.getInstance().getPrefix() + "§7Please use: §f/points [Name]"
                );
                player.sendMessage(
                        Origin.getInstance().getPrefix() + "§7Bitte nutze: §f/points add [Name] [Punkte]",
                        Origin.getInstance().getPrefix() + "§7Please use: §f/points add [Name] [Points]"
                );
                player.sendMessage(
                        Origin.getInstance().getPrefix() + "§7Bitte nutze: §f/points set [Name] [Punkte]",
                        Origin.getInstance().getPrefix() + "§7Please use: §f/points set [Name] [Points]"
                );
                player.sendMessage(
                        Origin.getInstance().getPrefix() + "§7Bitte nutze: §f/points remove [Name] [Punkte]",
                        Origin.getInstance().getPrefix() + "§7Please use: §f/points remove [Name] [Points]"
                );
                break;
        }
    }
}

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

public class CoinsCommand extends Command {

    public CoinsCommand() {
        super("coins");
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
                int coins = player.getCoins();
                player.sendMessage(
                        Origin.getInstance().getPrefix() + "§7Du hast derzeit §e" + coins + " §7" + (coins == 1 ? "Coin" : "Coins"),
                        Origin.getInstance().getPrefix() + "§7You currently have §e" + coins + " §7" + (coins == 1 ? "coin" : "coins")
                );
                break;
            case 1:
                OriginPlayer target = OriginManager.getInstance().getPlayer(args[0]);
                if (target != null) {
                    if (target != player) {
                        coins = target.getCoins();
                        player.sendMessage(
                                Origin.getInstance().getPrefix() + target.getDisplayName() + " §7hat derzeit §e" + coins + " §7" + (coins == 1 ? "Coin" : "Coins"),
                                Origin.getInstance().getPrefix() + target.getDisplayName() + " §7has currently §e" + coins + " §7" + (coins == 1 ? "coin" : "coins")
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
                        coins = Integer.parseInt(args[2]);
                        if (args[0].equalsIgnoreCase("add")) {
                            player.sendMessage(
                                    Origin.getInstance().getPrefix() + "§7Du hast " + target.getDisplayName() + " §e" + coins + " §7" + (coins == 1 ? "Coin" : "Coins") + " hinzugefügt.",
                                    Origin.getInstance().getPrefix() + "§7You added " + target.getDisplayName() + " §e" + coins + " §7" + (coins == 1 ? "Coin" : "Coins") + "."
                            );
                            ProxyServer.getInstance().getScheduler().runAsync(ProxySystem.getInstance(), () -> target.addCoins(coins));
                        } else if (args[0].equalsIgnoreCase("remove")) {
                            player.sendMessage(
                                    Origin.getInstance().getPrefix() + "§7Du hast " + target.getDisplayName() + " §e" + coins + " §7" + (coins == 1 ? "Coin" : "Coins") + " entfernt.",
                                    Origin.getInstance().getPrefix() + "§7You removed " + target.getDisplayName() + " §e" + coins + " §7" + (coins == 1 ? "Coin" : "Coins") + "."
                            );
                            ProxyServer.getInstance().getScheduler().runAsync(ProxySystem.getInstance(), () -> target.removeCoins(coins));
                        } else if (args[0].equalsIgnoreCase("set")) {
                            player.sendMessage(
                                    Origin.getInstance().getPrefix() + target.getDisplayName() + " §7hat nun §e" + coins + " §7" + (coins == 1 ? "Coin" : "Coins") + ".",
                                    Origin.getInstance().getPrefix() + target.getDisplayName() + " §7has now §e" + coins + " §7" + (coins == 1 ? "Coin" : "Coins") + "."
                            );
                            ProxyServer.getInstance().getScheduler().runAsync(ProxySystem.getInstance(), () -> target.setCoins(coins));
                        } else {
                            player.sendMessage(
                                    Origin.getInstance().getPrefix() + "§7Bitte nutze: §f/coins [Name]",
                                    Origin.getInstance().getPrefix() + "§7Please use: §f/coins [Name]"
                            );
                            player.sendMessage(
                                    Origin.getInstance().getPrefix() + "§7Bitte nutze: §f/coins add [Name] [Coins]",
                                    Origin.getInstance().getPrefix() + "§7Please use: §f/coins add [Name] [Coins]"
                            );
                            player.sendMessage(
                                    Origin.getInstance().getPrefix() + "§7Bitte nutze: §f/coins set [Name] [Coins]",
                                    Origin.getInstance().getPrefix() + "§7Please use: §f/coins set [Name] [Coins]"
                            );
                            player.sendMessage(
                                    Origin.getInstance().getPrefix() + "§7Bitte nutze: §f/coins remove [Name] [Coins]",
                                    Origin.getInstance().getPrefix() + "§7Please use: §f/coins remove [Name] [Coins]"
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
                        Origin.getInstance().getPrefix() + "§7Bitte nutze: §f/coins [Name]",
                        Origin.getInstance().getPrefix() + "§7Please use: §f/coins [Name]"
                );
                player.sendMessage(
                        Origin.getInstance().getPrefix() + "§7Bitte nutze: §f/coins add [Name] [Coins]",
                        Origin.getInstance().getPrefix() + "§7Please use: §f/coins add [Name] [Coins]"
                );
                player.sendMessage(
                        Origin.getInstance().getPrefix() + "§7Bitte nutze: §f/coins set [Name] [Coins]",
                        Origin.getInstance().getPrefix() + "§7Please use: §f/coins set [Name] [Coins]"
                );
                player.sendMessage(
                        Origin.getInstance().getPrefix() + "§7Bitte nutze: §f/coins remove [Name] [Coins]",
                        Origin.getInstance().getPrefix() + "§7Please use: §f/coins remove [Name] [Coins]"
                );
                break;
        }
    }
}

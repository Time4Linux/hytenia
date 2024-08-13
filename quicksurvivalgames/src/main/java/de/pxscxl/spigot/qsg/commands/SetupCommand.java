package de.pxscxl.spigot.qsg.commands;

import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.origin.utils.enums.Rank;
import de.pxscxl.spigot.qsg.QSG;
import de.pxscxl.spigot.qsg.manager.GameManager;
import de.pxscxl.spigot.qsg.manager.MapManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetupCommand extends Command {

    public SetupCommand() {
        super("setup");
    }

    @Override
    public boolean execute(CommandSender commandSender, String arg1, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("This command is only executable as a player!");
            return true;
        }

        OriginPlayer player = OriginManager.getInstance().getPlayer((Player) commandSender);
        if (player.hasPriorityAccess(Rank.YOUTUBER.getPriority())) {
            if (args.length != 0) {
                if (args[0].equalsIgnoreCase("setlobbyspawn")) {
                    if (args.length == 1) {
                        MapManager.getInstance().setWaitingLobby(player);
                    } else {
                        player.sendMessage(
                                QSG.getInstance().getPrefix() + "§7Bitte nutze: §f/setup setlobbyspawn",
                                QSG.getInstance().getPrefix() + "§7Please use: §f/setup setlobbyspawn"
                        );
                    }
                } else if (args[0].equalsIgnoreCase("addmap")) {
                    if (args.length == 2) {
                        MapManager.getInstance().addMap(player, args[1]);
                    } else {
                        player.sendMessage(
                                QSG.getInstance().getPrefix() + "§7Bitte nutze: §f/setup addmap [Map]",
                                QSG.getInstance().getPrefix() + "§7Please use: §f/setup addmap [Map]"
                        );
                    }
                } else if (args[0].equalsIgnoreCase("removemap")) {
                    if (args.length == 2) {
                        MapManager.getInstance().removeMap(player, args[1]);
                    } else {
                        player.sendMessage(
                                QSG.getInstance().getPrefix() + "§7Bitte nutze: §f/setup removemap [Map]",
                                QSG.getInstance().getPrefix() + "§7Please use: §f/setup removemap [Map]"
                        );
                    }
                } else if (args[0].equalsIgnoreCase("setspawn")) {
                    if (args.length == 3) {
                        String mapName = args[1];
                        int spawn = Integer.parseInt(args[2]);
                        if (MapManager.getInstance().getMap(mapName) != null) {
                            MapManager.Map map = MapManager.getInstance().getMap(mapName);
                            if (spawn >= 1 && spawn <= GameManager.getInstance().getMaxPlayers()) {
                                map.setSpawn(player, spawn);
                            } else {
                                player.sendMessage(QSG.getInstance().getPrefix() + "§8/§csetup setspawn §8<§cMap§8> §8<§c1-" + GameManager.getInstance().getMaxPlayers() + "§8>");
                            }
                        } else {
                            player.sendMessage(
                                    QSG.getInstance().getPrefix() + "§7Diese Karte existiert §cnicht",
                                    QSG.getInstance().getPrefix() + "§7This map does §cnot §7exist");
                        }
                    } else {
                        player.sendMessage(
                                QSG.getInstance().getPrefix() + "§7Bitte nutze: §f/setup setspawn [Map] [1-" + GameManager.getInstance().getMaxPlayers() + "]",
                                QSG.getInstance().getPrefix() + "§7Please use: §f/setup setspawn [Map] [1-" + GameManager.getInstance().getMaxPlayers() + "]"
                        );
                    }
                }
            } else {
                player.sendMessage(
                        QSG.getInstance().getPrefix() + "§7Bitte nutze: §f/setup setlobbyspawn",
                        QSG.getInstance().getPrefix() + "§7Please use: §f/setup setlobbyspawn"
                );
                player.sendMessage(
                        QSG.getInstance().getPrefix() + "§7Bitte nutze: §f/setup setspawn [Map] [1-" + GameManager.getInstance().getMaxPlayers() + "]",
                        QSG.getInstance().getPrefix() + "§7Please use: §f/setup setspawn [Map] [1-" + GameManager.getInstance().getMaxPlayers() + "]"
                );
                player.sendMessage(
                        QSG.getInstance().getPrefix() + "§7Bitte nutze: §f/setup addmap [Map]",
                        QSG.getInstance().getPrefix() + "§7Please use: §f/setup addmap [Map]"
                );
                player.sendMessage(
                        QSG.getInstance().getPrefix() + "§7Bitte nutze: §f/setup removemap [Map]",
                        QSG.getInstance().getPrefix() + "§7Please use: §f/setup removemap [Map]"
                );
            }
        } else {
            player.sendMessage(QSG.getInstance().getPrefix() + "§7You §cdon't §7have permission to perform this command!");
        }
        return false;
    }
}
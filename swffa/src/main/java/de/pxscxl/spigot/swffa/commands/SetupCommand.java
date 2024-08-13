package de.pxscxl.spigot.swffa.commands;

import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.origin.utils.enums.Rank;
import de.pxscxl.spigot.swffa.SkyWarsFFA;
import de.pxscxl.spigot.swffa.manager.MapManager;
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
        if (player.hasPriorityAccess(Rank.ADMINISTRATOR.getPriority())) {
            if (args.length != 0) {
                if (args[0].equalsIgnoreCase("addmap")) {
                    if (args.length == 2) {
                        MapManager.getInstance().addMap(player, args[1]);
                    } else {
                        player.sendMessage(
                                SkyWarsFFA.getInstance().getPrefix() + "§7Bitte nutze: §f/setup addmap [Map]",
                                SkyWarsFFA.getInstance().getPrefix() + "§7Please use: §f/setup addmap [Map]"
                        );
                    }
                } else if (args[0].equalsIgnoreCase("removemap")) {
                    if (args.length == 2) {
                        MapManager.getInstance().removeMap(player, args[1]);
                    } else {
                        player.sendMessage(
                                SkyWarsFFA.getInstance().getPrefix() + "§7Bitte nutze: §f/setup removemap [Map]",
                                SkyWarsFFA.getInstance().getPrefix() + "§7Please use: §f/setup removemap [Map]"
                        );
                    }
                } else if (args[0].equalsIgnoreCase("setspawn")) {
                    if (args.length == 2) {
                        if (MapManager.getInstance().getMapsYamlConfiguration().getStringList("Maps").stream().anyMatch(name -> name.equalsIgnoreCase(args[1]))) {
                            MapManager.getInstance().getMap(args[1]).setSpawn(player);
                        } else {
                            player.sendMessage(
                                    SkyWarsFFA.getInstance().getPrefix() + "§7Diese Map existiert §cnicht§7!",
                                    SkyWarsFFA.getInstance().getPrefix() + "§7This map does §cnot §7exist!"
                            );
                        }
                    } else {
                        player.sendMessage(
                                SkyWarsFFA.getInstance().getPrefix() + "§7Bitte nutze: §f/setup setspawn [Map]",
                                SkyWarsFFA.getInstance().getPrefix() + "§7Please use: §f/setup setspawn [Map]"
                        );
                    }
                } else if (args[0].equalsIgnoreCase("sethighest")) {
                    if (args.length == 2) {
                        if (MapManager.getInstance().getMapsYamlConfiguration().getStringList("Maps").stream().anyMatch(name -> name.equalsIgnoreCase(args[1]))) {
                            MapManager.getInstance().getMap(args[1]).setHighest(player);
                        } else {
                            player.sendMessage(
                                    SkyWarsFFA.getInstance().getPrefix() + "§7Diese Map existiert §cnicht§7!",
                                    SkyWarsFFA.getInstance().getPrefix() + "§7This map does §cnot §7exist!"
                            );
                        }
                    } else {
                        player.sendMessage(
                                SkyWarsFFA.getInstance().getPrefix() + "§7Bitte nutze: §f/setup sethighest [Map]",
                                SkyWarsFFA.getInstance().getPrefix() + "§7Please use: §f/setup sethighest [Map]"
                        );
                    }
                } else if (args[0].equalsIgnoreCase("setlowest")) {
                    if (args.length == 2) {
                        if (MapManager.getInstance().getMapsYamlConfiguration().getStringList("Maps").stream().anyMatch(name -> name.equalsIgnoreCase(args[1]))) {
                            MapManager.getInstance().getMap(args[1]).setLowest(player);
                        } else {
                            player.sendMessage(
                                    SkyWarsFFA.getInstance().getPrefix() + "§7Diese Map existiert §cnicht§7!",
                                    SkyWarsFFA.getInstance().getPrefix() + "§7This map does §cnot §7exist!"
                            );
                        }
                    } else {
                        player.sendMessage(
                                SkyWarsFFA.getInstance().getPrefix() + "§7Bitte nutze: §f/setup setlowest [Map]",
                                SkyWarsFFA.getInstance().getPrefix() + "§7Please use: §f/setup setlowest [Map]"
                        );
                    }
                }
            } else {
                player.sendMessage(
                        SkyWarsFFA.getInstance().getPrefix() + "§7Bitte nutze: §f/setup setspawn [Map]",
                        SkyWarsFFA.getInstance().getPrefix() + "§7Please use: §f/setup setspawn [Map]"
                );
                player.sendMessage(
                        SkyWarsFFA.getInstance().getPrefix() + "§7Bitte nutze: §f/setup sethighest [Map]",
                        SkyWarsFFA.getInstance().getPrefix() + "§7Please use: §f/setup sethighest [Map]"
                );
                player.sendMessage(
                        SkyWarsFFA.getInstance().getPrefix() + "§7Bitte nutze: §f/setup setlowest [Map]",
                        SkyWarsFFA.getInstance().getPrefix() + "§7Please use: §f/setup setlowest [Map]"
                );
                player.sendMessage(
                        SkyWarsFFA.getInstance().getPrefix() + "§7Bitte nutze: §f/setup addmap [Map]",
                        SkyWarsFFA.getInstance().getPrefix() + "§7Please use: §f/setup addmap [Map]"
                );
                player.sendMessage(
                        SkyWarsFFA.getInstance().getPrefix() + "§7Bitte nutze: §f/setup removemap [Map]",
                        SkyWarsFFA.getInstance().getPrefix() + "§7Please use: §f/setup removemap [Map]"
                );
            }
        } else {
            player.sendMessage(SkyWarsFFA.getInstance().getPrefix() + "§7You §cdon't §7have permission to perform this command!");
        }
        return false;
    }
}
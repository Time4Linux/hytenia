package de.pxscxl.spigot.qsg.commands;

import com.google.common.base.Joiner;
import de.pxscxl.origin.spigot.Origin;
import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.origin.utils.enums.Rank;
import de.pxscxl.spigot.qsg.QSG;
import de.pxscxl.spigot.qsg.manager.MapManager;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class ForcemapCommand extends Command {

    public ForcemapCommand() {
        super("forcemap");
    }

    @Override
    public boolean execute(CommandSender commandSender, String arg1, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("This command is only executable as a player!");
            return true;
        }

        OriginPlayer player = OriginManager.getInstance().getPlayer((Player) commandSender);
        if (player.hasPriorityAccess(Rank.YOUTUBER.getPriority())) {
            if (args.length == 1) {
                String name = args[0];
                if (name.equalsIgnoreCase("list")) {
                    if (!MapManager.getInstance().getAvailableMaps().isEmpty()) {
                        List<String> mapNames = MapManager.getInstance().getAvailableMaps().stream().map(MapManager.Map::getName).collect(Collectors.toList());
                        player.sendMessage(
                                QSG.getInstance().getPrefix() + "§7Derzeit existieren folgende Maps§8: §e" + Joiner.on(", ").join(mapNames),
                                QSG.getInstance().getPrefix() + "§7Currently the following maps exist§8: §e" + Joiner.on(", ").join(mapNames)
                        );
                    } else {
                        player.sendMessage(
                                QSG.getInstance().getPrefix() + "§7Derzeit wurden noch §ckeine §7Maps eingerichtet!",
                                QSG.getInstance().getPrefix() + "§7Currently §cno §7maps have been set up!"
                        );
                    }
                } else {
                    if (MapManager.getInstance().getAvailableMaps().stream().anyMatch(map -> map.getName().equalsIgnoreCase(args[0]))) {
                        MapManager.Map map = MapManager.getInstance().getMap(name);
                        if (MapManager.getInstance().getActiveMap() != map) {
                            MapManager.getInstance().setActiveMap(map);
                            player.sendMessage(
                                    QSG.getInstance().getPrefix() + "§7Die Map §e" + map.getName() + " §7wurde ausgewählt",
                                    QSG.getInstance().getPrefix() + "§7The map §e" + map.getName() + " §7was selected"
                            );
                            player.playSound(player.getLocation(), Sound.LEVEL_UP, 10F, 10F);
                        } else {
                            player.sendMessage(
                                    QSG.getInstance().getPrefix() + "§7Diese Map ist §ebereits §7ausgewählt!",
                                    QSG.getInstance().getPrefix() + "§7This map is §ealready §7selected!"
                            );
                        }
                    } else {
                        player.sendMessage(
                                QSG.getInstance().getPrefix() + "§7Diese Map existiert §cnicht§7!",
                                QSG.getInstance().getPrefix() + "§7This map does §cnot §7exist!"
                        );
                    }
                }
            } else {
                player.sendMessage(
                        QSG.getInstance().getPrefix() + "§7Bitte nutze: §f/forcemap [Map]",
                        QSG.getInstance().getPrefix() + "§7Please use: §f/forcemap [Map]"
                );
            }
        } else {
            player.sendMessage(QSG.getInstance().getPrefix() + "§7You §cdon't §7have permission to perform this command!");
        }
        return false;
    }
}
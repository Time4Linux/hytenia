package de.pxscxl.spigot.swffa.commands;

import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.origin.utils.enums.Rank;
import de.pxscxl.spigot.swffa.SkyWarsFFA;
import de.pxscxl.spigot.swffa.manager.GameManager;
import de.pxscxl.spigot.swffa.manager.MapManager;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NextMapCommand extends Command {

    public NextMapCommand() {
        super("nextmap");
    }

    @Override
    public boolean execute(CommandSender commandSender, String arg1, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("This command is only executable as a player!");
            return true;
        }

        OriginPlayer player = OriginManager.getInstance().getPlayer((Player) commandSender);
        if (player.hasPriorityAccess(Rank.ADMINISTRATOR.getPriority())) {
            if (GameManager.getInstance().isMapChange()) {
                if (args.length == 0) {
                    if (MapManager.getInstance().getTime() > 10) {
                        MapManager.getInstance().setTime(11);
                        player.sendMessage(
                                SkyWarsFFA.getInstance().getPrefix() + "§7Der §eMapwechsel §7wurde beschleunigt",
                                SkyWarsFFA.getInstance().getPrefix() + "§7The §emap change §7was accelerated"
                        );
                        player.playSound(player.getLocation(), Sound.LEVEL_UP, 10F, 10F);
                    } else {
                        player.sendMessage(
                                SkyWarsFFA.getInstance().getPrefix() + "§7Die Map wird §ebereits §7gewechselt!",
                                SkyWarsFFA.getInstance().getPrefix() + "§7The map is §ealready §7changing!"
                        );
                    }
                } else {
                    player.sendMessage(
                            SkyWarsFFA.getInstance().getPrefix() + "§7Bitte nutze: §f/nextmap",
                            SkyWarsFFA.getInstance().getPrefix() + "§7Please use: §f/nextmap"
                    );
                }
            } else {
                player.sendMessage(
                        SkyWarsFFA.getInstance().getPrefix() + "§7Der Map-Wechsel ist derzeit §cdeaktiviert",
                        SkyWarsFFA.getInstance().getPrefix() + "§7The Map-Change is currently §cdeactivated"
                );
            }
        } else {
            player.sendMessage(SkyWarsFFA.getInstance().getPrefix() + "§7You §cdon't §7have permission to perform this command!");
        }
        return false;
    }
}

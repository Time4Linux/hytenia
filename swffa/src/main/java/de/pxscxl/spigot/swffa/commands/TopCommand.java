package de.pxscxl.spigot.swffa.commands;

import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.spigot.swffa.SkyWarsFFA;
import de.pxscxl.spigot.swffa.manager.StatsManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.atomic.AtomicInteger;

public class TopCommand extends Command {

    public TopCommand() {
        super("top");
    }

    @Override
    public boolean execute(CommandSender commandSender, String arg, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("This command is only executable as a player!");
            return true;
        }

        OriginPlayer player = OriginManager.getInstance().getPlayer((Player) commandSender);
        if (args.length == 0) {
            player.sendMessage(
                    SkyWarsFFA.getInstance().getPrefix() + "§7Die Top10 §8[§eELO§8] §7wird §ageladen§8...",
                    SkyWarsFFA.getInstance().getPrefix() + "§7The Top10 §8[§eELO§8] §7will be §aloaded§8..."
            );
            Bukkit.getScheduler().runTaskLaterAsynchronously(SkyWarsFFA.getInstance(), () -> {
                AtomicInteger index = new AtomicInteger(1);
                StatsManager.getInstance().getTop10().forEach(stats -> player.sendMessage(SkyWarsFFA.getInstance().getPrefix() + "§e" + index.getAndIncrement() + " §8┃ " + OriginManager.getInstance().getPlayer(stats.getUuid()).getRealDisplayName() + " §8- §7Elo§8: §e" + stats.getElo()));
            }, 5);
        } else {
            player.sendMessage(
                    SkyWarsFFA.getInstance().getPrefix() + "§7Bitte nutze: §f/top",
                    SkyWarsFFA.getInstance().getPrefix() + "§7Please use: §f/top"
            );
        }
        return true;
    }
}

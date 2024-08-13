package de.pxscxl.spigot.mlgrush.commands;

import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.spigot.mlgrush.MLGRush;
import de.pxscxl.spigot.mlgrush.manager.StatsManager;
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
                    MLGRush.getInstance().getPrefix() + "§7Die Top10 §8[§eELO§8] §7wird §ageladen§8...",
                    MLGRush.getInstance().getPrefix() + "§7The Top10 §8[§eELO§8] §7will be §aloaded§8..."
            );
            Bukkit.getScheduler().runTaskLaterAsynchronously(MLGRush.getInstance(), () -> {
                AtomicInteger index = new AtomicInteger(1);
                StatsManager.getInstance().getTop10().forEach(stats -> player.sendMessage(MLGRush.getInstance().getPrefix() + "§e" + index.getAndIncrement() + " §8┃ " + OriginManager.getInstance().getPlayer(stats.getUuid()).getRealDisplayName() + " §8- §7Elo§8: §e" + stats.getElo()));
            }, 5);
        } else {
            player.sendMessage(
                    MLGRush.getInstance().getPrefix() + "§7Bitte nutze: §f/top",
                    MLGRush.getInstance().getPrefix() + "§7Please use: §f/top"
            );
        }
        return true;
    }
}

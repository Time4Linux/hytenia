package de.pxscxl.spigot.qsg.commands;

import de.pxscxl.cloud.CloudAPI;
import de.pxscxl.cloud.State;
import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.origin.utils.enums.Rank;
import de.pxscxl.spigot.qsg.QSG;
import de.pxscxl.spigot.qsg.manager.GameManager;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCommand extends Command {

    public StartCommand() {
        super("start");
    }

    @Override
    public boolean execute(CommandSender commandSender, String arg1, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("This command is only executable as a player!");
            return true;
        }

        OriginPlayer player = OriginManager.getInstance().getPlayer((Player) commandSender);
        if (player.hasPriorityAccess(Rank.YOUTUBER.getPriority())) {
            if (args.length == 0) {
                if (CloudAPI.getInstance().getLocalServer().getState().equals(State.LOBBY)) {
                    if (OriginManager.getInstance().getPlayers().size() >= GameManager.getInstance().getMinPlayers()) {
                        if (GameManager.getInstance().getCountdown() < 12) {
                            player.sendMessage(
                                    QSG.getInstance().getPrefix() + "§7Das Spiel wurde §ebereits §7gestartet",
                                    QSG.getInstance().getPrefix() + "§7The game has §ealready §7been started"
                            );
                        } else {
                            player.sendMessage(
                                    QSG.getInstance().getPrefix() + "§7Das §eSpiel §7wurde §agestartet",
                                    QSG.getInstance().getPrefix() + "§7The §egame §7was §astarted"
                            );
                            GameManager.getInstance().setCountdown(11);
                            OriginManager.getInstance().getPlayers().forEach(players -> players.setLevel(11));
                            player.playSound(player.getLocation(), Sound.LEVEL_UP, 10F, 10F);
                        }
                    } else {
                        player.sendMessage(
                                QSG.getInstance().getPrefix() + "§7Es sind §cnicht §7genügend Spieler online",
                                QSG.getInstance().getPrefix() + "§7There are §cnot §7enough players online"
                        );
                    }
                } else {
                    player.sendMessage(
                            QSG.getInstance().getPrefix() + "§7Das Spiel hat §ebereits §7begonnen",
                            QSG.getInstance().getPrefix() + "§7The game is §ealready §7running"
                    );
                }
            } else {
                player.sendMessage(
                        QSG.getInstance().getPrefix() + "§7Bitte nutze: §f/start",
                        QSG.getInstance().getPrefix() + "§7Please use: §f/start"
                );
            }
        } else {
            player.sendMessage(QSG.getInstance().getPrefix() + "§7You §cdon't §7have permission to perform this command!");
        }
        return false;
    }
}

package de.pxscxl.spigot.lobby.commands;

import de.pxscxl.origin.spigot.Origin;
import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.spigot.lobby.LobbySystem;
import de.pxscxl.spigot.lobby.Utils;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BuildCommand extends Command {

    public BuildCommand() {
        super("build");

        setPermission("system.build");
        setPermissionMessage(Origin.getInstance().getPrefix() + "§7You §cdon't §7have permission to perform this command!");
    }

    @Override
    public boolean execute(CommandSender commandSender, String arg, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("This command is only executable as a player!");
            return true;
        }
        OriginPlayer player = OriginManager.getInstance().getPlayer((Player) commandSender);
        if (testPermission(player.getBukkitPlayer())) {
            if (args.length == 0) {
                if (LobbySystem.getInstance().getBuildPlayers().contains(player)) {
                    LobbySystem.getInstance().getBuildPlayers().remove(player);
                    player.setGameMode(GameMode.SURVIVAL);
                    player.getInventory().clear();
                    Utils.setSpawnItems(player);
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10F, 10F);
                    player.sendMessage(
                            Origin.getInstance().getPrefix() + "§7Du kannst nun §cnicht §7mehr §ebauen",
                            Origin.getInstance().getPrefix() + "§7You can §cno §7longer §ebuild"
                    );
                } else {
                    LobbySystem.getInstance().getBuildPlayers().add(player);
                    player.setGameMode(GameMode.CREATIVE);
                    player.getInventory().clear();
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10F, 10F);
                    player.sendMessage(
                            Origin.getInstance().getPrefix() + "§7Du kannst nun §ebauen",
                            Origin.getInstance().getPrefix() + "§7You can now §ebuild"
                    );
                }
            } else if (args.length == 1) {
                OriginPlayer target = OriginManager.getInstance().getPlayer(args[0]);
                if (target != null) {
                    if (target != player) {
                        if (LobbySystem.getInstance().getBuildPlayers().contains(target)) {
                            LobbySystem.getInstance().getBuildPlayers().remove(target);
                            target.setGameMode(GameMode.SURVIVAL);
                            target.getInventory().clear();
                            Utils.setSpawnItems(target);
                            target.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10F, 10F);
                            target.sendMessage(
                                    Origin.getInstance().getPrefix() + "§7Du kannst nun §cnicht §7mehr §ebauen",
                                    Origin.getInstance().getPrefix() + "§7You can §cno §7longer §ebuild"
                            );
                            player.sendMessage(
                                    Origin.getInstance().getPrefix() + target.getDisplayName() + " §7kann nun §cnicht §7mehr §ebauen",
                                    Origin.getInstance().getPrefix() + target.getDisplayName() + " §7can §cno §7longer §ebuild"
                            );
                        } else {
                            LobbySystem.getInstance().getBuildPlayers().add(target);
                            target.setGameMode(GameMode.CREATIVE);
                            target.getInventory().clear();
                            target.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10F, 10F);
                            target.sendMessage(
                                    Origin.getInstance().getPrefix() + "§7Du kannst nun §ebauen",
                                    Origin.getInstance().getPrefix() + "§7You can now §ebuild"
                            );
                            player.sendMessage(
                                    Origin.getInstance().getPrefix() + target.getDisplayName() + " §7kann nun §efliegen",
                                    Origin.getInstance().getPrefix() + target.getDisplayName() + " §7can now §efly"
                            );
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
                        Origin.getInstance().getPrefix() + "§7Bitte nutze: §f/build [Name]",
                        Origin.getInstance().getPrefix() + "§7Please use: §f/build [Name]"
                );
            }
        }
        return false;
    }
}

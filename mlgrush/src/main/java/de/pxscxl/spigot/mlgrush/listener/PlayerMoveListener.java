package de.pxscxl.spigot.mlgrush.listener;

import de.pxscxl.cloud.CloudAPI;
import de.pxscxl.cloud.State;
import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.spigot.mlgrush.MLGRush;
import de.pxscxl.spigot.mlgrush.manager.GameManager;
import de.pxscxl.spigot.mlgrush.manager.MapManager;
import de.pxscxl.spigot.mlgrush.manager.TeamManager;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onPlayerMove(final PlayerMoveEvent event) {
        OriginPlayer player = OriginManager.getInstance().getPlayer(event.getPlayer());
        if (CloudAPI.getInstance().getLocalServer().getState().equals(State.INGAME)) {
            if (GameManager.getInstance().isProtection() && !MLGRush.getInstance().getSpectators().contains(player)) {
                if (event.getFrom().getX() != event.getTo().getX() || event.getFrom().getZ() != event.getTo().getZ()) {
                    event.setTo(event.getFrom());
                }
            }
            if (player.getLocation().getY() < MapManager.getInstance().getActiveMap().getRespawnHeight()) {
                if (TeamManager.getInstance().getPlayerTeams().get(player) == TeamManager.Team.RED) {
                    TeamManager.Team.RED.getMembers().forEach(red -> red.teleport(MapManager.getInstance().getActiveMap().getSpawns().get(0)));
                } else {
                    TeamManager.Team.BLUE.getMembers().forEach(blue -> blue.teleport(MapManager.getInstance().getActiveMap().getSpawns().get(1)));
                }
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10F, 10F);
            }
        }
    }
}

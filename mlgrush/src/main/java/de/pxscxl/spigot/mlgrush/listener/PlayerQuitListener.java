package de.pxscxl.spigot.mlgrush.listener;

import de.pxscxl.cloud.CloudAPI;
import de.pxscxl.cloud.State;
import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.spigot.mlgrush.MLGRush;
import de.pxscxl.spigot.mlgrush.manager.GameManager;
import de.pxscxl.spigot.mlgrush.manager.KitManager;
import de.pxscxl.spigot.mlgrush.manager.ScoreboardManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        OriginPlayer player = OriginManager.getInstance().getPlayer(event.getPlayer());

        KitManager.getInstance().getInventories().remove(player.getUniqueId());

        if (CloudAPI.getInstance().getLocalServer().getState().equals(State.LOBBY)) {
            MLGRush.getInstance().getPlayers().remove(player);
        } else if (CloudAPI.getInstance().getLocalServer().getState().equals(State.INGAME)) {
            if (!MLGRush.getInstance().getSpectators().contains(player)) {
                MLGRush.getInstance().getPlayers().remove(player);
                GameManager.getInstance().finishGame();
            }
        } else if (CloudAPI.getInstance().getLocalServer().getState().equals(State.RESTART)) {
            MLGRush.getInstance().getPlayers().remove(player);
        }

        if (CloudAPI.getInstance().getLocalServer().getState().equals(State.INGAME) || CloudAPI.getInstance().getLocalServer().getState().equals(State.RESTART)) {
            if (MLGRush.getInstance().getSpectators().contains(player)) {
                MLGRush.getInstance().getSpectators().forEach(spectators -> ScoreboardManager.getInstance().updatePlayersScore(spectators));
            }
            if (MLGRush.getInstance().getSpectators().contains(player)) {
                MLGRush.getInstance().getSpectators().forEach(spectators -> ScoreboardManager.getInstance().updateSpectatorsScore(spectators));
            }
        }
    }
}

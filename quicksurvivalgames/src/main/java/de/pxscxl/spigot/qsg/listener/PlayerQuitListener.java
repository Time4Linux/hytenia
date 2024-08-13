package de.pxscxl.spigot.qsg.listener;

import de.pxscxl.cloud.CloudAPI;
import de.pxscxl.cloud.State;
import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.spigot.qsg.QSG;
import de.pxscxl.spigot.qsg.manager.GameManager;
import de.pxscxl.spigot.qsg.manager.ScoreboardManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        OriginPlayer player = OriginManager.getInstance().getPlayer(event.getPlayer());

        if (CloudAPI.getInstance().getLocalServer().getState().equals(State.LOBBY)) {
            QSG.getInstance().getPlayers().remove(player);
        } else if (CloudAPI.getInstance().getLocalServer().getState().equals(State.INGAME)) {
            if (!QSG.getInstance().getSpectators().contains(player)) {
                QSG.getInstance().getPlayers().remove(player);
                if (QSG.getInstance().getPlayers().size() == 1) {
                    GameManager.getInstance().finishGame();
                }
            }
        } else if (CloudAPI.getInstance().getLocalServer().getState().equals(State.RESTART)) {
            QSG.getInstance().getPlayers().remove(player);
        }

        if (CloudAPI.getInstance().getLocalServer().getState().equals(State.INGAME) || CloudAPI.getInstance().getLocalServer().getState().equals(State.RESTART)) {
            if (QSG.getInstance().getSpectators().contains(player)) {
                QSG.getInstance().getSpectators().forEach(spectators -> ScoreboardManager.getInstance().updatePlayersScore(spectators));
            }
            if (QSG.getInstance().getSpectators().contains(player)) {
                QSG.getInstance().getSpectators().forEach(spectators -> ScoreboardManager.getInstance().updateSpectatorsScore(spectators));
            }
        }
    }
}

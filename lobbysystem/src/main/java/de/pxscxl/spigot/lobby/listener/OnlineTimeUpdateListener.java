package de.pxscxl.spigot.lobby.listener;

import de.pxscxl.origin.spigot.api.events.OnlineTimeUpdateEvent;
import de.pxscxl.spigot.lobby.LobbySystem;
import de.pxscxl.spigot.lobby.manager.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class OnlineTimeUpdateListener implements Listener {

    @EventHandler
    public void onOnlineTimeUpdate(OnlineTimeUpdateEvent event) {
        ScoreboardManager.getInstance().updateTimeScore(event.getPlayer());
    }
}

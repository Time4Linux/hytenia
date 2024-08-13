package de.pxscxl.spigot.lobby.listener;

import de.pxscxl.origin.spigot.api.events.OnlinePlayersUpdateEvent;
import de.pxscxl.spigot.lobby.manager.ScoreboardManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class OnlinePlayersUpdateListener implements Listener {

    @EventHandler
    public void onOnlinePlayersUpdate(OnlinePlayersUpdateEvent event) {
        ScoreboardManager.getInstance().updateFriendsScore(event.getPlayer());
    }
}

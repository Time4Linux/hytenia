package de.pxscxl.spigot.lobby.listener;

import de.pxscxl.origin.spigot.api.events.CoinsUpdateEvent;
import de.pxscxl.spigot.lobby.manager.ScoreboardManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CoinsUpdateListener implements Listener {

    @EventHandler
    public void onCoinsUpdate(CoinsUpdateEvent event) {
        ScoreboardManager.getInstance().updateCoinsScore(event.getPlayer());
    }
}

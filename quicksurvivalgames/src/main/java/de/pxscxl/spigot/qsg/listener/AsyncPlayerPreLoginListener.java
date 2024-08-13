package de.pxscxl.spigot.qsg.listener;

import de.pxscxl.spigot.qsg.manager.StatsManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class AsyncPlayerPreLoginListener implements Listener {

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        if (!StatsManager.getInstance().exist(event.getUniqueId())) {
            StatsManager.getInstance().insert(event.getUniqueId());
        }
    }
}

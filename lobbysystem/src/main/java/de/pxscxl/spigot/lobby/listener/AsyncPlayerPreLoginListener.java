package de.pxscxl.spigot.lobby.listener;

import de.pxscxl.spigot.lobby.manager.HiderManager;
import de.pxscxl.spigot.lobby.manager.LocationManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class AsyncPlayerPreLoginListener implements Listener {

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        if (!LocationManager.getInstance().exist(event.getUniqueId())) {
            LocationManager.getInstance().insert(event.getUniqueId());
        }
        LocationManager.getInstance().loadLocation(event.getUniqueId());

        if (!HiderManager.getInstance().exist(event.getUniqueId())) {
            HiderManager.getInstance().insert(event.getUniqueId());
        }
        HiderManager.getInstance().loadHideMode(event.getUniqueId());
    }
}

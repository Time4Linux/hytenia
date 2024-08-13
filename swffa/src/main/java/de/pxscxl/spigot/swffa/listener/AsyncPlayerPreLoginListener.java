package de.pxscxl.spigot.swffa.listener;

import de.pxscxl.spigot.swffa.manager.KitManager;
import de.pxscxl.spigot.swffa.manager.StatsManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class AsyncPlayerPreLoginListener implements Listener {

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        if (!StatsManager.getInstance().exist(event.getUniqueId())) {
            StatsManager.getInstance().insert(event.getUniqueId());
        }

        if (!KitManager.getInstance().exist(event.getUniqueId())) {
            KitManager.getInstance().insert(event.getUniqueId());
        }
        KitManager.getInstance().loadInventory(event.getUniqueId());
    }
}

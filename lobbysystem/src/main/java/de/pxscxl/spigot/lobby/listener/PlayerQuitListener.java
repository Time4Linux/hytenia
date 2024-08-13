package de.pxscxl.spigot.lobby.listener;

import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.spigot.lobby.LobbySystem;
import de.pxscxl.spigot.lobby.manager.HiderManager;
import de.pxscxl.spigot.lobby.manager.LocationManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        OriginPlayer player = OriginManager.getInstance().getPlayer(event.getPlayer());
        Bukkit.getScheduler().runTaskAsynchronously(LobbySystem.getInstance(), () -> {
            LocationManager.getInstance().saveLocation(player);
            HiderManager.getInstance().saveHideMode(player);
        });
    }
}

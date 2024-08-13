package de.pxscxl.spigot.mlgrush.listener;

import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class PlayerChangedWorldListener implements Listener {

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        OriginPlayer player = OriginManager.getInstance().getPlayer(event.getPlayer());
        player.getOpenInventory().close();
    }
}

package de.pxscxl.spigot.swffa.listener;

import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.spigot.swffa.SkyWarsFFA;
import de.pxscxl.spigot.swffa.manager.GameManager;
import de.pxscxl.spigot.swffa.manager.KitManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        OriginPlayer player = OriginManager.getInstance().getPlayer(event.getPlayer());
        SkyWarsFFA.getInstance().getPlayers().remove(player);
        GameManager.getInstance().getTargets().remove(player);
        KitManager.getInstance().getInventories().remove(player.getUniqueId());
    }
}

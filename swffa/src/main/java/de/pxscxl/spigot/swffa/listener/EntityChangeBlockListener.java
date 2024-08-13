package de.pxscxl.spigot.swffa.listener;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.manager.OriginManager;

public class EntityChangeBlockListener implements Listener {

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (event.getEntity() instanceof Player) {
            OriginPlayer player = OriginManager.getInstance().getPlayer((Player) event.getEntity());
            event.setCancelled(!player.getGameMode().equals(GameMode.CREATIVE));
        } else {
            event.setCancelled(true);
        }
    }
}

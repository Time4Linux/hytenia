package de.pxscxl.spigot.mlgrush.listener;

import de.pxscxl.cloud.CloudAPI;
import de.pxscxl.cloud.State;
import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.spigot.mlgrush.MLGRush;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class EntityDamageByBlockListener implements Listener {

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        OriginPlayer player = OriginManager.getInstance().getPlayer((Player) event.getEntity());
        event.setCancelled(!CloudAPI.getInstance().getLocalServer().getState().equals(State.INGAME) || MLGRush.getInstance().getSpectators().contains(player));
    }
}

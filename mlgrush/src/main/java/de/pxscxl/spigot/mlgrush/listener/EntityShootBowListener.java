package de.pxscxl.spigot.mlgrush.listener;

import de.pxscxl.cloud.CloudAPI;
import de.pxscxl.cloud.State;
import de.pxscxl.spigot.mlgrush.manager.GameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

public class EntityShootBowListener implements Listener {

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {
        event.setCancelled(!CloudAPI.getInstance().getLocalServer().getState().equals(State.INGAME) || GameManager.getInstance().isProtection());
    }
}

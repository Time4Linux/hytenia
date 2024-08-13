package de.pxscxl.spigot.qsg.listener;

import de.pxscxl.cloud.CloudAPI;
import de.pxscxl.cloud.State;
import de.pxscxl.spigot.qsg.manager.GameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class PlayerFishListener implements Listener {

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        event.setCancelled(!CloudAPI.getInstance().getLocalServer().getState().equals(State.INGAME) || GameManager.getInstance().isProtection());
    }
}

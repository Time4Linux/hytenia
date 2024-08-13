package de.pxscxl.spigot.mlgrush.listener;

import de.pxscxl.cloud.CloudAPI;
import de.pxscxl.cloud.State;
import de.pxscxl.spigot.mlgrush.manager.GameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class PlayerItemConsumeListener implements Listener {

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        event.setCancelled(GameManager.getInstance().isProtection() && CloudAPI.getInstance().getLocalServer().getState().equals(State.INGAME));
    }
}

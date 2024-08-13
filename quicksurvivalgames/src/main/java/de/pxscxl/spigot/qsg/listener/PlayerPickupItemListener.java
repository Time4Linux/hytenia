package de.pxscxl.spigot.qsg.listener;

import de.pxscxl.cloud.CloudAPI;
import de.pxscxl.cloud.State;
import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.spigot.qsg.QSG;
import de.pxscxl.spigot.qsg.manager.GameManager;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PlayerPickupItemListener implements Listener {

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        OriginPlayer player = OriginManager.getInstance().getPlayer(event.getPlayer());
        event.setCancelled((!player.getGameMode().equals(GameMode.CREATIVE) && GameManager.getInstance().isProtection() && CloudAPI.getInstance().getLocalServer().getState().equals(State.INGAME)) || QSG.getInstance().getSpectators().contains(player));
    }
}

package de.pxscxl.spigot.swffa.listener;

import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.spigot.swffa.manager.KitManager;
import de.pxscxl.spigot.swffa.manager.MapManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        OriginPlayer player = OriginManager.getInstance().getPlayer(event.getPlayer());
        if ((MapManager.getInstance().getActiveMap().getLowest().getBlockY() >= event.getTo().getBlockY()) && player.getInventory().contains(Material.ENDER_CHEST)) {
            KitManager.getInstance().setKitItems(player);
            player.playSound(player.getLocation(), Sound.CLICK, 10F, 10F);
        }
    }
}

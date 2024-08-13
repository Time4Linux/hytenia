package de.pxscxl.spigot.swffa.listener;

import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.spigot.swffa.manager.KitManager;
import de.pxscxl.spigot.swffa.manager.RegionManager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        OriginPlayer player = OriginManager.getInstance().getPlayer(event.getPlayer());
        if (!RegionManager.getInstance().isInRegion(player)) {
            if (event.getClickedBlock() != null && (event.getClickedBlock().getType().equals(Material.ENDER_CHEST) || event.getClickedBlock().getType().equals(Material.CHEST) || event.getClickedBlock().getType().equals(Material.TRAPPED_CHEST))) {
                event.setCancelled(true);
                return;
            }
            event.setCancelled(event.getItem() == null || (!event.getItem().getType().equals(Material.COBBLESTONE) && !event.getItem().getType().equals(Material.ENDER_PEARL) && !event.getItem().getType().equals(Material.WEB) && !event.getItem().getType().equals(Material.SNOW_BALL) && !event.getItem().getType().equals(Material.GOLDEN_APPLE) && !event.getItem().getType().equals(Material.POTION) && !event.getItem().getType().equals(Material.FISHING_ROD)));
        } else {
            if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                if (event.getItem() != null && player.getItemInHand().getType().equals(Material.ENDER_CHEST)) {
                    KitManager.getInstance().openGUI(player);
                } else if (event.getItem() != null && player.getItemInHand().getType().equals(Material.MAGMA_CREAM)) {
                    player.send("fallback");
                }
            }
        }
    }
}

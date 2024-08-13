package de.pxscxl.spigot.qsg.listener;

import de.pxscxl.cloud.CloudAPI;
import de.pxscxl.cloud.State;
import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.spigot.qsg.manager.GameManager;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class BlockPlaceListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        OriginPlayer player = OriginManager.getInstance().getPlayer(event.getPlayer());

        if (player.getGameMode().equals(GameMode.CREATIVE)) {
            event.setCancelled(false);
            return;
        }

        if (CloudAPI.getInstance().getLocalServer().getState().equals(State.INGAME) && !GameManager.getInstance().isProtection() && event.getItemInHand().getType().equals(Material.TNT)) {
            player.getInventory().removeItem(new ItemStack(Material.TNT, 1));
            player.updateInventory();
            event.getBlock().getLocation().getWorld().spawn(event.getBlock().getLocation().add(0.5, 0.5, 0.5), TNTPrimed.class).setFuseTicks(30);
            event.setCancelled(true);
            return;
        }

        event.setCancelled(true);
    }
}

package de.pxscxl.spigot.mlgrush.listener;

import de.pxscxl.cloud.CloudAPI;
import de.pxscxl.cloud.State;
import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.inventory.ItemStackBuilder;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.spigot.mlgrush.MLGRush;
import de.pxscxl.spigot.mlgrush.manager.GameManager;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        OriginPlayer player = OriginManager.getInstance().getPlayer(event.getPlayer());

        if (CloudAPI.getInstance().getLocalServer().getState().equals(State.INGAME) && !GameManager.getInstance().isProtection() && MLGRush.getInstance().getPlayers().contains(player)) {
            event.setCancelled(false);
            GameManager.getInstance().getPlacedBlocks().add(event.getBlock());

            player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStackBuilder(player.getInventory().getItemInHand().getType()).amount(player.getInventory().getItemInHand().getAmount()).build());
            return;
        }

        event.setCancelled(!player.getGameMode().equals(GameMode.CREATIVE) && (GameManager.getInstance().isProtection()));
    }
}

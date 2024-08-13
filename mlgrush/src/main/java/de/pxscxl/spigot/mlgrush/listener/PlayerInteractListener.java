package de.pxscxl.spigot.mlgrush.listener;

import de.pxscxl.cloud.CloudAPI;
import de.pxscxl.cloud.State;
import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.inventory.InventoryBuilder;
import de.pxscxl.origin.spigot.api.inventory.ItemStackBuilder;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.spigot.mlgrush.MLGRush;
import de.pxscxl.spigot.mlgrush.manager.GameManager;
import de.pxscxl.spigot.mlgrush.manager.KitManager;
import de.pxscxl.spigot.mlgrush.manager.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        OriginPlayer player = OriginManager.getInstance().getPlayer(event.getPlayer());

        if (CloudAPI.getInstance().getLocalServer().getState().equals(State.LOBBY) || CloudAPI.getInstance().getLocalServer().getState().equals(State.RESTART) || MLGRush.getInstance().getSpectators().contains(player) || GameManager.getInstance().isProtection()) {
            try {
                if (event.getClickedBlock().getType() == Material.HOPPER || event.getClickedBlock().getType() == Material.SPRUCE_FENCE_GATE || event.getClickedBlock().getType() == Material.ANVIL || event.getClickedBlock().getType() == Material.CRAFTING_TABLE || event.getClickedBlock().getType() == Material.NOTE_BLOCK || event.getClickedBlock().getType() == Material.CHEST || event.getClickedBlock().getType() == Material.ENDER_CHEST || event.getClickedBlock().getType() == Material.TRAPPED_CHEST || event.getClickedBlock().getType() == Material.STONE_BUTTON || event.getClickedBlock().getType() == Material.DRAGON_EGG || event.getClickedBlock().getType() == Material.FURNACE || event.getClickedBlock().getType() == Material.BEACON || event.getClickedBlock().getType() == Material.FURNACE) {
                    event.setCancelled(true);
                }
            } catch (Exception ignored) {

            }
        }

        if (event.getClickedBlock() != null && event.getClickedBlock().getType().equals(Material.LEGACY_BED_BLOCK) && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            player.sendMessage(
                    MLGRush.getInstance().getPrefix() + "§7Du kannst das Bett §cnicht §7betreten",
                    MLGRush.getInstance().getPrefix() + "§7You §ccan't §7enter the bed"
            );
            event.setCancelled(true);
            return;
        }

        if (event.getItem() == null) return;

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getItem().getType().equals(Material.LEGACY_BED_BLOCK)) {
                TeamManager.getInstance().openGUI(player);
            } else if (event.getItem().getType().equals(Material.ENDER_CHEST)) {
                KitManager.getInstance().openGUI(player);
            } else if (event.getItem().getType().equals(Material.COMPASS)) {
                InventoryBuilder inventory = new InventoryBuilder(1, "§8Teleporter");

                Bukkit.getScheduler().runTaskAsynchronously(MLGRush.getInstance(), () -> MLGRush.getInstance().getPlayers().forEach(alive -> inventory.addItem(new ItemStackBuilder(Material.LEGACY_SKULL_ITEM).setData((short) 3).setSkullOwner(alive.getNick() != null ? alive.getNick() : alive.getName()).setDisplayName(alive.getDisplayName()).addLoreLine("§8§m--------------------").addLoreLine(player.language("§7§oKlicke zum Teleportieren", "§7§oClick to teleport")).build())));

                player.openInventory(inventory);
                player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 10F, 10F);
            } else if (event.getItem().getType().equals(Material.MAGMA_CREAM)) {
                player.send("fallback");
            }
        }
    }
}

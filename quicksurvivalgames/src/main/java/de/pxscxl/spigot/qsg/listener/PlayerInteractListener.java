package de.pxscxl.spigot.qsg.listener;

import de.pxscxl.cloud.CloudAPI;
import de.pxscxl.cloud.State;
import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.inventory.InventoryBuilder;
import de.pxscxl.origin.spigot.api.inventory.ItemStackBuilder;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.spigot.qsg.QSG;
import de.pxscxl.spigot.qsg.manager.ChestLootManager;
import de.pxscxl.spigot.qsg.manager.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        OriginPlayer player = OriginManager.getInstance().getPlayer(event.getPlayer());

        if (CloudAPI.getInstance().getLocalServer().getState().equals(State.LOBBY) || CloudAPI.getInstance().getLocalServer().getState().equals(State.RESTART) || QSG.getInstance().getSpectators().contains(player) || GameManager.getInstance().isProtection()) {
            try {
                if (event.getClickedBlock().getType() == Material.HOPPER || event.getClickedBlock().getType() == Material.SPRUCE_FENCE_GATE || event.getClickedBlock().getType() == Material.ANVIL || event.getClickedBlock().getType() == Material.WORKBENCH || event.getClickedBlock().getType() == Material.NOTE_BLOCK || event.getClickedBlock().getType() == Material.CHEST || event.getClickedBlock().getType() == Material.TRAP_DOOR || event.getClickedBlock().getType() == Material.ENDER_CHEST || event.getClickedBlock().getType() == Material.TRAPPED_CHEST || event.getClickedBlock().getType() == Material.FENCE_GATE || event.getClickedBlock().getType() == Material.STONE_BUTTON || event.getClickedBlock().getType() == Material.DRAGON_EGG || event.getClickedBlock().getType() == Material.FURNACE || event.getClickedBlock().getType() == Material.BEACON || event.getClickedBlock().getType() == Material.FURNACE) {
                    event.setCancelled(true);
                }
            } catch (Exception ignored) {

            }
        }

        if (event.getClickedBlock() != null && event.getClickedBlock().getType().equals(Material.CHEST) && CloudAPI.getInstance().getLocalServer().getState().equals(State.INGAME) && !QSG.getInstance().getSpectators().contains(player)) {
            if (!ChestLootManager.getInstance().getChests().contains(event.getClickedBlock().getLocation())) {
                Chest chest = (Chest) event.getClickedBlock().getState();
                List<ItemStack> itemStacks = ChestLootManager.getInstance().getRandomLoot();
                itemStacks.forEach(itemStack -> chest.getInventory().setItem(new Random().nextInt(27), itemStack));
                chest.update();
                ChestLootManager.getInstance().getChests().add(event.getClickedBlock().getLocation());
            }
            return;
        }

        if (event.getItem() == null) return;

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getItem().getType().equals(Material.COMPASS) && CloudAPI.getInstance().getLocalServer().getState().equals(State.INGAME) && QSG.getInstance().getSpectators().contains(player)) {
                InventoryBuilder inventory = new InventoryBuilder(1, "§8Teleporter");

                Bukkit.getScheduler().runTaskAsynchronously(QSG.getInstance(), () -> QSG.getInstance().getPlayers().forEach(alive -> inventory.addItem(new ItemStackBuilder(Material.SKULL_ITEM).setData((short) 3).setSkullOwner(alive.getNick() != null ? alive.getNick() : alive.getName()).setDisplayName(alive.getDisplayName()).addLoreLine("§8§m--------------------").addLoreLine(player.language("§7§oKlicke zum Teleportieren", "§7§oClick to teleport")).build())));

                player.openInventory(inventory);
                player.playSound(player.getLocation(), Sound.CLICK, 10F, 10F);
            } else if (event.getItem() != null && player.getItemInHand().getType().equals(Material.MAGMA_CREAM)) {
                player.send("fallback");
            }
        }
    }
}

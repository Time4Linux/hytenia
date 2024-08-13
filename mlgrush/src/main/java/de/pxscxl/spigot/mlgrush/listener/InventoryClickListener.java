package de.pxscxl.spigot.mlgrush.listener;

import de.pxscxl.cloud.CloudAPI;
import de.pxscxl.cloud.State;
import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.manager.NickManager;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.spigot.mlgrush.MLGRush;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Map;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        OriginPlayer player = OriginManager.getInstance().getPlayer((Player) event.getWhoClicked());

        if (player.getGameMode().equals(GameMode.CREATIVE)) {
            event.setCancelled(false);
        } else {
            if (CloudAPI.getInstance().getLocalServer().getState().equals(State.LOBBY) || CloudAPI.getInstance().getLocalServer().getState().equals(State.RESTART)) {
                event.setCancelled(!event.getView().getTitle().equals("§8" + player.language("Inventar", "Inventory")));
            } else if (CloudAPI.getInstance().getLocalServer().getState().equals(State.INGAME)) {
                event.setCancelled(MLGRush.getInstance().getSpectators().contains(player));
            }
        }

        if (event.getView() == null || event.getInventory() == null || event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;

        if (event.getView().getTitle().equals("§8Teleporter")) {
            if (event.getCurrentItem().getType().equals(Material.LEGACY_SKULL_ITEM)) {
                String name = event.getCurrentItem().getItemMeta().getDisplayName().substring(2);

                OriginPlayer target;
                if (NickManager.getInstance().getCurrentNicks().containsValue(name)) {
                    target = OriginManager.getInstance().getPlayer(NickManager.getInstance().getCurrentNicks().entrySet().stream().filter(entry -> entry.getValue().contains(name)).min(Map.Entry.comparingByKey()).get().getKey());
                } else {
                    target = OriginManager.getInstance().getPlayer(name);
                }

                if (target != null) {
                    player.teleport(target.getLocation());
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10F, 10F);
                    player.sendMessage(
                            MLGRush.getInstance().getPrefix() + "§7Du hast dich zu " + event.getCurrentItem().getItemMeta().getDisplayName() + " §7teleportiert",
                            MLGRush.getInstance().getPrefix() + "§7You've been teleported to " + event.getCurrentItem().getItemMeta().getDisplayName()
                    );
                } else {
                    player.closeInventory();
                }
            }
        }
    }
}

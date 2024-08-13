package de.pxscxl.spigot.lobby.listener;

import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.inventory.ItemStackBuilder;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.origin.utils.enums.Rank;
import de.pxscxl.spigot.lobby.LobbySystem;
import de.pxscxl.spigot.lobby.manager.LobbyManager;
import de.pxscxl.spigot.lobby.manager.ProfileManager;
import de.pxscxl.spigot.lobby.manager.SettingsManager;
import de.pxscxl.spigot.lobby.manager.TeleporterManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

public class PlayerInteractListener implements Listener {

    private final List<OriginPlayer> nickCooldown = new ArrayList<>();
    private final List<OriginPlayer> hiderCooldown = new ArrayList<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        OriginPlayer player = OriginManager.getInstance().getPlayer(event.getPlayer());
        event.setCancelled(!LobbySystem.getInstance().getBuildPlayers().contains(player));

        if (event.getItem() == null) return;
        if (event.getItem() != null && event.getItem().getType().equals(Material.FISHING_ROD)) event.setCancelled(false);

        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            switch (event.getItem().getType()) {
                case COMPASS:
                    TeleporterManager.getInstance().openGUI(player);
                    break;
                case SHEARS:
                    SettingsManager.getInstance().openGUI(player);
                    break;
                case NAME_TAG:
                    if (player.hasPriorityAccess(Rank.JRYOUTUBER.getPriority())) {
                        if (nickCooldown.contains(player)) {
                            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10F, 10F);
                            return;
                        }
                        nickCooldown.add(player);
                        Bukkit.getScheduler().runTaskLater(LobbySystem.getInstance(), () -> nickCooldown.remove(player), 20);

                        player.setNickState(!player.isNicked());
                        if (player.hasPriorityAccess(Rank.JRYOUTUBER.getPriority())) {
                            if (player.isNicked()) {
                                player.getInventory().setItem(4, new ItemStackBuilder(Material.NAME_TAG).setGlow().setDisplayName("§aNickname §8» §7" + player.language("Rechtsklick", "Right click")).build());
                            } else {
                                player.getInventory().setItem(4, new ItemStackBuilder(Material.NAME_TAG).setDisplayName("§cNickname §8» §7" + player.language("Rechtsklick", "Right click")).build());
                            }
                        }
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10F, 10F);
                    }
                    break;
                case CLOCK:
                    LobbyManager.getInstance().openGUI(player);
                    break;
                case LEGACY_SKULL_ITEM:
                    ProfileManager.getInstance().openGUI(player, 1);
                    break;
            }
        }
    }
}

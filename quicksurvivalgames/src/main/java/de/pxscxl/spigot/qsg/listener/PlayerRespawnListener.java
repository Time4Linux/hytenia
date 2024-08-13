package de.pxscxl.spigot.qsg.listener;

import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.inventory.ItemStackBuilder;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.spigot.qsg.QSG;
import de.pxscxl.spigot.qsg.manager.MapManager;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener {

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        OriginPlayer player = OriginManager.getInstance().getPlayer(event.getPlayer());

        event.setRespawnLocation(MapManager.getInstance().getWaitingLobby());

        player.setGameMode(GameMode.ADVENTURE);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setLevel(0);
        player.setAllowFlight(true);
        player.setFlying(true);
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);

        player.getInventory().setItem(0, new ItemStackBuilder(Material.COMPASS).setDisplayName("§6Teleporter §8» §7" + player.language("Rechtsklick", "Right click")).build());
        player.getInventory().setItem(8, new ItemStackBuilder(Material.MAGMA_CREAM).setDisplayName("§6" + player.language("Verlassen", "Leave") + " §8» §7" + player.language("Rechtsklick", "Right click")).build());

        player.sendMessage(
                QSG.getInstance().getPrefix() + "§7Du bist nun ein §eSpectator",
                QSG.getInstance().getPrefix() + "§7You are now a §espectator"
        );
    }
}

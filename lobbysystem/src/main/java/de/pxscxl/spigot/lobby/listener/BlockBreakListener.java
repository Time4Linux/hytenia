package de.pxscxl.spigot.lobby.listener;

import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.spigot.lobby.LobbySystem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockEvent(BlockBreakEvent event) {
        OriginPlayer player = OriginManager.getInstance().getPlayer(event.getPlayer());
        event.setCancelled(!LobbySystem.getInstance().getBuildPlayers().contains(player));
    }
}

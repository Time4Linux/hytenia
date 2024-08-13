package de.pxscxl.spigot.qsg.listener;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.manager.OriginManager;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        OriginPlayer player = OriginManager.getInstance().getPlayer(event.getPlayer());
        event.setCancelled((!player.getGameMode().equals(GameMode.CREATIVE)));
    }
}

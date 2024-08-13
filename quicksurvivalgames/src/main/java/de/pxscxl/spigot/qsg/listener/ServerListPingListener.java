package de.pxscxl.spigot.qsg.listener;

import de.pxscxl.spigot.qsg.manager.MapManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerListPingListener implements Listener {

    @EventHandler
    public void onServerListPing(ServerListPingEvent event) {
        event.setMotd(MapManager.getInstance().getActiveMap() != null ? MapManager.getInstance().getActiveMap().getName() : "No Map");
    }
}

package de.pxscxl.spigot.qsg.listener;

import de.pxscxl.cloud.CloudAPI;
import de.pxscxl.cloud.State;
import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.spigot.qsg.QSG;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerLoginListener implements Listener {

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        OriginPlayer player = OriginManager.getInstance().getPlayer(event.getPlayer());
        if (CloudAPI.getInstance().getLocalServer().getState().equals(State.RESTART)) {
            event.setKickMessage(player.language(QSG.getInstance().getPrefix() + "§7Die Runde wurde §ebereits §7beendet", QSG.getInstance().getPrefix() + "§cThe round is §ealready §7finished"));
        }
    }
}

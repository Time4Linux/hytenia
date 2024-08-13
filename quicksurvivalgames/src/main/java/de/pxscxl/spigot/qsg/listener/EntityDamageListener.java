package de.pxscxl.spigot.qsg.listener;

import de.pxscxl.cloud.CloudAPI;
import de.pxscxl.cloud.State;
import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.spigot.qsg.QSG;
import de.pxscxl.spigot.qsg.manager.GameManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!GameManager.getInstance().isDamageAble()) {
            event.setCancelled(true);
        }
        if (event.getEntity() instanceof Player) {
            OriginPlayer player = OriginManager.getInstance().getPlayer((Player) event.getEntity());
            if (event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                event.setCancelled(!GameManager.getInstance().isDamageAble() || CloudAPI.getInstance().getLocalServer().getState().equals(State.LOBBY) || CloudAPI.getInstance().getLocalServer().getState().equals(State.RESTART));
            }
            if (!QSG.getInstance().getPlayers().contains(player)) {
                event.setCancelled(true);
            }
            return;
        }
        event.setCancelled(true);
    }
}

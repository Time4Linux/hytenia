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
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntityListener implements Listener {

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!GameManager.getInstance().isDamageAble() || CloudAPI.getInstance().getLocalServer().getState().equals(State.LOBBY) || CloudAPI.getInstance().getLocalServer().getState().equals(State.RESTART)) {
            event.setCancelled(true);
        }
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            OriginPlayer player = OriginManager.getInstance().getPlayer((Player) event.getDamager());
            OriginPlayer target = OriginManager.getInstance().getPlayer((Player) event.getEntity());

            if (!(QSG.getInstance().getPlayers().contains(player) && QSG.getInstance().getPlayers().contains(target))) {
                event.setCancelled(true);
            }

            if (!event.isCancelled()) {
                GameManager.getInstance().getTargets().put(target, player);
            }
            return;
        } else if (event.getEntity() instanceof Player) {
            OriginPlayer player = OriginManager.getInstance().getPlayer((Player) event.getEntity());
            if (!QSG.getInstance().getPlayers().contains(player)) {
                event.setCancelled(true);
            }
            return;
        }
        event.setCancelled(false);
    }
}

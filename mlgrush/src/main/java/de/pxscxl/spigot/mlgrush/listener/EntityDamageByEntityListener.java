package de.pxscxl.spigot.mlgrush.listener;

import de.pxscxl.cloud.CloudAPI;
import de.pxscxl.cloud.State;
import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.spigot.mlgrush.MLGRush;
import de.pxscxl.spigot.mlgrush.manager.GameManager;
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

            if (!(MLGRush.getInstance().getPlayers().contains(player) && MLGRush.getInstance().getPlayers().contains(target))) {
                event.setCancelled(true);
            }

            if (!event.isCancelled()) {
                GameManager.getInstance().getTargets().put(target, player);
            }
            event.setDamage(0);
            return;
        } else if (event.getEntity() instanceof Player) {
            OriginPlayer player = OriginManager.getInstance().getPlayer((Player) event.getEntity());
            if (!MLGRush.getInstance().getPlayers().contains(player)) {
                event.setCancelled(true);
            }
            return;
        }
        event.setCancelled(false);
    }
}

package de.pxscxl.spigot.swffa.listener;

import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.spigot.swffa.SkyWarsFFA;
import de.pxscxl.spigot.swffa.manager.GameManager;
import de.pxscxl.spigot.swffa.manager.MapManager;
import de.pxscxl.spigot.swffa.manager.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        OriginPlayer player = OriginManager.getInstance().getPlayer(event.getPlayer());
        SkyWarsFFA.getInstance().getPlayers().add(player);

        player.teleport(MapManager.getInstance().getActiveMap().getSpawn());

        ScoreboardManager.getInstance().setScoreboardTeams(player);
        ScoreboardManager.getInstance().setObjective(player);
        GameManager.getInstance().prepareSpawn(player);

        OriginManager.getInstance().getPlayers().forEach(players -> players.hidePlayer(player));
        Bukkit.getScheduler().runTaskLater(SkyWarsFFA.getInstance(), () -> OriginManager.getInstance().getPlayers().forEach(players -> players.showPlayer(player)), 2);
    }
}

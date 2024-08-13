package de.pxscxl.spigot.lobby.listener;

import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.hologram.Hologram;
import de.pxscxl.origin.spigot.api.manager.HologramManager;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.origin.utils.Pair;
import de.pxscxl.spigot.lobby.LobbySystem;
import de.pxscxl.spigot.lobby.Utils;
import de.pxscxl.spigot.lobby.manager.HiderManager;
import de.pxscxl.spigot.lobby.manager.LocationManager;
import de.pxscxl.spigot.lobby.manager.ScoreboardManager;
import de.pxscxl.spigot.lobby.utils.enums.Locations;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        OriginPlayer player = OriginManager.getInstance().getPlayer(event.getPlayer());

        player.setExp(0);
        player.setLevel(0);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.getInventory().clear();
        player.setGameMode(GameMode.SURVIVAL);

        Utils.setSpawnItems(player);
        HiderManager.getInstance().onPlayerJoin(player);
        LocationManager.getInstance().onPlayerJoin(player);
        ScoreboardManager.getInstance().setScoreboardTeams(player);
        ScoreboardManager.getInstance().setObjective(player);

        Bukkit.getScheduler().runTaskAsynchronously(LobbySystem.getInstance(), () -> {
            Hologram hologram = HologramManager.getInstance().createHologram(player, Locations.LEADERBOARD_HOLOGRAM.getLocation(), () -> {
                List<String> list = new ArrayList<>();
                list.add("§8× §6Leaderboard §8×");
                list.add("§8§m-----------------");
                AtomicInteger i = new AtomicInteger(1);
                OriginManager.getInstance().getDatabasePlayers().stream().map(dp -> new Pair<>(dp.getUniqueId(), dp.getPoints())).sorted((o1, o2) -> o2.getB().compareTo(o1.getB())).limit(10).map(pair -> OriginManager.getInstance().getPlayer(pair.getA())).forEach(players -> {
                    list.add("§7" + i.getAndIncrement() + "§8. " + players.getDisplayName() + " §8- §7" + players.getPoints());
                });
                list.add("§8§m-----------------");
                return list;
            });
            hologram.show();
        });
    }
}

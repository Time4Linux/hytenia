package de.pxscxl.spigot.mlgrush.listener;

import de.pxscxl.cloud.CloudAPI;
import de.pxscxl.cloud.State;
import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.inventory.ItemStackBuilder;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.spigot.mlgrush.MLGRush;
import de.pxscxl.spigot.mlgrush.manager.GameManager;
import de.pxscxl.spigot.mlgrush.manager.MapManager;
import de.pxscxl.spigot.mlgrush.manager.ScoreboardManager;
import de.pxscxl.spigot.mlgrush.manager.TeamManager;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        OriginPlayer player = OriginManager.getInstance().getPlayer(event.getPlayer());

        ScoreboardManager.getInstance().registerSpectatorTeam(player);
        TeamManager.getInstance().registerTeams(player);

        if (CloudAPI.getInstance().getLocalServer().getState().equals(State.LOBBY)) {
            MLGRush.getInstance().getPlayers().add(player);

            ScoreboardManager.getInstance().setScoreboardTeams(player);
            ScoreboardManager.getInstance().setLobbyObjective(player);

            player.teleport(MapManager.getInstance().getWaitingLobby());
            player.setGameMode(GameMode.ADVENTURE);
            player.setHealth(20);
            player.setFoodLevel(20);
            player.setLevel(0);
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);

            GameManager.getInstance().setSpawnItems(player);

            if (MLGRush.getInstance().getPlayers().size() >= GameManager.getInstance().getMinPlayers() && !GameManager.getInstance().isStarting()) {
                GameManager.getInstance().startLobbyCountdown();
            }
        } else if (CloudAPI.getInstance().getLocalServer().getState().equals(State.INGAME)) {
            MLGRush.getInstance().getPlayers().forEach(players -> {
                if (player != players) {
                    players.hidePlayer(player);
                }
            });

            ScoreboardManager.getInstance().setScoreboardTeams(player);
            ScoreboardManager.getInstance().setSpectatorObjective(player);

            MLGRush.getInstance().getSpectators().forEach(spectators -> ScoreboardManager.getInstance().updateSpectatorsScore(spectators));

            player.teleport(MapManager.getInstance().getWaitingLobby());
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
                    MLGRush.getInstance().getPrefix() + "§7Du bist nun ein §eSpectator",
                    MLGRush.getInstance().getPrefix() + "§7You are now a §espectator");
        }
    }
}

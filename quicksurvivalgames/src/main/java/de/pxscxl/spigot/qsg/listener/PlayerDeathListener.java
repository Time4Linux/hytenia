package de.pxscxl.spigot.qsg.listener;

import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.spigot.qsg.QSG;
import de.pxscxl.spigot.qsg.manager.GameManager;
import de.pxscxl.spigot.qsg.manager.ScoreboardManager;
import de.pxscxl.spigot.qsg.manager.StatsManager;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Random;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        OriginPlayer player = OriginManager.getInstance().getPlayer(event.getEntity().getPlayer());
        OriginPlayer killer = GameManager.getInstance().getTargets().get(player);

        if (QSG.getInstance().getSpectators().contains(killer)) {
            GameManager.getInstance().getTargets().put(player, null);
        }

        event.setDeathMessage(null);
        event.setDroppedExp(0);

        QSG.getInstance().getPlayers().remove(player);
        ScoreboardManager.getInstance().setScoreboardTeams(player);
        if (GameManager.getInstance().getTargets().get(player) != null) {
            StatsManager.Stats killerStats = StatsManager.getInstance().getStats(killer.getUniqueId());
            int coins = new Random().nextInt(100) + 1;
            int elo = new Random().nextInt(300) + 1;
            int globalElo = new Random().nextInt(20) + 1;

            killer.setHealth(20);
            killer.addCoins(coins);
            killer.addPoints(globalElo);

            killerStats.setElo(killerStats.getElo() + elo);
            killerStats.setKills(killerStats.getKills() + 1);

            OriginManager.getInstance().getPlayers().forEach(players -> {
                players.sendMessage(
                        QSG.getInstance().getPrefix() + (player == players ? player.getRealDisplayName() : player.getDisplayName()) + " §7wurde von " + (killer == players ? killer.getRealDisplayName() : killer.getDisplayName()) + " §7getötet.",
                        QSG.getInstance().getPrefix() + (player == players ? player.getRealDisplayName() : player.getDisplayName()) + " §7has been killed by " + (killer == players ? killer.getRealDisplayName() : killer.getDisplayName()) + "§7."
                );
                players.sendMessage(
                        QSG.getInstance().getPrefix() + "§e" + QSG.getInstance().getPlayers().size() + " §7Spieler verbleibend",
                        QSG.getInstance().getPrefix() + "§e" + QSG.getInstance().getPlayers().size() + " §7" + (QSG.getInstance().getPlayers().size() == 1 ? "player" : "players") + " remaining"
                );
            });
            killer.sendMessage(QSG.getInstance().getPrefix() + "§a+" + elo + " §7Elo §8┃ §a+" + coins + " §7" + (coins == 1 ? "Coin" : "Coins"));

            GameManager.getInstance().getKills().put(killer, GameManager.getInstance().getKills().get(killer) + 1);
            ScoreboardManager.getInstance().updateEloScore(killer, killerStats);
            ScoreboardManager.getInstance().updateKillsScore(killer);

            StatsManager.getInstance().runWrite(() -> StatsManager.getInstance().setStats(killer.getUniqueId(), killerStats));
        } else {
            OriginManager.getInstance().getPlayers().forEach(players -> {
                players.sendMessage(
                        QSG.getInstance().getPrefix() + (player == players ? player.getRealDisplayName() : player.getDisplayName()) + " §7ist gestorben",
                        QSG.getInstance().getPrefix() + (player == players ? player.getRealDisplayName() : player.getDisplayName()) + " §7died");
                players.sendMessage(
                        QSG.getInstance().getPrefix() + "§e" + QSG.getInstance().getPlayers().size() + " §7Spieler verbleibend",
                        QSG.getInstance().getPrefix() + "§e" + QSG.getInstance().getPlayers().size() + " §7" + (QSG.getInstance().getPlayers().size() == 1 ? "player" : "players") + " remaining");

            });
        }

        StatsManager.Stats playerStats = StatsManager.getInstance().getStats(player.getUniqueId());
        int coins = new Random().nextInt(30) + 1;
        int elo = new Random().nextInt(200) + 1;
        int globalElo = new Random().nextInt(5) + 1;

        player.addCoins(coins);
        if (player.getPoints() >= globalElo) {
            player.removePoints(globalElo);
        }

        if (playerStats.getElo() >= elo) {
            player.sendMessage(QSG.getInstance().getPrefix() + "§c-" + elo + " §7Elo §8┃ §a+" + coins + " §7" + (coins == 1 ? "Coin" : "Coins"));
            playerStats.setElo(playerStats.getElo() - elo);
        } else {
            player.sendMessage(
                    QSG.getInstance().getPrefix() + "§a+" + coins + " §7" + (coins == 1 ? "Coin" : "Coins"),
                    QSG.getInstance().getPrefix() + "§a+" + coins + " §7" + (coins == 1 ? "Coin" : "Coins"));
        }
        playerStats.setDeaths(playerStats.getDeaths() + 1);
        playerStats.setLoses(playerStats.getLoses() + 1);

        ScoreboardManager.getInstance().updateEloScore(player, playerStats);
        ScoreboardManager.getInstance().setSpectatorObjective(player);

        StatsManager.getInstance().runWrite(() -> StatsManager.getInstance().setStats(player.getUniqueId(), playerStats));

        QSG.getInstance().getSpectators().forEach(spectators -> {
            ScoreboardManager.getInstance().updatePlayersScore(spectators);
            ScoreboardManager.getInstance().updateSpectatorsScore(spectators);
        });
        QSG.getInstance().getPlayers().forEach(players -> players.hidePlayer(player));

        if (QSG.getInstance().getPlayers().size() == 1) {
            GameManager.getInstance().setDamageAble(false);
            Bukkit.getScheduler().runTaskLater(QSG.getInstance(), () -> GameManager.getInstance().finishGame(), 40);
        }

        respawn(player);
    }

    public void respawn(OriginPlayer player) {
        Bukkit.getScheduler().runTaskLater(QSG.getInstance(), () -> ((CraftPlayer) player.getBukkitPlayer()).getHandle().playerConnection.a(new PacketPlayInClientCommand(PacketPlayInClientCommand.EnumClientCommand.PERFORM_RESPAWN)), 2);
    }
}

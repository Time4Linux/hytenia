package de.pxscxl.spigot.swffa.listener;

import de.pxscxl.spigot.swffa.manager.GameManager;
import de.pxscxl.spigot.swffa.manager.KitManager;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.spigot.swffa.SkyWarsFFA;
import de.pxscxl.spigot.swffa.manager.ScoreboardManager;
import de.pxscxl.spigot.swffa.manager.StatsManager;

import java.util.Random;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        OriginPlayer player = OriginManager.getInstance().getPlayer(event.getEntity().getPlayer());
        OriginPlayer killer = GameManager.getInstance().getTargets().get(player);

        event.setDeathMessage(null);
        event.setDroppedExp(0);
        event.getDrops().clear();

        if (killer != null && killer != player) {
            StatsManager.Stats killerStats = StatsManager.getInstance().getStats(killer.getUniqueId());
            int coins = new Random().nextInt(100) + 1;
            int elo = new Random().nextInt(300) + 1;
            int globalElo = new Random().nextInt(20) + 1;

            killer.setHealth(20);
            killer.addCoins(coins);
            killer.addPoints(globalElo);
            killer.setLevel(killer.getLevel() + 1);

            killerStats.setElo(killerStats.getElo() + elo);
            killerStats.setKills(killerStats.getKills() + 1);

            OriginManager.getInstance().getPlayers().forEach(players -> players.sendMessage(
                    SkyWarsFFA.getInstance().getPrefix() + (player == players ? player.getRealDisplayName() : player.getDisplayName()) + " §7wurde von " + (killer == players ? killer.getRealDisplayName() : killer.getDisplayName()) + " §7getötet.",
                    SkyWarsFFA.getInstance().getPrefix() + (player == players ? player.getRealDisplayName() : player.getDisplayName()) + " §7has been killed by " + (killer == players ? killer.getRealDisplayName() : killer.getDisplayName()) + "§7."
            ));
            killer.sendMessage(SkyWarsFFA.getInstance().getPrefix() + "§a+" + elo + " §7Elo §8┃ §a+" + coins + " §7" + (coins == 1 ? "Coin" : "Coins"));

            if (killer.getLevel() == 10 || killer.getLevel() == 20 || killer.getLevel() == 30 || killer.getLevel() == 40 || killer.getLevel() == 50 || killer.getLevel() == 60 || killer.getLevel() == 70 || killer.getLevel() == 80 || killer.getLevel() == 90 || killer.getLevel() == 100) {
                killer.sendMessage(" ");
                killer.sendMessage(
                        SkyWarsFFA.getInstance().getPrefix() + "§7Du hast " + killer.getLevel() + " Spieler ohne einen Tod eliminiert",
                        SkyWarsFFA.getInstance().getPrefix() + "§7You eliminated " + killer.getLevel() + " players without a death");
                killer.sendMessage(
                        SkyWarsFFA.getInstance().getPrefix() + "§7Du erhältst §e1000 §7Punkte!",
                        SkyWarsFFA.getInstance().getPrefix() + "§7You get §e1000 §7points!");
                killer.sendMessage(" ");
                OriginManager.getInstance().getPlayers().forEach(players -> {
                    if (killer != players) {
                        players.sendMessage(" ");
                        players.sendMessage(
                                SkyWarsFFA.getInstance().getPrefix() + killer.getDisplayName() + " §7hat einen Killstreak von " + killer.getLevel() + "!",
                                SkyWarsFFA.getInstance().getPrefix() + killer.getDisplayName() + " §7has a killstreak of " + killer.getLevel() + "!");
                        players.sendMessage(" ");
                    }
                });
                killerStats.setElo(killerStats.getElo() + 1000);
            }

            ScoreboardManager.getInstance().updateKillsScore(killer, killerStats);
            ScoreboardManager.getInstance().updateEloScore(killer, killerStats);

            StatsManager.getInstance().runWrite(() -> StatsManager.getInstance().setStats(killer.getUniqueId(), killerStats));

            KitManager.getInstance().setKitItems(killer);
        } else {
            OriginManager.getInstance().getPlayers().forEach(players -> players.sendMessage(
                    SkyWarsFFA.getInstance().getPrefix() + (player == players ? player.getRealDisplayName() : player.getDisplayName()) + " §7ist gestorben.",
                    SkyWarsFFA.getInstance().getPrefix() + (player == players ? player.getRealDisplayName() : player.getDisplayName()) + " §7died."));
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
            player.sendMessage(SkyWarsFFA.getInstance().getPrefix() + "§c-" + elo + " §7Elo §8┃ §a+" + coins + " §7" + (coins == 1 ? "Coin" : "Coins"));
            playerStats.setElo(playerStats.getElo() - elo);
        } else {
            player.sendMessage(
                    SkyWarsFFA.getInstance().getPrefix() + "§a+" + coins + " §7" + (coins == 1 ? "Coin" : "Coins"),
                    SkyWarsFFA.getInstance().getPrefix() + "§a+" + coins + " §7" + (coins == 1 ? "Coin" : "Coins"));
        }
        playerStats.setDeaths(playerStats.getDeaths() + 1);

        ScoreboardManager.getInstance().updateEloScore(player, playerStats);

        StatsManager.getInstance().runWrite(() -> StatsManager.getInstance().setStats(player.getUniqueId(), playerStats));

        respawn(player);
    }

    public void respawn(OriginPlayer player) {
        Bukkit.getScheduler().runTaskLater(SkyWarsFFA.getInstance(), () -> ((CraftPlayer) player.getBukkitPlayer()).getHandle().playerConnection.a(new PacketPlayInClientCommand(PacketPlayInClientCommand.EnumClientCommand.PERFORM_RESPAWN)), 2);
    }
}

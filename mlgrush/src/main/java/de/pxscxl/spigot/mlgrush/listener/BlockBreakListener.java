package de.pxscxl.spigot.mlgrush.listener;

import de.pxscxl.cloud.CloudAPI;
import de.pxscxl.cloud.State;
import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.spigot.mlgrush.MLGRush;
import de.pxscxl.spigot.mlgrush.manager.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        OriginPlayer player = OriginManager.getInstance().getPlayer(event.getPlayer());

        if (event.getBlock().getType().equals(Material.LEGACY_BED_BLOCK) && CloudAPI.getInstance().getLocalServer().getState().equals(State.INGAME)) {
            Block[] bedBlocks = new Block[]{
                    event.getBlock(),
                    event.getBlock().getRelative(BlockFace.NORTH).getType() == Material.LEGACY_BED_BLOCK ? event.getBlock().getRelative(BlockFace.NORTH) :
                            event.getBlock().getRelative(BlockFace.EAST).getType() == Material.LEGACY_BED_BLOCK ? event.getBlock().getRelative(BlockFace.EAST) :
                                    event.getBlock().getRelative(BlockFace.WEST).getType() == Material.LEGACY_BED_BLOCK ? event.getBlock().getRelative(BlockFace.WEST) :
                                            event.getBlock().getRelative(BlockFace.SOUTH)
            };

            TeamManager.Team ownTeam = TeamManager.getInstance().getPlayerTeams().get(player);
            if (!Arrays.stream(bedBlocks).collect(Collectors.toList()).contains(ownTeam.getBed().getBlock())) {
                for (Block bedBlock : bedBlocks) {
                    if (MapManager.getInstance().getActiveMap().getBeds().contains(bedBlock.getLocation())) {
                        GameManager.getInstance().getBeds().put(player, GameManager.getInstance().getBeds().get(player) + 1);
                        OriginManager.getInstance().getPlayers().forEach(players -> {
                            players.sendMessage(
                                    MLGRush.getInstance().getPrefix() + "§7Das Bett wurde von " + (players == player ? player.getRealDisplayName() : player.getDisplayName()) + " §7abgebaut",
                                    MLGRush.getInstance().getPrefix() + "§7The bed was destroyed by " + (players == player ? player.getRealDisplayName() : player.getDisplayName())
                            );
                            players.playSound(players.getLocation(), Sound.ENTITY_WITHER_DEATH, 10F, 10F);
                            ScoreboardManager.getInstance().updateBedsScore(players);
                        });

                        int coins = new Random().nextInt(100) + 1;
                        int elo = new Random().nextInt(300) + 1;
                        int globalElo = new Random().nextInt(20) + 1;

                        player.addCoins(coins);
                        player.addPoints(globalElo);
                        player.sendMessage(MLGRush.getInstance().getPrefix() + "§a+" + elo + " §7Elo §8┃ §a+" + coins + " §7" + (coins == 1 ? "Coin" : "Coins"));

                        StatsManager.getInstance().runWrite(() -> {
                            StatsManager.Stats stats = StatsManager.getInstance().getStats(player.getUniqueId());
                            stats.setElo(stats.getElo() + elo);
                            StatsManager.getInstance().setStats(player.getUniqueId(), stats);
                        });

                        Bukkit.getScheduler().runTaskLater(MLGRush.getInstance(), () -> {
                            if (GameManager.getInstance().getBeds().get(player) == GameManager.getInstance().getRounds()) {
                                GameManager.getInstance().finishGame();
                            } else {
                                GameManager.getInstance().startWaitingCountdown();
                            }
                        }, 10);
                    }
                }
            }
        }

        event.setCancelled(!player.getGameMode().equals(GameMode.CREATIVE) && (!GameManager.getInstance().getPlacedBlocks().contains(event.getBlock()) || !CloudAPI.getInstance().getLocalServer().getState().equals(State.INGAME)));
    }
}

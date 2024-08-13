package de.pxscxl.spigot.lobby.minigames;

import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.inventory.ItemStackBuilder;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.origin.utils.Callback;
import de.pxscxl.spigot.lobby.LobbySystem;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Getter
public class TicTacToe implements Listener {

    @Getter
    private static TicTacToe instance;

    private final String prefix = "§eTicTacToe §f┃ §r";

    private final Map<OriginPlayer, Game> games = new HashMap<>();

    public TicTacToe() {
        instance = this;
        Bukkit.getPluginManager().registerEvents(this, LobbySystem.getInstance());
    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        OriginPlayer player = OriginManager.getInstance().getPlayer(event.getPlayer());

        if (event.getRightClicked() != null && event.getRightClicked().getCustomName() != null && event.getRightClicked().getCustomName().equals("§eTicTacToe")) {
            if (!games.containsKey(player)) {
                Game game = new Game(player, result -> {
                    games.remove(player);
                    if (result.equals(Result.WIN)) {
                        int coins = 20 + new Random().nextInt(10);
                        player.addCoins(coins);
                        player.sendMessage(
                                prefix + "§7Du hast §agewonnen §7und dabei §a" + coins + " §7Coins verdient",
                                prefix + "§7You have §awon §7and earned §a" + coins + " §7coins in the attempt"
                        );
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10F, 10F);
                        Bukkit.getScheduler().runTaskLater(LobbySystem.getInstance(), player::closeInventory, 10);
                    } else if (result.equals(Result.LOSE)) {
                        player.sendMessage(
                                prefix + "§7Du hast §cverloren§7, versuche es doch noch einmal",
                                prefix + "§7You have §clost§7, just try again"
                                );
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10F, 10F);
                        Bukkit.getScheduler().runTaskLater(LobbySystem.getInstance(), player::closeInventory, 10);
                    } else {
                        player.sendMessage(
                                prefix + "§7Das Spiel wurde §eunentschieden §7beendet. Niemand hat gewonnen/verloren",
                                prefix + "§7The game ended in a §edraw§7. Nobody won/lost"
                        );
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10F, 10F);
                        Bukkit.getScheduler().runTaskLater(LobbySystem.getInstance(), player::closeInventory, 10);
                    }
                });
                games.put(player, game);
                game.start();
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        OriginPlayer player = OriginManager.getInstance().getPlayer(event.getPlayer());
        if (games.containsKey(player)) {
            games.remove(player).cancel();
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        OriginPlayer player = OriginManager.getInstance().getPlayer((Player) event.getWhoClicked());
        if (games.containsKey(player) && event.getClickedInventory().getType().equals(InventoryType.DISPENSER)) {
            event.setCancelled(true);
            games.get(player).onInventoryClick(event.getSlot());
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        OriginPlayer player = OriginManager.getInstance().getPlayer((Player) event.getPlayer());
        if (games.containsKey(player)) {
            games.remove(player).cancel();
        }
    }

    @Getter
    public static class Game {

        private final OriginPlayer player;
        private final Inventory inventory;

        private final Callback<Result> callback;

        private boolean playersTurn;
        private boolean finished;

        public Game(OriginPlayer player, Callback<Result> callback) {
            this.player = player;
            this.inventory = Bukkit.createInventory(null, InventoryType.DISPENSER, "§8TicTacToe");
            this.callback = callback;
        }


        public void start() {
            player.openInventory(inventory);
            player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 10F, 10F);

            if (new Random().nextInt(2) == 0) playersTurn = true;
            else makeTurn(false);
        }

        public void cancel() {
            if (finished) return;
            finished = true;
        }

        public void onInventoryClick(int slot) {
            if (!playersTurn || finished) return;
            if (inventory.getItem(slot) != null) return;
            inventory.setItem(slot, new ItemStackBuilder(Material.GLOWSTONE_DUST).setNoName().build());
            if (checkResult()) return;
            playersTurn = false;
            makeTurn(true);
        }

        private void makeTurn(boolean delay) {
            ItemStack[] itemStacks = inventory.getContents();
            int calculation = -1;
            while (calculation == -1) {
                int slot = new Random().nextInt(9);
                if (itemStacks[slot] == null) calculation = slot;
            }

            int slot = calculation;
            Bukkit.getScheduler().runTaskLater(LobbySystem.getInstance(), () -> {
                if (finished) return;
                inventory.setItem(slot, new ItemStackBuilder(Material.REDSTONE).setNoName().build());
                if (checkResult()) return;
                playersTurn = true;
            }, delay ? 10 + new Random().nextInt(30) : 1);
        }

        private boolean checkResult() {
            ItemStack[] itemStacks = inventory.getContents();

            for (int i = 0; i < 3; i++) {
                if (itemStacks[i * 3] != null && itemStacks[1 + i * 3] != null && itemStacks[2 + i * 3] != null) {
                    Material material = itemStacks[i * 3].getType();
                    if (material.equals(itemStacks[1 + i * 3].getType()) && material.equals(itemStacks[2 + i * 3].getType())) {
                        if (material.equals(Material.GLOWSTONE_DUST)) {
                            finish(Result.WIN);
                        } else if (material.equals(Material.REDSTONE)) {
                            finish(Result.LOSE);
                        }
                        return true;
                    }
                }
            }

            for (int i = 0; i < 3; i++) {
                if (itemStacks[i] != null && itemStacks[3 + i] != null && itemStacks[6 + i] != null) {
                    Material material = itemStacks[i].getType();
                    if (material.equals(itemStacks[3 + i].getType()) && material.equals(itemStacks[6 + i].getType())) {
                        if (material.equals(Material.GLOWSTONE_DUST)) {
                            finish(Result.WIN);
                        } else if (material.equals(Material.REDSTONE)) {
                            finish(Result.LOSE);
                        }
                        return true;
                    }
                }
            }

            if (itemStacks[0] != null && itemStacks[4] != null && itemStacks[8] != null) {
                Material material = itemStacks[0].getType();
                if (material.equals(itemStacks[4].getType()) && material.equals(itemStacks[8].getType())) {
                    if (material.equals(Material.GLOWSTONE_DUST)) {
                        finish(Result.WIN);
                    } else if (material.equals(Material.REDSTONE)) {
                        finish(Result.LOSE);
                    }
                    return true;
                }
            }

            if (itemStacks[2] != null && itemStacks[4] != null && itemStacks[6] != null) {
                Material material = itemStacks[2].getType();
                if (material.equals(itemStacks[4].getType()) && material.equals(itemStacks[6].getType())) {
                    if (material.equals(Material.GLOWSTONE_DUST)) {
                        finish(Result.WIN);
                    } else if (material.equals(Material.REDSTONE)) {
                        finish(Result.LOSE);
                    }
                    finished = true;
                    return true;
                }
            }

            for (int i = 0; i < 9; i++) {
                if (itemStacks[i] == null) return false;
            }

            finish(Result.DRAW);
            return true;
        }

        private void finish(Result result) {
            finished = true;
            callback.done(result);
        }
    }

    public enum Result {
        WIN, LOSE, DRAW
    }
}
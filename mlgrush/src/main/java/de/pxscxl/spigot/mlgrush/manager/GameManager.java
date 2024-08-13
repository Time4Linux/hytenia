package de.pxscxl.spigot.mlgrush.manager;

import de.pxscxl.cloud.CloudAPI;
import de.pxscxl.cloud.State;
import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.inventory.ItemStackBuilder;
import de.pxscxl.origin.spigot.api.manager.NickManager;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.origin.utils.enums.Language;
import de.pxscxl.spigot.mlgrush.MLGRush;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

@Getter
public class GameManager {

    @Getter
    private static GameManager instance;

    @Setter
    private int round = 0;

    @Setter
    private int maxPlayers;
    @Setter
    private int minPlayers;
    @Setter
    private int rounds;
    @Setter
    private int countdown;
    private int scheduler;

    @Setter
    private boolean damageAble;
    @Setter
    private boolean protection;
    @Setter
    private boolean starting;

    private final ArrayList<Block> placedBlocks = new ArrayList<>();

    private final HashMap<OriginPlayer, Integer> beds = new HashMap<>();
    private final HashMap<OriginPlayer, OriginPlayer> targets = new HashMap<>();

    private final File file;
    private final YamlConfiguration yamlConfiguration;

    public GameManager() {
        instance = this;

        File source = new File("plugins/MLGRush");
        if (!source.exists()) {
            source.mkdirs();
        }
        file = new File("plugins/MLGRush/settings.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        if (yamlConfiguration.get("Settings.maxPlayers") == null) {
            yamlConfiguration.set("Settings.maxPlayers", 1);
            setMaxPlayers(1);
        } else {
            setMaxPlayers(yamlConfiguration.getInt("Settings.maxPlayers"));
        }
        if (yamlConfiguration.get("Settings.minPlayers") == null) {
            yamlConfiguration.set("Settings.minPlayers", 1);
            setMinPlayers(1);
        } else {
            setMinPlayers(yamlConfiguration.getInt("Settings.minPlayers"));
        }
        if (yamlConfiguration.get("Settings.rounds") == null) {
            yamlConfiguration.set("Settings.rounds", 5);
            setRounds(5);
        } else {
            setRounds(yamlConfiguration.getInt("Settings.rounds"));
        }
        try {
            yamlConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disable() {
        instance = null;
    }

    public void startLobbyCountdown() {
        countdown = 61;
        starting = true;

        Bukkit.getScheduler().cancelTask(scheduler);

        scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(MLGRush.getInstance(), () -> {
            countdown--;

            OriginManager.getInstance().getPlayers().forEach(players -> {
                players.setLevel(countdown);
                players.sendActionbar("§a" + countdown + " §7Sekunden bis zum Start", "§a" + countdown + " §7seconds until the start");
            });

            if (MLGRush.getInstance().getPlayers().size() >= GameManager.getInstance().getMinPlayers()) {
                if (countdown == 60 || countdown == 50 || countdown == 40 || countdown == 30 || countdown == 20 || countdown == 10 || countdown < 6 && countdown > 1) {
                    OriginManager.getInstance().getPlayers().forEach(players -> {
                        players.sendMessage(
                                MLGRush.getInstance().getPrefix() + "§7Das Spiel startet in §e" + countdown + " §7Sekunden",
                                MLGRush.getInstance().getPrefix() + "§7The game will start in §e" + countdown + " §7seconds");
                        players.playSound(players.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 10F, 10F);
                    });
                } else if (countdown == 1) {
                    OriginManager.getInstance().getPlayers().forEach(players -> {
                        players.sendActionbar("§a" + countdown + " §7Sekunde bis zum Start", "§a" + countdown + " §7second until the start");
                        players.sendMessage(
                                MLGRush.getInstance().getPrefix() + "§7Das Spiel startet in §e" + countdown + " §7Sekunde",
                                MLGRush.getInstance().getPrefix() + "§7The game will start in §e" + countdown + " §7second");
                        players.playSound(players.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 10F, 10F);
                    });
                } else if (countdown == 0) {
                    Bukkit.getScheduler().cancelTask(scheduler);

                    CloudAPI.getInstance().setState(State.INGAME);

                    OriginManager.getInstance().getPlayers().forEach(players -> {
                        if (!TeamManager.getInstance().getPlayerTeams().containsKey(players)) {
                            TeamManager.getInstance().selectRandomTeam(players);
                        }
                        ScoreboardManager.getInstance().updateTeamScore(players);

                        TeamManager.Team team = TeamManager.getInstance().getPlayerTeams().get(players);
                        players.sendMessage(
                                MLGRush.getInstance().getPrefix() + "§7Du wurdest dem Team " + team.getColor() + team.getGermanName() + " §7zugewiesen",
                                MLGRush.getInstance().getPrefix() + "§7You are now in the " + team.getColor() + team.getEnglishName().toLowerCase() + " §7team");
                        players.sendMessage(
                                MLGRush.getInstance().getPrefix() + "§7Alle Spieler werden in die §eArena §7teleportiert",
                                MLGRush.getInstance().getPrefix() + "§7All players will be teleported into the §earena");
                        players.sendMessage(MLGRush.getInstance().getPrefix() + "§a+ 3 §7Coins");

                        players.setGameMode(GameMode.ADVENTURE);
                        players.setHealth(20);
                        players.setFoodLevel(20);
                        players.setFireTicks(0);
                        players.setLevel(0);
                        players.getInventory().clear();
                        players.getInventory().setArmorContents(null);

                        GameManager.getInstance().getBeds().put(players, 0);
                    });
                    OriginManager.getInstance().getPlayers().forEach(players -> ScoreboardManager.getInstance().setInGameObjective(players));

                    startWaitingCountdown();
                }
            } else {
                Bukkit.getScheduler().cancelTask(scheduler);
                setStarting(false);
                OriginManager.getInstance().getPlayers().forEach(players -> players.setLevel(0));
            }
        }, 0, 20);
    }

    public void startWaitingCountdown() {
        countdown = 1;
        round = round + 1;

        prepareSpawn();

        scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(MLGRush.getInstance(), () -> {
            countdown--;
            OriginManager.getInstance().getPlayers().forEach(players -> {
                players.setLevel(countdown);
                players.sendActionbar("§a" + countdown + " §7Sekunden bis zum Beginn", "§a" + countdown + " §7seconds until the beginning");
            });
            if (countdown < 6 && countdown > 1) {
                OriginManager.getInstance().getPlayers().forEach(players -> {
                    players.sendMessage(
                            MLGRush.getInstance().getPrefix() + "§7Das Spiel beginnt in §a" + countdown + " §7Sekunden",
                            MLGRush.getInstance().getPrefix() + "§7The game begins in §a" + countdown + " §7seconds");
                    players.playSound(players.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 10F, 10F);
                });
            } else if (countdown == 1) {
                OriginManager.getInstance().getPlayers().forEach(players -> {
                    players.sendActionbar("§a" + countdown + " §7Sekunde bis zum Beginn", "§a" + countdown + " §7second until the beginning");
                    players.sendMessage(
                            MLGRush.getInstance().getPrefix() + "§7Das Spiel beginnt in §a" + countdown + " §7Sekunde",
                            MLGRush.getInstance().getPrefix() + "§7The game begins in §a" + countdown + " §7second");
                    players.playSound(players.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 10F, 10F);
                });
            } else if (countdown == 0) {
                Bukkit.getScheduler().cancelTask(scheduler);
                protection = false;
                damageAble = true;

                OriginManager.getInstance().getPlayers().forEach(players -> {
                    players.setLevel(0);
                    players.sendMessage(
                            MLGRush.getInstance().getPrefix() + "§7Die §a" + translateRounds(players) + " §7Runde beginnt jetzt",
                            MLGRush.getInstance().getPrefix() + "§7The §a" + translateRounds(players) + " §7round begins now"
                    );
                });
            }
        }, 0, 20);
    }

    public void startEndCountdown() {
        countdown = 11;

        CloudAPI.getInstance().setState(State.RESTART);

        Bukkit.getScheduler().cancelTask(scheduler);

        scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(MLGRush.getInstance(), () -> {
            countdown--;
            OriginManager.getInstance().getPlayers().forEach(players -> {
                players.setLevel(countdown);
                players.sendActionbar("§c" + countdown + " §7Sekunden bis zum Ende", "§c" + countdown + " §7seconds until the end");
            });
            if (countdown == 10 || countdown < 6 && countdown > 1) {
                OriginManager.getInstance().getPlayers().forEach(players -> {
                    players.sendMessage(
                            MLGRush.getInstance().getPrefix() + "§7Der Server startet in §c" + countdown + " §7Sekunden neu",
                            MLGRush.getInstance().getPrefix() + "§7The server restarts in §c" + countdown + " §7seconds");
                    players.playSound(players.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 10F, 10F);
                });
            } else if (countdown == 1) {
                OriginManager.getInstance().getPlayers().forEach(players -> {
                    players.sendActionbar("§c" + countdown + " §7Sekunde bis zum Ende", "§c" + countdown + " §7second until the end");
                    players.sendMessage(
                            MLGRush.getInstance().getPrefix() + "§7Der Server startet in §c" + countdown + " §7Sekunde neu",
                            MLGRush.getInstance().getPrefix() + "§7The server restarts in §c" + countdown + " §7second");
                    players.playSound(players.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 10F, 10F);
                });
            } else if (countdown == 0) {
                OriginManager.getInstance().getPlayers().forEach(players -> players.send("fallback"));
                Bukkit.getScheduler().runTaskLater(MLGRush.getInstance(), () -> {
                    Bukkit.getScheduler().cancelTask(scheduler);
                    Bukkit.shutdown();
                }, 60);
            }
        }, 20, 20);
    }

    public void finishGame() {
        OriginPlayer player = MLGRush.getInstance().getPlayers().size() == 1 ? MLGRush.getInstance().getPlayers().get(0) : GameManager.getInstance().getBeds().get(MLGRush.getInstance().getPlayers().get(0)) == GameManager.getInstance().getRounds() ? MLGRush.getInstance().getPlayers().get(0) : MLGRush.getInstance().getPlayers().get(1);

        Firework firework = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();
        FireworkEffect.Builder builder = FireworkEffect.builder();
        builder.withTrail();
        builder.withFlicker();
        builder.withFade(Color.ORANGE);
        builder.withColor(Color.YELLOW);
        builder.withColor(Color.RED);
        builder.with(FireworkEffect.Type.BALL_LARGE);
        fireworkMeta.addEffects(builder.build());
        fireworkMeta.setPower(1);
        firework.setFireworkMeta(fireworkMeta);

        OriginManager.getInstance().getPlayers().forEach(players -> {
            if (players.getNick() != null) {
                NickManager.getInstance().unnickPlayer(players);
                players.sendMessage(
                        MLGRush.getInstance().getPrefix() + "§7Dein Nickname wurde §centfernt",
                        MLGRush.getInstance().getPrefix() + "§7Your nickname was §cremoved");
            }

            players.sendMessage("");
            players.sendMessage(
                    MLGRush.getInstance().getPrefix() + "§7Das Spiel wurde §cbeendet",
                    MLGRush.getInstance().getPrefix() + "§7The game has §cended");
            players.sendMessage(
                    MLGRush.getInstance().getPrefix() + player.getDisplayName() + " §7hat das Spiel §agewonnen",
                    MLGRush.getInstance().getPrefix() + player.getDisplayName() + " §7has §awon §7the game");
            players.sendMessage("");

            MLGRush.getInstance().getSpectators().forEach(players::showPlayer);

            Bukkit.getScheduler().runTaskLater(MLGRush.getInstance(), () -> {
                players.setGameMode(GameMode.ADVENTURE);
                players.setHealth(20);
                players.setFoodLevel(20);
                players.setFireTicks(0);
                players.getInventory().clear();
                players.getInventory().setArmorContents(null);
                players.teleport(MapManager.getInstance().getWaitingLobby());
                players.setAllowFlight(false);
                players.setFlying(false);

                ScoreboardManager.getInstance().setRegularScoreboardTeams(players);

                players.getInventory().setItem(8, new ItemStackBuilder(Material.MAGMA_CREAM).setDisplayName("§6" + players.language("Verlassen", "Leave") + " §8» §7" + players.language("Rechtsklick", "Right click")).build());
            }, 20);
        });

        StatsManager.Stats stats = StatsManager.getInstance().getStats(player.getUniqueId());
        int coins = new Random().nextInt(100) + 1;
        int elo = new Random().nextInt(300) + 1;
        int globalElo = new Random().nextInt(20) + 1;

        player.addCoins(coins);
        player.addPoints(globalElo);

        stats.setElo(stats.getElo() + elo);
        stats.setWins(stats.getWins() + 1);

        StatsManager.getInstance().runWrite(() -> StatsManager.getInstance().setStats(player.getUniqueId(), stats));

        player.sendMessage(MLGRush.getInstance().getPrefix() + "§a+" + elo + " §7Elo §8┃ §a+" + coins + " §7" + (coins == 1 ? "Coin" : "Coins"));

        startEndCountdown();
    }

    public void prepareSpawn() {
        setProtection(true);
        setDamageAble(false);

        teleportTeamPlayers();

        placedBlocks.forEach(block -> block.setType(Material.AIR));
        placedBlocks.clear();

        MLGRush.getInstance().getPlayers().forEach(players -> {
            players.setGameMode(GameMode.SURVIVAL);
            players.setHealth(20);
            players.setFireTicks(0);

            players.getInventory().clear();
            KitManager.getInstance().setKitItems(players);
        });
    }

    private void teleportTeamPlayers() {
        TeamManager.Team.RED.getMembers().forEach(red -> red.teleport(MapManager.getInstance().getActiveMap().getSpawns().get(0)));
        TeamManager.Team.BLUE.getMembers().forEach(blue -> blue.teleport(MapManager.getInstance().getActiveMap().getSpawns().get(1)));
    }

    public String translateRounds(OriginPlayer player) {
        String rounds = null;
        if (player.getLanguage().equals(Language.GERMAN)) {
            rounds = round + ".";
        } else {
            if (round == 1) {
                rounds = "1st";
            } else if (round == 2) {
                rounds = "2nd";
            } else if (round == 3) {
                rounds = "3rd";
            } else if (round >= 4) {
                rounds = round + "th";
            }
        }
        return rounds;
    }

    public void setSpawnItems(OriginPlayer player) {
        player.getInventory().setItem(0, new ItemStackBuilder(Material.RED_BED).setDisplayName("§6Team §8» §7" + player.language("Rechtsklick", "Right click")).build());
        player.getInventory().setItem(4, new ItemStackBuilder(Material.ENDER_CHEST).setDisplayName("§6" + player.language("Inventar", "Inventory") + " §8» §7" + player.language("Rechtsklick", "Right click")).build());
        player.getInventory().setItem(8, new ItemStackBuilder(Material.MAGMA_CREAM).setDisplayName("§6" + player.language("Verlassen", "Leave") + " §8» §7" + player.language("Rechtsklick", "Right click")).build());
    }
}

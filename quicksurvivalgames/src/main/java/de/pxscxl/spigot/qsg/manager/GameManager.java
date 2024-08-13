package de.pxscxl.spigot.qsg.manager;

import de.pxscxl.cloud.CloudAPI;
import de.pxscxl.cloud.State;
import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.inventory.ItemStackBuilder;
import de.pxscxl.origin.spigot.api.manager.NickManager;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.spigot.qsg.QSG;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class GameManager {

    @Getter
    private static GameManager instance;

    @Setter
    private int maxPlayers;
    @Setter
    private int minPlayers;
    @Setter
    private int countdown;
    private int scheduler;

    @Setter
    private boolean damageAble;
    @Setter
    private boolean protection;
    @Setter
    private boolean starting;
    @Setter
    private boolean teaming;

    private final HashMap<OriginPlayer, Integer> kills = new HashMap<>();
    private final HashMap<OriginPlayer, OriginPlayer> targets = new HashMap<>();

    private final File file;
    private final YamlConfiguration yamlConfiguration;

    public GameManager() {
        instance = this;

        File source = new File("plugins/QSG");
        if (!source.exists()) {
            source.mkdirs();
        }
        file = new File("plugins/QSG/settings.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        if (yamlConfiguration.get("Settings.teaming") == null) {
            yamlConfiguration.set("Settings.teaming", false);
            setTeaming(false);
        } else {
            setTeaming(yamlConfiguration.getBoolean("Settings.teaming"));
        }
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
        try {
            yamlConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startLobbyCountdown() {
        countdown = 61;
        starting = true;

        Bukkit.getScheduler().cancelTask(scheduler);

        scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(QSG.getInstance(), () -> {
            countdown--;

            OriginManager.getInstance().getPlayers().forEach(players -> {
                players.setLevel(countdown);
                players.sendActionbar("§a" + countdown + " §7Sekunden bis zum Start", "§a" + countdown + " §7seconds until the start");
            });

            if (QSG.getInstance().getPlayers().size() >= GameManager.getInstance().getMinPlayers()) {
                if (countdown == 60 || countdown == 50 || countdown == 40 || countdown == 30 || countdown == 20 || countdown == 10 || countdown < 6 && countdown > 1) {
                    OriginManager.getInstance().getPlayers().forEach(players -> {
                        players.sendMessage(
                                QSG.getInstance().getPrefix() + "§7Das Spiel startet in §e" + countdown + " §7Sekunden",
                                QSG.getInstance().getPrefix() + "§7The game will start in §e" + countdown + " §7seconds");
                        players.playSound(players.getLocation(), Sound.CLICK, 10F, 10F);
                    });
                } else if (countdown == 1) {
                    OriginManager.getInstance().getPlayers().forEach(players -> {
                        players.sendActionbar("§a" + countdown + " §7Sekunde bis zum Start", "§a" + countdown + " §7second until the start");
                        players.sendMessage(
                                QSG.getInstance().getPrefix() + "§7Das Spiel startet in §e" + countdown + " §7Sekunde",
                                QSG.getInstance().getPrefix() + "§7The game will start in §e" + countdown + " §7second");
                        players.playSound(players.getLocation(), Sound.CLICK, 10F, 10F);
                    });
                } else if (countdown == 0) {
                    Bukkit.getScheduler().cancelTask(scheduler);

                    CloudAPI.getInstance().setState(State.INGAME);

                    OriginManager.getInstance().getPlayers().forEach(players -> {
                        players.sendMessage(
                                QSG.getInstance().getPrefix() + "§7Alle Spieler werden in die §eArena §7teleportiert",
                                QSG.getInstance().getPrefix() + "§7All players will be teleported into the §earena");
                        players.sendMessage(QSG.getInstance().getPrefix() + "§a+ 3 §7Coins");

                        players.setGameMode(GameMode.ADVENTURE);
                        players.setHealth(20);
                        players.setFoodLevel(20);
                        players.setFireTicks(0);
                        players.setLevel(0);
                        players.getInventory().clear();
                        players.getInventory().setArmorContents(null);

                        GameManager.getInstance().getKills().put(players, 0);
                        ScoreboardManager.getInstance().setInGameObjective(players);
                    });

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
        countdown = 6;

        prepareSpawn();

        scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(QSG.getInstance(), () -> {
            countdown--;
            OriginManager.getInstance().getPlayers().forEach(players -> {
                players.setLevel(countdown);
                players.sendActionbar("§a" + countdown + " §7Sekunden bis zum Beginn", "§a" + countdown + " §7seconds until the beginning");
            });
            if (countdown < 6 && countdown > 1) {
                OriginManager.getInstance().getPlayers().forEach(players -> {
                    players.sendMessage(
                            QSG.getInstance().getPrefix() + "§7Das Spiel beginnt in §a" + countdown + " §7Sekunden",
                            QSG.getInstance().getPrefix() + "§7The game begins in §a" + countdown + " §7seconds");
                    players.playSound(players.getLocation(), Sound.NOTE_STICKS, 10F, 10F);
                });
            } else if (countdown == 1) {
                OriginManager.getInstance().getPlayers().forEach(players -> {
                    players.sendActionbar("§a" + countdown + " §7Sekunde bis zum Beginn", "§a" + countdown + " §7second until the beginning");
                    players.sendMessage(
                            QSG.getInstance().getPrefix() + "§7Das Spiel beginnt in §a" + countdown + " §7Sekunde",
                            QSG.getInstance().getPrefix() + "§7The game begins in §a" + countdown + " §7second");
                    players.playSound(players.getLocation(), Sound.NOTE_STICKS, 10F, 10F);
                });
            } else if (countdown == 0) {
                Bukkit.getScheduler().cancelTask(scheduler);
                protection = false;

                OriginManager.getInstance().getPlayers().forEach(players -> {
                    players.setLevel(0);
                    players.sendMessage(
                            QSG.getInstance().getPrefix() + "§7Das Spiel beginnt §ajetzt",
                            QSG.getInstance().getPrefix() + "§7The game begins §anow");
                });

                startProtectionCountdown();
            }
        }, 0, 20);
    }

    public void startProtectionCountdown() {
        setCountdown(10);

        scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(QSG.getInstance(), () -> {
            countdown--;
            OriginManager.getInstance().getPlayers().forEach(players -> {
                players.setLevel(countdown);
                players.sendActionbar("§a" + countdown + " §7Sekunden bis zum Ende der Schutzzeit", "§a" + countdown + " §7seconds until the of the protection period");
            });
            if (countdown == 10 || countdown < 6 && countdown > 1) {
                OriginManager.getInstance().getPlayers().forEach(players -> {
                    players.sendMessage(
                            QSG.getInstance().getPrefix() + "§7Die Schutzzeit endet in §a" + countdown + " §7Sekunden",
                            QSG.getInstance().getPrefix() + "§7The protection period ends in §a" + countdown + " §7seconds");
                    players.playSound(players.getLocation(), Sound.NOTE_STICKS, 10F, 10F);
                });
            } else if (countdown == 1) {
                OriginManager.getInstance().getPlayers().forEach(players -> {
                    players.sendActionbar("§a" + countdown + " §7Sekunde bis zum Ende der Schutzzeit", "§a" + countdown + " §7seconds until the of the protection period");
                    players.sendMessage(
                            QSG.getInstance().getPrefix() + "§7Die Schutzzeit endet in §a" + countdown + " §7Sekunde",
                            QSG.getInstance().getPrefix() + "§7The protection period ends in §a" + countdown + " §7second");
                    players.playSound(players.getLocation(), Sound.NOTE_STICKS, 10F, 10F);
                });
            } else if (countdown == 0) {
                Bukkit.getScheduler().cancelTask(scheduler);
                damageAble = true;

                OriginManager.getInstance().getPlayers().forEach(players -> {
                    players.setLevel(0);
                    players.sendMessage(
                            QSG.getInstance().getPrefix() + "§7Die Schutzzeit ist vorbei. Das Spiel §abeginnt§7!",
                            QSG.getInstance().getPrefix() + "§7The protection period is over. The game §abeginns§7!");
                });
            }
        }, 20, 20);
    }

    public void startEndCountdown() {
        countdown = 11;

        CloudAPI.getInstance().setState(State.RESTART);

        Bukkit.getScheduler().cancelTask(scheduler);

        scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(QSG.getInstance(), () -> {
            countdown--;
            OriginManager.getInstance().getPlayers().forEach(players -> {
                players.setLevel(countdown);
                players.sendActionbar("§c" + countdown + " §7Sekunden bis zum Ende", "§c" + countdown + " §7seconds until the end");
            });
            if (countdown == 10 || countdown < 6 && countdown > 1) {
                OriginManager.getInstance().getPlayers().forEach(players -> {
                    players.sendMessage(
                            QSG.getInstance().getPrefix() + "§7Der Server startet in §c" + countdown + " §7Sekunden neu",
                            QSG.getInstance().getPrefix() + "§7The server restarts in §c" + countdown + " §7seconds");
                    players.playSound(players.getLocation(), Sound.CLICK, 10F, 10F);
                });
            } else if (countdown == 1) {
                OriginManager.getInstance().getPlayers().forEach(players -> {
                    players.sendActionbar("§c" + countdown + " §7Sekunde bis zum Ende", "§c" + countdown + " §7second until the end");
                    players.sendMessage(
                            QSG.getInstance().getPrefix() + "§7Der Server startet in §c" + countdown + " §7Sekunde neu",
                            QSG.getInstance().getPrefix() + "§7The server restarts in §c" + countdown + " §7second");
                    players.playSound(players.getLocation(), Sound.CLICK, 10F, 10F);
                });
            } else if (countdown == 0) {
                OriginManager.getInstance().getPlayers().forEach(players -> players.send("fallback"));
                Bukkit.getScheduler().runTaskLater(QSG.getInstance(), () -> {
                    Bukkit.getScheduler().cancelTask(scheduler);
                    Bukkit.shutdown();
                }, 60);
            }
        }, 20, 20);
    }

    public void finishGame() {
        OriginPlayer player = QSG.getInstance().getPlayers().get(0);

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
                        QSG.getInstance().getPrefix() + "§7Dein Nickname wurde §centfernt",
                        QSG.getInstance().getPrefix() + "§7Your nickname was §cremoved");
            }
            players.sendMessage("");
            players.sendMessage(
                    QSG.getInstance().getPrefix() + "§7Das Spiel wurde §cbeendet",
                    QSG.getInstance().getPrefix() + "§7The game has §cended");
            players.sendMessage(
                    QSG.getInstance().getPrefix() + (player == players ? player.getRealDisplayName() : player.getDisplayName()) + " §7hat das Spiel §agewonnen",
                    QSG.getInstance().getPrefix() + (player == players ? player.getRealDisplayName() : player.getDisplayName()) + " §7has §awon §7the game");
            players.sendMessage("");

            QSG.getInstance().getSpectators().forEach(players::showPlayer);

            Bukkit.getScheduler().runTaskLater(QSG.getInstance(), () -> {
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

        player.sendMessage(QSG.getInstance().getPrefix() + "§a+" + elo + " §7Elo §8┃ §a+" + coins + " §7" + (coins == 1 ? "Coin" : "Coins"));

        startEndCountdown();
    }

    public void prepareSpawn() {
        setProtection(true);
        setDamageAble(false);

        AtomicInteger spawn = new AtomicInteger(0);
        QSG.getInstance().getPlayers().forEach(players -> {
            players.teleport(MapManager.getInstance().getActiveMap().getSpawns().get(spawn.getAndIncrement()));
            players.setGameMode(GameMode.SURVIVAL);
            players.setHealth(20);
            players.setFireTicks(0);
            players.getInventory().setItem(0, new ItemStackBuilder(Material.WOOD_AXE).build());
        });
    }
}

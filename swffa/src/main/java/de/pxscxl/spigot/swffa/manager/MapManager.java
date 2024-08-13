package de.pxscxl.spigot.swffa.manager;

import de.pxscxl.cloud.CloudAPI;
import de.pxscxl.cloud.ServerInfo;
import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.spigot.swffa.SkyWarsFFA;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@Getter
public class MapManager {

    @Getter
    private static MapManager instance;

    private final File mapsFile;
    private final File spawnsFile;

    private final YamlConfiguration mapsYamlConfiguration;
    private final YamlConfiguration spawnsYamlConfiguration;

    @Setter
    private int time = 601;

    private Map activeMap;
    @Setter
    private Map forcedMap = null;

    private final ArrayList<Map> availableMaps = new ArrayList<>();

    public MapManager() {
        instance = this;

        mapsFile = new File("plugins/SkyWarsFFA/maps.yml");
        if (!mapsFile.exists()) {
            try {
                mapsFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        spawnsFile = new File("plugins/SkyWarsFFA/spawns.yml");
        if (!spawnsFile.exists()) {
            try {
                spawnsFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mapsYamlConfiguration = YamlConfiguration.loadConfiguration(mapsFile);
        if (mapsYamlConfiguration.get("Maps") == null) {
            mapsYamlConfiguration.set("Maps", new ArrayList<String>());
        }
        spawnsYamlConfiguration = YamlConfiguration.loadConfiguration(spawnsFile);

        mapsYamlConfiguration.getStringList("Maps").forEach(string -> availableMaps.add(new Map(string)));

        availableMaps.forEach(Map::loadBukkitWorld);

        activeMap = availableMaps.isEmpty() ? null : availableMaps.get(new Random().nextInt(availableMaps.size()));
        if (activeMap != null) {
            activeMap.load();

            ServerInfo serverInfo = CloudAPI.getInstance().getLocalServer().getServerInfo();
            serverInfo.setMap(activeMap.getName());
            CloudAPI.getInstance().setServerInfo(serverInfo);
        }

        Bukkit.getScheduler().runTaskTimer(SkyWarsFFA.getInstance(), () -> {
            if (GameManager.getInstance().isMapChange()) {
                time--;
                if (time == 10 || time == 5 || time == 4 || time == 3 || time == 2) {
                    OriginManager.getInstance().getPlayers().forEach(players -> {
                        players.sendMessage(
                                SkyWarsFFA.getInstance().getPrefix() + "§7Die Karte wird in §e" + time + " §7Sekunden gewechselt",
                                SkyWarsFFA.getInstance().getPrefix() + "§7The map will be changed in §e" + time + " §7seconds");
                        players.playSound(players.getLocation(), Sound.CLICK, 10F, 10F);
                    });
                } else if (time == 1) {
                    OriginManager.getInstance().getPlayers().forEach(players -> {
                        players.sendMessage(
                                SkyWarsFFA.getInstance().getPrefix() + "§7Die Karte wird in §e" + time + " §7Sekunde gewechselt",
                                SkyWarsFFA.getInstance().getPrefix() + "§7The map will be changed in §e" + time + " §7second");
                        players.playSound(players.getLocation(), Sound.CLICK, 10F, 10F);
                    });
                } else if (time == 0) {
                    if (forcedMap != null) {
                        setActiveMap(forcedMap);
                        setForcedMap(null);
                    } else {
                        List<Map> maps = availableMaps.stream().filter(map -> !Objects.equals(map.getName(), activeMap.getName())).collect(Collectors.toList());
                        setActiveMap(maps.size() == 0 ? activeMap : maps.get(new Random().nextInt(maps.size())));
                    }

                    OriginManager.getInstance().getPlayers().forEach(players -> {
                        players.teleport(MapManager.getInstance().getActiveMap().getSpawn());
                        players.sendMessage(
                                SkyWarsFFA.getInstance().getPrefix() + "§7Neue Karte§8: §f" + activeMap.getName(),
                                SkyWarsFFA.getInstance().getPrefix() + "§7New map§8: §f" + activeMap.getName());
                        players.playSound(players.getLocation(), Sound.ENDERMAN_TELEPORT, 10F, 10F);

                        GameManager.getInstance().prepareSpawn(players);
                        ScoreboardManager.getInstance().updateMapScore(players);
                    });
                    time = 601;
                }
            }
        }, 0, 20);
    }

    public void addMap(OriginPlayer player, String mapName) {
        List<String> maps = mapsYamlConfiguration.getStringList("Maps");
        if (!maps.contains(mapName)) {
            player.sendMessage(
                    SkyWarsFFA.getInstance().getPrefix() + "§7Du hast die Karte §e" + mapName + " §7hinzugefügt",
                    SkyWarsFFA.getInstance().getPrefix() + "§7You've added the map §e" + mapName
            );
            maps.add(mapName);
            mapsYamlConfiguration.set("Maps", maps);
            availableMaps.add(new Map(mapName));
            try {
                mapsYamlConfiguration.save(mapsFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            player.sendMessage(
                    SkyWarsFFA.getInstance().getPrefix() + "§cDiese Karte existiert §ebereits§7!",
                    SkyWarsFFA.getInstance().getPrefix() + "§cThis map §ealready §7exist!");
        }
    }

    public void removeMap(OriginPlayer player, String mapName) {
        List<String> maps = mapsYamlConfiguration.getStringList("Maps");
        if (maps.contains(mapName)) {
            player.sendMessage(
                    SkyWarsFFA.getInstance().getPrefix() + "§7Du hast die Karte §e" + mapName + " §7entfernt",
                    SkyWarsFFA.getInstance().getPrefix() + "§7You've removed the map §e" + mapName
            );
            maps.remove(mapName);
            mapsYamlConfiguration.set("Maps", maps);
            availableMaps.remove(getMap(mapName));
            try {
                mapsYamlConfiguration.save(mapsFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            player.sendMessage(
                    SkyWarsFFA.getInstance().getPrefix() + "§7Diese Karte existiert §cnicht§7!",
                    SkyWarsFFA.getInstance().getPrefix() + "§7This map does §cnot §7exist!");
        }
    }

    public Map getMap(String name) {
        return availableMaps.stream().filter(map -> Objects.equals(map.getName().toLowerCase(), name.toLowerCase())).findFirst().orElse(null);
    }

    public void setActiveMap(Map map) {
        this.activeMap = map;
        map.load();

        ServerInfo serverInfo = CloudAPI.getInstance().getLocalServer().getServerInfo();
        serverInfo.setMap(map.getName());
        CloudAPI.getInstance().setServerInfo(serverInfo);

        OriginManager.getInstance().getPlayers().forEach(players -> ScoreboardManager.getInstance().updateMapScore(players));
    }

    @Getter
    public class Map {

        private final String name;

        private World world;

        private Location spawn;
        private Location highest;
        private Location lowest;

        public Map(String name) {
            this.name = name;
        }

        public void loadBukkitWorld() {
            if (!new File(name).exists()) {
                return;
            }
            World world = Bukkit.getWorld(name);
            if (world != null) {
                return;
            }
            WorldCreator creator = new WorldCreator(name);
            creator.environment(World.Environment.NORMAL);
            creator.generateStructures(false);
            world = creator.createWorld();
            world.setDifficulty(Difficulty.EASY);
            world.setSpawnFlags(false, false);
            world.setPVP(true);
            world.setStorm(false);
            world.setThundering(false);
            world.setKeepSpawnInMemory(false);
            world.setTicksPerAnimalSpawns(0);
            world.setTicksPerMonsterSpawns(0);
            world.setWeatherDuration(0);

            world.setAutoSave(false);
            world.setGameRuleValue("doFireTick", "false");
            world.setGameRuleValue("doMobSpawning", "false");
            world.setTime(6000L);
        }

        public void load() {
            if (spawnsYamlConfiguration.get("Spawns." + name + ".World") != null) {
                String worldName = spawnsYamlConfiguration.getString("Spawns." + name + ".World");
                double x = spawnsYamlConfiguration.getDouble("Spawns." + name + ".X");
                double y = spawnsYamlConfiguration.getDouble("Spawns." + name + ".Y");
                double z = spawnsYamlConfiguration.getDouble("Spawns." + name + ".Z");
                float yaw = (float) spawnsYamlConfiguration.getDouble("Spawns." + name + ".Yaw");
                float pitch = (float) spawnsYamlConfiguration.getDouble("Spawns." + name + ".Pitch");
                world = Bukkit.createWorld(new WorldCreator(worldName));
                spawn = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
            }

            if (spawnsYamlConfiguration.get("Highest." + name) != null) {
                double x = spawnsYamlConfiguration.getDouble("Highest." + name + ".X");
                double y = spawnsYamlConfiguration.getDouble("Highest." + name + ".Y");
                double z = spawnsYamlConfiguration.getDouble("Highest." + name + ".Z");
                highest = new Location(world, x, y, z);
            }

            if (spawnsYamlConfiguration.get("Lowest." + name) != null) {
                double x = spawnsYamlConfiguration.getDouble("Lowest." + name + ".X");
                double y = spawnsYamlConfiguration.getDouble("Lowest." + name + ".Y");
                double z = spawnsYamlConfiguration.getDouble("Lowest." + name + ".Z");
                lowest = new Location(world, x, y, z);
            }
        }

        public void setSpawn(OriginPlayer player) {
            Location location = player.getLocation();

            spawnsYamlConfiguration.set("Spawns." + name + ".World", location.getWorld().getName());
            spawnsYamlConfiguration.set("Spawns." + name + ".X", location.getX());
            spawnsYamlConfiguration.set("Spawns." + name + ".Y", location.getY());
            spawnsYamlConfiguration.set("Spawns." + name + ".Z", location.getZ());
            spawnsYamlConfiguration.set("Spawns." + name + ".Yaw", location.getYaw());
            spawnsYamlConfiguration.set("Spawns." + name + ".Pitch", location.getPitch());

            spawn = location;

            player.sendMessage(
                    SkyWarsFFA.getInstance().getPrefix() + "§7Du hast den Spawn für die Map §e" + name + " §7gesetzt",
                    SkyWarsFFA.getInstance().getPrefix() + "§7You've set the spawn for the map §e" + name
            );

            try {
                spawnsYamlConfiguration.save(spawnsFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void setHighest(OriginPlayer player) {
            Location location = player.getLocation();

            spawnsYamlConfiguration.set("Highest." + name + ".X", location.getX());
            spawnsYamlConfiguration.set("Highest." + name + ".Y", location.getY());
            spawnsYamlConfiguration.set("Highest." + name + ".Z", location.getZ());

            highest = new Location(Bukkit.getWorld("world"), location.getX(), location.getY(), location.getZ());

            player.sendMessage(
                    SkyWarsFFA.getInstance().getPrefix() + "§7Du hast die obere Ecke für die Map §e" + name + " §7gesetzt",
                    SkyWarsFFA.getInstance().getPrefix() + "§7You've set the upper corner for the map §e" + name
            );

            try {
                spawnsYamlConfiguration.save(spawnsFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void setLowest(OriginPlayer player) {
            Location location = player.getLocation();

            spawnsYamlConfiguration.set("Lowest." + name + ".X", location.getX());
            spawnsYamlConfiguration.set("Lowest." + name + ".Y", location.getY());
            spawnsYamlConfiguration.set("Lowest." + name + ".Z", location.getZ());

            lowest = new Location(Bukkit.getWorld("world"), location.getX(), location.getY(), location.getZ());

            player.sendMessage(
                    SkyWarsFFA.getInstance().getPrefix() + "§7Du hast die untere Ecke für die Map §e" + name + " §7gesetzt",
                    SkyWarsFFA.getInstance().getPrefix() + "§7You've set the lower corner for the map §e" + name
            );

            try {
                spawnsYamlConfiguration.save(spawnsFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
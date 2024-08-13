package de.pxscxl.spigot.qsg.manager;

import de.pxscxl.cloud.CloudAPI;
import de.pxscxl.cloud.ServerInfo;
import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.spigot.qsg.QSG;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Getter
public class MapManager {

    @Getter
    private static MapManager instance;

    private final File mapsFile;
    private final File spawnsFile;

    private final YamlConfiguration mapsYamlConfiguration;
    private final YamlConfiguration spawnsYamlConfiguration;

    private Map activeMap;

    private final ArrayList<Map> availableMaps = new ArrayList<>();

    public MapManager() {
        instance = this;

        mapsFile = new File("plugins/QSG/maps.yml");
        if (!mapsFile.exists()) {
            try {
                mapsFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        spawnsFile = new File("plugins/QSG/spawns.yml");
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
    }
    
    public Location getWaitingLobby() {
        if (spawnsYamlConfiguration.get("Spawns.Lobby.World") != null) {
            String world = spawnsYamlConfiguration.getString("Spawns.Lobby.World");
            double x = spawnsYamlConfiguration.getDouble("Spawns.Lobby.X");
            double y = spawnsYamlConfiguration.getDouble("Spawns.Lobby.Y");
            double z = spawnsYamlConfiguration.getDouble("Spawns.Lobby.Z");
            float yaw = (float) spawnsYamlConfiguration.getDouble("Spawns.Lobby.Yaw");
            float pitch = (float) spawnsYamlConfiguration.getDouble("Spawns.Lobby.Pitch");
            return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
        }
        return null;
    }

    public void setWaitingLobby(OriginPlayer player) {
        Location location = player.getLocation();

        spawnsYamlConfiguration.set("Spawns.Lobby.World", location.getWorld().getName());
        spawnsYamlConfiguration.set("Spawns.Lobby.X", location.getX());
        spawnsYamlConfiguration.set("Spawns.Lobby.Y", location.getY());
        spawnsYamlConfiguration.set("Spawns.Lobby.Z", location.getZ());
        spawnsYamlConfiguration.set("Spawns.Lobby.Yaw", location.getYaw());
        spawnsYamlConfiguration.set("Spawns.Lobby.Pitch", location.getPitch());

        player.sendMessage(
                QSG.getInstance().getPrefix() + "§7Du hast die §eWarteLobby §7gesetzt",
                QSG.getInstance().getPrefix() + "§7You've set the §ewaiting lobby"
        );

        try {
            spawnsYamlConfiguration.save(spawnsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addMap(OriginPlayer player, String mapName) {
        List<String> maps = mapsYamlConfiguration.getStringList("Maps");
        if (!maps.contains(mapName)) {
            player.sendMessage(
                    QSG.getInstance().getPrefix() + "§7Du hast die Karte §e" + mapName + " §7hinzugefügt",
                    QSG.getInstance().getPrefix() + "§7You've added the map §e" + mapName
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
                    QSG.getInstance().getPrefix() + "§cDiese Karte existiert §ebereits§7!",
                    QSG.getInstance().getPrefix() + "§cThis map §ealready §7exist!");
        }
    }

    public void removeMap(OriginPlayer player, String mapName) {
        List<String> maps = mapsYamlConfiguration.getStringList("Maps");
        if (maps.contains(mapName)) {
            player.sendMessage(
                    QSG.getInstance().getPrefix() + "§7Du hast die Karte §e" + mapName + " §7entfernt",
                    QSG.getInstance().getPrefix() + "§7You've removed the map §e" + mapName
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
                    QSG.getInstance().getPrefix() + "§7Diese Karte existiert §cnicht§7!",
                    QSG.getInstance().getPrefix() + "§7This map does §cnot §7exist!");
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

        private final ArrayList<Location> spawns = new ArrayList<>();

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
            for (int i = 1; i <= GameManager.getInstance().getMaxPlayers(); i++) {
                if (spawnsYamlConfiguration.get("Spawns." + name + "." + i + ".World") != null) {
                    String world = spawnsYamlConfiguration.getString("Spawns." + name + "." + i + ".World");
                    double x = spawnsYamlConfiguration.getDouble("Spawns." + name + "." + i + ".X");
                    double y = spawnsYamlConfiguration.getDouble("Spawns." + name + "." + i + ".Y");
                    double z = spawnsYamlConfiguration.getDouble("Spawns." + name + "." + i + ".Z");
                    float yaw = (float) spawnsYamlConfiguration.getDouble("Spawns." + name + "." + i + ".Yaw");
                    float pitch = (float) spawnsYamlConfiguration.getDouble("Spawns." + name + "." + i + ".Pitch");
                    Bukkit.createWorld(new WorldCreator(world));
                    spawns.add(new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch));
                }
            }
        }

        public void setSpawn(OriginPlayer player, Integer spawn) {
            Location location = player.getLocation();

            spawnsYamlConfiguration.set("Spawns." + name + "." + spawn + ".World", location.getWorld().getName());
            spawnsYamlConfiguration.set("Spawns." + name + "." + spawn + ".X", location.getX());
            spawnsYamlConfiguration.set("Spawns." + name + "." + spawn + ".Y", location.getY());
            spawnsYamlConfiguration.set("Spawns." + name + "." + spawn + ".Z", location.getZ());
            spawnsYamlConfiguration.set("Spawns." + name + "." + spawn + ".Yaw", location.getYaw());
            spawnsYamlConfiguration.set("Spawns." + name + "." + spawn + ".Pitch", location.getPitch());

            spawns.add(location);

            player.sendMessage(
                    QSG.getInstance().getPrefix() + "§7Du hast den Spawn §e" + spawn + " §7für die Map §e" + name + " §7gesetzt",
                    QSG.getInstance().getPrefix() + "§7You've set the spawn §e" + spawn + " §7for the map §e" + name
            );

            try {
                spawnsYamlConfiguration.save(spawnsFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
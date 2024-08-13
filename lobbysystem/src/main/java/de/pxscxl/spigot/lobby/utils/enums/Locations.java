package de.pxscxl.spigot.lobby.utils.enums;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

@Getter
public enum Locations {

    SPAWN("Spawn", new Location(Bukkit.getWorld("world"), 0.5, 79.0, 0.5, -90, 0)),
    LOBBY_PVP_HIGHEST("LobbyPvP Highest", new Location(Bukkit.getWorld("world"), 20000, 20000, 20000)),
    LOBBY_PVP_LOWEST("LobbyPvP Lowest", new Location(Bukkit.getWorld("world"), 20000, 20000, 20000)),
    LEADERBOARD_HOLOGRAM("Leaderboard Hologram", new Location(Bukkit.getWorld("world"), -6.5, 80.0, 0.5));

    final String name;
    final Location location;

    Locations(String name, Location location) {
        this.name = name;
        this.location = location;
    }
}

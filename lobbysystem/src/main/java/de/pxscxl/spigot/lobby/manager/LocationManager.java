package de.pxscxl.spigot.lobby.manager;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.pxscxl.origin.spigot.Origin;
import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.utils.session.Session;
import de.pxscxl.origin.utils.session.query.QueryStatement;
import de.pxscxl.origin.utils.session.query.UpdateStatement;
import de.pxscxl.spigot.lobby.utils.enums.Locations;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class LocationManager {

    @Getter
    private static LocationManager instance;

    private final UpdateStatement create;
    private final UpdateStatement insert;

    private final QueryStatement exist;

    private final QueryStatement getLocation;
    private final UpdateStatement setLocation;

    @Getter
    private final HashMap<UUID, Location> locations = new HashMap<>();

    public LocationManager() {
        instance = this;
        Session session = Origin.getInstance().getSession();

        create = session.prepareUpdateStatement("CREATE TABLE IF NOT EXISTS lobby_locations (uuid VARCHAR(36), location TEXT)");
        insert = session.prepareUpdateStatement("INSERT INTO lobby_locations (uuid, location) VALUES (?, ?)");

        exist = session.prepareQueryStatement("SELECT uuid FROM lobby_locations WHERE uuid = ?");

        getLocation = session.prepareQueryStatement("SELECT location FROM lobby_locations WHERE uuid = ?");
        setLocation = session.prepareUpdateStatement("UPDATE lobby_locations SET location = ? WHERE uuid = ?");

        create();
    }

    private void create() {
        try {
            create.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert(UUID uuid) {
        try {
            JsonObject object = new JsonObject();
            Location location = Locations.SPAWN.getLocation();

            object.addProperty("world", location.getWorld().getName());
            object.addProperty("x", location.getX());
            object.addProperty("y", location.getY());
            object.addProperty("z", location.getZ());
            object.addProperty("yaw", location.getY());
            object.addProperty("pitch", location.getPitch());

            insert.execute(uuid, object);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean exist(UUID uuid) {
        try {
            ResultSet resultSet = exist.execute(uuid);
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void loadLocation(UUID uuid) {
        try {
            ResultSet resultSet = getLocation.execute(uuid);
            if (resultSet.next()) {
                JsonObject object = (JsonObject) new JsonParser().parse(resultSet.getString("location"));
                locations.put(uuid, new Location(
                        Bukkit.getWorld(object.get("world").getAsString()),
                        object.get("x").getAsDouble(),
                        object.get("y").getAsDouble(),
                        object.get("z").getAsDouble(),
                        object.get("yaw").getAsFloat(),
                        object.get("pitch").getAsFloat()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveLocation(OriginPlayer player) {
        try {
            JsonObject object = new JsonObject();
            Location location = player.getLocation();

            object.addProperty("world", location.getWorld().getName());
            object.addProperty("x", location.getX());
            object.addProperty("y", location.getY());
            object.addProperty("z", location.getZ());
            object.addProperty("yaw", location.getYaw());
            object.addProperty("pitch", location.getPitch());

            setLocation.execute(object, player.getUniqueId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        locations.remove(player.getUniqueId());
    }

    public void onPlayerJoin(OriginPlayer player) {
        player.teleport(LocationManager.getInstance().getLocations().get(player.getUniqueId()).clone().add(0, 0.5, 0));
    }
}

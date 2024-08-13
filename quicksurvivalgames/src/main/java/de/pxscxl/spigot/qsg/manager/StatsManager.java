package de.pxscxl.spigot.qsg.manager;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.pxscxl.origin.spigot.Origin;
import de.pxscxl.origin.utils.Pair;
import de.pxscxl.origin.utils.session.Session;
import de.pxscxl.origin.utils.session.query.QueryStatement;
import de.pxscxl.origin.utils.session.query.UpdateStatement;
import de.pxscxl.spigot.qsg.QSG;
import de.pxscxl.spigot.qsg.utils.enums.Rank;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class StatsManager {

    @Getter
    private static StatsManager instance;

    private final UpdateStatement create;
    private final UpdateStatement insert;
    private final QueryStatement exist;

    private final QueryStatement getStats;
    private final UpdateStatement setStats;

    private final QueryStatement getAllStats;

    @Getter
    private final HashMap<String, Stats> fakeStats = new HashMap<>();

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);

    public StatsManager() {
        instance = this;
        Session session = Origin.getInstance().getSession();

        create = session.prepareUpdateStatement("CREATE TABLE IF NOT EXISTS qsg_stats (uuid VARCHAR(36), stats TEXT)");
        insert = session.prepareUpdateStatement("INSERT INTO qsg_stats (uuid, stats) VALUES (?, ?)");
        exist = session.prepareQueryStatement("SELECT uuid FROM qsg_stats WHERE uuid = ?");

        getStats = session.prepareQueryStatement("SELECT * FROM qsg_stats WHERE uuid = ?");
        setStats = session.prepareUpdateStatement("UPDATE qsg_stats SET stats = ? WHERE uuid = ?");

        getAllStats = session.prepareQueryStatement("SELECT * FROM qsg_stats");

        create();
    }

    public void create() {
        try {
            create.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert(UUID uuid) {
        try {
            JsonObject object = new JsonObject();
            object.addProperty("kills", 0);
            object.addProperty("deaths", 0);
            object.addProperty("elo", 100);
            object.addProperty("wins", 0);
            object.addProperty("loses", 0);

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

    public Stats getStats(UUID uuid) {
        try {
            ResultSet resultSet = getStats.execute(uuid);
            if (resultSet.next()) {
                JsonObject object = (JsonObject) new JsonParser().parse(resultSet.getString("stats"));
                return new Stats(UUID.fromString(resultSet.getString("uuid")), object.get("kills").getAsInt(), object.get("deaths").getAsInt(), object.get("elo").getAsInt(), object.get("wins").getAsInt(), object.get("loses").getAsInt());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setStats(UUID uuid, Stats stats) {
        try {
            JsonObject object = new JsonObject();
            object.addProperty("kills", stats.getKills());
            object.addProperty("deaths", stats.getDeaths());
            object.addProperty("elo", stats.getElo());
            object.addProperty("wins", stats.getWins());
            object.addProperty("loses", stats.getLoses());

            setStats.execute(object, uuid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Stats> getTop10() {
        List<Stats> list = new ArrayList<>();
        try {
            ResultSet resultSet = getAllStats.execute();
            while (resultSet.next()) {
                JsonObject object = (JsonObject) new JsonParser().parse(resultSet.getString("stats"));
                list.add(new Stats(UUID.fromString(resultSet.getString("uuid")), object.get("kills").getAsInt(), object.get("deaths").getAsInt(), object.get("elo").getAsInt(), object.get("wins").getAsInt(), object.get("loses").getAsInt()));
            }
            return list.stream().map(stats -> new Pair<>(stats.getUuid(), stats.getElo())).sorted((o1, o2) -> o2.getB().compareTo(o1.getB())).limit(10).map(pair -> list.stream().filter(stats -> stats.getUuid() == pair.getA() && stats.getElo() == pair.getB()).findFirst().orElse(null)).collect(Collectors.toList());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void runWrite(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(QSG.getInstance(), () -> {
            readWriteLock.writeLock().lock();
            runnable.run();
            readWriteLock.writeLock().unlock();
        });
    }

    @Setter
    @Getter
    public static class Stats {

        private final UUID uuid;

        private int kills;
        private int deaths;
        private int elo;
        private int wins;
        private int loses;

        public Stats(UUID uuid, int kills, int deaths, int elo, int wins, int loses) {
            this.uuid = uuid;
            this.kills = kills;
            this.deaths = deaths;
            this.elo = elo;
            this.wins = wins;
            this.loses = loses;
        }

        public Rank getRank() {
            if (elo >= Rank.UNRANKED.getMinElo() && elo <= Rank.UNRANKED.getMaxElo()) {
                return Rank.UNRANKED;
            } else if (elo >= Rank.BRONZE.getMinElo() && elo <= Rank.BRONZE.getMaxElo()) {
                return Rank.BRONZE;
            } else if (elo >= Rank.SILVER.getMinElo() && elo <= Rank.SILVER.getMaxElo()) {
                return Rank.SILVER;
            } else if (elo >= Rank.GOLD.getMinElo() && elo <= Rank.GOLD.getMaxElo()) {
                return Rank.GOLD;
            } else {
                return Rank.DIAMOND;
            }
        }
    }
}

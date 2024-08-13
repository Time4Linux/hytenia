package de.pxscxl.spigot.lobby.manager;

import de.pxscxl.origin.spigot.Origin;
import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.origin.utils.enums.Rank;
import de.pxscxl.origin.utils.session.Session;
import de.pxscxl.origin.utils.session.query.QueryStatement;
import de.pxscxl.origin.utils.session.query.UpdateStatement;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class HiderManager {

    @Getter
    private static HiderManager instance;

    private final UpdateStatement create;
    private final UpdateStatement insert;
    private final QueryStatement exist;

    private final QueryStatement getHideMode;
    private final UpdateStatement setHideMode;

    @Getter
    private final HashMap<UUID, Integer> hider = new HashMap<>();

    public HiderManager() {
        instance = this;
        Session session = Origin.getInstance().getSession();

        create = session.prepareUpdateStatement("CREATE TABLE IF NOT EXISTS lobby_hidemode (uuid VARCHAR(36), hidemode INT(100))");
        insert = session.prepareUpdateStatement("INSERT INTO lobby_hidemode (uuid, hidemode) VALUES (?, ?)");

        exist = session.prepareQueryStatement("SELECT uuid FROM lobby_hidemode WHERE uuid = ?");

        getHideMode = session.prepareQueryStatement("SELECT hidemode FROM lobby_hidemode WHERE uuid = ?");
        setHideMode = session.prepareUpdateStatement("UPDATE lobby_hidemode SET hidemode = ? WHERE uuid = ?");

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
            insert.execute(uuid, 0);
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

    public int getHideMode(OriginPlayer player) {
        return hider.get(player.getUniqueId());
    }

    public void setHideMode(OriginPlayer player, int mode) {
        hider.put(player.getUniqueId(), mode);
    }

    public void loadHideMode(UUID uuid) {
        try {
            ResultSet resultSet = getHideMode.execute(uuid);
            if (resultSet.next()) {
                hider.put(uuid, resultSet.getInt("hideMode"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveHideMode(OriginPlayer player) {
        try {
            setHideMode.execute(hider.get(player.getUniqueId()), player.getUniqueId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        hider.remove(player.getUniqueId());
    }

    public void onPlayerJoin(OriginPlayer player) {
        hidePlayer(player);
        hidePlayers(player);
    }

    public void hidePlayers(OriginPlayer player) {
        switch (getHideMode(player)) {
            case 0:
                OriginManager.getInstance().getPlayers().forEach(players -> {
                    if (!Origin.getInstance().getVanishPlayers().contains(players)) {
                        player.showPlayer(players);
                    }
                });
                break;
            case 1:
                OriginManager.getInstance().getPlayers().stream().filter(OriginPlayer::isOnline).forEach(players -> {
                    if (players.hasPriorityAccess(Rank.GOLD.getPriority())) {
                        if (!Origin.getInstance().getVanishPlayers().contains(players)) {
                            player.showPlayer(players);
                        }
                    } else {
                        if (players != player) {
                            player.hidePlayer(players);
                        }
                    }
                });
                break;
            case 2:
                OriginManager.getInstance().getPlayers().forEach(players -> {
                    if (players != player) {
                        player.hidePlayer(players);
                    }
                });
                break;
        }
    }

    public void hidePlayer(OriginPlayer player) {
        OriginManager.getInstance().getPlayers().stream().filter(OriginPlayer::isOnline).forEach(players -> {
            int mode = getHideMode(players);
            if (mode == 0 || (mode == 1 && player.hasPriorityAccess(Rank.GOLD.getPriority()))) {
                players.showPlayer(player);
            } else {
                players.hidePlayer(player);
            }
        });
    }
}

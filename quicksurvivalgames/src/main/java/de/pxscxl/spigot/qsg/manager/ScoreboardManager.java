package de.pxscxl.spigot.qsg.manager;

import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.origin.spigot.api.scoreboard.ScoreboardObjective;
import de.pxscxl.spigot.qsg.QSG;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.HashSet;

public class ScoreboardManager {

    @Getter
    private static ScoreboardManager instance;

    private int count = 1;

    public ScoreboardManager() {
        instance = this;

        Bukkit.getScheduler().runTaskTimerAsynchronously(QSG.getInstance(), () -> {
            if (count != 2) {
                count++;
            } else {
                count = 1;
            }
            OriginManager.getInstance().getPlayers().forEach(this::setCountScore);
        }, 0, 500);
    }

    public void setLobbyObjective(OriginPlayer player) {
        ScoreboardObjective objective = player.getScoreboard().getObjective();
        StatsManager.Stats stats = StatsManager.getInstance().getStats(player.getUniqueId());

        objective.setScore(13, "§7", "§8");
        objective.setScore(12, "§7", "§7" + player.language("Karte", "Map"));
        objective.setScore(11, " §8» ", "§e" + MapManager.getInstance().getActiveMap().getName());
        objective.setScore(10, "§5", "§6");
        objective.setScore(9, "§7Team", "§7ing");
        objective.setScore(8, " §8» ", GameManager.getInstance().isTeaming() ? "§a✔" : "§c✘");
        objective.setScore(7, "§3", "§4");
        objective.setScore(6, "§7Ra", "§7n" + player.language("g", "k"));
        objective.setScore(5, " §8» ", "§e" + stats.getRank().getColor() + player.language(stats.getRank().getGerman(), stats.getRank().getEnglish()));
        objective.setScore(4, "§1", "§2");
        switch (count) {
            case 1:
                objective.setScore(3, "§7Team", "§7Speak");
                objective.setScore(2, " §8» §ets.", "§ehytenia.eu");
                break;
            case 2:
                objective.setScore(3, "§7Twi", "§7tter");
                objective.setScore(2, " §8» §e@", "§eHytenia");
                break;
        }
        objective.setScore(1, "§0", "§4");

        objective.setObjective();
    }

    public void setInGameObjective(OriginPlayer player) {
        ScoreboardObjective objective = player.getScoreboard().getObjective();
        StatsManager.Stats stats = StatsManager.getInstance().getStats(player.getUniqueId());

        objective.resetObjective();

        objective.setScore(13, "§7", "§8");
        objective.setScore(12, "§7", "§7" + player.language("Karte", "Map"));
        objective.setScore(11, " §8» ", "§e" + MapManager.getInstance().getActiveMap().getName());
        objective.setScore(10, "§5", "§6");
        objective.setScore(9, "§7" , "§7Kills");
        objective.setScore(8, " §8» ", "§3§e" + GameManager.getInstance().getKills().get(player));
        objective.setScore(7, "§3", "§4");
        objective.setScore(6, "§7", "§7Elo");
        objective.setScore(5, " §8» ", "§4§e" + stats.getElo());
        objective.setScore(4, "§1", "§2");
        switch (count) {
            case 1:
                objective.setScore(3, "§7Team", "§7Speak");
                objective.setScore(2, " §8» §ets.", "§ehytenia.eu");
                break;
            case 2:
                objective.setScore(3, "§7Twi", "§7tter");
                objective.setScore(2, " §8» §e@", "§eHytenia");
                break;
        }
        objective.setScore(1, "§0", "§4");
        
        objective.setObjective();
    }

    public void setSpectatorObjective(OriginPlayer player) {
        ScoreboardObjective objective = player.getScoreboard().getObjective();

        objective.resetObjective();
        
        objective.setScore(13, "§7", "§8");
        objective.setScore(12, "§7", "§7" + player.language("Karte", "Map"));
        objective.setScore(11, " §8» ", "§e" + MapManager.getInstance().getActiveMap().getName());
        objective.setScore(10, "§5", "§6");
        objective.setScore(9, "§7", "§7" + player.language("Spieler", "Players"));
        objective.setScore(8, " §8» ", "§1§e" + QSG.getInstance().getPlayers().size());
        objective.setScore(7, "§3", "§4");
        objective.setScore(6, "§7", "§7" + player.language("Zuschauer", "Spectators"));
        objective.setScore(5, " §8» ", "§2§e" + QSG.getInstance().getSpectators().size());
        objective.setScore(4, "§1", "§2");
        switch (count) {
            case 1:
                objective.setScore(3, "§7Team", "§7Speak");
                objective.setScore(2, " §8» §ets.", "§ehytenia.eu");
                break;
            case 2:
                objective.setScore(3, "§7Twi", "§7tter");
                objective.setScore(2, " §8» §e@", "§eHytenia");
                break;
        }
        objective.setScore(1, "§0", "§4");

        objective.setObjective();
    }

    public void setCountScore(OriginPlayer player) {
        switch (count) {
            case 1:
                player.getScoreboard().getObjective().updateScore(3, "§7Team", "§7Speak");
                player.getScoreboard().getObjective().updateScore(2, " §8» §ets.", "§ehytenia.eu");
                break;
            case 2:
                player.getScoreboard().getObjective().updateScore(3, "§7Twi", "§7tter");
                player.getScoreboard().getObjective().updateScore(2, " §8» §e@", "§eHytenia");
                break;
        }
    }

    public void updateMapScore(OriginPlayer player) {
        player.getScoreboard().getObjective().updateScore(11, " §8» ", "§e" + MapManager.getInstance().getActiveMap().getName());
    }

    public void updatePlayersScore(OriginPlayer player) {
        player.getScoreboard().getObjective().updateScore(8, " §8» ", "§1§e" + QSG.getInstance().getPlayers().size());
    }

    public void updateSpectatorsScore(OriginPlayer player) {
        player.getScoreboard().getObjective().updateScore(5, " §8» ", "§2§e" + QSG.getInstance().getSpectators().size());
    }

    public void updateKillsScore(OriginPlayer player) {
        player.getScoreboard().getObjective().updateScore(8, " §8» ", "§3§e" + GameManager.getInstance().getKills().get(player));
    }

    public void updateEloScore(OriginPlayer player, StatsManager.Stats stats) {
        player.getScoreboard().getObjective().updateScore(5, " §8» ", "§4§e" + stats.getElo());
    }

    public void registerSpectatorTeam(OriginPlayer player) {
        player.getScoreboard().registerScoreboardTeam("z9_spectator", "§7", "", new HashSet<>());
    }

    public void setScoreboardTeams(OriginPlayer player) {
        OriginManager.getInstance().getPlayers().forEach(players -> {
            if (QSG.getInstance().getSpectators().contains(players)) {
                player.getScoreboard().getScoreboardTeam("z9_spectator").addEntry(players.getNick() != null && !players.equals(player) ? players.getNick() : players.getName());
            } else {
                player.getScoreboard().registerPlayer(players);
            }

            if (QSG.getInstance().getSpectators().contains(player)) {
                players.getScoreboard().getScoreboardTeam("z9_spectator").addEntry(player.getNick() != null && !players.equals(player) ? player.getNick() : player.getName());
            } else {
                players.getScoreboard().registerPlayer(player);
            }
        });
    }

    public void setRegularScoreboardTeams(OriginPlayer player) {
        OriginManager.getInstance().getPlayers().forEach(players -> {
            player.getScoreboard().registerPlayer(players);
            players.getScoreboard().registerPlayer(player);
        });
    }
}

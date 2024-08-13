package de.pxscxl.spigot.mlgrush.manager;

import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.origin.spigot.api.scoreboard.ScoreboardObjective;
import de.pxscxl.spigot.mlgrush.MLGRush;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.HashSet;

@Getter
public class ScoreboardManager {

    @Getter
    private static ScoreboardManager instance;

    private int count = 1;

    public ScoreboardManager() {
        instance = this;

        Bukkit.getScheduler().runTaskTimerAsynchronously(MLGRush.getInstance(), () -> {
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
        objective.setScore(9, "§7Te", "§7am");
        objective.setScore(8, " §8» ", "§e" + team(player));
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

        objective.resetObjective();

        objective.setScore(10, "§7", "§8");
        objective.setScore(9, "§7", "§7" + player.language("Karte", "Map"));
        objective.setScore(8, " §8» ", "§e" + MapManager.getInstance().getActiveMap().getName());
        objective.setScore(7, "§5", "§6");
        objective.setScore(6, " §8» " + TeamManager.Team.RED.getColor() + player.language(TeamManager.Team.RED.getGermanName(), TeamManager.Team.RED.getEnglishName()), " §8× §7" + GameManager.getInstance().getBeds().get(TeamManager.Team.RED.getMembers().get(0)));
        objective.setScore(5, " §8» " + TeamManager.Team.BLUE.getColor() + player.language(TeamManager.Team.BLUE.getGermanName(), TeamManager.Team.BLUE.getEnglishName()), " §8× §7" + GameManager.getInstance().getBeds().get(TeamManager.Team.BLUE.getMembers().get(0)));
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
        objective.setScore(8, " §8» ", "§1§e" + MLGRush.getInstance().getPlayers().size());
        objective.setScore(7, "§3", "§4");
        objective.setScore(6, "§7", "§7" + player.language("Zuschauer", "Spectators"));
        objective.setScore(5, " §8» ", "§2§e" + MLGRush.getInstance().getSpectators().size());
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
        player.getScoreboard().getObjective().updateScore(12, " §8» ", "§e" + MapManager.getInstance().getActiveMap().getName());
    }

    public void updatePlayersScore(OriginPlayer player) {
        player.getScoreboard().getObjective().updateScore(8, " §8» ", "§1§e" + MLGRush.getInstance().getPlayers().size());
    }

    public void updateSpectatorsScore(OriginPlayer player) {
        player.getScoreboard().getObjective().updateScore(5, " §8» ", "§2§e" + MLGRush.getInstance().getSpectators().size());
    }

    public void updateBedsScore(OriginPlayer player) {
        player.getScoreboard().getObjective().updateScore(6, " §8» " + TeamManager.Team.RED.getColor() + player.language(TeamManager.Team.RED.getGermanName(), TeamManager.Team.RED.getEnglishName()), " §8× §7" + GameManager.getInstance().getBeds().get(TeamManager.Team.RED.getMembers().get(0)));
        player.getScoreboard().getObjective().updateScore(5, " §8» " + TeamManager.Team.BLUE.getColor() + player.language(TeamManager.Team.BLUE.getGermanName(), TeamManager.Team.BLUE.getEnglishName()), " §8× §7" + GameManager.getInstance().getBeds().get(TeamManager.Team.BLUE.getMembers().get(0)));
    }

    public void updateTeamScore(OriginPlayer player) {
        player.getScoreboard().getObjective().updateScore(8, " §8» ", "§e" + team(player));
    }

    public void registerSpectatorTeam(OriginPlayer player) {
        player.getScoreboard().registerScoreboardTeam("z9_spectator", "§7", "", new HashSet<>());
    }

    public void setScoreboardTeams(OriginPlayer player) {
        OriginManager.getInstance().getPlayers().forEach(players -> {
            if (TeamManager.getInstance().getPlayerTeams().get(players) != null) {
                player.getScoreboard().getScoreboardTeam(TeamManager.getInstance().getPlayerTeams().get(players).getTabSorting()).addEntry(players.getNick() != null && !players.equals(player) ? players.getNick() : players.getName());
            } else if (MLGRush.getInstance().getSpectators().contains(players)) {
                player.getScoreboard().getScoreboardTeam("z9_spectator").addEntry(players.getNick() != null && !players.equals(player) ? players.getNick() : players.getName());
            } else {
                player.getScoreboard().registerPlayer(players);
            }

            if (TeamManager.getInstance().getPlayerTeams().get(player) != null) {
                players.getScoreboard().getScoreboardTeam(TeamManager.getInstance().getPlayerTeams().get(player).getTabSorting()).addEntry(player.getNick() != null && !players.equals(player) ? player.getNick() : player.getName());
            } else if (MLGRush.getInstance().getSpectators().contains(player)) {
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


    private String team(OriginPlayer player) {
        String team;
        if (TeamManager.getInstance().getPlayerTeams().containsKey(player)) {
            team = TeamManager.getInstance().getPlayerTeams().get(player).getColor() + "" + player.language(TeamManager.getInstance().getPlayerTeams().get(player).getGermanName(), TeamManager.getInstance().getPlayerTeams().get(player).getEnglishName());
        } else {
            team = "§c✘";
        }
        return team;
    }
}

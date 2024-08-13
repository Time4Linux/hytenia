package de.pxscxl.spigot.lobby.manager;

import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.manager.ClanManager;
import de.pxscxl.origin.spigot.api.manager.FriendManager;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.origin.spigot.api.scoreboard.ScoreboardObjective;
import de.pxscxl.spigot.lobby.LobbySystem;
import lombok.Getter;
import org.bukkit.Bukkit;

public class ScoreboardManager {

    @Getter
    private static ScoreboardManager instance;

    private int count = 1;

    public ScoreboardManager() {
        instance = this;

        Bukkit.getScheduler().runTaskTimerAsynchronously(LobbySystem.getInstance(), () -> {
            if (count != 2) {
                count++;
            } else {
                count = 1;
            }
            OriginManager.getInstance().getPlayers().forEach(this::setCountScore);
        }, 0, 500);
    }

    public void setObjective(OriginPlayer player) {
        ScoreboardObjective objective = player.getScoreboard().getObjective();

        objective.setScore(13, "§6", "§7");
        objective.setScore(12, "§7", "§7" + player.language("Rang", "Rank"));
        objective.setScore(11, " §8» ", player.getRank().getColor() + player.getRank().getName());
        objective.setScore(10, "§5", "§e");
        switch (count) {
            case 1:
                objective.setScore(9, "§7", "§7Clan");
                objective.setScore(8, " §8» ", (ClanManager.getInstance().getClanByUuid(player.getUniqueId()) != null ? "§8[" + ClanManager.getInstance().getClanByUuid(player.getUniqueId()).getColor() + ClanManager.getInstance().getClanByUuid(player.getUniqueId()).getTag() + "§8]" : "§c✘"));
                break;
            case 2:
                objective.setScore(9, "§7", "§7Coins");
                objective.setScore(8, " §8» ", "§e" + player.getCoins());
                break;
        }
        objective.setScore(7, "§3", "§4");
        switch (count) {
            case 1:
                objective.setScore(6, "§7" + player.language("Spiel", "Time "), "§7" + player.language("zeit", "online"));
                objective.setScore(5, " §8» ", formatTime(player));
                break;
            case 2:
                objective.setScore(6, "§7", "§7" + player.language("Freunde", "Friends"));
                objective.setScore(5, " §8» §e" + (int) FriendManager.getInstance().getFriends(player.getUniqueId()).stream().filter(friend -> OriginManager.getInstance().getPlayer(friend.getUuid()).isOnline()).count(), "§8/§7" + FriendManager.getInstance().getFriends(player.getUniqueId()).size());
                break;
        }
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

    public void updateScoresLanguage(OriginPlayer player) {
        player.getScoreboard().getObjective().updateScore(12, "§7", "§7" + player.language("Rang", "Rank"));
        setCountScore(player);
    }

    public void setCountScore(OriginPlayer player) {
        switch (count) {
            case 1:
                player.getScoreboard().getObjective().updateScore(9, "§7", "§7Clan");
                player.getScoreboard().getObjective().updateScore(8, " §8» ", (ClanManager.getInstance().getClanByUuid(player.getUniqueId()) != null ? "§8[" + ClanManager.getInstance().getClanByUuid(player.getUniqueId()).getColor() + ClanManager.getInstance().getClanByUuid(player.getUniqueId()).getTag() + "§8]" : "§c✘"));

                player.getScoreboard().getObjective().updateScore(6, "§7" + player.language("Spiel", "Time "), "§7" + player.language("zeit", "online"));
                player.getScoreboard().getObjective().updateScore(5, " §8» ", formatTime(player));

                player.getScoreboard().getObjective().updateScore(3, "§7Team", "§7Speak");
                player.getScoreboard().getObjective().updateScore(2, " §8» §ets.", "§ehytenia.eu");
                break;
            case 2:
                player.getScoreboard().getObjective().updateScore(9, "§7", "§7Coins");
                player.getScoreboard().getObjective().updateScore(8, " §8» ", "§e" + player.getCoins());

                player.getScoreboard().getObjective().updateScore(6, "§7", "§7" + player.language("Freunde", "Friends"));
                player.getScoreboard().getObjective().updateScore(5, " §8» §e" + (int) FriendManager.getInstance().getFriends(player.getUniqueId()).stream().filter(friend -> OriginManager.getInstance().getPlayer(friend.getUuid()).isOnline()).count(), "§8/§7" + FriendManager.getInstance().getFriends(player.getUniqueId()).size());

                player.getScoreboard().getObjective().updateScore(3, "§7Twi", "§7tter");
                player.getScoreboard().getObjective().updateScore(2, " §8» §e@", "§eHytenia");
                break;
        }
    }

    public void updateTimeScore(OriginPlayer player) {
        if (count != 1) return;
        player.getScoreboard().getObjective().updateScore(5, " §8» ", formatTime(player));
    }

    public void updateFriendsScore(OriginPlayer player) {
        if (count != 2) return;
        Bukkit.getScheduler().runTaskAsynchronously(LobbySystem.getInstance(), () -> {
            int count = (int) FriendManager.getInstance().getFriends(player.getUniqueId()).stream().filter(friend -> OriginManager.getInstance().getPlayer(friend.getUuid()).isOnline()).count();
            int all = FriendManager.getInstance().getFriends(player.getUniqueId()).size();
            Bukkit.getScheduler().runTask(LobbySystem.getInstance(), () -> player.getScoreboard().getObjective().updateScore(5, " §8» §e" + count, "§8/§7" + all));
        });
    }

    public void updateCoinsScore(OriginPlayer player) {
        if (count != 2) return;
        player.getScoreboard().getObjective().updateScore(8, " §8» ", "§e" + player.getCoins());
    }

    private String formatTime(OriginPlayer player) {
        long hours = (long) Math.floor((double) player.getOnlineTime() / 60L);
        return "§e" + hours + player.language(" Std§8.", "h");
    }

    public void setScoreboardTeams(OriginPlayer player) {
        OriginManager.getInstance().getPlayers().forEach(players -> {
            player.getScoreboard().registerPlayer(players);
            players.getScoreboard().registerPlayer(player);
        });
    }
}

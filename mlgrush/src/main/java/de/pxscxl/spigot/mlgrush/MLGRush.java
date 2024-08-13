package de.pxscxl.spigot.mlgrush;

import de.pxscxl.cloud.CloudAPI;
import de.pxscxl.cloud.State;
import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.spigot.mlgrush.manager.*;
import de.pxscxl.spigot.mlgrush.utils.registration.CommandRegistration;
import de.pxscxl.spigot.mlgrush.utils.registration.EventRegistration;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MLGRush extends JavaPlugin {

    @Getter
    private static MLGRush instance;

    @Getter
    private final String prefix = "§8» §6MLGRush §8● §r";

    private CommandRegistration commandRegistration;
    private EventRegistration eventRegistration;

    @Getter
    private final ArrayList<OriginPlayer> players = new ArrayList<>();

    @Override
    public void onLoad() {
        instance = this;

        commandRegistration = new CommandRegistration();
        eventRegistration = new EventRegistration();
    }

    @Override
    public void onEnable() {
        commandRegistration.registerAllCommands();
        eventRegistration.registerAllEvents();

        new GameManager();
        new MapManager();
        new ScoreboardManager();
        new StatsManager();
        new TeamManager();
        new KitManager();

        Bukkit.getScheduler().runTaskTimerAsynchronously(MLGRush.getInstance(), () -> {
            if (players.size() < GameManager.getInstance().getMinPlayers() && CloudAPI.getInstance().getLocalServer().getState().equals(State.LOBBY)) {
                OriginManager.getInstance().getPlayers().forEach(players -> players.sendActionbar(
                        "§7Es werden §eweitere §7Spieler benötigt",
                        "§7Waiting for §emore §7players to start"));
            }
        }, 0, 20);
    }

    public List<OriginPlayer> getSpectators() {
        return OriginManager.getInstance().getPlayers().stream().filter(player -> !players.contains(player)).collect(Collectors.toList());
    }
}

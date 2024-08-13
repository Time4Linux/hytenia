package de.pxscxl.spigot.qsg;

import de.pxscxl.cloud.CloudAPI;
import de.pxscxl.cloud.State;
import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.spigot.qsg.manager.*;
import de.pxscxl.spigot.qsg.utils.registration.CommandRegistration;
import de.pxscxl.spigot.qsg.utils.registration.EventRegistration;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QSG extends JavaPlugin {

    @Getter
    private static QSG instance;

    @Getter
    private final String prefix = "§8» §6QuickSG §8● §r";

    private CommandRegistration commandRegistry;
    private EventRegistration eventRegistry;

    @Getter
    private final ArrayList<OriginPlayer> players = new ArrayList<>();

    @Override
    public void onLoad() {
        instance = this;

        commandRegistry = new CommandRegistration();
        eventRegistry = new EventRegistration();
    }

    @Override
    public void onEnable() {
        commandRegistry.registerAllCommands();
        eventRegistry.registerAllEvents();

        new ChestLootManager();
        new ScoreboardManager();
        new StatsManager();
        new GameManager();
        new MapManager();

        Bukkit.getScheduler().runTaskTimerAsynchronously(QSG.getInstance(), () -> {
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

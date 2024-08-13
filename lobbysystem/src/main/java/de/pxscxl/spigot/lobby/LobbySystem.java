package de.pxscxl.spigot.lobby;

import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.spigot.lobby.manager.*;
import de.pxscxl.spigot.lobby.minigames.TicTacToe;
import de.pxscxl.spigot.lobby.utils.registration.CommandRegistration;
import de.pxscxl.spigot.lobby.utils.registration.EventRegistration;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class LobbySystem extends JavaPlugin {

    @Getter
    private static LobbySystem instance;

    private CommandRegistration commandRegistration;
    private EventRegistration eventRegistration;

    @Getter
    private final ArrayList<OriginPlayer> buildPlayers = new ArrayList<>();

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

        new ProfileManager();
        new HiderManager();
        new LobbyManager();
        new LocationManager();
        new SettingsManager();
        new ScoreboardManager();
        new TeleporterManager();

        new TicTacToe();
    }
}

package de.pxscxl.spigot.lobby.listener;

import de.pxscxl.origin.spigot.api.events.LanguageUpdateEvent;
import de.pxscxl.spigot.lobby.Utils;
import de.pxscxl.spigot.lobby.manager.ScoreboardManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class LanguageUpdateListener implements Listener {

    @EventHandler
    public void onLanguageUpdate(LanguageUpdateEvent event) {
        ScoreboardManager.getInstance().updateScoresLanguage(event.getPlayer());
        Utils.setSpawnItems(event.getPlayer());
    }
}

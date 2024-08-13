package de.pxscxl.spigot.lobby.utils.registration;

import de.pxscxl.spigot.lobby.LobbySystem;
import de.pxscxl.spigot.lobby.listener.*;
import org.bukkit.Bukkit;

public class EventRegistration {

    public void registerAllEvents() {
        Bukkit.getPluginManager().registerEvents(new AsyncPlayerChatListener(), LobbySystem.getInstance());
        Bukkit.getPluginManager().registerEvents(new AsyncPlayerPreLoginListener(), LobbySystem.getInstance());
        Bukkit.getPluginManager().registerEvents(new BlockBreakListener(), LobbySystem.getInstance());
        Bukkit.getPluginManager().registerEvents(new BlockPhysicsListener(), LobbySystem.getInstance());
        Bukkit.getPluginManager().registerEvents(new BlockPlaceListener(), LobbySystem.getInstance());
        Bukkit.getPluginManager().registerEvents(new CoinsUpdateListener(), LobbySystem.getInstance());
        Bukkit.getPluginManager().registerEvents(new FoodLevelChangeListener(), LobbySystem.getInstance());
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), LobbySystem.getInstance());
        Bukkit.getPluginManager().registerEvents(new LanguageUpdateListener(), LobbySystem.getInstance());
        Bukkit.getPluginManager().registerEvents(new OnlineTimeUpdateListener(), LobbySystem.getInstance());
        Bukkit.getPluginManager().registerEvents(new OnlinePlayersUpdateListener(), LobbySystem.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerArmorStandManipulateListener(), LobbySystem.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerDropItemListener(), LobbySystem.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerInteractEntityListener(), LobbySystem.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), LobbySystem.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), LobbySystem.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerPickupItemListener(), LobbySystem.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), LobbySystem.getInstance());
        Bukkit.getPluginManager().registerEvents(new WeatherChangeListener(), LobbySystem.getInstance());
    }
}

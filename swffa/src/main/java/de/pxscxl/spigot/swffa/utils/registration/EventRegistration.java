package de.pxscxl.spigot.swffa.utils.registration;

import de.pxscxl.spigot.swffa.SkyWarsFFA;
import de.pxscxl.spigot.swffa.listener.*;
import org.bukkit.Bukkit;

public class EventRegistration {

    public void registerAllEvents() {
        Bukkit.getPluginManager().registerEvents(new AsyncPlayerChatListener(), SkyWarsFFA.getInstance());
        Bukkit.getPluginManager().registerEvents(new AsyncPlayerPreLoginListener(), SkyWarsFFA.getInstance());
        Bukkit.getPluginManager().registerEvents(new BlockBreakListener(), SkyWarsFFA.getInstance());
        Bukkit.getPluginManager().registerEvents(new BlockPlaceListener(), SkyWarsFFA.getInstance());
        Bukkit.getPluginManager().registerEvents(new BlockPhysicsListener(), SkyWarsFFA.getInstance());
        Bukkit.getPluginManager().registerEvents(new CreatureSpawnListener(), SkyWarsFFA.getInstance());
        Bukkit.getPluginManager().registerEvents(new EntityDamageByEntityListener(), SkyWarsFFA.getInstance());
        Bukkit.getPluginManager().registerEvents(new EntityDamageListener(), SkyWarsFFA.getInstance());
        Bukkit.getPluginManager().registerEvents(new EntityExplodeListener(), SkyWarsFFA.getInstance());
        Bukkit.getPluginManager().registerEvents(new EntityChangeBlockListener(), SkyWarsFFA.getInstance());
        Bukkit.getPluginManager().registerEvents(new FoodLevelChangeListener(), SkyWarsFFA.getInstance());
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), SkyWarsFFA.getInstance());
        Bukkit.getPluginManager().registerEvents(new NickNameUpdateListener(), SkyWarsFFA.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerDropItemListener(), SkyWarsFFA.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), SkyWarsFFA.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), SkyWarsFFA.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerRespawnListener(), SkyWarsFFA.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(), SkyWarsFFA.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerBedEnterListener(), SkyWarsFFA.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(), SkyWarsFFA.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), SkyWarsFFA.getInstance());
        Bukkit.getPluginManager().registerEvents(new ProjectileHitListener(), SkyWarsFFA.getInstance());
        Bukkit.getPluginManager().registerEvents(new ServerListPingListener(), SkyWarsFFA.getInstance());
        Bukkit.getPluginManager().registerEvents(new WeatherChangeListener(), SkyWarsFFA.getInstance());
    }
}

package de.pxscxl.spigot.qsg.utils.registration;

import de.pxscxl.spigot.qsg.QSG;
import de.pxscxl.spigot.qsg.listener.*;
import org.bukkit.Bukkit;

public class EventRegistration {

    public void registerAllEvents() {
        Bukkit.getPluginManager().registerEvents(new AsyncPlayerChatListener(), QSG.getInstance());
        Bukkit.getPluginManager().registerEvents(new AsyncPlayerPreLoginListener(), QSG.getInstance());
        Bukkit.getPluginManager().registerEvents(new BlockBreakListener(), QSG.getInstance());
        Bukkit.getPluginManager().registerEvents(new BlockPlaceListener(), QSG.getInstance());
        Bukkit.getPluginManager().registerEvents(new CreatureSpawnListener(), QSG.getInstance());
        Bukkit.getPluginManager().registerEvents(new EntityDamageByEntityListener(), QSG.getInstance());
        Bukkit.getPluginManager().registerEvents(new EntityDamageListener(), QSG.getInstance());
        Bukkit.getPluginManager().registerEvents(new EntityExplodeListener(), QSG.getInstance());
        Bukkit.getPluginManager().registerEvents(new EntityChangeBlockListener(), QSG.getInstance());
        Bukkit.getPluginManager().registerEvents(new EntityShootBowListener(), QSG.getInstance());
        Bukkit.getPluginManager().registerEvents(new FoodLevelChangeListener(), QSG.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerArmorStandManipulateListener(), QSG.getInstance());
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), QSG.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerDropItemListener(), QSG.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), QSG.getInstance());
        Bukkit.getPluginManager().registerEvents(new NickNameUpdateListener(), QSG.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), QSG.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerRespawnListener(), QSG.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerLoginListener(), QSG.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(), QSG.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerBedEnterListener(), QSG.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(), QSG.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerFishListener(), QSG.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerPickupItemListener(), QSG.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), QSG.getInstance());
        Bukkit.getPluginManager().registerEvents(new ServerListPingListener(), QSG.getInstance());
        Bukkit.getPluginManager().registerEvents(new WeatherChangeListener(), QSG.getInstance());
    }
}

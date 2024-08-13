package de.pxscxl.spigot.mlgrush.utils.registration;

import de.pxscxl.spigot.mlgrush.MLGRush;
import de.pxscxl.spigot.mlgrush.listener.*;
import org.bukkit.Bukkit;

public class EventRegistration {

    public void registerAllEvents() {
        Bukkit.getPluginManager().registerEvents(new AsyncPlayerChatListener(), MLGRush.getInstance());
        Bukkit.getPluginManager().registerEvents(new AsyncPlayerPreLoginListener(), MLGRush.getInstance());
        Bukkit.getPluginManager().registerEvents(new BlockBreakListener(), MLGRush.getInstance());
        Bukkit.getPluginManager().registerEvents(new BlockBurnListener(), MLGRush.getInstance());
        Bukkit.getPluginManager().registerEvents(new BlockPlaceListener(), MLGRush.getInstance());
        Bukkit.getPluginManager().registerEvents(new CreatureSpawnListener(), MLGRush.getInstance());
        Bukkit.getPluginManager().registerEvents(new EntityDamageByBlockListener(), MLGRush.getInstance());
        Bukkit.getPluginManager().registerEvents(new EntityChangeBlockListener(), MLGRush.getInstance());
        Bukkit.getPluginManager().registerEvents(new EntityDamageByEntityListener(), MLGRush.getInstance());
        Bukkit.getPluginManager().registerEvents(new EntityDamageListener(), MLGRush.getInstance());
        Bukkit.getPluginManager().registerEvents(new EntityShootBowListener(), MLGRush.getInstance());
        Bukkit.getPluginManager().registerEvents(new FoodLevelChangeListener(), MLGRush.getInstance());
        Bukkit.getPluginManager().registerEvents(new NickNameUpdateListener(), MLGRush.getInstance());
        Bukkit.getPluginManager().registerEvents(new HangingBreakByEntityListener(), MLGRush.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerArmorStandManipulateListener(), MLGRush.getInstance());
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), MLGRush.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerItemConsumeListener(), MLGRush.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerChangedWorldListener(), MLGRush.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerBedEnterListener(), MLGRush.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(), MLGRush.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerDropItemListener(), MLGRush.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), MLGRush.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), MLGRush.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerLoginListener(), MLGRush.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(), MLGRush.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerPickupItemListener(), MLGRush.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), MLGRush.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerRespawnListener(), MLGRush.getInstance());
        Bukkit.getPluginManager().registerEvents(new ProjectileHitListener(), MLGRush.getInstance());
        Bukkit.getPluginManager().registerEvents(new ServerListPingListener(), MLGRush.getInstance());
        Bukkit.getPluginManager().registerEvents(new WeatherChangeListener(), MLGRush.getInstance());
        Bukkit.getPluginManager().registerEvents(new ItemSpawnListener(), MLGRush.getInstance());
    }
}

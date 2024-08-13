package de.pxscxl.spigot.swffa.manager;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.pxscxl.origin.spigot.Origin;
import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.inventory.InventoryBuilder;
import de.pxscxl.origin.spigot.api.inventory.ItemStackBuilder;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.origin.utils.session.Session;
import de.pxscxl.origin.utils.session.query.QueryStatement;
import de.pxscxl.origin.utils.session.query.UpdateStatement;
import de.pxscxl.spigot.swffa.SkyWarsFFA;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.IntStream;

public class KitManager implements Listener {

    @Getter
    private static KitManager instance;

    private final UpdateStatement create;
    private final UpdateStatement insert;
    private final QueryStatement exist;

    private final QueryStatement getInventory;
    private final UpdateStatement setInventory;

    @Getter
    private final HashMap<UUID, JsonObject> inventories = new HashMap<>();

    public KitManager() {
        instance = this;
        Bukkit.getPluginManager().registerEvents(this, SkyWarsFFA.getInstance());
        Session session = Origin.getInstance().getSession();

        create = session.prepareUpdateStatement("CREATE TABLE IF NOT EXISTS skywarsffa_inventory (uuid VARCHAR(36), inventory TEXT)");
        insert = session.prepareUpdateStatement("INSERT INTO skywarsffa_inventory (uuid, inventory) VALUES (?, ?)");
        exist = session.prepareQueryStatement("SELECT uuid FROM skywarsffa_inventory WHERE uuid = ?");

        getInventory = session.prepareQueryStatement("SELECT * FROM skywarsffa_inventory WHERE uuid = ?");
        setInventory = session.prepareUpdateStatement("UPDATE skywarsffa_inventory SET inventory = ? WHERE uuid = ?");

        create();
    }

    public void create() {
        try {
            create.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert(UUID uuid) {
        try {
            JsonObject object = new JsonObject();
            object.addProperty("DIAMOND_SWORD", 0);
            object.addProperty("FISHING_ROD", 1);
            object.addProperty("WEB", 2);
            object.addProperty("COBBLESTONE", 3);
            object.addProperty("GOLDEN_APPLE", 4);
            object.addProperty("POTION", 5);
            object.addProperty("ENDER_PEARL", 6);
            object.addProperty("SNOW_BALL", 7);

            insert.execute(uuid, object);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean exist(UUID uuid) {
        try {
            ResultSet resultSet = exist.execute(uuid);
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void loadInventory(UUID uuid) {
        try {
            ResultSet resultSet = getInventory.execute(uuid);
            if (resultSet.next()) {
                inventories.put(uuid, (JsonObject) new JsonParser().parse(resultSet.getString("inventory")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setInventory(UUID uuid, JsonObject object) {
        try {
            setInventory.execute(uuid, object);

            inventories.put(uuid, object);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setKitItems(OriginPlayer player) {
        player.getInventory().clear();

        player.getInventory().setHelmet(new ItemStackBuilder(Material.DIAMOND_HELMET).setUnbreakable(true).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
        player.getInventory().setChestplate(new ItemStackBuilder(Material.DIAMOND_CHESTPLATE).setUnbreakable(true).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
        player.getInventory().setLeggings(new ItemStackBuilder(Material.DIAMOND_LEGGINGS).setUnbreakable(true).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
        player.getInventory().setBoots(new ItemStackBuilder(Material.DIAMOND_BOOTS).setUnbreakable(true).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());

        Arrays.asList(
                new ItemStackBuilder(Material.DIAMOND_SWORD).setUnbreakable(true).enchant(Enchantment.DAMAGE_ALL, 2).build(),
                new ItemStackBuilder(Material.FISHING_ROD).setUnbreakable(true).build(),
                new ItemStackBuilder(Material.WEB).amount(32).build(),
                new ItemStackBuilder(Material.COBBLESTONE).amount(64).build(),
                new ItemStackBuilder(Material.GOLDEN_APPLE).amount(5).build(),
                new ItemStackBuilder(Material.POTION).setData((short) 16389).build(),
                new ItemStackBuilder(Material.ENDER_PEARL).amount(2).build(),
                new ItemStackBuilder(Material.SNOW_BALL).amount(16).build()
        ).forEach(itemStack -> player.getInventory().setItem(inventories.get(player.getUniqueId()).get(itemStack.getType().name()).getAsInt(), itemStack));
    }

    public void openGUI(OriginPlayer player) {
        InventoryBuilder inventory = new InventoryBuilder(1, "§8" + player.language("Inventar", "Inventory"));

        Arrays.asList(
                new ItemStackBuilder(Material.DIAMOND_SWORD).setUnbreakable(true).enchant(Enchantment.DAMAGE_ALL, 2).build(),
                new ItemStackBuilder(Material.FISHING_ROD).setUnbreakable(true).build(),
                new ItemStackBuilder(Material.WEB).amount(32).build(),
                new ItemStackBuilder(Material.COBBLESTONE).amount(64).build(),
                new ItemStackBuilder(Material.GOLDEN_APPLE).amount(5).build(),
                new ItemStackBuilder(Material.POTION).setData((short) 16389).build(),
                new ItemStackBuilder(Material.ENDER_PEARL).amount(2).build(),
                new ItemStackBuilder(Material.SNOW_BALL).amount(16).build()
        ).forEach(itemStack -> inventory.setItem(inventories.get(player.getUniqueId()).get(itemStack.getType().name()).getAsInt(), itemStack));

        player.openInventory(inventory);
        player.getInventory().clear();
        player.playSound(player.getLocation(), Sound.CLICK, 10F, 10F);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        OriginPlayer player = OriginManager.getInstance().getPlayer((Player) event.getWhoClicked());

        if (event.getView().getTitle().equals("§8" + player.language("Inventar", "Inventory"))) {
            if (event.getClickedInventory().getHolder() instanceof Player) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        OriginPlayer player = OriginManager.getInstance().getPlayer((Player) event.getPlayer());

        if (event.getView().getTitle().equals("§8" + player.language("Inventar", "Inventory"))) {
            List<ItemStack> contents = new ArrayList<>();
            for (ItemStack content : event.getInventory().getContents()) {
                if (content != null) contents.add(content);
            }
            if (contents.size() == 8) {
                player.sendMessage(
                        SkyWarsFFA.getInstance().getPrefix() + "§7Dein Inventar wurde §aerfolgreich §7gespeichert",
                        SkyWarsFFA.getInstance().getPrefix() + "§7Your inventory was §asuccessfully §7saved"
                );
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 10F, 10F);

                JsonObject object = new JsonObject();
                IntStream.range(0, 9).forEach(value -> {
                    ItemStack item = event.getInventory().getItem(value);
                    if (item != null) {
                        object.addProperty(item.getType().name(), value);
                    }
                });
                Bukkit.getScheduler().runTaskAsynchronously(SkyWarsFFA.getInstance(), () -> setInventory(player.getUniqueId(), object));
            } else {
                player.sendMessage(
                        SkyWarsFFA.getInstance().getPrefix() + "§7Est ist ein §cFehler §7beim Speichern aufgetreten",
                        SkyWarsFFA.getInstance().getPrefix() + "§7An §cerror §7occurred while saving"
                );
                player.playSound(player.getLocation(), Sound.VILLAGER_NO, 10F, 10F);
            }
            Bukkit.getScheduler().runTaskLater(SkyWarsFFA.getInstance(), () -> {
                player.getInventory().clear();
                GameManager.getInstance().setSpawnItems(player);
            }, 1);
        }
    }
}

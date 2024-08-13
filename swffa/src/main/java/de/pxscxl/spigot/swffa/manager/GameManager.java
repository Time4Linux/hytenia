package de.pxscxl.spigot.swffa.manager;

import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.inventory.ItemStackBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

@Getter
public class GameManager {

    @Getter
    private static GameManager instance;

    @Setter
    private int maxPlayers;

    @Setter
    private boolean teaming;
    @Setter
    private boolean mapChange;

    private final File file;
    private final YamlConfiguration yamlConfiguration;

    private final HashMap<OriginPlayer, OriginPlayer> targets = new HashMap<>();

    public GameManager() {
        instance = this;

        file = new File("plugins/SkyWarsFFA/settings.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        if (yamlConfiguration.get("Settings.teaming") == null) {
            yamlConfiguration.set("Settings.teaming", false);
            setTeaming(false);
        } else {
            setTeaming(yamlConfiguration.getBoolean("Settings.teaming"));
        }
        if (yamlConfiguration.get("Settings.mapChange") == null) {
            yamlConfiguration.set("Settings.mapChange", true);
            setMapChange(true);
        } else {
            setMapChange(yamlConfiguration.getBoolean("Settings.mapChange"));
        }
        if (yamlConfiguration.get("Settings.maxPlayers") == null) {
            yamlConfiguration.set("Settings.maxPlayers", 1);
            setMaxPlayers(1);
        } else {
            setMaxPlayers(yamlConfiguration.getInt("Settings.maxPlayers"));
        }
        try {
            yamlConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void prepareSpawn(OriginPlayer player) {
        player.setGameMode(GameMode.SURVIVAL);

        player.setLevel(0);
        player.setHealth(20);
        player.setFireTicks(0);
        player.setFoodLevel(20);

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);

        setSpawnItems(player);
    }

    public void setSpawnItems(OriginPlayer player) {
        player.getInventory().setItem(2, new ItemStackBuilder(Material.ENDER_CHEST).setDisplayName("§6" + player.language("Inventar", "Inventory") + " §8» §7" + player.language("Rechtsklick", "Right click")).build());
        player.getInventory().setItem(6, new ItemStackBuilder(Material.MAGMA_CREAM).setDisplayName("§6" + player.language("Verlassen", "Leave") + " §8» §7" + player.language("Rechtsklick", "Right click")).build());
    }
}

package de.pxscxl.spigot.lobby;

import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.inventory.ItemStackBuilder;
import de.pxscxl.origin.utils.enums.Rank;
import org.bukkit.Material;

public class Utils {

    public static void setSpawnItems(OriginPlayer player) {
        player.getInventory().setItem(0, new ItemStackBuilder(Material.COMPASS).setDisplayName("§6Teleporter §8» §7" + player.language("Rechtsklick", "Right click")).build());
        player.getInventory().setItem(1, new ItemStackBuilder(Material.LEGACY_SKULL_ITEM).setData((short) 3).setSkullOwner(player.getName()).setDisplayName("§6" + player.language("Freunde", "Friends") + " §8» §7" + player.language("Rechtsklick", "Right click")).build());

        if (player.hasPriorityAccess(Rank.JRYOUTUBER.getPriority())) {
            if (player.isNicked()) {
                player.getInventory().setItem(4, new ItemStackBuilder(Material.NAME_TAG).setGlow().setDisplayName("§aNickname §8» §7" + player.language("Rechtsklick", "Right click")).build());
            } else {
                player.getInventory().setItem(4, new ItemStackBuilder(Material.NAME_TAG).setDisplayName("§cNickname §8» §7" + player.language("Rechtsklick", "Right click")).build());
            }
        }

        player.getInventory().setItem(7, new ItemStackBuilder(Material.CLOCK).setDisplayName("§6" + player.language("Lobby wechseln", "Switch lobby") + " §8» §7" + player.language("Rechtsklick", "Right click")).build());
        player.getInventory().setItem(8, new ItemStackBuilder(Material.SHEARS).setDisplayName("§6" + player.language("Einstellungen", "Settings") + " §8» §7" + player.language("Rechtsklick", "Right click")).setUnbreakable(true).build());
    }
}

package de.pxscxl.spigot.lobby.manager;

import de.pxscxl.cloud.CloudAPI;
import de.pxscxl.cloud.CloudServer;
import de.pxscxl.cloud.manager.ServerManager;
import de.pxscxl.origin.spigot.Origin;
import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.inventory.InventoryBuilder;
import de.pxscxl.origin.spigot.api.inventory.ItemStackBuilder;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.spigot.lobby.LobbySystem;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class LobbyManager implements Listener {

    @Getter
    private static LobbyManager instance;

    public LobbyManager() {
        instance = this;
        Bukkit.getPluginManager().registerEvents(this, LobbySystem.getInstance());
    }

    public boolean isSilentLobby() {
        return CloudAPI.getInstance().getLocalServer().getName().toLowerCase().contains("silent");
    }

    public void openGUI(OriginPlayer player) {
        InventoryBuilder inventory = new InventoryBuilder(4, player.language("§8Lobby wechseln", "Switch lobby"));

        inventory.setItem(1, 1, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());
        inventory.setItem(1, 9, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());

        inventory.setItem(2, 1, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());
        inventory.setItem(2, 9, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());

        inventory.setItem(3, 1, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());
        inventory.setItem(3, 9, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());

        inventory.setItem(4, 1, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());
        inventory.setItem(4, 9, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());

        Bukkit.getScheduler().runTaskAsynchronously(LobbySystem.getInstance(), () -> {
            AtomicInteger slot = new AtomicInteger(3);
            ServerManager.getInstance().getServers().stream().filter(server -> server.getName().toLowerCase().startsWith("lobby-")).forEach(server -> {
                if (slot.get() <= 7) {
                    inventory.setItem(2, slot.getAndIncrement(), new ItemStackBuilder(Material.LEGACY_INK_SACK).setData((short) (server.getServerInfo().getPlayers() < server.getServerInfo().getMaxPlayers() ? 10 : 8)).setDisplayName("§8» §7" + server.getName())
                            .addLoreLine("")
                            .addLoreLine("§8● §e" + server.getServerInfo().getPlayers() + " §7" + (server.getServerInfo().getPlayers() == 1 ? player.language("Spieler", "Player") : player.language("Spieler", "Players")) + " online...")
                            .addLoreLine("")
                            .addLoreLine(Objects.equals(server.getName(), CloudAPI.getInstance().getLocalServer().getName()) ? "§8× §7" + player.language("§7Du bist §ebereits §7hier!", "§7You are §ealready §7here!") : "§e" + player.language("Betreten", "Join") + " §8» §7" + player.language("Rechtsklick", "Right click"))
                            .build());
                }
            });
        });

        player.openInventory(inventory);
        player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 10, 3);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        OriginPlayer player = OriginManager.getInstance().getPlayer((Player) event.getWhoClicked());

        if (event.getView() == null || event.getInventory() == null || event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;

        if (event.getView().getTitle().equals(player.language("§8Lobby wechseln", "Switch lobby"))) {
            event.setCancelled(true);

            if (event.getCurrentItem().getType().equals(Material.LEGACY_INK_SACK) && event.getCurrentItem().getDurability() != 15) {
                CloudServer server = CloudAPI.getInstance().getServers().stream().filter(servers -> Objects.equals(servers.getName(), ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).replace("» ", ""))).findFirst().orElse(null);
                if (server != null) {
                    if (!player.getServer().equalsIgnoreCase(server.getName())) {
                        player.sendMessage(
                                Origin.getInstance().getPrefix() + "§7Du wirst mit §e" + server.getName() + " §7verbunden",
                                Origin.getInstance().getPrefix() + "§7You will be connected with §e" + server.getName()
                        );
                        player.send(server.getName());
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10F, 10F);
                    } else {
                        player.sendMessage(
                                Origin.getInstance().getPrefix() + "§7Du bist §ebereits §7auf diesem Server",
                                Origin.getInstance().getPrefix() + "§7You are §ealready §7on this server"
                        );
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10F, 10F);
                        player.closeInventory();
                    }
                } else {
                    player.sendMessage(
                            Origin.getInstance().getPrefix() + "§7Dieser Server wurde §cnicht §7gefunden",
                            Origin.getInstance().getPrefix() + "§7This server was §cnot §7found"
                    );
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10F, 10F);
                }
                player.closeInventory();
            }
        }
    }

    @Getter
    private enum LobbyType {

        NORMAL("lobby"),
        PREMIUM("premiumlobby"),
        SILENT("silentlobby");

        final String cloudPattern;

        LobbyType(String cloudPattern) {
            this.cloudPattern = cloudPattern;
        }
    }
}

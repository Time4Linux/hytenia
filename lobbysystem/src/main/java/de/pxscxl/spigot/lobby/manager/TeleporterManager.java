package de.pxscxl.spigot.lobby.manager;

import de.pxscxl.cloud.CloudAPI;
import de.pxscxl.cloud.CloudServer;
import de.pxscxl.cloud.State;
import de.pxscxl.cloud.manager.ServerManager;
import de.pxscxl.origin.spigot.Origin;
import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.inventory.InventoryBuilder;
import de.pxscxl.origin.spigot.api.inventory.ItemStackBuilder;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.spigot.lobby.LobbySystem;
import de.pxscxl.spigot.lobby.utils.enums.Locations;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TeleporterManager implements Listener {

    @Getter
    private static TeleporterManager instance;

    public TeleporterManager() {
        instance = this;
        Bukkit.getPluginManager().registerEvents(this, LobbySystem.getInstance());
    }

    public void openGUI(OriginPlayer player) {
        InventoryBuilder inventory = new InventoryBuilder(5, "§8Navigator");

        inventory.fill(new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName());

        inventory.setItem(2, 4, buildItem(player, "SKYWARSFFA", "skywarsffa", Material.IRON_SWORD, true));
        inventory.setItem(2, 6, buildItem(player, "QSG", "qsg", Material.IRON_CHESTPLATE, true));

        inventory.setItem(3, 3, buildItem(player, "MLGRUSH", "mlgrush", Material.IRON_PICKAXE, true));
        inventory.setItem(3, 5, buildItem(player, "SPAWN", "lobby", Material.MAGMA_CREAM, false));
        inventory.setItem(3, 7, buildItem(player, "SKYWARS", "skywars2x1", Material.GRASS, true));

        inventory.setItem(4, 4, buildItem(player, "WATERFIGHT", "waterfight", Material.WATER_BUCKET, true));
        inventory.setItem(4, 6, buildItem(player, "BUILDFFA", "buildffa", Material.SANDSTONE, true));

        player.openInventory(inventory);
        player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 10, 3);
    }

    private ItemStack buildItem(OriginPlayer player, String name, String cloudPattern, Material material, boolean game) {
        AtomicInteger count = new AtomicInteger();
        ServerManager.getInstance().getServers().stream().filter(server -> server.getName().toLowerCase().contains(cloudPattern)).forEach(server -> count.set(count.get() + server.getServerInfo().getPlayers()));

        return new ItemStackBuilder(material).setDisplayName("§f§l" + name)
                .addLoreLine("")
                .addLoreLine("§8● §e" + count + " §7" + (count.get() == 1 ? player.language("Spieler", "Player") : player.language("Spieler", "Players")) + " online...")
                .addLoreLine("")
                .addLoreLine(game ? "§eServer " + player.language("Auswahl", "selection") + " §8» §7" + player.language("Rechtsklick", "Right click") : "§e" + player.language("Teleportieren", "Teleport") + " §8» §7" + player.language("Rechtsklick", "Right click"))
                .build();
    }

    private void openServers(OriginPlayer player, Gamemode gamemode) {
        InventoryBuilder inventory = new InventoryBuilder(5, player.language("§8Server", "§8Servers"));

        IntStream.range(1, 10).forEach(slot -> inventory.setItem(1, slot, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build()));
        IntStream.range(1, 10).forEach(slot -> inventory.setItem(5, slot, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build()));

        inventory.setItem(2, 1, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());
        inventory.setItem(2, 9, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());

        inventory.setItem(3, 1, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());
        inventory.setItem(3, 9, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());

        inventory.setItem(4, 1, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());
        inventory.setItem(4, 9, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());

        inventory.setItem(5, 1, new ItemStackBuilder(ItemStackBuilder.getSkull("https://textures.minecraft.net/texture/bb0f6e8af46ac6faf88914191ab66f261d6726a7999c637cf2e4159fe1fc477", "§7" + player.language("Zurück", "Back"))).build());

        if (gamemode == Gamemode.SKYWARS) {
            Bukkit.getScheduler().runTaskAsynchronously(LobbySystem.getInstance(), () -> {
                if (getServers(gamemode).isEmpty()) {
                    inventory.setItem(3, 5, new ItemStackBuilder(Material.LEGACY_STAINED_CLAY).setData((short) 14).setDisplayName(player.language("§cEs sind keine Server verfügbar!", "§cThere are no servers available!")).build());
                    return;
                }

                CloudAPI.getInstance().getServers().stream().filter(server -> server.getName().toLowerCase().startsWith("skywars2x1")).filter(server -> server.getState() == State.LOBBY).forEach(server -> inventory.addItem(new ItemStackBuilder(Material.LEGACY_STAINED_CLAY).setData((short) 5).setDisplayName("§f§l" + server.getName())
                        .addLoreLine("")
                        .addLoreLine("§8● §7" + player.language("Spieler", "Players") + "§8: §e" + server.getServerInfo().getPlayers() + "§8/§e" + server.getServerInfo().getMaxPlayers())
                        .addLoreLine("§8● §7Map§8: §e" + server.getServerInfo().getMap())
                        .addLoreLine("")
                        .addLoreLine("§e" + player.language("Betreten", "Join") + " §8» §7" + player.language("Rechtsklick", "Right click"))
                        .build()
                ));

                CloudAPI.getInstance().getServers().stream().filter(server -> server.getName().toLowerCase().startsWith("skywars4x1")).filter(server -> server.getState() == State.LOBBY).forEach(server -> inventory.addItem(new ItemStackBuilder(Material.LEGACY_STAINED_CLAY).setData((short) 5).setDisplayName("§f§l" + server.getName())
                        .addLoreLine("")
                        .addLoreLine("§8● §7" + player.language("Spieler", "Players") + "§8: §e" + server.getServerInfo().getPlayers() + "§8/§e" + server.getServerInfo().getMaxPlayers())
                        .addLoreLine("§8● §7Map§8: §e" + server.getServerInfo().getMap())
                        .addLoreLine("")
                        .addLoreLine("§e" + player.language("Betreten", "Join") + " §8» §7" + player.language("Rechtsklick", "Right click"))
                        .build()
                ));

                CloudAPI.getInstance().getServers().stream().filter(server -> server.getName().toLowerCase().startsWith("skywars8x1")).filter(server -> server.getState() == State.LOBBY).forEach(server -> inventory.addItem(new ItemStackBuilder(Material.LEGACY_STAINED_CLAY).setData((short) 5).setDisplayName("§f§l" + server.getName())
                        .addLoreLine("")
                        .addLoreLine("§8● §7" + player.language("Spieler", "Players") + "§8: §e" + server.getServerInfo().getPlayers() + "§8/§e" + server.getServerInfo().getMaxPlayers())
                        .addLoreLine("§8● §7Map§8: §e" + server.getServerInfo().getMap())
                        .addLoreLine("")
                        .addLoreLine("§e" + player.language("Betreten", "Join") + " §8» §7" + player.language("Rechtsklick", "Right click"))
                        .build()
                ));
            });

            player.openInventory(inventory);
            player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 10, 3);
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(LobbySystem.getInstance(), () -> {
            if (getServers(gamemode).isEmpty()) {
                inventory.setItem(3, 5, new ItemStackBuilder(Material.LEGACY_STAINED_CLAY).setData((short) 14).setDisplayName(player.language("§cEs sind keine Server verfügbar!", "§cThere are no servers available!")).build());
                return;
            }

            CloudAPI.getInstance().getServers().stream().filter(server -> server.getName().toLowerCase().startsWith(gamemode.getName().toLowerCase())).filter(server -> server.getState() == State.LOBBY).forEach(server -> inventory.addItem(new ItemStackBuilder(Material.LEGACY_STAINED_CLAY).setData((short) 5).setDisplayName("§f§l" + server.getName())
                    .addLoreLine("")
                    .addLoreLine("§8● §7" + player.language("Spieler", "Players") + "§8: §e" + server.getServerInfo().getPlayers() + "§8/§e" + server.getServerInfo().getMaxPlayers())
                    .addLoreLine("§8● §7Map§8: §e" + server.getServerInfo().getMap())
                    .addLoreLine("")
                    .addLoreLine("§e" + player.language("Betreten", "Join") + " §8» §7" + player.language("Rechtsklick", "Right click"))
                    .build()
            ));
        });

        player.openInventory(inventory);
        player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 10, 3);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        OriginPlayer player = OriginManager.getInstance().getPlayer((Player) event.getWhoClicked());

        if (event.getView() == null || event.getInventory() == null || event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;

        if (event.getView().getTitle().startsWith("§8Navigator")) {
            event.setCancelled(true);

            if (event.getCurrentItem().getItemMeta().getDisplayName().equals("§f§lSPAWN")) {
                player.teleport(Locations.SPAWN.getLocation());
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10F, 10F);
            }
            Arrays.stream(Gamemode.values()).filter(gamemode -> Objects.equals(gamemode.getName(), ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()))).findFirst().ifPresent(game -> openServers(player, game));
        } else if (event.getView().getTitle().equals(player.language("§8Server", "§8Servers"))) {
            if (event.getCurrentItem().getType().equals(Material.LEGACY_STAINED_CLAY) && event.getCurrentItem().getDurability() != 14) {
                CloudServer server = CloudAPI.getInstance().getServers().stream().filter(servers -> Objects.equals(servers.getName(), ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()))).findFirst().orElse(null);
                if (server != null) {
                    player.sendMessage(
                            Origin.getInstance().getPrefix() + "§7Du wirst mit §e" + server.getName() + " §7verbunden",
                            Origin.getInstance().getPrefix() + "§7You will be connected with §e" + server.getName()
                    );
                    player.send(server.getName());
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10F, 10F);
                } else {
                    player.sendMessage(
                            Origin.getInstance().getPrefix() + "§7Dieser Server wurde §cnicht §7gefunden",
                            Origin.getInstance().getPrefix() + "§7This server was §cnot §7found"
                    );
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10F, 10F);
                }
                player.closeInventory();
            } else if (event.getCurrentItem().getType().equals(Material.LEGACY_SKULL_ITEM)) {
                openGUI(player);
            }
        }
    }

    private List<CloudServer> getServers(Gamemode gamemode) {
        List<CloudServer> servers = new ArrayList<>();
        if (gamemode == Gamemode.SKYWARS) {
            servers.addAll(CloudAPI.getInstance().getServers().stream().filter(server -> server.getName().toLowerCase().startsWith("skywars8x1")).filter(server -> server.getState() == State.LOBBY).collect(Collectors.toList()));
            servers.addAll(CloudAPI.getInstance().getServers().stream().filter(server -> server.getName().toLowerCase().startsWith("skywars4x1")).filter(server -> server.getState() == State.LOBBY).collect(Collectors.toList()));
            servers.addAll(CloudAPI.getInstance().getServers().stream().filter(server -> server.getName().toLowerCase().startsWith("skywars2x1")).filter(server -> server.getState() == State.LOBBY).collect(Collectors.toList()));
            return servers;
        }
        servers.addAll(CloudAPI.getInstance().getServers().stream().filter(server -> server.getName().toLowerCase().startsWith(gamemode.getName().toLowerCase())).filter(server -> server.getState() == State.LOBBY).collect(Collectors.toList()));
        return servers;
    }


    @Getter
    private enum Gamemode {

        BUILDFFA("BUILDFFA"),
        MLGRUSH("MLGRUSH"),
        QSG("QSG"),
        SKYWARS("SKYWARS"),
        SKYWARSFFA("SKYWARSFFA"),
        WATERFIGHT("WATERFIGHT");

        final String name;

        Gamemode(String name) {
            this.name = name;
        }
    }
}

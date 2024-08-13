package de.pxscxl.spigot.lobby.manager;

import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.inventory.InventoryBuilder;
import de.pxscxl.origin.spigot.api.inventory.ItemStackBuilder;
import de.pxscxl.origin.spigot.api.manager.ClanManager;
import de.pxscxl.origin.spigot.api.manager.FriendManager;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.origin.utils.enums.Language;
import de.pxscxl.origin.utils.objects.clans.ClanSettings;
import de.pxscxl.origin.utils.objects.friends.FriendSettings;
import de.pxscxl.spigot.lobby.LobbySystem;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class SettingsManager implements Listener {

    @Getter
    private static SettingsManager instance;

    public SettingsManager() {
        instance = this;
        Bukkit.getPluginManager().registerEvents(this, LobbySystem.getInstance());
    }

    public void openGUI(OriginPlayer player) {
        InventoryBuilder inventory = new InventoryBuilder(3, "§8" + player.language("Einstellungen", "Settings"));

        inventory.fill(new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName());

        inventory.setItem(2, 2, new ItemStackBuilder(Material.COMPASS).setDisplayName(player.language("§f§lLOBBY", "§f§lLOBBY"))
                .addLoreLine("")
                .addLoreLine("§e" + player.language("Öffnen", "Open") + " §8» §7" + player.language("Rechtsklick", "Right click"))
                .build());

        inventory.setItem(2, 4, new ItemStackBuilder(Material.LEGACY_SKULL_ITEM).setData((short) 3).setSkullOwner(player.getName()).setDisplayName(player.language("§f§lFREUNDE", "§f§lFRIENDS"))
                .addLoreLine("")
                .addLoreLine("§e" + player.language("Öffnen", "Open") + " §8» §7" + player.language("Rechtsklick", "Right click"))
                .build());

        inventory.setItem(2, 6, new ItemStackBuilder(Material.IRON_CHESTPLATE).setDisplayName("§f§lCLAN")
                .addLoreLine("")
                .addLoreLine("§e" + player.language("Öffnen", "Open") + " §8» §7" + player.language("Rechtsklick", "Right click"))
                .build());

        inventory.setItem(2, 8, new ItemStackBuilder(Material.LEGACY_BOOK_AND_QUILL).setDisplayName(player.language("§f§lSPRACHE", "§f§lLANGUAGE"))
                .addLoreLine("")
                .addLoreLine("§e" + player.language("Öffnen", "Open") + " §8» §7" + player.language("Rechtsklick", "Right click"))
                .build());

        player.openInventory(inventory);
        player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 10, 3);
    }

    public void openSubSettingsGUI(OriginPlayer player, String setting) {
        String title;
        switch (setting) {
            case "lobby":
                title = "Lobby";
                break;
            case "friends":
                title = player.language("Freunde", "Friends");
                break;
            case "clan":
                title = "Clan";
                break;
            case "language":
                title = player.language("Sprache", "Language");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + setting);
        }
        InventoryBuilder inventory = new InventoryBuilder(5, "§8" + player.language("Einstellungen", "Settings") + " ┃ " + title);

        inventory.setItem(1, 1, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());
        inventory.setItem(1, 9, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());

        inventory.setItem(2, 1, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());
        inventory.setItem(2, 9, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());

        inventory.setItem(3, 1, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());
        inventory.setItem(3, 9, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());

        inventory.setItem(4, 1, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());
        inventory.setItem(4, 9, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());

        inventory.setItem(5, 1, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());
        inventory.setItem(5, 9, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());

        switch (setting) {
            case "lobby":
                Bukkit.getScheduler().runTaskAsynchronously(LobbySystem.getInstance(), () -> {
                    inventory.setItem(2, 5, new ItemStackBuilder(Material.ENDER_EYE).setDisplayName(player.language("§f§lSICHTBARKEIT", "§f§lVISIBILITY")).build());
                    switch (HiderManager.getInstance().getHideMode(player)) {
                        case 0:
                            inventory.setItem(3, 5, new ItemStackBuilder(Material.LEGACY_WOOL).setData((short) 13).setDisplayName(player.language("§aAlle Spieler sichtbar", "§aAll players visible")).build());
                            break;
                        case 1:
                            inventory.setItem(3, 5, new ItemStackBuilder(Material.LEGACY_WOOL).setData((short) 10).setDisplayName(player.language("§5Nur VIP's sichtbar", "§5Only VIP's visible")).build());
                            break;
                        case 2:
                            inventory.setItem(3, 5, new ItemStackBuilder(Material.LEGACY_WOOL).setData((short) 7).setDisplayName(player.language("§7Alle Spieler unsichtbar", "§7All players invisible")).build());
                            break;
                    }
                });
                break;
            case "friends":
                Bukkit.getScheduler().runTaskAsynchronously(LobbySystem.getInstance(), () -> {
                    inventory.setItem(2, 3, new ItemStackBuilder(Material.PAPER).setDisplayName(player.language("§f§lNACHRICHTEN", "§f§lMESSAGES")).build());
                    inventory.setItem(2, 5, new ItemStackBuilder(Material.MOVING_PISTON).setDisplayName(player.language("§f§lONLINE§f-§lSTATUS", "§f§lONLINE§f-§lSTATE")).build());
                    inventory.setItem(2, 7, new ItemStackBuilder(Material.LEATHER_BOOTS).setColor(Color.BLUE).setDisplayName(player.language("§f§lANFRAGEN", "§f§lREQUESTS")).build());

                    FriendSettings settings = FriendManager.getInstance().getSettings(player.getUniqueId());
                    inventory.setItem(3, 3, new ItemStackBuilder(Material.LEGACY_WOOL).setData((short) (settings.isReceivingMessage() ? 13 : 7)).setDisplayName(settings.isReceivingMessage() ? player.language("§aAktiviert", "§aActivated") : player.language("§7Deaktiviert", "§7Deactivated")).build());
                    inventory.setItem(3, 5, new ItemStackBuilder(Material.LEGACY_WOOL).setData((short) (settings.isDisplayedAsOnline() ? 13 : 7)).setDisplayName(settings.isDisplayedAsOnline() ? player.language("§aAktiviert", "§aActivated") : player.language("§7Deaktiviert", "§7Deactivated")).build());
                    inventory.setItem(3, 7, new ItemStackBuilder(Material.LEGACY_WOOL).setData((short) (settings.isReceivingRequests() ? 13 : 7)).setDisplayName(settings.isReceivingRequests() ? player.language("§aAktiviert", "§aActivated") : player.language("§7Deaktiviert", "§7Deactivated")).build());
                });
                break;
            case "clan":
                Bukkit.getScheduler().runTaskAsynchronously(LobbySystem.getInstance(), () -> {
                    inventory.setItem(2, 3, new ItemStackBuilder(Material.PAPER).setDisplayName(player.language("§f§lNACHRICHTEN", "§f§lMESSAGES")).build());
                    inventory.setItem(2, 5, new ItemStackBuilder(Material.MOVING_PISTON).setDisplayName(player.language("§f§lONLINE§f-§lSTATUS", "§f§lONLINE§f-§lSTATE")).build());
                    inventory.setItem(2, 7, new ItemStackBuilder(Material.LEATHER_BOOTS).setColor(Color.BLUE).setDisplayName(player.language("§f§lANFRAGEN", "§f§lREQUESTS")).build());

                    ClanSettings settings = ClanManager.getInstance().getSettings(player.getUniqueId());
                    inventory.setItem(3, 3, new ItemStackBuilder(Material.LEGACY_WOOL).setData((short) (settings.isReceivingMessage() ? 13 : 7)).setDisplayName(settings.isReceivingMessage() ? player.language("§aAktiviert", "§aActivated") : player.language("§7Deaktiviert", "§7Deactivated")).build());
                    inventory.setItem(3, 5, new ItemStackBuilder(Material.LEGACY_WOOL).setData((short) (settings.isDisplayedAsOnline() ? 13 : 7)).setDisplayName(settings.isDisplayedAsOnline() ? player.language("§aAktiviert", "§aActivated") : player.language("§7Deaktiviert", "§7Deactivated")).build());
                    inventory.setItem(3, 7, new ItemStackBuilder(Material.LEGACY_WOOL).setData((short) (settings.isReceivingRequests() ? 13 : 7)).setDisplayName(settings.isReceivingRequests() ? player.language("§aAktiviert", "§aActivated") : player.language("§7Deaktiviert", "§7Deactivated")).build());
                });
                break;
            case "language":
                inventory.setItem(2, 5, new ItemStackBuilder(Material.LEGACY_BOOK_AND_QUILL).setDisplayName(player.language("§f§lSPRACHE", "§f§lLANGUAGE")).build());
                inventory.setItem(3, 5, new ItemStackBuilder(ItemStackBuilder.getSkull(
                        player.language(
                                "https://textures.minecraft.net/texture/5e7899b4806858697e283f084d9173fe487886453774626b24bd8cfecc77b3f",
                                "https://textures.minecraft.net/texture/879d99d9c46474e2713a7e84a95e4ce7e8ff8ea4d164413a592e4435d2c6f9dc"
                        ),
                        player.language(
                                "§eDeutsch",
                                "§eEnglish"
                        )
                )).build());
                break;
        }

        inventory.setItem(5, 5, new ItemStackBuilder(ItemStackBuilder.getSkull("https://textures.minecraft.net/texture/bb0f6e8af46ac6faf88914191ab66f261d6726a7999c637cf2e4159fe1fc477", "§7" + player.language("Zurück", "Back"))).build());

        player.openInventory(inventory);
        player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 10, 3);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        OriginPlayer player = OriginManager.getInstance().getPlayer((Player) event.getWhoClicked());

        if (event.getView() == null || event.getInventory() == null || event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;

        InventoryBuilder inventory = (InventoryBuilder) player.openedInventory;

        if (event.getView().getTitle().contains("§8" + player.language("Einstellungen", "Settings"))) {
            switch (event.getView().getTitle()) {
                case "§8Einstellungen":
                case "§8Settings":
                    switch (event.getSlot()) {
                        case 10:
                            openSubSettingsGUI(player, "lobby");
                            break;
                        case 12:
                            openSubSettingsGUI(player, "friends");
                            break;
                        case 14:
                            openSubSettingsGUI(player, "clan");
                            break;
                        case 16:
                            openSubSettingsGUI(player, "language");
                            break;
                    }
                    break;
                case "§8Einstellungen ┃ Lobby":
                case "§8Settings ┃ Lobby":
                    if (event.getSlot() == 22) {
                        int value = HiderManager.getInstance().getHideMode(player) + 1;
                        if (value > 2) value = 0;
                        HiderManager.getInstance().setHideMode(player, value);

                        switch (value) {
                            case 0:
                                inventory.setItem(3, 5, new ItemStackBuilder(Material.LEGACY_WOOL).setData((short) 13).setDisplayName(player.language("§aAlle Spieler sichtbar", "§aAll players visible")).build());
                                break;
                            case 1:
                                inventory.setItem(3, 5, new ItemStackBuilder(Material.LEGACY_WOOL).setData((short) 10).setDisplayName(player.language("§5Nur VIP's sichtbar", "§5Only VIP's visible")).build());
                                break;
                            case 2:
                                inventory.setItem(3, 5, new ItemStackBuilder(Material.LEGACY_WOOL).setData((short) 7).setDisplayName(player.language("§7Alle Spieler unsichtbar", "§7All players invisible")).build());
                                break;
                        }
                        HiderManager.getInstance().hidePlayers(player);
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10F, 10F);
                    }
                    break;
                case "§8Einstellungen ┃ Freunde":
                case "§8Settings ┃ Friends":
                    switch (event.getSlot()) {
                        case 20:
                            Bukkit.getScheduler().runTaskAsynchronously(LobbySystem.getInstance(), () -> {
                                FriendSettings settings = FriendManager.getInstance().getSettings(player.getUniqueId());
                                settings.setReceivingMessage(event.getCurrentItem().getDurability() != 13);
                                FriendManager.getInstance().setSettings(player.getUniqueId(), settings);

                                inventory.setItem(3, 3, new ItemStackBuilder(Material.LEGACY_WOOL).setData((short) (settings.isReceivingMessage() ? 13 : 7)).setDisplayName(settings.isReceivingMessage() ? player.language("§aAktiviert", "§aActivated") : player.language("§7Deaktiviert", "§7Deactivated")).build());
                                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10F, 10F);
                            });
                            break;
                        case 22:
                            Bukkit.getScheduler().runTaskAsynchronously(LobbySystem.getInstance(), () -> {
                                FriendSettings settings = FriendManager.getInstance().getSettings(player.getUniqueId());
                                settings.setDisplayedAsOnline(event.getCurrentItem().getDurability() != 13);
                                FriendManager.getInstance().setSettings(player.getUniqueId(), settings);

                                inventory.setItem(3, 5, new ItemStackBuilder(Material.LEGACY_WOOL).setData((short) (settings.isDisplayedAsOnline() ? 13 : 7)).setDisplayName(settings.isDisplayedAsOnline() ? player.language("§aAktiviert", "§aActivated") : player.language("§7Deaktiviert", "§7Deactivated")).build());
                                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10F, 10F);
                            });
                            break;
                        case 24:
                            Bukkit.getScheduler().runTaskAsynchronously(LobbySystem.getInstance(), () -> {
                                FriendSettings settings = FriendManager.getInstance().getSettings(player.getUniqueId());
                                settings.setReceivingRequests(event.getCurrentItem().getDurability() != 13);
                                FriendManager.getInstance().setSettings(player.getUniqueId(), settings);

                                inventory.setItem(3, 7, new ItemStackBuilder(Material.LEGACY_WOOL).setData((short) (settings.isReceivingRequests() ? 13 : 7)).setDisplayName(settings.isReceivingRequests() ? player.language("§aAktiviert", "§aActivated") : player.language("§7Deaktiviert", "§7Deactivated")).build());
                                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10F, 10F);
                            });
                            break;
                    }
                    break;
                case "§8Einstellungen ┃ Clan":
                case "§8Settings ┃ Clan":
                    switch (event.getSlot()) {
                        case 20:
                            Bukkit.getScheduler().runTaskAsynchronously(LobbySystem.getInstance(), () -> {
                                ClanSettings settings = ClanManager.getInstance().getSettings(player.getUniqueId());
                                settings.setReceivingMessage(event.getCurrentItem().getDurability() != 13);
                                ClanManager.getInstance().setSettings(player.getUniqueId(), settings);

                                inventory.setItem(3, 3, new ItemStackBuilder(Material.LEGACY_WOOL).setData((short) (settings.isReceivingMessage() ? 13 : 7)).setDisplayName(settings.isReceivingMessage() ? player.language("§aAktiviert", "§aActivated") : player.language("§7Deaktiviert", "§7Deactivated")).build());
                                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10F, 10F);
                            });
                            break;
                        case 22:
                            Bukkit.getScheduler().runTaskAsynchronously(LobbySystem.getInstance(), () -> {
                                ClanSettings settings = ClanManager.getInstance().getSettings(player.getUniqueId());
                                settings.setDisplayedAsOnline(event.getCurrentItem().getDurability() != 13);
                                ClanManager.getInstance().setSettings(player.getUniqueId(), settings);

                                inventory.setItem(3, 5, new ItemStackBuilder(Material.LEGACY_WOOL).setData((short) (settings.isDisplayedAsOnline() ? 13 : 7)).setDisplayName(settings.isDisplayedAsOnline() ? player.language("§aAktiviert", "§aActivated") : player.language("§7Deaktiviert", "§7Deactivated")).build());
                                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10F, 10F);
                            });
                            break;
                        case 24:
                            Bukkit.getScheduler().runTaskAsynchronously(LobbySystem.getInstance(), () -> {
                                ClanSettings settings = ClanManager.getInstance().getSettings(player.getUniqueId());
                                settings.setReceivingRequests(event.getCurrentItem().getDurability() != 13);
                                ClanManager.getInstance().setSettings(player.getUniqueId(), settings);

                                inventory.setItem(3, 7, new ItemStackBuilder(Material.LEGACY_WOOL).setData((short) (settings.isReceivingRequests() ? 13 : 7)).setDisplayName(settings.isReceivingRequests() ? player.language("§aAktiviert", "§aActivated") : player.language("§7Deaktiviert", "§7Deactivated")).build());
                                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10F, 10F);
                            });
                            break;
                    }
                    break;
                case "§8Einstellungen ┃ Sprache":
                case "§8Settings ┃ Language":
                    if (event.getCurrentItem().getItemMeta().getDisplayName().equals("§eDeutsch")) {
                        Bukkit.getScheduler().runTaskAsynchronously(LobbySystem.getInstance(), () -> player.setLanguage(Language.ENGLISH));
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10F, 10F);
                        player.closeInventory();
                    } else if (event.getCurrentItem().getItemMeta().getDisplayName().equals("§eEnglish")) {
                        Bukkit.getScheduler().runTaskAsynchronously(LobbySystem.getInstance(), () -> player.setLanguage(Language.GERMAN));
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10F, 10F);
                        player.closeInventory();
                    }
                    break;
            }
            if (event.getCurrentItem().getItemMeta().getDisplayName().equals(player.language("§7Zurück", "§7Back"))) {
                openGUI(player);
            }
        }
    }
}

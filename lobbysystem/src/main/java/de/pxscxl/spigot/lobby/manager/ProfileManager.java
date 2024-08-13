package de.pxscxl.spigot.lobby.manager;

import de.pxscxl.cloud.CloudAPI;
import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.inventory.InventoryBuilder;
import de.pxscxl.origin.spigot.api.inventory.ItemStackBuilder;
import de.pxscxl.origin.spigot.api.manager.FriendManager;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.origin.utils.objects.friends.Friend;
import de.pxscxl.origin.utils.objects.friends.FriendRequest;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ProfileManager implements Listener {

    @Getter
    private static ProfileManager instance;

    public ProfileManager() {
        instance = this;
        Bukkit.getPluginManager().registerEvents(this, LobbySystem.getInstance());
    }

    public void openGUI(OriginPlayer player, int page) {
        InventoryBuilder inventory = new InventoryBuilder(6, "§8" + player.language("Freunde ┃ Seite ", "Friends ┃ Page ") + page);

        inventory.setItem(1, 1, new ItemStackBuilder(Material.ORANGE_STAINED_GLASS_PANE).setNoName().build());
        inventory.setItem(1, 2, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());
        inventory.setItem(2, 1, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());

        inventory.setItem(1, 8, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());
        inventory.setItem(1, 9, new ItemStackBuilder(Material.ORANGE_STAINED_GLASS_PANE).setNoName().build());
        inventory.setItem(2, 9, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());

        inventory.setItem(5, 1, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());
        inventory.setItem(6, 1, new ItemStackBuilder(Material.ORANGE_STAINED_GLASS_PANE).setNoName().build());
        inventory.setItem(6, 2, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());

        inventory.setItem(5, 9, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());
        inventory.setItem(6, 9, new ItemStackBuilder(Material.ORANGE_STAINED_GLASS_PANE).setNoName().build());
        inventory.setItem(6, 8, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());

        inventory.setItem(3, 1, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());
        inventory.setItem(4, 1, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());

        inventory.setItem(3, 9, new ItemStackBuilder(Material.LEGACY_SKULL_ITEM).setData((short) 3).setSkullOwner(player.getName()).setDisplayName(player.language("§e§lFREUNDE", "§e§lFRIENDS")).setGlow().build());
        inventory.setItem(4, 9, new ItemStackBuilder(Material.LEGACY_BOOK_AND_QUILL).setDisplayName(player.language("§e§lANFRAGEN", "§e§lREQUESTS")).build());

        inventory.setItem(6, 4, new ItemStackBuilder(ItemStackBuilder.getSkull("https://textures.minecraft.net/texture/bb0f6e8af46ac6faf88914191ab66f261d6726a7999c637cf2e4159fe1fc477", "§7" + player.language("Vorherige Seite", "Previous page"))).build());
        inventory.setItem(6, 5, new ItemStackBuilder(ItemStackBuilder.getSkull("https://textures.minecraft.net/texture/fc271052719ef64079ee8c1498951238a74dac4c27b95640db6fbddc2d6b5b6e", "§7" + page)).amount(page).build());
        inventory.setItem(6, 6, new ItemStackBuilder(ItemStackBuilder.getSkull("https://textures.minecraft.net/texture/f2f3a2dfce0c3dab7ee10db385e5229f1a39534a8ba2646178e37c4fa93b", "§7" + player.language("Nächste Seite", "Next page"))).build());

        Bukkit.getScheduler().runTaskAsynchronously(LobbySystem.getInstance(), () -> {
            if (!FriendManager.getInstance().getFriends(player.getUniqueId()).isEmpty()) {
                startAnimation(player, inventory, page);
            } else {
                inventory.setItem(3, 5, ItemStackBuilder.getSkull("http://textures.minecraft.net/texture/badc048a7ce78f7dad72a07da27d85c0916881e5522eeed1e3daf217a38c1a", player.language("§cDu hast keine Freunde!", "§cYou don't have any friends!")));
            }
        });

        player.openInventory(inventory);
        player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 10, 3);
    }

    public void openRequestsGUI(OriginPlayer player, int page) {
        InventoryBuilder inventory = new InventoryBuilder(6, "§8" + player.language("Anfragen ┃ Seite ", "Requests ┃ Page ") + page);

        inventory.setItem(1, 1, new ItemStackBuilder(Material.ORANGE_STAINED_GLASS_PANE).setNoName().build());
        inventory.setItem(1, 2, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());
        inventory.setItem(2, 1, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());

        inventory.setItem(1, 8, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());
        inventory.setItem(1, 9, new ItemStackBuilder(Material.ORANGE_STAINED_GLASS_PANE).setNoName().build());
        inventory.setItem(2, 9, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());

        inventory.setItem(5, 1, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());
        inventory.setItem(6, 1, new ItemStackBuilder(Material.ORANGE_STAINED_GLASS_PANE).setNoName().build());
        inventory.setItem(6, 2, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());

        inventory.setItem(5, 9, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());
        inventory.setItem(6, 9, new ItemStackBuilder(Material.ORANGE_STAINED_GLASS_PANE).setNoName().build());
        inventory.setItem(6, 8, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());

        inventory.setItem(3, 1, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());
        inventory.setItem(4, 1, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());

        inventory.setItem(3, 9, new ItemStackBuilder(Material.LEGACY_SKULL_ITEM).setData((short) 3).setSkullOwner(player.getName()).setDisplayName(player.language("§e§lFREUNDE", "§e§lFRIENDS")).build());
        inventory.setItem(4, 9, new ItemStackBuilder(Material.LEGACY_SKULL_ITEM).setDisplayName(player.language("§e§lANFRAGEN", "§e§lREQUESTS")).setGlow().build());

        inventory.setItem(6, 4, new ItemStackBuilder(ItemStackBuilder.getSkull("https://textures.minecraft.net/texture/bb0f6e8af46ac6faf88914191ab66f261d6726a7999c637cf2e4159fe1fc477", "§7" + player.language("Vorherige Seite", "Previous page"))).build());
        inventory.setItem(6, 5, new ItemStackBuilder(ItemStackBuilder.getSkull("https://textures.minecraft.net/texture/fc271052719ef64079ee8c1498951238a74dac4c27b95640db6fbddc2d6b5b6e", "§7" + page)).amount(page).build());
        inventory.setItem(6, 6, new ItemStackBuilder(ItemStackBuilder.getSkull("https://textures.minecraft.net/texture/f2f3a2dfce0c3dab7ee10db385e5229f1a39534a8ba2646178e37c4fa93b", "§7" + player.language("Nächste Seite", "Next page"))).build());

        Bukkit.getScheduler().runTaskAsynchronously(LobbySystem.getInstance(), () -> {
            if (!FriendManager.getInstance().getRequests(player.getUniqueId()).isEmpty()) {
                getRequests(player, page).forEach(friend -> {
                    OriginPlayer target = OriginManager.getInstance().getPlayer(friend.getUuid());
                    inventory.addItem(new ItemStackBuilder(Material.LEGACY_SKULL_ITEM).setData((short) 3).setSkullOwner(target.getName()).setDisplayName("§1§8" + target.getDisplayName())
                            .addLoreLine("")
                            .addLoreLine("§8● §7" + player.language("Linksklick", "Left click") + " §2» §a" + player.language("Annehmen", "Accept"))
                            .addLoreLine("§4§l▎ §7" + player.language("Rechtsklick", "Right click") + " §4» §c" + player.language("Ablehnen", "Deny"))
                            .build()
                    );
                });
            } else {
                inventory.setItem(3, 5, ItemStackBuilder.getSkull("http://textures.minecraft.net/texture/badc048a7ce78f7dad72a07da27d85c0916881e5522eeed1e3daf217a38c1a", player.language("§cDu hast keine Anfragen!", "§cYou don't have any requests!")));
            }
        });

        player.openInventory(inventory);
        player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 10, 3);
    }

    public void openFriendsManageGUI(OriginPlayer player, OriginPlayer target) {
        InventoryBuilder inventory = new InventoryBuilder(3, "§1§8" + player.language("Freunde ┃ ", "Friends ┃ ") + target.getName());

        inventory.setItem(2, target.isOnline() ? 4 : 5, new ItemStackBuilder(Material.BARRIER).setDisplayName(player.language("§cFreund entfernen", "§cRemove friend")).build());
        if (target.isOnline()) {
            inventory.setItem(2, 6, new ItemStackBuilder(Material.ENDER_PEARL).setDisplayName(player.language("§aNachspringen", "§aJump")).build());
        }

        inventory.setItem(3, 1, new ItemStackBuilder(ItemStackBuilder.getSkull("https://textures.minecraft.net/texture/bb0f6e8af46ac6faf88914191ab66f261d6726a7999c637cf2e4159fe1fc477", "§7" + player.language("Zurück", "Back"))).build());

        player.openInventory(inventory);
        player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 10, 3);
    }

    public List<Friend> getFriends(OriginPlayer player, int page) {
        List<Friend> friends = FriendManager.getInstance().getFriends(player.getUniqueId());
        int maxPage = 0;
        for (int i = 0; i < friends.size(); i += 28) {
            maxPage++;
        }
        if (page > maxPage) page = maxPage;
        friends.sort((o1, o2) -> {
            OriginPlayer o1Player = OriginManager.getInstance().getPlayer(o1.getUuid());
            OriginPlayer o2Player = OriginManager.getInstance().getPlayer(o2.getUuid());

            String s1 = o1Player.isOnline() ? "a" : "b" + o1Player.getRank().getId() + o1Player.getName();
            String s2 = o2Player.isOnline() ? "a" : "b" + o2Player.getRank().getId() + o2Player.getName();

            return s1.compareTo(s2);
        });
        List<Friend> list = new ArrayList<>();
        for (int i = page * 28 - 28; i < friends.size() && i < page * 28; i++) list.add(friends.get(i));
        return list;
    }

    public List<FriendRequest> getRequests(OriginPlayer player, int page) {
        List<FriendRequest> requests = FriendManager.getInstance().getRequests(player.getUniqueId());
        int maxPage = 0;
        for (int i = 0; i < requests.size(); i += 28) {
            maxPage++;
        }
        if (page > maxPage) page = maxPage;
        requests.sort((o1, o2) -> {
            OriginPlayer o1Player = OriginManager.getInstance().getPlayer(o1.getUuid());
            OriginPlayer o2Player = OriginManager.getInstance().getPlayer(o2.getUuid());

            String s1 = o1Player.isOnline() ? "a" : "b" + o1Player.getRank().getId() + o1Player.getName();
            String s2 = o2Player.isOnline() ? "a" : "b" + o2Player.getRank().getId() + o2Player.getName();

            return s1.compareTo(s2);
        });
        List<FriendRequest> list = new ArrayList<>();
        for (int i = page * 28 - 28; i < requests.size() && i < page * 28; i++) list.add(requests.get(i));
        return list;
    }

    private void startAnimation(OriginPlayer player, InventoryBuilder inventory, int page) {
        AtomicInteger integer = new AtomicInteger(1);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(LobbySystem.getInstance(), () -> {
            switch (integer.getAndIncrement()) {
                case 1:
                    inventory.setItem(3, 4, new ItemStackBuilder(Material.ORANGE_STAINED_GLASS_PANE).setDisplayName("§4§l▎ §cLoading...").build());
                    break;
                case 2:
                    inventory.setItem(3, 5, new ItemStackBuilder(Material.ORANGE_STAINED_GLASS_PANE).setDisplayName("§4§l▎ §cLoading...").build());
                    break;
                case 3:
                    inventory.setItem(3, 6, new ItemStackBuilder(Material.ORANGE_STAINED_GLASS_PANE).setDisplayName("§4§l▎ §cLoading...").build());
                    break;
                case 4:
                    inventory.clear(3);

                    inventory.setItem(3, 1, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());
                    inventory.setItem(3, 9, new ItemStackBuilder(Material.LEGACY_SKULL_ITEM).setData((short) 3).setSkullOwner(player.getName()).setDisplayName(player.language("§e§lFREUNDE", "§e§lFRIENDS")).build());

                    Bukkit.getScheduler().runTaskAsynchronously(LobbySystem.getInstance(), () -> {
                        AtomicInteger slot = new AtomicInteger(10);
                        getFriends(player, page).forEach(friend -> {
                            OriginPlayer target = OriginManager.getInstance().getPlayer(friend.getUuid());
                            inventory.setItem(slot.getAndIncrement(), new ItemStackBuilder(Material.LEGACY_SKULL_ITEM).setData((short) (target.isOnline() && friend.getSettings().isDisplayedAsOnline() ? 3 : 0)).setSkullOwner(target.getName()).setDisplayName("§1§8" + target.getDisplayName())
                                    .addLoreLine("")
                                    .addLoreLine(target.isOnline() && friend.getSettings().isDisplayedAsOnline() ? "§8● §aOnline §7" + player.language("auf", "on") + " §a" + target.getServer() : "§4§l▎ §cOffline")
                                    .build()
                            );
                        });
                    });
                    break;
            }
        }, 5, 6);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        OriginPlayer player = OriginManager.getInstance().getPlayer((Player) event.getWhoClicked());

        if (event.getView() == null || event.getInventory() == null || event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;

        switch (event.getCurrentItem().getItemMeta().getDisplayName()) {
            case "§e§lFREUNDE":
            case "§e§lFRIENDS":
                if (!event.getView().getTitle().contains(player.language("Freunde", "Friends"))) openGUI(player, 1);
                break;
            case "§e§lANFRAGEN":
            case "§e§lREQUESTS":
                if (!event.getView().getTitle().contains(player.language("Anfragen", "Requests"))) openRequestsGUI(player, 1);
                break;
        }

        if (event.getView().getTitle().startsWith("§8" + player.language("Freunde", "Friends"))) {
            switch (event.getCurrentItem().getItemMeta().getDisplayName()) {
                case "§7Vorherige Seite":
                case "§7Previous page":
                    int page = Integer.parseInt(ChatColor.stripColor(event.getView().getTitle().replace("§8" + player.language("Freunde ┃ Seite ", "Friends ┃ Page "), "")));
                    if (page != 1) {
                        openGUI(player, page - 1);
                    } else {
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10F, 10F);
                    }
                    break;
                case "§7Nächste Seite":
                case "§7Next page":
                    page = Integer.parseInt(ChatColor.stripColor(event.getView().getTitle().replace("§8" + player.language("Freunde ┃ Seite ", "Friends ┃ Page "), "")));
                    int maxPage = 0;
                    for (int i = 0; i < FriendManager.getInstance().getFriends(player.getUniqueId()).size(); i += 36) {
                        maxPage++;
                    }
                    if (page != maxPage && maxPage != 0) {
                        openGUI(player, page + 1);
                    } else {
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10F, 10F);
                    }
                    break;

            }
            if (event.getCurrentItem().getType().equals(Material.LEGACY_SKULL_ITEM) && event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§1§8")) {
                openFriendsManageGUI(player, OriginManager.getInstance().getPlayer(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName())));
            }
        } else if (event.getView().getTitle().startsWith("§8" + player.language("Anfragen ┃ Seite ", "Requests ┃ Page "))) {
            if (event.getCurrentItem().getType().equals(Material.LEGACY_SKULL_ITEM) && event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§1§8")) {
                OriginPlayer target = OriginManager.getInstance().getPlayer(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()));
                switch (event.getClick()) {
                    case LEFT:
                        player.sendMessage(
                                FriendManager.getInstance().getPrefix() + "§7Du hast die Anfrage von " + target.getDisplayName() + " §aangenommen",
                                FriendManager.getInstance().getPrefix() + "§7You have §aaccepted §7the request of " + target.getDisplayName()
                        );
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10F, 10F);
                        player.closeInventory();
                        Bukkit.getScheduler().runTaskAsynchronously(LobbySystem.getInstance(), () -> {
                            if (FriendManager.getInstance().getRequest(player.getUniqueId(), target.getUniqueId()) != null) {
                                FriendManager.getInstance().deleteRequest(player.getUniqueId(), target.getUniqueId());
                                FriendManager.getInstance().deleteRequest(target.getUniqueId(), player.getUniqueId());

                                long time = new Date().getTime();
                                FriendManager.getInstance().insertFriends(player.getUniqueId(), target.getUniqueId(), time);
                                FriendManager.getInstance().insertFriends(target.getUniqueId(), player.getUniqueId(), time);
                            }
                        });
                        break;
                    case RIGHT:
                        player.sendMessage(
                                FriendManager.getInstance().getPrefix() + "§7Du hast die Anfrage von " + target.getDisplayName() + " §cabgelehnt",
                                FriendManager.getInstance().getPrefix() + "§7You have §cdenied §7the request of " + target.getDisplayName()
                        );
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10F, 10F);
                        player.closeInventory();
                        Bukkit.getScheduler().runTaskAsynchronously(LobbySystem.getInstance(), () -> {
                            if (FriendManager.getInstance().getRequest(player.getUniqueId(), target.getUniqueId()) != null) {
                                FriendManager.getInstance().deleteRequest(player.getUniqueId(), target.getUniqueId());
                            }
                        });
                        break;
                }
            }
            switch (event.getCurrentItem().getItemMeta().getDisplayName()) {
                case "§7Vorherige Seite":
                case "§7Previous page":
                    int page = Integer.parseInt(ChatColor.stripColor(event.getView().getTitle().replace("§8" + player.language("Anfragen ┃ Seite ", "Requests ┃ Page "), "")));
                    if (page != 1) {
                        openRequestsGUI(player, page - 1);
                    } else {
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10F, 10F);
                    }
                    break;
                case "§7Nächste Seite":
                case "§7Next page":
                    page = Integer.parseInt(ChatColor.stripColor(event.getView().getTitle().replace("§8" + player.language("Anfragen ┃ Seite ", "Requests ┃ Page "), "")));
                    int maxPage = 0;
                    for (int i = 0; i < FriendManager.getInstance().getRequests(player.getUniqueId()).size(); i += 36) {
                        maxPage++;
                    }
                    if (page != maxPage && maxPage != 0) {
                        openRequestsGUI(player, page + 1);
                    } else {
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10F, 10F);
                    }
                    break;

            }
        } else if (event.getView().getTitle().startsWith("§1§8")) {
            OriginPlayer target = OriginManager.getInstance().getPlayer(ChatColor.stripColor(event.getView().getTitle().replace(player.language("Freunde ┃ ", "Friends ┃ "), "")));
            switch (event.getCurrentItem().getType()) {
                case BARRIER:
                    player.sendMessage(
                            FriendManager.getInstance().getPrefix() + "§7Die Freundschaft mit " + target.getDisplayName() + " §7wurde §caufgelöst",
                            FriendManager.getInstance().getPrefix() + "§7The friendship with " + target.getDisplayName() + " §7was §ccancelled"
                    );
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10F, 10F);
                    player.closeInventory();
                    Bukkit.getScheduler().runTaskAsynchronously(LobbySystem.getInstance(), () -> {
                        if (FriendManager.getInstance().getFriend(player.getUniqueId(), target.getUniqueId()) != null) {
                            FriendManager.getInstance().deleteFriend(player.getUniqueId(), target.getUniqueId());
                            FriendManager.getInstance().deleteFriend(target.getUniqueId(), player.getUniqueId());
                        }
                    });
                    break;
                case ENDER_PEARL:
                    if (target.isOnline()) {
                        if (CloudAPI.getInstance().getServer(target.getServer()) != null) {
                            player.sendMessage(
                                    FriendManager.getInstance().getPrefix() + "§7Du wirst auf den Server von " + target.getDisplayName() + " §7verbunden",
                                    FriendManager.getInstance().getPrefix() + "§7You will be connected to the server of " + target.getDisplayName()
                            );
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10F, 10F);
                            player.closeInventory();
                            player.send(target.getServer());
                        } else {
                            player.sendMessage(
                                    FriendManager.getInstance().getPrefix() + "§7Dieser Server wurde §cnicht §7gefunden!",
                                    FriendManager.getInstance().getPrefix() + "§7This server was §cnot §7found!"
                            );
                            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10F, 10F);
                            player.closeInventory();
                        }
                    } else {
                        player.sendMessage(
                                FriendManager.getInstance().getPrefix() + "§7Dieser Spieler wurde §cnicht §7gefunden!",
                                FriendManager.getInstance().getPrefix() + "§7This player was §cnot §7found!"
                        );
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10F, 10F);
                        player.closeInventory();
                    }
                    break;
                case LEGACY_SKULL_ITEM:
                    openGUI(player, 1);
                    break;
            }
        }
    }
}
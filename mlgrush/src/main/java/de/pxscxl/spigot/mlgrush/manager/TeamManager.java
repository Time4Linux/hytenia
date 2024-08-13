package de.pxscxl.spigot.mlgrush.manager;

import de.pxscxl.origin.spigot.api.OriginPlayer;
import de.pxscxl.origin.spigot.api.inventory.InventoryBuilder;
import de.pxscxl.origin.spigot.api.manager.OriginManager;
import de.pxscxl.origin.utils.enums.Language;
import de.pxscxl.spigot.mlgrush.MLGRush;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class TeamManager implements Listener {

    @Getter
    private static TeamManager instance;

    @Setter
    private int maxTeamSize = 1;

    private final List<Team> teams = new ArrayList<>();

    private final HashMap<OriginPlayer, Team> playerTeams = new HashMap<>();

    public TeamManager() {
        instance = this;

        teams.add(Team.RED);
        teams.add(Team.BLUE);

        Bukkit.getPluginManager().registerEvents(this, MLGRush.getInstance());
    }

    public void disable() {
        instance = null;
    }

    public void openGUI(OriginPlayer player) {
        InventoryBuilder inventory = new InventoryBuilder(1, "§8Teams");

        inventory.addItem(Team.RED.getItem(player));
        inventory.addItem(Team.BLUE.getItem(player));

        player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 10.0F, 10.0F);
        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        OriginPlayer player = OriginManager.getInstance().getPlayer((Player) event.getWhoClicked());

        if (event.getCurrentItem() == null || event.getCurrentItem().getItemMeta() == null || event.getCurrentItem().getType() == Material.LEGACY_STAINED_GLASS_PANE || event.getCurrentItem().getType() == Material.AIR) {
            return;
        }

        if (event.getView().getTitle().equals("§8Teams")) {
            if (event.getClick().equals(ClickType.RIGHT) || event.getClick().equals(ClickType.LEFT)) {
                if (event.getCurrentItem().getType().equals(Material.LEGACY_STAINED_CLAY)) {
                    Team team = getTeamFromShort(event.getCurrentItem().getDurability());
                    if (team != null) {
                        if (playerTeams.get(player) != team) {
                            if (team.getMembers().size() == maxTeamSize) {
                                player.closeInventory();
                                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10F, 10F);
                                player.sendMessage(
                                        MLGRush.getInstance().getPrefix() + "§7Dieses Team ist §ebereits §7voll",
                                        MLGRush.getInstance().getPrefix() + "§7This team is §ealready §7full");
                                return;
                            }
                            team.addPlayer(player);
                            ScoreboardManager.getInstance().updateTeamScore(player);
                            player.closeInventory();
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10F, 10F);
                            player.sendMessage(
                                    MLGRush.getInstance().getPrefix() + "§7Du hast das Team " + team.getColor() + team.getGermanName() + " §7betreten",
                                    MLGRush.getInstance().getPrefix() + "§7You joined the team " + team.getColor() + team.getEnglishName());
                        } else {
                            player.closeInventory();
                            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10F, 10F);
                            player.sendMessage(
                                    MLGRush.getInstance().getPrefix() + "§7Du bist §ebereits §7in diesem Team",
                                    MLGRush.getInstance().getPrefix() + "§7You are §ealready §7in this team");
                        }
                    } else {
                        player.closeInventory();
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10F, 10F);
                        player.sendMessage(
                                MLGRush.getInstance().getPrefix() + "§7Dieses Team wird §cnicht §7unterstützt",
                                MLGRush.getInstance().getPrefix() + "§7This team is §cnot §7supported");
                    }
                } else if (event.getCurrentItem().getType().equals(Material.BARRIER)) {
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10F, 10F);
                    player.sendMessage(
                            MLGRush.getInstance().getPrefix() + "§7Diese Variante wird §cnicht §7unterstützt",
                            MLGRush.getInstance().getPrefix() + "§7This variant is §cnot §7supported");
                }
            }
        }
    }

    public void registerTeams(OriginPlayer player) {
        teams.forEach(team -> player.getScoreboard().registerScoreboardTeam(team.getTabSorting(), team.getColor() + "▉ §8× " + team.getColor(), "", new HashSet<>()));
    }

    public void selectRandomTeam(OriginPlayer player) {
        List<Team> emptyTeams = teams.stream().filter(Team::isNotFull).collect(Collectors.toList());
        emptyTeams.get(0).addPlayer(player);
    }

    public List<Team> getAliveTeams() {
        return teams.stream().filter(team -> !team.getMembers().isEmpty()).collect(Collectors.toList());
    }

    public boolean isTeamMode() {
        return maxTeamSize > 1;
    }

    private Team getTeamFromShort(short data) {
        if (data == Team.RED.getData()) {
            return Team.RED;
        } else if (data == Team.BLUE.getData()) {
            return Team.BLUE;
        }
        return null;
    }

    @Getter
    public enum Team {

        RED("00_red", "Rot", "Red", "§c", (short) 14),
        BLUE("02_blue", "Blau", "Blue", "§9", (short) 11);

        final String tabSorting;
        final String germanName;
        final String englishName;
        final String color;

        final short data;

        Team(String tabSorting, String germanName, String englishName, String color, short data) {
            this.tabSorting = tabSorting;
            this.germanName = germanName;
            this.englishName = englishName;
            this.color = color;
            this.data = data;
        }

        public boolean isNotFull() {
            return getMembers().size() != TeamManager.getInstance().getMaxTeamSize();
        }

        public void addPlayer(OriginPlayer player) {
            if (TeamManager.getInstance().getPlayerTeams().get(player) != this)
                TeamManager.getInstance().getPlayerTeams().put(player, this);
            ScoreboardManager.getInstance().setScoreboardTeams(player);
        }

        public void removePlayer(OriginPlayer player) {
            if (TeamManager.getInstance().getPlayerTeams().get(player) == this)
                TeamManager.getInstance().getPlayerTeams().remove(player);
            ScoreboardManager.getInstance().setScoreboardTeams(player);
        }

        public List<OriginPlayer> getMembers() {
            List<OriginPlayer> teamMember = new ArrayList<>();
            for (OriginPlayer players : OriginManager.getInstance().getPlayers()) {
                if (TeamManager.getInstance().getPlayerTeams().get(players) == this) {
                    teamMember.add(players);
                }
            }
            return teamMember;
        }

        public Location getBed() {
            return MapManager.getInstance().getActiveMap().getBeds().get(this.equals(RED) ? 0 : 1);
        }

        public ItemStack getItem(OriginPlayer player) {
            ItemStack itemStack = new ItemStack(Material.LEGACY_STAINED_CLAY, 1, data);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(color + (player.getLanguage().equals(Language.GERMAN) ? germanName : englishName));
            List<String> lore = new ArrayList<>();
            lore.add("§8➜ §7" + getMembers().size() + "§8/§7" + TeamManager.getInstance().getMaxTeamSize());
            lore.add("§8§m---------------");
            for (OriginPlayer players : getMembers()) {
                lore.add("§8× " + (player.equals(players) ? players.getRealDisplayName() : players.getDisplayName()));
            }
            itemMeta.setLore(lore);
            itemStack.setAmount(getMembers().size());
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }
    }
}

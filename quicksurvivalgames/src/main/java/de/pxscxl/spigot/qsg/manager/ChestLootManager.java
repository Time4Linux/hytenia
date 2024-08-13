package de.pxscxl.spigot.qsg.manager;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
public class ChestLootManager {

    @Getter
    private static ChestLootManager instance;

    private final List<Location> chests = new ArrayList<>();

    public ChestLootManager() {
        instance = this;
    }

    public ItemStack getChestItem() {
        ArrayList<ItemStack> items = new ArrayList<>();

        items.add(new ItemStack(Material.GOLD_BOOTS));
        items.add(new ItemStack(Material.GOLD_BOOTS));
        items.add(new ItemStack(Material.GOLD_BOOTS));
        items.add(new ItemStack(Material.GOLD_LEGGINGS));
        items.add(new ItemStack(Material.GOLD_LEGGINGS));
        items.add(new ItemStack(Material.GOLD_LEGGINGS));
        items.add(new ItemStack(Material.GOLD_CHESTPLATE));
        items.add(new ItemStack(Material.GOLD_CHESTPLATE));
        items.add(new ItemStack(Material.GOLD_CHESTPLATE));
        items.add(new ItemStack(Material.GOLD_HELMET));
        items.add(new ItemStack(Material.GOLD_HELMET));
        items.add(new ItemStack(Material.GOLD_HELMET));
        items.add(new ItemStack(Material.WOOD_SWORD));
        items.add(new ItemStack(Material.WOOD_SWORD));
        items.add(new ItemStack(Material.WOOD_SWORD));
        items.add(new ItemStack(Material.STONE_SWORD));
        items.add(new ItemStack(Material.STONE_SWORD));
        items.add(new ItemStack(Material.STONE_SWORD));
        items.add(new ItemStack(Material.FISHING_ROD));
        items.add(new ItemStack(Material.FISHING_ROD));
        items.add(new ItemStack(Material.FISHING_ROD));
        items.add(new ItemStack(Material.BOW));
        items.add(new ItemStack(Material.BOW));
        items.add(new ItemStack(Material.BOW));
        items.add(new ItemStack(Material.ARROW, 4 + new Random().nextInt(5)));
        items.add(new ItemStack(Material.ARROW, 4 + new Random().nextInt(5)));
        items.add(new ItemStack(Material.BREAD, 4 + new Random().nextInt(5)));
        items.add(new ItemStack(Material.BREAD, 4 + new Random().nextInt(5)));
        items.add(new ItemStack(Material.BREAD, 4 + new Random().nextInt(5)));
        items.add(new ItemStack(Material.COOKED_BEEF, 4 + new Random().nextInt(5)));
        items.add(new ItemStack(Material.COOKED_BEEF, 4 + new Random().nextInt(5)));
        items.add(new ItemStack(Material.COOKED_BEEF, 4 + new Random().nextInt(5)));
        items.add(new ItemStack(Material.PORK, 4 + new Random().nextInt(5)));
        items.add(new ItemStack(Material.PORK, 4 + new Random().nextInt(5)));
        items.add(new ItemStack(Material.PORK, 4 + new Random().nextInt(5)));
        items.add(new ItemStack(Material.TNT, 1 + new Random().nextInt(3)));
        items.add(new ItemStack(Material.TNT, 1 + new Random().nextInt(3)));
        items.add(new ItemStack(Material.TNT, 1 + new Random().nextInt(3)));

        items.add(new ItemStack(Material.IRON_BOOTS));
        items.add(new ItemStack(Material.IRON_LEGGINGS));
        items.add(new ItemStack(Material.IRON_CHESTPLATE));
        items.add(new ItemStack(Material.IRON_HELMET));
        items.add(new ItemStack(Material.DIAMOND_CHESTPLATE));
        items.add(new ItemStack(Material.IRON_BOOTS));
        items.add(new ItemStack(Material.IRON_LEGGINGS));
        items.add(new ItemStack(Material.IRON_CHESTPLATE));
        items.add(new ItemStack(Material.IRON_HELMET));
        items.add(new ItemStack(Material.IRON_BOOTS));
        items.add(new ItemStack(Material.IRON_LEGGINGS));
        items.add(new ItemStack(Material.IRON_CHESTPLATE));
        items.add(new ItemStack(Material.IRON_HELMET));
        items.add(new ItemStack(Material.IRON_BOOTS));
        items.add(new ItemStack(Material.IRON_LEGGINGS));
        items.add(new ItemStack(Material.IRON_CHESTPLATE));
        items.add(new ItemStack(Material.IRON_HELMET));
        items.add(new ItemStack(Material.IRON_SWORD));
        items.add(new ItemStack(Material.GOLDEN_APPLE));

        return items.get(new Random().nextInt(items.size()));
    }

    public List<ItemStack> getRandomLoot() {
        List<ItemStack> addedMaterials = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            ItemStack itemStack = getChestItem();
            if (addedMaterials.contains(itemStack)) continue;
            addedMaterials.add(itemStack);
        }
        return addedMaterials;
    }
}

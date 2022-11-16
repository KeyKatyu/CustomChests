package fr.keykatyu.customchests.gui;

import fr.keykatyu.customchests.util.ItemBuilder;
import fr.keykatyu.customchests.util.Utils;
import fr.keykatyu.customchests.util.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;

public class CChestGUI {

    private Block block;
    private ChestLines numberOfLines;
    private String displayName;
    private String permissionToOpen;

    public CChestGUI(Block block, ChestLines numberOfLines, String displayName, String permissionToOpen) {
        this.block = block;
        this.numberOfLines = numberOfLines;
        this.displayName = displayName;
        this.permissionToOpen = permissionToOpen;
    }

    public Inventory create() {
        Inventory inv = Bukkit.createInventory(null, 9, Config.getString("messages.create-chest-gui-title"));
        inv.setItem(1, new ItemBuilder(this.block.getType())
                .setName("§6§lBlock")
                .setLore("§7Material ID :", "§e" + this.block.getType().name())
                .toItemStack());
        inv.setItem(2, new ItemBuilder(Material.COMPASS)
                .setName("§9§lLocation")
                .setLore("§7x = §b" + block.getX(), "§7y = §b" + block.getY(), "§7z = §b" + block.getZ(), "§7world = §b" + block.getWorld().getName())
                .toItemStack());
        inv.setItem(3, new ItemBuilder(Material.BONE_MEAL)
                .setName("§2§lSize")
                .setLore(this.numberOfLines.getDisplayString(), "§8§o(left-click to next size)", "§8§o(right-click to previous size)")
                .toItemStack());
        inv.setItem(4, new ItemBuilder(Material.NAME_TAG)
                .setName("§4§lDisplay name")
                .setLore(displayName(this.displayName), "§8§o(click to edit in chat)").toItemStack());
        inv.setItem(5, new ItemBuilder(Material.CRAFTING_TABLE)
                .setName("§5§lPermission")
                .setLore(displayPermission(this.permissionToOpen), "§8§o(click to edit in chat)")
                .toItemStack());
        inv.setItem(8, new ItemBuilder(Material.GREEN_CANDLE)
                .setName("§a§lFinish").toItemStack());
        Utils.completeInventory(inv);
        return inv;
    }

    public Block getBlock() {
        return block;
    }

    public ChestLines getNumberOfLines() {
        return numberOfLines;
    }

    public String getPermissionToOpen() {
        return permissionToOpen;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static String displayPermission(String permission) {
        if(permission == null) {
            return "§d❌";
        } else {
            return "§d" + permission;
        }
    }

    public static String displayName(String displayName) {
        if(displayName == null || displayName.isEmpty()) {
            return "§c❌";
        } else {
            return displayName;
        }
    }
}

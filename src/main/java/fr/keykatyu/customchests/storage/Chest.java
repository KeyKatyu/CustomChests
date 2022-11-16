package fr.keykatyu.customchests.storage;

import fr.keykatyu.customchests.gui.ChestLines;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

//Custom chest data class
public class Chest {

    private String id;
    private String player;
    private Date dateCreated;

    private String block;
    private ChestLocation location;
    private ChestLines chestLines;
    private String displayName;
    private String permission;
    private String items;

    public Chest(String player, String block, ChestLocation location, ChestLines chestLines, String displayName, String permission, Inventory items) {
        this.player = player;
        this.block = block;
        this.location = location;
        this.chestLines = chestLines;
        this.displayName = displayName;
        this.permission = permission;
        this.id = UUID.randomUUID().toString();
        this.dateCreated = new Date();
        this.items = ChestsStorage.toBase64(items);
    }

    public String getId() {
        return id;
    }

    public String getPlayer() {
        return player;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public String getBlock() {
        return block;
    }

    public ChestLocation getLocation() {
        return location;
    }

    public ChestLines getChestLines() {
        return chestLines;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPermission() {
        return permission;
    }

    public ItemStack[] getItems() {
        try {
            return ChestsStorage.fromBase64(items);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setItems(Inventory inventory) {
        this.items = ChestsStorage.toBase64(inventory);
    }
}

package fr.keykatyu.customchests.storage;

import com.google.gson.Gson;
import fr.keykatyu.customchests.Main;
import fr.keykatyu.customchests.gui.ChestLines;
import fr.keykatyu.customchests.util.Utils;
import fr.keykatyu.customchests.util.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Class to manage JSON custom chest storage
public class ChestsStorage {

    private static ArrayList<Chest> chests = new ArrayList<>();

    public static Chest createChest(Player p, Block block, ChestLocation location, ChestLines chestLines, String displayName, String permission) {
        Chest chest = new Chest(p.getName(), block.getType().name(), location, chestLines, displayName, permission, Bukkit.createInventory(null, chestLines.getSlots(), displayName));
        chests.add(chest);
        saveChests();

        if(!Config.getBoolean("plugin.invisible-chests")) {
            BukkitTask bukkitTask = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), () -> {
                location.getWorld().spawnParticle(Particle.BLOCK_CRACK, new Location(location.getWorld(), location.getX() + 0.5, location.getY() + 0.5, location.getZ() + 0.5), 500,
                        Material.getMaterial(chest.getBlock()).createBlockData());
            }, 1, 20);
            Main.getParticlesTasksMap().put(chest.getId(), bukkitTask.getTaskId());
        }

        return chest;
    }

    public static void removeChest(String id) {
        chests.remove(getChest(id));
        saveChests();
    }

    public static Chest getChest(String id) {
        for(Chest chest : chests) {
            if(chest.getId().equalsIgnoreCase(id)) {
                return chest;
            }
        }
        return null;
    }

    public static Chest getChestFromLocation(Location blockLoc) {
        for(Chest chest : ChestsStorage.getAllChests()) {
            ChestLocation chestLoc = chest.getLocation();
            if(blockLoc.getWorld() == chestLoc.getWorld() && blockLoc.getX() == chestLoc.getX() && blockLoc.getY() == chestLoc.getY()
                    && blockLoc.getZ() == chestLoc.getZ()) {
                return chest;
            }
        }
        return null;
    }

    public static List<Chest> getAllChests() {
        return chests;
    }

    public static void updateChestItems(String id, Inventory inventory) {
        getChest(id).setItems(inventory);
        saveChests();
    }

    public static void loadChests() {
        Gson gson = new Gson();
        File file = new File(Main.getInstance().getDataFolder(), "chests.json");
        if(file.exists()) {
            try {
                Reader reader = new FileReader(file);
                Chest[] n = gson.fromJson(reader, Chest[].class);
                chests = new ArrayList<>(Arrays.asList(n));
            } catch (FileNotFoundException e) {
                Utils.console("§c§lAn error occurred during the custom chests data loading !");
            }
        }
    }

    public static void saveChests() {
        Gson gson = new Gson();
        File file = new File(Main.getInstance().getDataFolder(), "chests.json");
        file.getParentFile().mkdir();
        try {
            file.createNewFile();
            Writer writer = new FileWriter(file, false);
            gson.toJson(chests, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            Utils.console("§c§lThe custom chests backup failed !");
        }
    }

    public static String toBase64(Inventory inventory) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            // Write the size of the inventory
            dataOutput.writeInt(inventory.getSize());
            // Save every element in the list
            for (int i = 0; i < inventory.getSize(); i++) {
                dataOutput.writeObject(inventory.getItem(i));
            }
            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            Utils.console("§cAn error occurred during the items saving");
            e.printStackTrace();
            return null;
        }
    }

    public static ItemStack[] fromBase64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];

            // Read the serialized inventory
            for (int i = 0; i < items.length; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }

            dataInput.close();
            return items;
        } catch (ClassNotFoundException e) {
            Utils.console("§cAn error occurred during the items decoding");
            e.printStackTrace();
            return null;
        }
    }
}

package fr.keykatyu.customchests.util;

import fr.keykatyu.customchests.Main;
import fr.keykatyu.customchests.util.config.CShortcuts;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class Utils {

    public static void console(String msg) {
        Main.getInstance().getServer().getConsoleSender().sendMessage(CShortcuts.PREFIX + " " + msg);
    }
    
    public static void completeInventory(Inventory inventory) {
        for(int i = 0; i < inventory.getSize(); i++) {
            if(inventory.getItem(i) == null) {
                inventory.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE)
                        .setName("§8-*-*-").toItemStack());
            }
        }
    }
}

package fr.keykatyu.customchests.command;

import fr.keykatyu.customchests.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

public class AnalyzeCChestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;

        player.getInventory().addItem(new ItemBuilder(Material.STICK).setName("§d§lChest-founder stick")
                .setLore("§7Right-click on a block and", "§7the stick will tell you if", "§7it's a chest or not !")
                .addUnsafeEnchant(Enchantment.MENDING, 1)
                .setItemsFlags(ItemFlag.HIDE_ENCHANTS)
                .toItemStack());
        return false;
    }
}

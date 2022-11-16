package fr.keykatyu.customchests.command;

import fr.keykatyu.customchests.Main;
import fr.keykatyu.customchests.storage.Chest;
import fr.keykatyu.customchests.storage.ChestsStorage;
import fr.keykatyu.customchests.util.config.CShortcuts;
import fr.keykatyu.customchests.util.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class RemoveCChestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;

        Set<Material> materialSet = new HashSet<>();
        materialSet.add(Material.WATER);
        materialSet.add(Material.AIR);
        materialSet.add(Material.LAVA);
        Block block = player.getTargetBlock(materialSet, 10);

        if(block.isEmpty()) {
            player.sendMessage(CShortcuts.PREFIX + Config.getString("messages.target-block"));
            return true;
        }

        Chest chest = ChestsStorage.getChestFromLocation(block.getLocation());
        if(chest == null) player.sendMessage(CShortcuts.PREFIX + " " + Config.getString("messages.block-isnt-custom-chest"));

        block.breakNaturally();
        World world = player.getWorld();
        for(ItemStack is : chest.getItems()) {
            if(is == null) {
                continue;
            }
            world.dropItem(new Location(world, block.getX(), block.getY(), block.getZ()), is);
        }
        ChestsStorage.removeChest(chest.getId());
        Bukkit.getScheduler().cancelTask(Main.getParticlesTasksMap().get(chest.getId()));
        player.sendMessage(CShortcuts.PREFIX + " " + Config.getString("messages.chest-removed"));

        return false;
    }
}

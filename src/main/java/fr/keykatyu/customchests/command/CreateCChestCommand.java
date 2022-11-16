package fr.keykatyu.customchests.command;

import fr.keykatyu.customchests.Main;
import fr.keykatyu.customchests.gui.CChestGUI;
import fr.keykatyu.customchests.gui.ChestLines;
import fr.keykatyu.customchests.listener.CChestGUIListener;
import fr.keykatyu.customchests.storage.ChestsStorage;
import fr.keykatyu.customchests.util.config.CShortcuts;
import fr.keykatyu.customchests.util.config.Config;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class CreateCChestCommand implements CommandExecutor {

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
            player.sendMessage(CShortcuts.PREFIX + " " +  Config.getString("messages.target-block"));
            return true;
        }

        if(ChestsStorage.getChestFromLocation(block.getLocation()) != null) {
            player.sendMessage(CShortcuts.PREFIX + " " + Config.getString("messages.block-already-chest"));
            return true;
        }

        CChestGUI CChestGUI = new CChestGUI(block, ChestLines.ONE, null,null);
        player.openInventory(CChestGUI.create());

        Main.getInstance().getServer().getPluginManager().registerEvents(new CChestGUIListener(player, CChestGUI.getBlock(),
                CChestGUI.getNumberOfLines(), CChestGUI.getDisplayName(), CChestGUI.getPermissionToOpen()), Main.getInstance());

        return true;
    }
}

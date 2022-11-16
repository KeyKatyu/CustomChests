package fr.keykatyu.customchests.listener;

import fr.keykatyu.customchests.Main;
import fr.keykatyu.customchests.gui.CChestGUI;
import fr.keykatyu.customchests.storage.Chest;
import fr.keykatyu.customchests.storage.ChestsStorage;
import fr.keykatyu.customchests.util.Utils;
import fr.keykatyu.customchests.util.config.CShortcuts;
import fr.keykatyu.customchests.util.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CChestsListener implements Listener {

    //Listener for analyzer stick & open GUI of the custom chest to player
    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockClick(PlayerInteractEvent e){
        if(e.getHand() == EquipmentSlot.HAND) return;
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
            Player player = e.getPlayer();
            Block block = e.getClickedBlock();
            Chest chest = ChestsStorage.getChestFromLocation(block.getLocation());
            if(chest == null) {
                if(!player.getInventory().getItemInMainHand().getType().equals(Material.AIR) && player.getInventory().getItemInMainHand().hasItemMeta()
                        && player.getInventory().getItemInMainHand().getItemMeta().hasDisplayName() && player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals("§d§lChest-founder stick")) {
                    player.sendMessage(CShortcuts.PREFIX + " " + Config.getString("messages.block-isnt-custom-chest"));
                }
                return;
            }

            if(!player.getInventory().getItemInMainHand().getType().equals(Material.AIR) && player.getInventory().getItemInMainHand().hasItemMeta()
                    && player.getInventory().getItemInMainHand().getItemMeta().hasDisplayName() && player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals("§d§lChest-founder stick")) {
                player.sendMessage(CShortcuts.PREFIX + " " + Config.getString("messages.custom-chest-detected"));
                player.sendMessage("§7§l------ §e§l**** §7§l------");
                player.sendMessage("§4§l➪ §4Chest ID : §c" + chest.getId());
                player.sendMessage("§b§l➪ §bDisplay name : §r" + CChestGUI.displayName(chest.getDisplayName()));
                player.sendMessage("§6§l➪ §6Block : §e" + chest.getBlock());
                player.sendMessage("§9§l➪ §9Location : §b" + chest.getLocation().toString());
                player.sendMessage("§2§l➪ §2Chest size : " + chest.getChestLines().getDisplayString());
                player.sendMessage("§c§l➪ §cInventory : §4" + chest.getItems().length + " items");
                player.sendMessage("§5§l➪ §5Permission : §d" + CChestGUI.displayPermission(chest.getPermission()));
                player.sendMessage("§8§l➪ §8Owner : §7" + chest.getPlayer());
                player.sendMessage("§3§l➪ §3Created at : §f" + chest.getDateCreated());
                player.sendMessage("§7§l------ §e§l**** §7§l------");
                return;
            }

            if(chest.getPermission() != null && !player.hasPermission(chest.getPermission())) {
                player.sendMessage(CShortcuts.PREFIX + " " + Config.getString("messages.no-perm-custom-chest").replace("[permission]", chest.getPermission()));
                return;
            }
            Inventory inv = Bukkit.createInventory(null, chest.getChestLines().getSlots(), chest.getDisplayName());
            if(chest.getItems() == null) {
                player.openInventory(inv);
            } else {
                inv.setContents(chest.getItems());
                player.openInventory(inv);
            }
            Main.getInstance().getServer().getPluginManager().registerEvents(new CChestOpenedListener(player, chest), Main.getInstance());
        }
    }

    //Listener for remove chests when block is destroyed
    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent e) {
        Block block = e.getBlock();
        Chest chest = ChestsStorage.getChestFromLocation(block.getLocation());
        if(chest == null) return;

        Player player = e.getPlayer();

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
    }
}

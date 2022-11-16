package fr.keykatyu.customchests.listener;

import fr.keykatyu.customchests.Main;
import fr.keykatyu.customchests.gui.CChestGUI;
import fr.keykatyu.customchests.gui.ChestLines;
import fr.keykatyu.customchests.storage.ChestLocation;
import fr.keykatyu.customchests.storage.ChestsStorage;
import fr.keykatyu.customchests.util.config.CShortcuts;
import fr.keykatyu.customchests.util.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

//Create a custom chest GUI listener
public class CChestGUIListener implements Listener {

    private Player player;
    private Block block;
    private ChestLines chestsLines;
    private String displayName;
    private String permissionToOpen;

    public CChestGUIListener(Player player, Block block, ChestLines lines, String displayName, String permissionToOpen) {
        this.player = player;
        this.block = block;
        this.chestsLines = lines;
        this.displayName = displayName;
        this.permissionToOpen = permissionToOpen;
    }

    @EventHandler
    public void onItemClicked(InventoryClickEvent e) {
        if(!e.getView().getTitle().equals(Config.getString("messages.create-chest-gui-title"))) return;
        if(e.getWhoClicked() != player) return;

        e.setCancelled(true);

        switch (e.getCurrentItem().getType())
        {
            case BONE_MEAL:
                if(e.getClick() == ClickType.LEFT) {
                    this.chestsLines = chestsLines.next();
                } else if(e.getClick() == ClickType.RIGHT) {
                    this.chestsLines = chestsLines.prev();
                }
                player.closeInventory();
                player.openInventory(new CChestGUI(this.block, this.chestsLines, this.displayName, this.permissionToOpen).create());
                break;
            case NAME_TAG:
                player.closeInventory();
                player.sendMessage(CShortcuts.PREFIX + " " + Config.getString("messages.chest-display-name-chat"));
                Main.getInstance().getServer().getPluginManager().registerEvents(new CChestsGUIDisplayNameListener(), Main.getInstance());
                break;
            case CRAFTING_TABLE:
                player.closeInventory();
                player.sendMessage(CShortcuts.PREFIX + " " + Config.getString("messages.chest-permission-chat"));
                Main.getInstance().getServer().getPluginManager().registerEvents(new CChestsGUIPermissionListener(), Main.getInstance());
                break;
            case GREEN_CANDLE:
                HandlerList.unregisterAll(this);
                player.closeInventory();
                player.sendMessage(CShortcuts.PREFIX + " §aYour custom chest has been successfully created !");
                if(displayName == null) displayName = "";
                ChestsStorage.createChest(player, block, new ChestLocation(block.getWorld(), block.getX(), block.getY(), block.getZ()), chestsLines, displayName, permissionToOpen);
                break;
        }
    }

    public class CChestsGUIDisplayNameListener implements Listener {
        @EventHandler
        public void onChatMessageSent(AsyncPlayerChatEvent e) {
            if(e.getPlayer() != player) return;

            e.setCancelled(true);
            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                player.sendMessage(CShortcuts.PREFIX + " " +  Config.getString("messages.chat-message-received"));
                displayName = e.getMessage().replaceAll("&", "§");
                player.openInventory(new CChestGUI(block, chestsLines, displayName, permissionToOpen).create());
                HandlerList.unregisterAll(this);
            });
        }
    }

    public class CChestsGUIPermissionListener implements Listener {
        @EventHandler
        public void onChatMessageSent(AsyncPlayerChatEvent e) {
            if(e.getPlayer() != player) return;

            e.setCancelled(true);
            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                player.sendMessage(CShortcuts.PREFIX + " " + Config.getString("messages.chat-message-received"));
                permissionToOpen = e.getMessage();
                player.openInventory(new CChestGUI(block, chestsLines, displayName, permissionToOpen).create());
                HandlerList.unregisterAll(this);
            });
        }
    }
}

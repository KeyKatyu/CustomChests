package fr.keykatyu.customchests.listener;

import fr.keykatyu.customchests.storage.Chest;
import fr.keykatyu.customchests.storage.ChestsStorage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

//Listener to save the items data when the player closes a custom chest GUI
public class CChestOpenedListener implements Listener {

    private Player player;
    private Chest chest;

    public CChestOpenedListener(Player player, Chest chest) {
        this.player = player;
        this.chest = chest;
    }

    @EventHandler
    public void onChestGUIClosed(InventoryCloseEvent e) {
        if(!e.getView().getTitle().equals(chest.getDisplayName())) return;
        if(e.getPlayer() != player) return;

        ChestsStorage.updateChestItems(chest.getId(), e.getInventory());
        HandlerList.unregisterAll(this);
    }
}

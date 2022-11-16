package fr.keykatyu.customchests;

import fr.keykatyu.customchests.command.AnalyzeCChestCommand;
import fr.keykatyu.customchests.command.CreateCChestCommand;
import fr.keykatyu.customchests.command.RemoveCChestCommand;
import fr.keykatyu.customchests.listener.CChestsListener;
import fr.keykatyu.customchests.storage.Chest;
import fr.keykatyu.customchests.storage.ChestLocation;
import fr.keykatyu.customchests.storage.ChestsStorage;
import fr.keykatyu.customchests.util.Utils;
import fr.keykatyu.customchests.util.config.CShortcuts;
import fr.keykatyu.customchests.util.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public final class Main extends JavaPlugin {

    private static Main instance;
    private static HashMap<String, Integer> particlesTasksMap = new HashMap<>();;

    @Override
    public void onEnable() {
        instance = this;

        //Save default config, load chests
        saveDefaultConfig();
        CShortcuts.syncConfig();
        ChestsStorage.loadChests();

        //Load particles
        if(!Config.getBoolean("plugin.invisible-chests")) {
            for(Chest chest : ChestsStorage.getAllChests()) {
                ChestLocation loc = chest.getLocation();
                BukkitTask bukkitTask = Bukkit.getScheduler().runTaskTimer(this, () -> {
                   loc.getWorld().spawnParticle(Particle.BLOCK_CRACK, new Location(loc.getWorld(), loc.getX() + 0.5, loc.getY() + 0.5, loc.getZ() + 0.5), 500,
                           Material.getMaterial(chest.getBlock()).createBlockData());
                }, 1, 20);
                particlesTasksMap.put(chest.getId(), bukkitTask.getTaskId());
            }
        }

        //Register commands/events
        getCommand("cccreate").setExecutor(new CreateCChestCommand());
        getCommand("ccremove").setExecutor(new RemoveCChestCommand());
        getCommand("ccanalyze").setExecutor(new AnalyzeCChestCommand());
        getServer().getPluginManager().registerEvents(new CChestsListener(), this);

        Utils.console("§7Plugin §a§lLOADED");
    }

    @Override
    public void onDisable() {
        saveConfig();
        ChestsStorage.saveChests();
        Utils.console("§7Plugin §c§lDISABLED");
    }

    public static Main getInstance() {
        return instance;
    }

    public static HashMap<String, Integer> getParticlesTasksMap() {
        return particlesTasksMap;
    }
}

package fr.keykatyu.customchests.storage;

import fr.keykatyu.customchests.Main;
import org.bukkit.World;

public class ChestLocation {

    private String world;
    private double x;
    private double y;
    private double z;

    public ChestLocation(World world, double x, double y, double z) {
        this.world = world.getName();
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public World getWorld() {
        return Main.getInstance().getServer().getWorld(world);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public String toString() {
        return "x = " + this.x + " y = " + this.y + " z = " + this.z + " (" + world + ")";
    }
}

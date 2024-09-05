package me.lowlauch.walo.misc;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;

public class WorldUtil {
    public static int calculateFloorY(int minFloorY, Player p) {
        for (int i = 255; i >= minFloorY; i--) {
            if (p.getWorld().getBlockAt(0, i, 0).getType() != Material.AIR) {
                return i + 5; // avoid spawning in the floor
            }
        }
        return minFloorY;
    }

    public static boolean blockIsAdjacentToMaterial(Block block, Material material) {
        ArrayList<Location> checkLocations = new ArrayList<>();

        Location blockLocation = block.getLocation();

        Location left = new Location(block.getWorld(), blockLocation.getX() - 1, blockLocation.getY(), blockLocation.getZ());
        Location right = new Location(block.getWorld(), blockLocation.getX() + 1, blockLocation.getY(), blockLocation.getZ());
        Location top = new Location(block.getWorld(), blockLocation.getX(), blockLocation.getY() + 1, blockLocation.getZ());
        Location bottom = new Location(block.getWorld(), blockLocation.getX(), blockLocation.getY() - 1, blockLocation.getZ());
        Location forward = new Location(block.getWorld(), blockLocation.getX(), blockLocation.getY(), blockLocation.getZ() - 1);
        Location backward = new Location(block.getWorld(), blockLocation.getX(), blockLocation.getY(), blockLocation.getZ() + 1);

        checkLocations.add(left);
        checkLocations.add(right);
        checkLocations.add(top);
        checkLocations.add(bottom);
        checkLocations.add(forward);
        checkLocations.add(backward);

        for (Location checkLocation : checkLocations) {
            Material blockType = checkLocation.getBlock().getType();
            Bukkit.getLogger().info(checkLocation.toString() + blockType.toString());
            if (blockType == material) {
                return true;
            }
        }

        return false;
    }
}

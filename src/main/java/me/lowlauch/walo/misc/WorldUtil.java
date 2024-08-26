package me.lowlauch.walo.misc;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.io.File;

public class WorldUtil {
    public static int calculateFloorY(int minFloorY, Player p) {
        for (int i = 255; i >= minFloorY; i--) {
            if (p.getWorld().getBlockAt(0, i, 0).getType() != Material.AIR) {
                return i + 5; // avoid spawning in the floor
            }
        }
        return minFloorY;
    }
}

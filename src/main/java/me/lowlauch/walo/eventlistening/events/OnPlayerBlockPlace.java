package me.lowlauch.walo.eventlistening.events;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.misc.GlobalVariables;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class OnPlayerBlockPlace implements Listener {
    @EventHandler
    public void onPlayerBlockPlace(BlockPlaceEvent e) {
        int protectionRadius = 10;
        Location playerLocation = e.getPlayer().getLocation();

        if (e.getPlayer().getWorld().getEnvironment().equals(World.Environment.NETHER)) {
            if (locationDistance2D(playerLocation, GlobalVariables.netherPortalLocation) < protectionRadius) {
                cancel(e);
            }
        }

        if (e.getPlayer().getWorld().getName().equals("world")) {
            if (locationDistance2D(playerLocation, Bukkit.getWorld("world").getSpawnLocation()) < protectionRadius) {
                cancel(e);
            }
        }
    }

    private void cancel(BlockPlaceEvent e) {
        e.getPlayer().sendMessage(Main.prefix + ChatColor.RED + "Unterlasse dies bitte.");
        e.setCancelled(true);
    }

    private double locationDistance2D(Location location1, Location location2) {
        double x1 = location1.getX();
        double z1 = location1.getZ();
        
        double x2 = location2.getX();
        double z2 = location2.getZ();

        final double result = Math.sqrt((x2 - x1)*(x2 - x1) + (z2 - z1)*(z2 - z1));
        return result;
    }
}

package me.lowlauch.walo.eventlistening.events;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.misc.GlobalVariables;
import me.lowlauch.walo.misc.WorldUtil;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class OnPlayerBlockPlace implements Listener {
    @EventHandler
    public void onPlayerBlockPlace(BlockPlaceEvent e) {
        int protectionRadius = 10;
        Location playerLocation = e.getPlayer().getLocation();

        if (e.getPlayer().getWorld().getEnvironment().equals(World.Environment.NETHER)
                // this potentially takes a lot of cpu cycles...
                // but is important for fair play
                && WorldUtil.blockIsAdjacentToMaterial(e.getBlock(), Material.PORTAL)) {
            e.getPlayer().sendMessage(Main.prefix + "§cBitte bau keine Nether Portale zu!");
            e.setCancelled(true);
        }

        /* Why did I add this again? Seems dumb ...

        if (e.getPlayer().getWorld().getName().equals("world")) {
            if (locationDistance2D(playerLocation, Bukkit.getWorld("world").getSpawnLocation()) < protectionRadius) {
                cancel(e);
            }
        }
         */
    }

    private double locationDistance2D(Location location1, Location location2) {
        double x1 = location1.getX();
        double z1 = location1.getZ();
        
        double x2 = location2.getX();
        double z2 = location2.getZ();

        return Math.sqrt((x2 - x1)*(x2 - x1) + (z2 - z1)*(z2 - z1));
    }
}

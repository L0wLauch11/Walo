package me.lowlauch.walo.eventlistening.events;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.misc.GlobalVariables;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class OnPlayerBlockPlace {
    @EventHandler
    public void onPlayerBlockPlace(BlockPlaceEvent e) {
        int protectionRadius = 16;
        Location playerLocation = e.getPlayer().getLocation();

        if (e.getPlayer().getWorld().getEnvironment().equals(World.Environment.NETHER)) {
            if (playerLocation.distance(GlobalVariables.netherPortalLocation) < protectionRadius) {
                cancel(e);
            }
        }

        if (e.getPlayer().getWorld().getName().equals("world")) {
            if (playerLocation.distance(Bukkit.getWorld("world").getSpawnLocation()) < protectionRadius) {
                cancel(e);
            }
        }
    }

    private void cancel(BlockPlaceEvent e) {
        e.getPlayer().sendMessage(Main.prefix + ChatColor.RED + "Unterlasse dies bitte.");
        e.setCancelled(true);
    }
}

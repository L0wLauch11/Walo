package me.lowlauch.walo.eventlistening.events;

import me.lowlauch.walo.misc.GlobalVariables;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class OnPlayerItemPickup implements Listener {
    @EventHandler
    public void onPlayerItemPickup(PlayerPickupItemEvent e) {
        // Disable collecting items if game has not started
        if (!GlobalVariables.started) {
            e.setCancelled(true);
            e.getItem().remove();
        }
    }
}

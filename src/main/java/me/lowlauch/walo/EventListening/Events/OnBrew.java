package me.lowlauch.walo.EventListening.Events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.BrewEvent;

public class OnBrew implements Listener {
    @EventHandler
    public void onBrew(BrewEvent e) {
        // Disable Strength Potions
        if (e.getContents().contains(Material.BLAZE_POWDER)
                || e.getContents().contains(Material.FERMENTED_SPIDER_EYE))
            e.setCancelled(true);
    }
}

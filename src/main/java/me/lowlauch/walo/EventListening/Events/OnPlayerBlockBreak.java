package me.lowlauch.walo.EventListening.Events;

import me.lowlauch.walo.Main;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class OnPlayerBlockBreak implements Listener {
    @EventHandler
    public void onPlayerBlockBreak(BlockBreakEvent e) {
        if (e.getPlayer().getWorld().getName().contains("nether") && e.getBlock().getType() == Material.OBSIDIAN) {
            e.getPlayer().sendMessage(Main.prefix + "Du kannst kein Obsidian im Nether abbauen!");
            e.setCancelled(true);
        }
    }
}

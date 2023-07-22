package me.lowlauch.walo.eventlistening.events;

import me.lowlauch.walo.Main;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class OnPlayerBlockBreak implements Listener {
    @EventHandler
    public void onPlayerBlockBreak(BlockBreakEvent e) {
        if (e.getPlayer().getWorld().getEnvironment().equals(World.Environment.NETHER) && e.getBlock().getType() == Material.OBSIDIAN) {
            e.getPlayer().sendMessage(Main.prefix + "Du kannst kein Obsidian im Nether abbauen!");
            e.setCancelled(true);
        }
    }
}

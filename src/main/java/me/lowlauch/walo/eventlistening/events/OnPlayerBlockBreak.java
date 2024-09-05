package me.lowlauch.walo.eventlistening.events;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.misc.WorldUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.*;

public class OnPlayerBlockBreak implements Listener {
    @EventHandler
    public void onPlayerBlockBreak(BlockBreakEvent e) {
        if (e.getPlayer().getWorld().getEnvironment().equals(World.Environment.NETHER)
                && (e.getBlock().getType() == Material.PORTAL)
                || (e.getBlock().getType() == Material.OBSIDIAN && WorldUtil.blockIsAdjacentToMaterial(e.getBlock(), Material.PORTAL))) {
            e.getPlayer().sendMessage(Main.prefix + "§cDu kannst keine Nether Portale abbauen!");
            e.setCancelled(true);
        }
    }
}

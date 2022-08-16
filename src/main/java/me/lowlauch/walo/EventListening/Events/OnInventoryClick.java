package me.lowlauch.walo.EventListening.Events;

import me.lowlauch.walo.Teams.TeamsInventoryItem;
import me.lowlauch.walo.misc.GlobalVariables;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class OnInventoryClick implements Listener {
    @EventHandler
    public void onInventoryClick(PlayerInteractEvent e) {
        if (!GlobalVariables.started) {
            TeamsInventoryItem.process(e);
        }
    }
}

package me.lowlauch.walo.eventlistening.events;

import me.lowlauch.walo.misc.GlobalVariables;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class OnPlayerDrop implements Listener {
    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent e) {
        // Disable dropping if game has not started
        if (!GlobalVariables.started) {
            e.setCancelled(true);
        }
    }
}

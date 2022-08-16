package me.lowlauch.walo.EventListening.Events;

import me.lowlauch.walo.Teams.TeamSettingsItems.TeamsInventoryItem;
import me.lowlauch.walo.misc.GlobalVariables;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class OnPlayerInteract implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (!GlobalVariables.started) {
            TeamsInventoryItem.process(e);
        }
    }
}

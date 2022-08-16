package me.lowlauch.walo.eventlistening.events;

import me.lowlauch.walo.teams.teamsettingsitems.TeamsInventoryItem;
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

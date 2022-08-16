package me.lowlauch.walo.eventlistening.events;

import me.lowlauch.walo.misc.GlobalVariables;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class OnPlayerDamage implements Listener {
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        // Cancel damage before game has started
        if (!GlobalVariables.started && e.getEntity() instanceof Player) {
            e.setCancelled(true);
        }
    }
}

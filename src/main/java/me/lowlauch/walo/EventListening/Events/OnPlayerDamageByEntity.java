package me.lowlauch.walo.EventListening.Events;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.Teams.Teams;
import me.lowlauch.walo.WaloConfig;
import me.lowlauch.walo.misc.GlobalVariables;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.List;
import java.util.Objects;

public class OnPlayerDamageByEntity implements Listener {
    @EventHandler
    public void onPlayerDamageByEntity(EntityDamageByEntityEvent e) {
        // This only applies to players
        if (!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player)) {
            return;
        }

        if (!GlobalVariables.started) {
            e.getDamager().sendMessage(Main.prefix + "Das Spiel hat noch nicht gestartet!");
            e.setCancelled(true);

            return;
        }

        if (GlobalVariables.protection) {
            e.getDamager().sendMessage(Main.prefix + "Die Schutzzeit ist noch an!");
            e.setCancelled(true);

            return;
        }

        // Stop team mates from damaging each other
        Player victim = (Player) e.getEntity();
        Player damager = (Player) e.getDamager();

        if (Objects.equals(Teams.getTeamOfPlayer(victim), Teams.getTeamOfPlayer(damager))) {
            e.setCancelled(true);
        }
    }
}

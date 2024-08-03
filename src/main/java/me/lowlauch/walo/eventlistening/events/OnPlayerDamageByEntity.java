package me.lowlauch.walo.eventlistening.events;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.teams.Teams;
import me.lowlauch.walo.misc.GlobalVariables;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Objects;

public class OnPlayerDamageByEntity implements Listener {
    @EventHandler
    public void onPlayerDamageByEntity(EntityDamageByEntityEvent e) {
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

        // Stop teammates from damaging each other
        Player victim = (Player) e.getEntity();
        Player damager = (Player) e.getDamager();
        String victimTeam = Teams.getTeamOfPlayer(victim);
        String damagerTeam = Teams.getTeamOfPlayer(damager);

        if (Objects.equals(victimTeam, damagerTeam) && victimTeam != null) {
            e.setCancelled(true);
        }
    }
}

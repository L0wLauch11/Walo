package me.lowlauch.walo.EventListening.Events;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.WaloConfig;
import me.lowlauch.walo.misc.GlobalVariables;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.List;

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
        WaloConfig.reload();

        Player victim = (Player) e.getEntity();
        Player damager = (Player) e.getDamager();

        List<String> victimMates;
        List<String> damagerMates;

        victimMates = WaloConfig.getPlayerMates(victim);
        damagerMates = WaloConfig.getPlayerMates(damager);

        int length = Math.max(victimMates.size(), damagerMates.size());

        // Check if he is team mate
        for (int i = 0; i < length; i++) {
            if (victimMates.get(i).equals(damager.getUniqueId().toString()) || damagerMates.get(i).equals(victim.getUniqueId().toString()))
                e.setCancelled(true);
        }
    }
}

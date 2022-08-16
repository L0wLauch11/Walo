package me.lowlauch.walo.EventListening.Events;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.database.WaloDatabase;
import me.lowlauch.walo.misc.GlobalVariables;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class OnPlayerDie implements Listener {
    @EventHandler
    public void onPlayerDie(PlayerDeathEvent e) {
        if (!GlobalVariables.started)
            return;

        // Ban the player if he dies and the game has started
        Player p = e.getEntity();
        String deathMessage;
        Player killer = p.getKiller();

        // Player death
        if (killer != null) {
            deathMessage = Main.prefix + "§6" + p.getName() + "§4 wurde von §6" + killer.getName() + "§4 getötet.";

            // Ban the player
            Bukkit.getBanList(BanList.Type.NAME).addBan(p.getName(), Main.prefix + "Du bist gestorben", null, "Tot");
            p.kickPlayer(Main.prefix + "§cDu bist gestorben.");

            // Give the killer a kill in stats
            if (GlobalVariables.statsDisabled) {
                killer.sendMessage(Main.prefix + "Du hast §4keinen Kill§7 in den Stats bekommen!");
            } else {
                /*  Old way of saving kills, without a database

                    String path = "stats.kills." + e.getEntity().getKiller().getUniqueId().toString();
                    Main.getInstance().getConfig().set(path, Main.getInstance().getConfig().getInt(path) + 1);

                */

                // Add kill to database
                WaloDatabase.addPlayerKill(killer);
            }
        } else {
            // Normal death
            deathMessage = Main.prefix + "§6" + p.getName() + "§4 ist gestorben.";

            // Ban the player
            Bukkit.getBanList(BanList.Type.NAME).addBan(p.getName(), Main.prefix + "Du bist gestorben", null, "Tot");
            p.kickPlayer(Main.prefix + "§cDu bist gestorben.");
        }

        // Change death message
        e.setDeathMessage(deathMessage);
    }
}

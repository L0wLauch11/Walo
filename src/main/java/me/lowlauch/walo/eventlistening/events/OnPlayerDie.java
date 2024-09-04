package me.lowlauch.walo.eventlistening.events;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.database.WaloDatabase;
import me.lowlauch.walo.discord.webhook.DiscordWebHook;
import me.lowlauch.walo.misc.GlobalVariables;
import me.lowlauch.walo.misc.StringUtil;
import me.lowlauch.walo.tasks.GameRestartTask;
import me.lowlauch.walo.teams.Teams;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OnPlayerDie implements Listener {
    @EventHandler
    public void onPlayerDie(PlayerDeathEvent e) {
        if (!GlobalVariables.started)
            return;

        // Ban the player if he dies and the game has started
        Player p = e.getEntity();
        String deathMessage;
        Player killer = p.getKiller();

        if (killer != null) {
            deathMessage = Main.prefix + "§6" + p.getName() + "§4 wurde von §6" + killer.getName() + "§4 getötet.";

            Bukkit.getBanList(BanList.Type.NAME).addBan(p.getName(), Main.prefix + "Du bist gestorben", null, "Tot");
            p.kickPlayer(Main.prefix + "§cDu bist gestorben.");

            // Give the killer a kill in stats
            if (GlobalVariables.statsDisabled) {
                killer.sendMessage(Main.prefix + "Du hast §4keinen Kill§7 in den Stats bekommen!");
            } else {
                WaloDatabase.addPlayerKill(killer);
            }
        } else {
            // Non-player kill death
            deathMessage = Main.prefix + "§6" + p.getName() + "§4 ist gestorben.";

            // Ban the player
            Bukkit.getBanList(BanList.Type.NAME).addBan(p.getName(), Main.prefix + "Du bist gestorben", null, "Tot");
            p.kickPlayer(Main.prefix + "§cDu bist gestorben.");
        }
        e.setDeathMessage(deathMessage);
    }
}

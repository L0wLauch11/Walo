package me.lowlauch.walo.eventlistening.events;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.database.WaloDatabase;
import me.lowlauch.walo.discord.webhook.DiscordWebHook;
import me.lowlauch.walo.misc.GlobalVariables;
import me.lowlauch.walo.misc.StringUtil;
import me.lowlauch.walo.tasks.CheckWinTask;
import me.lowlauch.walo.tasks.GameRestartTask;
import me.lowlauch.walo.teams.Teams;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OnPlayerDisconnect implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDisconnect(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        EntityDamageEvent damageCause = p.getLastDamageCause();

        // Tell remaining Teams that no one should disconnect now!
        // 3 because this event gets triggered BEFORE the recently disconnected player IS ACTUALLY DISCONNECTED
        // thus, there are still technically 3 Teams online (the 3rd Team's player just needs to be disconnected, still)
        if (Teams.getAliveTeams().size() == 3) {
            Bukkit.getServer().broadcastMessage(Main.prefix + "§6Ihr seid die letzten beiden Teams!§7\n" +
                    Main.prefix + "Jetzt darf kein Team mehr vollkommen disconnecten, sonst gewinnt das andere Team!");
        }

        // Detect wins
        Bukkit.getServer().getScheduler().runTaskLater(Main.getInstance(), new CheckWinTask(), 20L);

        // Save leave timestamp to ban player on rejoin if he was offline too long
        GlobalVariables.playerLeaveTimestamps.put(p.getUniqueId(), System.currentTimeMillis());

        // Combat logging protection
        if (p.getHealth() >= 10.0f || p.getHealth() == 0.0f || !GlobalVariables.started)
            return;

        // Drop Inventory & Armor Contents
        for (ItemStack itemStack : p.getInventory()) {
            if (itemStack != null && !itemStack.getType().equals(Material.AIR))
                p.getWorld().dropItemNaturally(p.getLocation(), itemStack);
        }
        p.getInventory().clear();

        ItemStack[] armorContents = p.getInventory().getArmorContents();
        for (ItemStack itemStack : armorContents) {
            if (itemStack != null && !itemStack.getType().equals(Material.AIR))
                p.getWorld().dropItemNaturally(p.getLocation(), itemStack);
        }
        p.getInventory().setArmorContents(null);

        // Count the kill towards the player that last damaged them
        if (damageCause instanceof EntityDamageByEntityEvent) {
            // Ban the player if he dies and the game has already started
            String deathMessage;
            Entity killer = ((EntityDamageByEntityEvent) p.getLastDamageCause()).getDamager();

            if (!(killer instanceof Player)) {
                // Ban Player
                Bukkit.getBanList(BanList.Type.NAME).addBan(e.getPlayer().getName(), Main.prefix + "Du hast gelefted wie du wenige Leben hattest", null, "Tot");
                p.kickPlayer(Main.prefix + "§cDu bist gestorben.");

                deathMessage = Main.prefix + "§6" + e.getPlayer().getName() + " §7hat geleftet wie dieser Spieler wenige Leben hatte! §cAusgeschieden§7!";
                e.setQuitMessage(deathMessage);

                return;
            }

            deathMessage = Main.prefix + "§6" + p.getName() + "§4 hatte zu viel Angst vor §6" + killer.getName() + ".";
            e.setQuitMessage(deathMessage);

            // Ban the player
            Bukkit.getBanList(BanList.Type.NAME).addBan(p.getName(), Main.prefix + "Du bist gestorben", null, "Tot");
            p.kickPlayer(Main.prefix + "§cDu bist gestorben.");

            // Give the killer a kill in stats
            if (GlobalVariables.statsDisabled) {
                killer.sendMessage(Main.prefix + "Du hast §4keinen Kill§7 in den Stats bekommen!");
            } else {
                WaloDatabase.addPlayerKill((Player) killer);
            }

        } else {
            // Ban Player
            Bukkit.getBanList(BanList.Type.NAME).addBan(e.getPlayer().getName(), Main.prefix + "Du hast gelefted wie du wenige leben hattest", null, "Tot");
            p.kickPlayer(Main.prefix + "§cDu bist gestorben.");
            Bukkit.broadcastMessage(Main.prefix + "§6" + e.getPlayer().getName() + " §7hat geleftet wie dieser Spieler wenige leben hatte! §cAusgeschieden§7!");
        }

    }
}

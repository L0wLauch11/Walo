package me.lowlauch.walo.eventlistening.events;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.database.WaloDatabase;
import me.lowlauch.walo.discord.webhook.DiscordWebHook;
import me.lowlauch.walo.misc.GlobalVariables;
import me.lowlauch.walo.misc.StringUtils;
import me.lowlauch.walo.teams.Teams;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.ArrayList;

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

        // Detect wins
        ArrayList<String> aliveTeams = new ArrayList<>();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers())  {
            String teamOfPlayer = Teams.getTeamOfPlayer(onlinePlayer);
            if (!aliveTeams.contains(teamOfPlayer))
                aliveTeams.add(teamOfPlayer);
        }

        if (aliveTeams.size() <= 1) {
            String onlinePlayersString = "";
            String stylizedComma = ChatColor.GRAY + ", " + ChatColor.GREEN;
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayersString += onlinePlayer.getName() + stylizedComma;
            }

            onlinePlayersString = onlinePlayersString.substring(0, onlinePlayersString.length()-stylizedComma.length());
            int posLastComma = onlinePlayersString.lastIndexOf(", ");

            onlinePlayersString = StringUtils.replaceCharAt(onlinePlayersString, posLastComma, '#');
            onlinePlayersString = onlinePlayersString.replaceAll("#", ChatColor.GRAY + " und");
            String finalMessage = ChatColor.GREEN + onlinePlayersString + ChatColor.GOLD + ChatColor.BOLD + " hat das Walo gewonnen!";

            DiscordWebHook.sendText("@everyone\n" + onlinePlayersString + " hat das Walo gewonnen!");
            Bukkit.getServer().broadcastMessage(Main.prefix + finalMessage);

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.sendTitle("§c§kw§r§c " + Teams.getTeamName(aliveTeams.get(0)) + " §kw", "§6hat Walo gewonnen");
                WaloDatabase.addPlayerWin(onlinePlayer);
            }

            for(OfflinePlayer player : Bukkit.getOfflinePlayers()) {
                Bukkit.getBanList(BanList.Type.NAME).pardon(player.getName());
            }
        }
    }
}
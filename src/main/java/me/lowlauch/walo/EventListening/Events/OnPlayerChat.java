package me.lowlauch.walo.EventListening.Events;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.Teams.Teams;
import me.lowlauch.walo.WaloConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.UUID;

public class OnPlayerChat implements Listener {
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        String message = e.getMessage();

        // Renaming teams
        if (!Teams.playersWhoWantToRenameTheirTeam.isEmpty()) {
            UUID removeThisPlayer = null;
            for (UUID playerUUID : Teams.playersWhoWantToRenameTheirTeam) {
                if (p.getUniqueId().equals(playerUUID)) {
                    removeThisPlayer = playerUUID;

                    String teamName = message.replace("&", "§");

                    Teams.renameTeam(Teams.getTeamOfPlayer(p), teamName + " §r§f");
                    p.sendMessage(Main.prefix + ChatColor.BOLD + "Euer Teamname ist jetzt: §r§f" + teamName);
                    e.setCancelled(true);
                }
            }

            if (removeThisPlayer != null)
                Teams.playersWhoWantToRenameTheirTeam.remove(removeThisPlayer);
        }

        // Inviting players
        if (!Teams.playersWhoWantToInviteSomeone.isEmpty()) {
            UUID removeThisPlayer = null;
            for (UUID playerUUID : Teams.playersWhoWantToInviteSomeone) {
                if (p.getUniqueId().equals(playerUUID)) {
                    removeThisPlayer = playerUUID;

                    // Find player
                    Player invitedPlayer = Bukkit.getPlayer(message);

                    if (!Bukkit.getOnlinePlayers().contains(invitedPlayer)) {
                        p.sendMessage(Main.prefix + ChatColor.DARK_RED + "Der Spieler " + ChatColor.GOLD + message + ChatColor.DARK_RED + " wurde nicht gefunden!");
                        e.setCancelled(true);
                        return;
                    }

                    Teams.invitePlayer(p, invitedPlayer);
                    e.setCancelled(true);
                }
            }

            if (removeThisPlayer != null)
                Teams.playersWhoWantToInviteSomeone.remove(removeThisPlayer);
        }

        // Better chat formatting
        e.setFormat(p.getDisplayName() + "§7: " + message);
        e.setMessage("");
    }
}

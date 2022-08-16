package me.lowlauch.walo.EventListening.Events;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.Teams.Teams;
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

        // Better chat formatting
        e.setFormat(p.getDisplayName() + "§7: " + message);
        e.setMessage("");
    }
}

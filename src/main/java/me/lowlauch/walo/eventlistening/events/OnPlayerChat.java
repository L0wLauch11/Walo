package me.lowlauch.walo.eventlistening.events;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.WaloConfig;
import me.lowlauch.walo.teams.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;

public class OnPlayerChat implements Listener {
    public static String prepareMessageForChecking(String message) {
        message = message.toLowerCase();
        message = message.replaceAll("4", "a")
                .replaceAll("3", "e")
                .replaceAll("1", "i")
                .replaceAll("5", "s")
                .replaceAll("7", "t")
                .replaceAll("0", "o");
        message = message.replaceAll("[^a-zA-Z]", "");

        return message;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        String message = e.getMessage();

        // Filter swearwords
        if (WaloConfig.getFilterBadWords()) {
            String messageForChecking = prepareMessageForChecking(message);

            String badWordsFilePath = Main.getInstance().getDataFolder().getAbsolutePath() + "/badwords.txt";
            try {
                BufferedReader reader = new BufferedReader(new FileReader(badWordsFilePath));

                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.isEmpty()
                            || line.startsWith("#")) { // comment
                        continue;
                    }

                    Bukkit.getLogger().info(line);

                    if (messageForChecking.contains(line.toLowerCase())) {
                        p.sendMessage(Main.prefix + "Kraftausdrücke werden nicht geduldet.");
                        e.setCancelled(true);
                        return;
                    }
                }
                reader.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

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

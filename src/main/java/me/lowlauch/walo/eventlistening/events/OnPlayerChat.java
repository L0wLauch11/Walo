package me.lowlauch.walo.eventlistening.events;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.WaloConfig;
import me.lowlauch.walo.chatactions.ChatAction;
import me.lowlauch.walo.chatactions.ChatActionManager;
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

        // Handle current chat action of player
        ChatAction currentChatAction = ChatActionManager.getChatAction(p);
        if (currentChatAction != null) {
            currentChatAction.behaviour(e);
            ChatActionManager.setChatAction(p, null);
        }

        // Better chat formatting
        e.setFormat(p.getDisplayName() + "§7: " + message);
        e.setMessage("");
    }
}

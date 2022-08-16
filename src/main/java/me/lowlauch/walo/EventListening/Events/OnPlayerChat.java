package me.lowlauch.walo.EventListening.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class OnPlayerChat implements Listener {
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        // Better chat formatting
        String message = e.getMessage();
        e.setFormat(e.getPlayer().getDisplayName() + "§7: " + message);
        e.setMessage("");
    }
}

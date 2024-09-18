package me.lowlauch.walo.chatactions;

import org.bukkit.event.player.AsyncPlayerChatEvent;

public interface ChatAction {
    // Sadly, functions in an interface cannot be static.
    void behaviour(AsyncPlayerChatEvent e);
}

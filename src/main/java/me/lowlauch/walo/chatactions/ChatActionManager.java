package me.lowlauch.walo.chatactions;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class ChatActionManager {
    private static HashMap<String, ChatAction> playerChatActions = new HashMap<>();

    public static void setChatAction(Player player, ChatAction chatAction) {
        String playerName = player.getName();
        playerChatActions.put(playerName, chatAction);
    }

    public static ChatAction getChatAction(Player player) {
        String playerName = player.getName();
        return playerChatActions.get(playerName);
    }
}

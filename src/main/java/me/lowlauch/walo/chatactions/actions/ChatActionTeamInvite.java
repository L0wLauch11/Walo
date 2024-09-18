package me.lowlauch.walo.chatactions.actions;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.chatactions.ChatAction;
import me.lowlauch.walo.teams.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatActionTeamInvite implements ChatAction {
    @Override
    public void behaviour(AsyncPlayerChatEvent e) {
        // Find player
        Player invitedPlayer = Bukkit.getPlayer(e.getMessage());

        if (!Bukkit.getOnlinePlayers().contains(invitedPlayer)) {
            e.getPlayer().sendMessage(Main.prefix + ChatColor.DARK_RED + "Der Spieler " + ChatColor.GOLD + e.getMessage() + ChatColor.DARK_RED + " wurde nicht gefunden!");
            e.setCancelled(true);
            return;
        }

        Teams.invitePlayer(e.getPlayer(), invitedPlayer);
        e.setCancelled(true);
    }
}

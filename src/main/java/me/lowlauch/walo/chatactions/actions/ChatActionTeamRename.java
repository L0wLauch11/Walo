package me.lowlauch.walo.chatactions.actions;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.WaloConfig;
import me.lowlauch.walo.chatactions.ChatAction;
import me.lowlauch.walo.teams.Teams;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatActionTeamRename implements ChatAction {
    @Override
    public void behaviour(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        String teamName = e.getMessage().replace("&", "§");
        if (teamName.length() >= 16) {
            teamName = teamName.substring(0, 16);
        }

        Teams.renameTeam(Teams.getTeamOfPlayer(p), teamName + " §r§f");
        p.sendMessage(Main.prefix + ChatColor.BOLD + "Euer Teamname ist jetzt: §r§f" + teamName);
        e.setCancelled(true);
    }
}

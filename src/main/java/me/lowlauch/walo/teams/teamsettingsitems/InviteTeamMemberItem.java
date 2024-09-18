package me.lowlauch.walo.teams.teamsettingsitems;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.chatactions.ChatActionManager;
import me.lowlauch.walo.chatactions.actions.ChatActionTeamInvite;
import me.lowlauch.walo.teams.TeamItem;
import me.lowlauch.walo.teams.Teams;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InviteTeamMemberItem implements TeamItem {
    @Override
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player))
            return;

        Player p = (Player) e.getWhoClicked();
        ChatActionManager.setChatAction(p, new ChatActionTeamInvite());
        p.sendMessage(Main.prefix + ChatColor.BOLD + "Wen möchtest du einladen? (Chat):");
        p.closeInventory();
    }

    @Override
    public String displayName() {
        return TeamsInventoryItem.inviteMemberDisplayName;
    }
}

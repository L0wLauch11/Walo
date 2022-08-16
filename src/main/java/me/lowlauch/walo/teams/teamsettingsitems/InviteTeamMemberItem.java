package me.lowlauch.walo.teams.teamsettingsitems;

import me.lowlauch.walo.Main;
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
        Teams.playersWhoWantToInviteSomeone.add(p.getUniqueId());
        p.sendMessage(Main.prefix + ChatColor.BOLD + "Wen möchtest du einladen?:");
        p.closeInventory();
    }

    @Override
    public String displayName() {
        return TeamsInventoryItem.inviteMemberDisplayName;
    }
}

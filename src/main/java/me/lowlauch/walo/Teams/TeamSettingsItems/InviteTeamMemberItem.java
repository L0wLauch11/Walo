package me.lowlauch.walo.Teams.TeamSettingsItems;

import me.lowlauch.walo.Teams.TeamItem;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InviteTeamMemberItem implements TeamItem {
    @Override
    public void onClick(InventoryClickEvent e) {

    }

    @Override
    public String displayName() {
        return TeamsInventoryItem.inviteMemberDisplayName;
    }
}

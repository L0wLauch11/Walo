package me.lowlauch.walo.Teams.TeamSettingsItems;

import me.lowlauch.walo.Teams.TeamItem;
import me.lowlauch.walo.Teams.Teams;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class LeaveTeamItem implements TeamItem {
    @Override
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player))
            return;

        Player p = (Player) e.getWhoClicked();

        Teams.leaveTeam(p);
    }

    @Override
    public String displayName() {
        return TeamsInventoryItem.leaveTeamDisplayName;
    }
}

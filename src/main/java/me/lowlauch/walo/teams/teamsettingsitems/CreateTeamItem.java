package me.lowlauch.walo.teams.teamsettingsitems;

import me.lowlauch.walo.teams.TeamItem;
import me.lowlauch.walo.teams.Teams;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class CreateTeamItem implements TeamItem {
    @Override
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player))
            return;

        Player p = (Player) e.getWhoClicked();
        Teams.createTeamFor(p);
        p.closeInventory();
    }

    @Override
    public String displayName() {
        return TeamsInventoryItem.createTeamDisplayName;
    }
}

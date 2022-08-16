package me.lowlauch.walo.Teams.TeamSettingsItems;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.Teams.TeamItem;
import me.lowlauch.walo.Teams.Teams;
import me.lowlauch.walo.WaloConfig;
import org.bukkit.ChatColor;
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

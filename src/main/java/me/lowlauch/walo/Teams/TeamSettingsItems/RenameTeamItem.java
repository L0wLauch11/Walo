package me.lowlauch.walo.Teams.TeamSettingsItems;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.Teams.TeamItem;
import me.lowlauch.walo.Teams.Teams;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class RenameTeamItem implements TeamItem {
    @Override
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player))
            return;

        Player p = (Player) e.getWhoClicked();
        Teams.playersWhoWantToRenameTheirTeam.add(p.getUniqueId());
        p.sendMessage(Main.prefix + ChatColor.BOLD + "Bitte gib einen Team-Namen ein:");
        p.closeInventory();
    }

    @Override
    public String displayName() {
        return TeamsInventoryItem.renameTeamDisplayName;
    }
}

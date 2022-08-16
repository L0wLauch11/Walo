package me.lowlauch.walo.Teams.TeamSettingsItems;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.Teams.TeamItem;
import me.lowlauch.walo.Teams.Teams;
import me.lowlauch.walo.WaloConfig;
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

        WaloConfig.save();
    }

    @Override
    public String displayName() {
        return TeamsInventoryItem.inviteMemberDisplayName;
    }
}

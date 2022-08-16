package me.lowlauch.walo.EventListening.Events;

import me.lowlauch.walo.Teams.TeamItem;
import me.lowlauch.walo.Teams.TeamSettingsItems.CreateTeamItem;
import me.lowlauch.walo.Teams.TeamSettingsItems.InviteTeamMemberItem;
import me.lowlauch.walo.Teams.TeamSettingsItems.LeaveTeamItem;
import me.lowlauch.walo.Teams.TeamSettingsItems.RenameTeamItem;
import me.lowlauch.walo.misc.GlobalVariables;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;

public class OnInventoryClick implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (GlobalVariables.started)
            return;

        ArrayList<TeamItem> teamItems = new ArrayList<>();

        teamItems.add(new CreateTeamItem());
        teamItems.add(new InviteTeamMemberItem());
        teamItems.add(new LeaveTeamItem());
        teamItems.add(new RenameTeamItem());

        for (TeamItem teamItem : teamItems) {
            if (!e.getCurrentItem().hasItemMeta())
                continue;

            if (e.getCurrentItem().getItemMeta().getDisplayName().equals(teamItem.displayName())) {
                teamItem.onClick(e);
                e.setCancelled(true);
            }
        }
    }
}

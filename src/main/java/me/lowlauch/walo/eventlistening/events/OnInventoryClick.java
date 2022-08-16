package me.lowlauch.walo.eventlistening.events;

import me.lowlauch.walo.teams.TeamItem;
import me.lowlauch.walo.teams.teamsettingsitems.CreateTeamItem;
import me.lowlauch.walo.teams.teamsettingsitems.InviteTeamMemberItem;
import me.lowlauch.walo.teams.teamsettingsitems.LeaveTeamItem;
import me.lowlauch.walo.teams.teamsettingsitems.RenameTeamItem;
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

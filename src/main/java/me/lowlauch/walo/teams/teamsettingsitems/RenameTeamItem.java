package me.lowlauch.walo.teams.teamsettingsitems;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.teams.TeamItem;
import me.lowlauch.walo.teams.Teams;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class RenameTeamItem implements TeamItem {
    @Override
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player))
            return;

        Player p = (Player) e.getWhoClicked();

        if (Teams.getTeamOfPlayer(p) == null) {
            p.sendMessage(Main.prefix + ChatColor.BOLD + "Erstelle bitte vorher ein Team.");
            return;
        }

        Teams.playersWhoWantToRenameTheirTeam.add(p.getUniqueId());
        p.sendMessage(Main.prefix + "§1F§2a§3r§4b§5e§6n§7codes: §f&f §0&0 §1&1 §2&2 §3&3 §4&4 §5&5 §6&6 §7&7 §8&8 §9&9 §f§l&l§r §m&m§r §n&n§r §o&o§r §r&r");
        p.sendMessage(Main.prefix + ChatColor.BOLD + "Bitte gib einen Team-Namen ein (max. 16 Zeichen):");
        p.closeInventory();
    }

    @Override
    public String displayName() {
        return TeamsInventoryItem.renameTeamDisplayName;
    }
}

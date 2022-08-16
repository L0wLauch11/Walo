package me.lowlauch.walo.teams;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface TeamItem {
    void onClick(InventoryClickEvent e);

    String displayName();
}

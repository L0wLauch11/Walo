package me.lowlauch.walo.teams.teamsettingsitems;

import me.lowlauch.walo.misc.ItemUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TeamsInventoryItem {
    public static String displayName = ChatColor.BOLD + "Verwalte dein Team";
    public static String createTeamDisplayName = ChatColor.BOLD + "Erstelle ein Team";
    public static String renameTeamDisplayName = ChatColor.BOLD + "Benenne dein Team um";
    public static String inviteMemberDisplayName = ChatColor.BOLD + "Lade jemanden in dein Team ein";
    public static String leaveTeamDisplayName = ChatColor.BOLD + "Verlasse das Team";

    public static void addItemToPlayerInventory(Player p) {
        ItemStack teamsItem = ItemUtil.createItemDisplayName(displayName, Material.BOOK_AND_QUILL, 1);
        ItemMeta itemMeta = teamsItem.getItemMeta();
        itemMeta.addEnchant(Enchantment.DURABILITY, 69, true);
        teamsItem.setItemMeta(itemMeta);

        p.getInventory().setItem(8, teamsItem);
        p.updateInventory();
    }

    public static void process(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (!p.getItemInHand().hasItemMeta())
            return;

        if (!p.getItemInHand().getItemMeta().getDisplayName().equals(displayName))
            return;

        ItemStack createTeamItem = ItemUtil.createItemDisplayName(createTeamDisplayName, Material.WOOL, 1, DyeColor.GREEN.getData());
        ItemMeta itemMeta = createTeamItem.getItemMeta();
        itemMeta.addEnchant(Enchantment.DURABILITY, 69, true);
        createTeamItem.setItemMeta(itemMeta);

        ItemStack leaveTeamItem = ItemUtil.createItemDisplayName(leaveTeamDisplayName, Material.WOOL, 1, DyeColor.RED.getData());
        itemMeta = leaveTeamItem.getItemMeta();
        itemMeta.addEnchant(Enchantment.DURABILITY, 69, true);
        leaveTeamItem.setItemMeta(itemMeta);

        ItemStack inviteMemberItem = ItemUtil.createItemDisplayName(inviteMemberDisplayName, Material.WOOL, 1, DyeColor.BLUE.getData());
        itemMeta = inviteMemberItem.getItemMeta();
        itemMeta.addEnchant(Enchantment.DURABILITY, 69, true);
        inviteMemberItem.setItemMeta(itemMeta);

        ItemStack renameTeamItem = ItemUtil.createItemDisplayName(renameTeamDisplayName, Material.WOOL, 1, DyeColor.YELLOW.getData());
        itemMeta = renameTeamItem.getItemMeta();
        itemMeta.addEnchant(Enchantment.DURABILITY, 69, true);
        renameTeamItem.setItemMeta(itemMeta);

        Inventory teamsInventory = Bukkit.createInventory(null, 9, "Team Einstellungen");
        teamsInventory.setItem(1, createTeamItem);
        teamsInventory.setItem(3, inviteMemberItem);
        teamsInventory.setItem(5, renameTeamItem);
        teamsInventory.setItem(7, leaveTeamItem);
        p.openInventory(teamsInventory);
    }
}

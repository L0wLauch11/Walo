package me.lowlauch.walo.eventlistening.events;

import me.lowlauch.walo.Main;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public class OnCraft implements Listener {
    @EventHandler
    public void onCraft(CraftItemEvent e) {
        // Disable Golden OP Apples
        ItemStack itemStack = e.getRecipe().getResult();
        ItemStack enchantedGoldenApple = new ItemStack(Material.GOLDEN_APPLE, 1, (short) 1);

        if (itemStack.equals(enchantedGoldenApple)) {
            e.getInventory().setResult(new ItemStack(Material.AIR));
            for (HumanEntity he : e.getViewers()) {
                if (he instanceof Player) {
                    he.sendMessage(Main.prefix + "§6OP Apples§7 sind §cverboten§7!");
                }
            }
        }
    }
}

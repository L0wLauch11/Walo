package me.lowlauch.walo.eventlistening.events;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.misc.GlobalVariables;
import me.lowlauch.walo.misc.WorldUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class OnPlayerTravel implements Listener {
    @EventHandler
    public void onPlayerTravel(final PlayerTeleportEvent e) {
        boolean destinationIsNether = e.getTo().getWorld().getEnvironment().equals(World.Environment.NETHER);
        boolean originIsNether = e.getFrom().getWorld().getEnvironment().equals(World.Environment.NETHER);

        // If the nether is disabled then stop the event
        if (!GlobalVariables.started && destinationIsNether) {
            e.getPlayer().sendMessage(Main.prefix + "§cDer Nether ist erst nach dem Spielstart verfügbar!");
            if (e.getFrom() != null)
                e.getPlayer().teleport(new Location(e.getFrom().getWorld(), e.getFrom().getX(), e.getFrom().getY(), e.getFrom().getZ()));
            e.setCancelled(true);

            return;
        }

        if (!destinationIsNether || originIsNether || !(e.getPlayer().getGameMode().equals(GameMode.SURVIVAL)))
            return;

        // Overwrite nether difficulty
        e.getTo().getWorld().setDifficulty(Difficulty.NORMAL);

        // Update nether portal location, useful for anti portal griefing
        GlobalVariables.netherPortalLocation = e.getTo();

        final double playerX = e.getTo().getX();
        final double playerY = e.getTo().getY();
        final double playerZ = e.getTo().getZ();

        final int rad = 15;
        final int obsidianCheckRad = 5; // is smaller because takes lots of performance

        // Make a protecting bedrock circle under the player
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
            Material[] unfairBlocks = {
                Material.LAVA,
                Material.STATIONARY_LAVA,
                Material.FIRE,
                Material.BED
            };

            // Replace flying obsidian not attached to portals, avoid being blocked in by mean players
            for (int i = -obsidianCheckRad; i < obsidianCheckRad; i++) {
                for (int j = -obsidianCheckRad; j < obsidianCheckRad; j++) {
                    for (int k = -obsidianCheckRad; k < obsidianCheckRad; k++) {
                        Block blockAtCurrentPos = e.getTo().getWorld().getBlockAt(new Location(e.getTo().getWorld(), playerX + i, playerY + j, playerZ + k));

                        if (!WorldUtil.blockIsAdjacentToMaterial(blockAtCurrentPos, Material.PORTAL)) {
                            blockAtCurrentPos.setType(Material.AIR);
                        }
                    }
                }
            }

            // Spawn a protecting bedrock ring
            for (int i = -rad; i < rad; i++) {
                for (int j = -rad; j < rad; j++) {
                    for (int k = -rad; k < rad; k++) {
                        Block blockAtCurrentPos = e.getTo().getWorld().getBlockAt(new Location(e.getTo().getWorld(), playerX + i, playerY + j, playerZ + k));

                        for (Material unfairBlock : unfairBlocks) {
                            if (blockAtCurrentPos.getType().equals(unfairBlock)) {
                                blockAtCurrentPos.setType(Material.AIR);
                            }
                        }

                    }

                    // Spawn a protecting bedrock floor under players feet
                    e.getTo().getWorld().getBlockAt(new Location(e.getTo().getWorld(), playerX + i, playerY - 2, playerZ + j)).setType(Material.BEDROCK);
                }
            }
        }, 10L);// 60 L == 3 sec, 20 ticks == 1 sec
    }
}

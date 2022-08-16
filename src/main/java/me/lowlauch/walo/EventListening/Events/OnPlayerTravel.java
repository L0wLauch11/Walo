package me.lowlauch.walo.EventListening.Events;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.misc.GlobalVariables;
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

        // Spawn a protecting bedrock ring
        final double playerX = e.getTo().getX();
        final double playerY = e.getTo().getY();
        final double playerZ = e.getTo().getZ();

        final int rad = 15;
        // Make a protecting bedrock circle under the player
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
            for (int i = -rad; i < rad; i++) {
                for (int j = -rad; j < rad; j++) {
                    for (int k = -rad; k < rad; k++) {
                        Block blockAtCurrentPos = e.getTo().getWorld().getBlockAt(new Location(e.getTo().getWorld(), playerX + i, playerY + j, playerZ + k));

                        // Replace unfair objects with nothing
                        Material[] unfairBlocks = {

                                Material.LAVA,
                                Material.STATIONARY_LAVA,
                                Material.FIRE,
                                Material.BED

                        };

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

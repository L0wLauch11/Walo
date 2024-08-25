package me.lowlauch.walo.commands.subcommands;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.WaloConfig;
import me.lowlauch.walo.commands.SubCommand;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetupBorderCommand implements SubCommand {

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        // Set world border
        World world = Bukkit.getWorld("world");
        WorldBorder worldBorder = world.getWorldBorder();

        double centerX = 0;
        double centerZ = 0;
        double borderSize = 25;

        // If the server crashed and restarted, we wouldn't want to reset the border.
        if (worldBorder.getSize() > WaloConfig.getWorldBorderSize() + 1) {
            worldBorder.setCenter(centerX, centerZ);
            worldBorder.setSize(borderSize, 0);
        }

        // Replace floor of water with grass
        for (int i = 255; i > 0; i--) {
            Block block = world.getBlockAt(0, i, 0);

            if (block.getType() == Material.STATIONARY_WATER) {
                // -1 to get the blocks edging towards the border too
                for (int j = -1; j <= borderSize; j++) {
                    for (int k = -1; k <= borderSize; k++) {
                        world.getBlockAt((int) (centerX - borderSize / 2 + j), i, (int) (centerZ - borderSize / 2 + k))
                                .setType(Material.DIRT);
                    }
                }

                break;
            }
        }

        // Teleport player to 0, 0 and set the world spawn there
        if (commandSender instanceof Player) {
            Player p = (Player) commandSender;
            p.teleport(new Location(world, 0, 80, 0));
            world.setSpawnLocation(0, 80, 0);

            commandSender.sendMessage(Main.prefix + "Das Spiel wurde hergerichtet.");
        }
    }

    @Override
    public String getName() {
        return "setupborder";
    }

    @Override
    public String getHelp() {
        return ": §7Setzt die Border bei 0, 0 auf 25. Nützlich vor dem Spielstart.";
    }

    @Override
    public boolean requiresOp() {
        return true;
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }

    @Override
    public int requiredArguments() {
        return 0;
    }
}

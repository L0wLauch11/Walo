package me.lowlauch.walo.commands.subcommands;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetupBorderCommand implements SubCommand {

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        // Set worldborder to lobby
        World world = Bukkit.getWorld("world");
        WorldBorder worldBorder = world.getWorldBorder();

        worldBorder.setCenter(0, 0);
        worldBorder.setSize(25, 0);

        // Teleport player to 0, 0 and set the worldspawn there
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

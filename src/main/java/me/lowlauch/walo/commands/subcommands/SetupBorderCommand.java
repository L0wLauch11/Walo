package me.lowlauch.walo.commands.subcommands;

import me.lowlauch.walo.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.WorldBorder;
import org.bukkit.command.CommandSender;

public class SetupBorderCommand implements SubCommand {

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        // Set worldborder to lobby
        WorldBorder worldBorder = Bukkit.getWorld("world").getWorldBorder();

        worldBorder.setCenter(0, 0);
        worldBorder.setSize(25, 0);
    }

    @Override
    public String getName() {
        return "setupborder";
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

package me.lowlauch.Walo.Commands.SubCommands;

import me.lowlauch.Walo.Commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.WorldBorder;
import org.bukkit.command.CommandSender;

public class SetupBorderCommand implements SubCommand
{

    @Override
    public void execute(CommandSender commandSender, String[] args)
    {
        // Set worldborder to lobby
        WorldBorder worldBorder = Bukkit.getWorld("world").getWorldBorder();

        worldBorder.setCenter(0, 0);
        worldBorder.setSize(25, 0);
    }

    @Override
    public String getName()
    {
        return "setupborder";
    }

    @Override
    public boolean needsOp()
    {
        return true;
    }

    @Override
    public boolean needsPlayer()
    {
        return false;
    }

    @Override
    public int neededArguments()
    {
        return 0;
    }
}

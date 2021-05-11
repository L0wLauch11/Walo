package me.lowlauch.Walo.Commands.SubCommands;

import me.lowlauch.Walo.Commands.SubCommand;
import me.lowlauch.Walo.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import static me.lowlauch.Walo.Commands.CommandVariables.*;

public class DisableStatsCommand implements SubCommand
{
    @Override
    public void execute(CommandSender commandSender, String[] args)
    {
        statsDisabled = true;
        Bukkit.getServer().broadcastMessage(Main.prefix + "In diesem Game werden §4§lkeine §7Stats aufgezeichnet!");
    }

    @Override
    public String getName()
    {
        return "disablestats";
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

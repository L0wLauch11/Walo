package me.lowlauch.walo.commands.subcommands;

import me.lowlauch.walo.commands.SubCommand;
import me.lowlauch.walo.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import static me.lowlauch.walo.misc.GlobalVariables.*;

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

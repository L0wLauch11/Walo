package me.lowlauch.walo.commands.subcommands;

import me.lowlauch.walo.commands.SubCommand;
import me.lowlauch.walo.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class BroadcastCommand implements SubCommand
{
    @Override
    public void execute(CommandSender commandSender, String[] args)
    {
        String message = "";
        for(int i = 1; i < args.length; i++)
        {
            message += args[i] + " ";
        }
        message = message.replaceAll("&", "§");

        Bukkit.getServer().broadcastMessage(Main.prefix + message);
    }

    @Override
    public String getName()
    {
        return "bc";
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
        return 1;
    }
}

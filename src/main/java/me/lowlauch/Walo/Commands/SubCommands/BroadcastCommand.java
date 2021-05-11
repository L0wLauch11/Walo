package me.lowlauch.Walo.Commands.SubCommands;

import me.lowlauch.Walo.Commands.SubCommand;
import me.lowlauch.Walo.Main;
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

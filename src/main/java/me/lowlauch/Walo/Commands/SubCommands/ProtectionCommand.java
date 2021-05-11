package me.lowlauch.Walo.Commands.SubCommands;

import me.lowlauch.Walo.Commands.SubCommand;
import me.lowlauch.Walo.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import static me.lowlauch.Walo.Commands.CommandVariables.*;

public class ProtectionCommand implements SubCommand
{
    @Override
    public void execute(CommandSender commandSender, String[] args)
    {
        if(args.length >= 2)
        {
            // Toggle protection period
            try
            {
                protection = Boolean.parseBoolean(args[1]);
            } catch(Exception ex)
            {
                // Let the player know if something failed
                commandSender.sendMessage(Main.prefix + "Du musst true oder false eingeben!: " + ex.toString());
            }

            String string = "Die Schutzzeit ist " + protection;

            string = string.replaceAll("false", "§caus");
            string = string.replaceAll("true", "§aan");

            Bukkit.broadcastMessage(Main.prefix + string + "§7!");
        } else
        {
            // Set protection period to true or false
            protection = !protection;

            String string = "Die Schutzzeit ist " + protection;

            string = string.replaceAll("false", "§caus");
            string = string.replaceAll("true", "§aan");

            Bukkit.broadcastMessage(Main.prefix + string + "§7!");
        }
    }

    @Override
    public String getName()
    {
        return "protection";
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

package me.lowlauch.Walo.Commands.Subcommands;

import me.lowlauch.Walo.Commands.SubCommand;
import me.lowlauch.Walo.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.lowlauch.Walo.Commands.CommandVariables.*;

public class StopCommand implements SubCommand
{
    @Override
    public void execute(CommandSender commandSender, String[] args)
    {
        for (Player p : Bukkit.getOnlinePlayers())
            p.setGameMode(GameMode.ADVENTURE);

        started = false;
        Bukkit.broadcastMessage(Main.prefix + "Walo wurde §4gestoppt§7.");
    }

    @Override
    public String getName()
    {
        return "stop";
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

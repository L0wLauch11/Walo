package me.lowlauch.Walo.Commands.SubCommands;

import me.lowlauch.Walo.Commands.SubCommand;
import me.lowlauch.Walo.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.lowlauch.Walo.Commands.CommandVariables.*;

public class ResumeCommand implements SubCommand
{
    @Override
    public void execute(CommandSender commandSender, String[] args)
    {
        comSender = commandSender;

        // Loop through all players and heal them and all that other stuff
        for (Player p : Bukkit.getOnlinePlayers())
        {
            p.setHealth(20.0f);
            p.setSaturation(20.0f);
            p.setFoodLevel(20);
            p.setGameMode(GameMode.SURVIVAL);
            p.setWalkSpeed(0.2f);

            p.playSound(p.getLocation(), Sound.NOTE_PLING, 10, 1);
        }

        // Some properties change
        Bukkit.dispatchCommand(comSender, "difficulty normal");

        started = true;
        Bukkit.broadcastMessage(Main.prefix + "Walo wurde §6fortgesetzt§7.");
    }

    @Override
    public String getName()
    {
        return "resume";
    }

    @Override
    public boolean needsOp()
    {
        return true;
    }

    @Override
    public boolean needsPlayer()
    {
        return true;
    }

    @Override
    public int neededArguments()
    {
        return 0;
    }
}

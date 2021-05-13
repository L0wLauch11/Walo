package me.lowlauch.walo.commands.subcommands;

import me.lowlauch.walo.ScoreboardHandler;
import me.lowlauch.walo.commands.SubCommand;
import me.lowlauch.walo.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.lowlauch.walo.misc.GlobalVariables.*;

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

        // Update scoreboard
        ScoreboardHandler.updateScoreboard(true);
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

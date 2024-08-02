package me.lowlauch.walo.commands.subcommands;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.ScoreboardHandler;
import me.lowlauch.walo.commands.SubCommand;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import static me.lowlauch.walo.misc.GlobalVariables.started;

public class ResumeCommand implements SubCommand {
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        // Loop through all players and heal them and all that other stuff
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.setHealth(20.0f);
            p.setSaturation(20.0f);
            p.setFoodLevel(20);
            p.setGameMode(GameMode.SURVIVAL);
            p.setWalkSpeed(0.2f);

            p.playSound(p.getLocation(), Sound.NOTE_PLING, 10, 1);
        }

        // Some properties change
        for (World w : Bukkit.getServer().getWorlds()) {
            w.setDifficulty(Difficulty.NORMAL);
        }

        started = true;
        Bukkit.broadcastMessage(Main.prefix + "Walo wurde §6fortgesetzt§7.");

        // Update scoreboard
        ScoreboardHandler.updateScoreboard();
    }

    @Override
    public String getName() {
        return "resume";
    }

    @Override
    public String getHelp() {
        return ": §7Führt ein Walo fort, z. B. nach einem Serverneustart (instabil, nur im Notfall zu gebrauchen)";
    }

    @Override
    public boolean requiresOp() {
        return true;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public int requiredArguments() {
        return 0;
    }
}

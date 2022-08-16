package me.lowlauch.walo.commands.subcommands;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.WaloConfig;
import me.lowlauch.walo.commands.SubCommand;
import me.lowlauch.walo.database.WaloDatabase;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatsCommand implements SubCommand {
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        WaloConfig.reload();

        Player checkStatsPlayer;

        if (args.length != 1) {
            // Display stats for another player
            checkStatsPlayer = Bukkit.getPlayer(args[1]);
        } else {
            // Show the stats for himself
            checkStatsPlayer = (Player) commandSender;
        }

        if (checkStatsPlayer != null) {
            String kills = WaloDatabase.getPlayerKills(checkStatsPlayer);

            // Send him the stats for this player
            commandSender.sendMessage(Main.prefix + "Stats für: §a" + checkStatsPlayer.getName()
                    + "\n§6Kills: §7" + kills);
        } else
            commandSender.sendMessage(Main.prefix + "Dieser Spieler ist nicht online, oder existiert nicht.");
    }

    @Override
    public String getName() {
        return "stats";
    }

    @Override
    public String getHelp() {
        return "(Spieler): §7Zeigt die Statistiken eines Spielers an.";
    }

    @Override
    public boolean requiresOp() {
        return false;
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }

    @Override
    public int requiredArguments() {
        return 0;
    }
}

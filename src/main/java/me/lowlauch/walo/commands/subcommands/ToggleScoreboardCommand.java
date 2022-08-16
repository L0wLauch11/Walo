package me.lowlauch.walo.commands.subcommands;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.ScoreboardHandler;
import me.lowlauch.walo.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class ToggleScoreboardCommand implements SubCommand {
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        Player p = (Player) commandSender;

        if (p.hasMetadata("no-scoreboard")) {
            p.removeMetadata("no-scoreboard", Main.getInstance());
            ScoreboardHandler.updatePlayerScoreboard(p);
            p.sendMessage(Main.prefix + "Das Scoreboard wurde aktiviert.");
        } else {
            p.setMetadata("no-scoreboard", new FixedMetadataValue(Main.getInstance(), "a"));
            p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            p.sendMessage(Main.prefix + "Das Scoreboard wurde deaktiviert.");
        }
    }

    @Override
    public String getName() {
        return "scoreboard";
    }

    @Override
    public String getHelp() {
        return ": §7Deaktivert, bzw. Aktiviert das Scoreboard";
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

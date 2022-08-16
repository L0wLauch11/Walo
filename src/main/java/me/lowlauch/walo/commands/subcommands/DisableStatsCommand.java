package me.lowlauch.walo.commands.subcommands;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import static me.lowlauch.walo.misc.GlobalVariables.statsDisabled;

public class DisableStatsCommand implements SubCommand {
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        statsDisabled = true;
        Bukkit.getServer().broadcastMessage(Main.prefix + "In diesem Game werden §4§lkeine §7Stats aufgezeichnet!");
    }

    @Override
    public String getName() {
        return "disablestats";
    }

    @Override
    public String getHelp() {
        return ": §7Deaktiviert die Stats für ein Game";
    }

    @Override
    public boolean requiresOp() {
        return true;
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

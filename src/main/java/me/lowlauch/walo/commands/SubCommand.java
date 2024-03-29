package me.lowlauch.walo.commands;

import org.bukkit.command.CommandSender;

public interface SubCommand {
    void execute(CommandSender commandSender, String[] args);

    String getName();

    String getHelp();

    boolean requiresOp();

    boolean requiresPlayer();

    int requiredArguments();
}

package me.lowlauch.Walo.Commands;

import org.bukkit.command.CommandSender;

public interface SubCommand
{
    void execute(CommandSender commandSender, String[] args);

    String getName();
    boolean needsOp();
    boolean needsPlayer();
    int neededArguments();
}

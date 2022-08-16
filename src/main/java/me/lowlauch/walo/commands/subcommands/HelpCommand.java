package me.lowlauch.walo.commands.subcommands;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.commands.SubCommand;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

public class HelpCommand implements SubCommand {
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        for (SubCommand subCommand : Main.getCommandsManager().getSubCommands()) {
            if (!subCommand.requiresOp() || (commandSender.isOp())) {
                commandSender.sendMessage(ChatColor.GREEN + "/walo " + subCommand.getName() + " §6" + subCommand.getHelp());
            }
        }
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getHelp() {
        return ": §7Zeigt Hilfe zu allen Commands an.";
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

package me.lowlauch.walo.commands.subcommands;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class BroadcastCommand implements SubCommand {
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        String message = "";
        for (int i = 1; i < args.length; i++) {
            message += args[i] + " ";
        }
        message = message.replaceAll("&", "§");

        Bukkit.getServer().broadcastMessage(Main.prefix + message);
    }

    @Override
    public String getName() {
        return "bc";
    }

    @Override
    public String getHelp() {
        return "<Nachricht>: §7Schickt eine Nachricht an alle Spieler.";
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
        return 1;
    }
}

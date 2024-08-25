package me.lowlauch.walo.commands.subcommands;

import me.lowlauch.walo.commands.SubCommand;
import me.lowlauch.walo.discord.webhook.DiscordWebHook;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCommand implements SubCommand {
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (args[1].equalsIgnoreCase("xp")) {
            Player p = (Player) commandSender;
            p.setLevel(Integer.parseInt(args[2]));
        }
    }

    @Override
    public String getName() {
        return "test";
    }

    @Override
    public String getHelp() {
        return ": §7Dieser command wird nur fürs Plugin Testen verwendet. Ignoriere ihn";
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

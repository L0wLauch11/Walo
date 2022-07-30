package me.lowlauch.walo.commands.subcommands;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import static me.lowlauch.walo.misc.GlobalVariables.protection;

public class ProtectionCommand implements SubCommand {
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (args.length >= 2) {
            // Toggle protection period
            try {
                protection = Boolean.parseBoolean(args[1]);
            } catch (Exception ex) {
                // Let the player know if something failed
                commandSender.sendMessage(Main.prefix + "Du musst true oder false eingeben!: " + ex.toString());
            }

            String string = "Die Schutzzeit ist " + protection;

            string = string.replaceAll("false", "§caus");
            string = string.replaceAll("true", "§aan");

            Bukkit.broadcastMessage(Main.prefix + string + "§7!");
        } else {
            // Set protection period to true or false
            protection = !protection;

            String string = "Die Schutzzeit ist " + protection;

            string = string.replaceAll("false", "§caus");
            string = string.replaceAll("true", "§aan");

            Bukkit.broadcastMessage(Main.prefix + string + "§7!");
        }
    }

    @Override
    public String getName() {
        return "protection";
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

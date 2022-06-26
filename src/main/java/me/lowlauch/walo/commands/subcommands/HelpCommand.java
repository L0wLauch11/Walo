package me.lowlauch.walo.commands.subcommands;

import me.lowlauch.walo.commands.SubCommand;
import org.bukkit.command.CommandSender;

public class HelpCommand implements SubCommand
{
    @Override
    public void execute(CommandSender commandSender, String[] args)
    {
        if(commandSender.isOp())
        {
            commandSender.sendMessage("§7/walo §6start §7- §aStartet das Walo.\n" +
                    "§7/walo §6stop §7- §aStoppt das Walo.\n" +
                    "§7/walo §6resume §7- §aSetzt Walo fort, falls was schiefgegangen ist.\n" +
                    "§7/walo §6stats <Spieler> §7- §aZeigt die Stats eines Spielers an.\n" +
                    "§7/walo §6setupborder §7- §aSetzt die Border auf 25, 25.\n" +
                    "§7/walo §6disablestats §7- §aDeaktiviert temporär Stats.\n" +
                    "§7/walo §6protection §7- §aToggled manuell die Schutzzeit.\n" +
                    "§7/walo §6addmate <Spieler1> <Spieler2> §7- §aAdded einen Mate zu dem Team.\n" +
                    "§7/walo §6teamtag <Spieler> <String> §7- §aSetzt einen Team-Tag Text für alle " +
                    "Spieler in dem Team.\n" +
                    "§7/walo §6bc <Nachricht> §7- §aSendet eine Nachricht mit dem Walo Prefix an den ganzen Server");
        } else
        {
            commandSender.sendMessage("§7/walo §6stats [Spieler] §7- §aZeigt die Stats eines Spielers an.");
        }
    }

    @Override
    public String getName() {
        return "help";
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

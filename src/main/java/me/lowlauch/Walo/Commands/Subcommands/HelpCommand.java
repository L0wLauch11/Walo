package me.lowlauch.Walo.Commands.Subcommands;

import me.lowlauch.Walo.Commands.SubCommand;
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
                    "§7/walo §bc <Nachricht> §7- Sendet eine Nachricht mit dem Walo Prefix an den ganzen Server");
        }
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public boolean needsOp() {
        return false;
    }

    @Override
    public boolean needsPlayer() {
        return false;
    }

    @Override
    public int neededArguments() {
        return 0;
    }
}

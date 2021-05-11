package me.lowlauch.walo.commands;

import me.lowlauch.walo.commands.subcommands.*;
import me.lowlauch.walo.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CommandsManager implements CommandExecutor
{
    private ArrayList<SubCommand> subCommands = new ArrayList<>();

    HelpCommand helpCommand = new HelpCommand();

    public CommandsManager()
    {
        // Register all subcommands
        subCommands.add(new AddMateCommand());
        subCommands.add(new BroadcastCommand());
        subCommands.add(new DisableStatsCommand());
        subCommands.add(helpCommand);
        subCommands.add(new ProtectionCommand());
        subCommands.add(new ResumeCommand());
        subCommands.add(new SetupBorderCommand());
        subCommands.add(new StatsCommand());
        subCommands.add(new StartCommand());
        subCommands.add(new StopCommand());
        subCommands.add(new TeamTagCommand());
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String commandLabel, String[] args)
    {
        if(commandLabel.equalsIgnoreCase("walo"))
        {
            for(SubCommand subCommand : subCommands)
            {
                // Execute subcommand
                if(args[0].equalsIgnoreCase(subCommand.getName()))
                {
                    if(!commandSender.isOp() && subCommand.needsOp())
                    {
                        commandSender.sendMessage(Main.prefix + "Du hast keine Rechte für diesen Command!");
                        return true;
                    }

                    if(!(commandSender instanceof Player) && subCommand.needsPlayer())
                    {
                        commandSender.sendMessage(Main.prefix + "Du musst ein Spieler sein!");
                        return true;
                    }

                    if(args.length < subCommand.neededArguments())
                    {
                        commandSender.sendMessage(Main.prefix + "Nicht genug Argumente!");
                        helpCommand.execute(commandSender, args);
                        return true;
                    }

                    subCommand.execute(commandSender, args);
                    return true;
                }
            }

            // Command not found, show help
            commandSender.sendMessage(Main.prefix + "Command wurde nicht gefunden!");
            helpCommand.execute(commandSender, args);
        }

        return true;
    }
}

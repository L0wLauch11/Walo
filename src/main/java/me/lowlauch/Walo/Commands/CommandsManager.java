package me.lowlauch.Walo.Commands;

import me.lowlauch.Walo.Commands.Subcommands.*;
import me.lowlauch.Walo.Main;
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
                if(commandLabel.equalsIgnoreCase(subCommand.getName()) &&
                        (commandSender.isOp() || !subCommand.needsOp()) &&
                        (commandSender instanceof Player || !subCommand.needsPlayer()) &&
                        args.length-1 >= subCommand.neededArguments())
                {
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

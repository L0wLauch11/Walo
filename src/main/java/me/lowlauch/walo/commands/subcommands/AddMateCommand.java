package me.lowlauch.walo.commands.subcommands;

import me.lowlauch.walo.WaloConfig;
import me.lowlauch.walo.commands.SubCommand;
import me.lowlauch.walo.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class AddMateCommand implements SubCommand
{
    @Override
    public void execute(CommandSender commandSender, String[] args)
    {
        if(args.length == 3)
        {
            Player player = Bukkit.getServer().getPlayer(args[1]);
            Player destinationPlayer = Bukkit.getServer().getPlayer(args[2]);

            // Don't let the player add himself as mate
            assert player != null;
            assert destinationPlayer != null;
            if(!player.getName().equals(destinationPlayer.getName()))
            {
                // Add these players as mates for eachother
                List<String> mates = WaloConfig.getPlayerMates(player);
                List<String> matesDestinationPlayer = WaloConfig.getPlayerMates(destinationPlayer);

                mates.add(destinationPlayer.getUniqueId().toString());
                matesDestinationPlayer.add(player.getUniqueId().toString());

                WaloConfig.setPlayerMates(player, mates);
                WaloConfig.setPlayerMates(destinationPlayer, matesDestinationPlayer);

                // Inform both players that they are now mates
                commandSender.sendMessage(Main.prefix + "Zu §6" + player.getName() + "§7 seinen Mates wurde §6" + destinationPlayer.getName() + "§7 hinzugefügt.");

                player.sendMessage(Main.prefix + "§6" + destinationPlayer.getName() + "§7 ist jetzt einer deiner Mates.");
                destinationPlayer.sendMessage(Main.prefix + "§6" + player.getName() + "§7 ist jetzt einer deiner Mates.");

                // Save the config
                WaloConfig.save();
            } else
            {
                commandSender.sendMessage(Main.prefix + "Du kannst dich nicht selber als Mate adden!");
            }
        } else
        {
            commandSender.sendMessage(Main.prefix + "Du musst: §c/walo addmate <Spieler> <Team-Mate>§7 machen");
        }
    }

    @Override
    public String getName()
    {
        return "addmate";
    }

    @Override
    public boolean requiresOp()
    {
        return true;
    }

    @Override
    public boolean requiresPlayer()
    {
        return false;
    }

    @Override
    public int requiredArguments()
    {
        return 0;
    }
}

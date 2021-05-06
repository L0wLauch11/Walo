package me.lowlauch.Walo.Commands.Subcommands;

import me.lowlauch.Walo.Commands.SubCommand;
import me.lowlauch.Walo.Main;
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
                // Set the argument as mate in both the lists
                String path = "mates." + player.getUniqueId().toString();
                List<String> mates = Main.getInstance().getConfig().getStringList(path);

                String pathDestinationPlayer = "mates." + destinationPlayer.getUniqueId().toString();
                List<String> matesDestinationPlayer = Main.getInstance().getConfig().getStringList(pathDestinationPlayer);

                mates.add(destinationPlayer.getUniqueId().toString());
                matesDestinationPlayer.add(player.getUniqueId().toString());

                Main.getInstance().getConfig().set(pathDestinationPlayer, matesDestinationPlayer);
                Main.getInstance().getConfig().set(path, mates);

                // Inform both players that they are now mates
                commandSender.sendMessage(Main.prefix + "Zu §6" + player.getName() + "§7 seinen Mates wurde §6" + destinationPlayer.getName() + "§7 hinzugefügt.");

                player.sendMessage(Main.prefix + "§6" + destinationPlayer.getName() + "§7 ist jetzt einer deiner Mates.");
                destinationPlayer.sendMessage(Main.prefix + "§6" + player.getName() + "§7 ist jetzt einer deiner Mates.");

                // Save the config
                Main.getInstance().saveConfig();
                Main.getInstance().reloadConfig();
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
    public boolean needsOp()
    {
        return true;
    }

    @Override
    public boolean needsPlayer()
    {
        return false;
    }

    @Override
    public int neededArguments()
    {
        return 0;
    }
}

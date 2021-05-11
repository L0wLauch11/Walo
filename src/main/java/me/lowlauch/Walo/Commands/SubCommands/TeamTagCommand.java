package me.lowlauch.Walo.Commands.SubCommands;

import me.lowlauch.Walo.Commands.SubCommand;
import me.lowlauch.Walo.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class TeamTagCommand implements SubCommand
{
    @Override
    public void execute(CommandSender commandSender, String[] args)
    {
        // Save custom tag under variable
        String customTag = args[2];

        // Replace & with the color code symbol
        customTag = customTag.replaceAll("&", "§");

        // Sets a custom tag for a player and mates
        Player p = Bukkit.getPlayer(args[1]);

        List<String> playerMates = Main.getInstance().getConfig().getStringList("mates." + p.getUniqueId().toString());

        int length = playerMates.size();

        String finalName = customTag + " §r" + p.getName();

        p.setDisplayName(finalName);
        p.setPlayerListName(finalName);
        Main.getInstance().getConfig().set("tags." + p.getUniqueId().toString(), finalName);

        p.sendMessage(Main.prefix + "Euer Team-Tag ist jetzt: " + customTag);

        if(!customTag.equals("reset"))
        {
            // Loop through all mates and change their tag
            for (String playerMate : playerMates)
            {
                Player changeTagPlayer = Bukkit.getPlayer(UUID.fromString(playerMate));
                finalName = customTag + " §r" + changeTagPlayer.getName();

                // Save tag under config
                Main.getInstance().getConfig().set("tags." + changeTagPlayer.getUniqueId().toString(), finalName);

                changeTagPlayer.setDisplayName(finalName);
                changeTagPlayer.setPlayerListName(finalName);

                changeTagPlayer.sendMessage(Main.prefix + "Euer Team-Tag ist jetzt: " + customTag);
            }

            Main.getInstance().saveConfig();
            Main.getInstance().reloadConfig();
        } else
        {
            p.setDisplayName(p.getName());
            p.setPlayerListName(p.getName());

            p.sendMessage(Main.prefix + "Euer Team-Tag ist jetzt resettet worden :(");

            // Save tag under config
            Main.getInstance().getConfig().set("tags." + p.getUniqueId().toString(), p.getName());


            for(String playerMate : playerMates)
            {
                Player changeTagPlayer = Bukkit.getPlayer(UUID.fromString(playerMate));
                changeTagPlayer.setDisplayName(changeTagPlayer.getName());
                changeTagPlayer.setPlayerListName(changeTagPlayer.getName());

                // Save tag under config
                Main.getInstance().getConfig().set("tags." + changeTagPlayer.getUniqueId().toString(), changeTagPlayer.getName());

                changeTagPlayer.sendMessage(Main.prefix + "Euer Team-Tag ist jetzt resettet worden :(");
            }

            Main.getInstance().saveConfig();
            Main.getInstance().reloadConfig();
        }
    }

    @Override
    public String getName()
    {
        return "teamtag";
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
        return 2;
    }
}

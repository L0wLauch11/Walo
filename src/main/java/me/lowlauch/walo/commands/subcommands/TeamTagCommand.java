package me.lowlauch.walo.commands.subcommands;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.WaloConfig;
import me.lowlauch.walo.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class TeamTagCommand implements SubCommand {
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        // Save custom tag under variable
        String customTag = args[2];

        // Replace & with the color code symbol
        customTag = customTag.replaceAll("&", "§");

        // Sets a custom tag for a player and mates
        Player p = Bukkit.getPlayer(args[1]);

        List<String> playerMates = WaloConfig.getPlayerMates(p);

        int length = playerMates.size();

        String finalName = customTag + " §r" + p.getName();

        p.setDisplayName(finalName);
        p.setPlayerListName(finalName);
        WaloConfig.setPlayerTeamTag(p, finalName);

        p.sendMessage(Main.prefix + "Euer Team-Tag ist jetzt: " + customTag);

        if (!customTag.equals("reset")) {
            // Loop through all mates and change their tag
            for (String playerMate : playerMates) {
                Player changeTagPlayer = Bukkit.getPlayer(UUID.fromString(playerMate));
                finalName = customTag + " §r" + changeTagPlayer.getName();

                // Save tag under config
                WaloConfig.setPlayerTeamTag(changeTagPlayer, finalName);

                changeTagPlayer.setDisplayName(finalName);
                changeTagPlayer.setPlayerListName(finalName);

                changeTagPlayer.sendMessage(Main.prefix + "Euer Team-Tag ist jetzt: " + customTag);
            }

            WaloConfig.save();
            WaloConfig.reload();
        } else {
            p.setDisplayName(p.getName());
            p.setPlayerListName(p.getName());

            p.sendMessage(Main.prefix + "Euer Team-Tag ist jetzt resettet worden :(");

            // Reset tag
            WaloConfig.setPlayerTeamTag(p, p.getName());

            for (String playerMate : playerMates) {
                Player changeTagPlayer = Bukkit.getPlayer(UUID.fromString(playerMate));
                changeTagPlayer.setDisplayName(changeTagPlayer.getName());
                changeTagPlayer.setPlayerListName(changeTagPlayer.getName());

                // Reset tag for mates as well
                WaloConfig.setPlayerTeamTag(changeTagPlayer, changeTagPlayer.getName());

                changeTagPlayer.sendMessage(Main.prefix + "Euer Team-Tag ist jetzt resettet worden :(");
            }

            WaloConfig.save();
            WaloConfig.reload();
        }
    }

    @Override
    public String getName() {
        return "teamtag";
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
        return 2;
    }
}

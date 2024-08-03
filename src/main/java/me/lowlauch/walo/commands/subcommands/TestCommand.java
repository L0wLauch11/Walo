package me.lowlauch.walo.commands.subcommands;

import me.lowlauch.walo.commands.SubCommand;
import me.lowlauch.walo.discord.webhook.DiscordWebHook;
import org.bukkit.command.CommandSender;

public class TestCommand implements SubCommand {
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        /* previous test

        String onlinePlayersString = "";
        String stylizedComma = ChatColor.GRAY + ", " + ChatColor.GREEN;
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayersString += onlinePlayer.getName() + stylizedComma;
        }

        onlinePlayersString = onlinePlayersString.substring(0, onlinePlayersString.length()-stylizedComma.length());
        int posLastComma = onlinePlayersString.lastIndexOf(", ");

        onlinePlayersString = StringUtils.replaceCharAt(onlinePlayersString, posLastComma, '#');
        onlinePlayersString = onlinePlayersString.replaceAll("#", ChatColor.GRAY + " und");
        String finalMessage = ChatColor.GREEN + onlinePlayersString + ChatColor.GOLD + ChatColor.BOLD + " hat das Walo gewonnen!";

        Bukkit.getServer().broadcastMessage(Main.prefix + finalMessage);

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.sendTitle("§c§kw§r§c " + "randomteamname" + " §kw", "§6hat Walo gewonnen");
        }
        */

        DiscordWebHook.sendText("@everyone Irgendein team hat Walo gewonnen!");
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

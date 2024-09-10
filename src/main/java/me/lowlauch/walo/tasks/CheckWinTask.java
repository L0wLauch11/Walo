package me.lowlauch.walo.tasks;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.database.WaloDatabase;
import me.lowlauch.walo.discord.webhook.DiscordWebHook;
import me.lowlauch.walo.misc.GlobalVariables;
import me.lowlauch.walo.misc.StringUtil;
import me.lowlauch.walo.teams.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CheckWinTask implements Runnable {

    @Override
    public void run() {
        ArrayList<String> aliveTeams = Teams.getAliveTeams();
        boolean soloWin = Bukkit.getOnlinePlayers().size() == 1;
        if (GlobalVariables.started
                && (aliveTeams.size() <= 1 || soloWin)) {
            GlobalVariables.startPlayersUUID.clear(); // No one can join anymore

            String onlinePlayersString = "";
            String stylizedComma = ChatColor.GRAY + ", " + ChatColor.GREEN;

            if (soloWin) {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    onlinePlayersString += onlinePlayer.getName();
                }
            } else {
                List<String> aliveTeam = Teams.getTeamMembers(aliveTeams.get(0));

                for (String playerUUIDString : aliveTeam) {
                    UUID playerUUID = UUID.fromString(playerUUIDString);
                    onlinePlayersString += Bukkit.getOfflinePlayer(playerUUID).getName() + stylizedComma;
                }
            }

            if (onlinePlayersString.contains(",")) {
                onlinePlayersString = onlinePlayersString.substring(0, onlinePlayersString.length()-stylizedComma.length());
            }
            int posLastComma = onlinePlayersString.lastIndexOf(", ");

            if (posLastComma != -1) {
                onlinePlayersString = StringUtil.replaceCharAt(onlinePlayersString, posLastComma, '#');
                onlinePlayersString = onlinePlayersString.replaceAll("#", ChatColor.GRAY + " und");
            }
            String finalMessage = ChatColor.GREEN + onlinePlayersString + ChatColor.GOLD + ChatColor.BOLD + " hat das Walo gewonnen!";

            String onlinePlayersWithoutDecoration = onlinePlayersString
                    .replaceAll("§7", "")
                    .replaceAll("§a", "");

            DiscordWebHook.sendText("@everyone " + onlinePlayersWithoutDecoration + " hat das Walo gewonnen!");
            Bukkit.getServer().broadcastMessage(Main.prefix + finalMessage);

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                String winners = Teams.getTeamName(aliveTeams.get(0));
                if (soloWin) {
                    winners = onlinePlayer.getName();
                }

                onlinePlayer.sendTitle("§c§kw§r§c " + winners + " §kw", "§6hat Walo gewonnen");
                WaloDatabase.addPlayerWin(onlinePlayer);
            }

            // Trigger post-game behaviour
            Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new GameRestartTask(), 1L, 20L);
        }
    }
}

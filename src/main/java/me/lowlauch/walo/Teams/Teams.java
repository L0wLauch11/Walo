package me.lowlauch.walo.Teams;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.WaloConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Teams {
    public static ArrayList<UUID> playersWhoWantToRenameTheirTeam = new ArrayList<>(); // long ass variable name
    public static void createTeamFor(Player p) {
        if (getTeamOfPlayer(p) != null){
            p.sendMessage(Main.prefix + ChatColor.BOLD + "Du hast bereits ein Team!");
            return;
        }

        WaloConfig.addTeamInConfig(p);
        playersWhoWantToRenameTheirTeam.add(p.getUniqueId());
        p.sendMessage(Main.prefix + ChatColor.BOLD + "Bitte gib einen Team-Namen ein:");
    }

    public static void renameTeam(String teamID, String newTeamName) {
        WaloConfig.setTeamName(teamID, newTeamName);

        // Update name for all online players
        for (String playerUUID : getTeamMembers(teamID)) {
            Player p = Bukkit.getPlayer(UUID.fromString(playerUUID));

            // Change name
            String playerTeamName = Teams.getTeamName(Teams.getTeamOfPlayer(p));
            if (playerTeamName != null) {
                p.setDisplayName(playerTeamName + "" + p.getName());
            }

            // Set the player tab name to display name
            p.setPlayerListName(p.getDisplayName());
        }
    }

    public static String getTeamName(String teamID) {
        return WaloConfig.getTeamName(teamID);
    }

    public static List<String> getTeamMembers(String teamID) {
        return WaloConfig.getTeamMembers(teamID);
    }

    public static String getTeamOfPlayer(Player p) {
        if (WaloConfig.getTeams() == null)
            return null;

        for (String teamID : WaloConfig.getTeams()) {
            if (getTeamMembers(teamID).contains(p.getUniqueId().toString())) {
                return teamID;
            }
        }

        return null;
    }

    public static void leaveTeam(Player p) {
        String playerUUID = p.getUniqueId().toString();
        String playerTeam = getTeamOfPlayer(p);

        if (getTeamMembers(playerTeam).contains(playerUUID)) {
            WaloConfig.removeTeamMember(playerTeam, playerUUID);
            p.setDisplayName(p.getName());
            p.setPlayerListName(p.getDisplayName());

            p.sendMessage(Main.prefix + "Du hast das Team erfolgreich verlassen.");
        } else {
            p.sendMessage(Main.prefix + "Du bist in keinem Team.");
        }
    }
}

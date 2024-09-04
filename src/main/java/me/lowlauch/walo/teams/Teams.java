package me.lowlauch.walo.teams;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.WaloConfig;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Teams {
    public static ArrayList<UUID> playersWhoWantToRenameTheirTeam = new ArrayList<>(); // long ass variable names
    public static ArrayList<UUID> playersWhoWantToInviteSomeone = new ArrayList<>();

    public static void invitePlayer(Player teamOwner, Player invitedPlayer) {
        String playerTeam = getTeamOfPlayer(invitedPlayer);
        String ownerTeam = getTeamOfPlayer(teamOwner);

        if (!getTeamMembers(ownerTeam).contains(teamOwner.getUniqueId().toString())) {
            teamOwner.sendMessage(Main.prefix + "Du bist in keinem Team!");
            return;
        }

        if (getTeamMembers(playerTeam).contains(invitedPlayer.getUniqueId().toString())) {
            teamOwner.sendMessage(Main.prefix + ChatColor.GOLD + invitedPlayer.getName() + ChatColor.GRAY + " ist bereits in einem Team!");
            return;
        }

        teamOwner.sendMessage(Main.prefix + ChatColor.GOLD + invitedPlayer.getName() + ChatColor.GRAY + " wurde erfolgreich eingeladen!");

        String msg = Main.prefix + "Du wurdest von " + ChatColor.GOLD + teamOwner.getName() + ChatColor.GRAY + " in sein/ihr/* Team eingeladen! \n" +
                "§dDrücke auf diese Nachricht um anzunehmen!";
        TextComponent component = new TextComponent(TextComponent.fromLegacyText(msg));
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/walo acceptinvite " + teamOwner.getName()));
        invitedPlayer.spigot().sendMessage(component);
    }

    public static void createTeamFor(Player p) {
        if (getTeamMembers(getTeamOfPlayer(p)).contains(p.getUniqueId().toString())) {
            p.sendMessage(Main.prefix + ChatColor.BOLD + "Du hast bereits ein Team!");
            return;
        }

        WaloConfig.addTeamInConfig(p);
        playersWhoWantToRenameTheirTeam.add(p.getUniqueId());
        p.sendMessage(Main.prefix + "§1F§2a§3r§4b§5e§6n§7codes: §f&f §0&0 §1&1 §2&2 §3&3 §4&4 §5&5 §6&6 §7&7 §8&8 §9&9 §f§l&l§r §m&m§r §n&n§r §o&o§r §r&r");
        p.sendMessage(Main.prefix + ChatColor.BOLD + "Bitte gib einen Team-Namen ein:");
    }

    public static void joinTeam(String teamID, String playerUUID) {
        WaloConfig.addTeamMember(teamID, playerUUID);
        Player p = Bukkit.getPlayer(UUID.fromString(playerUUID));

        // Change name
        String playerTeamName = Teams.getTeamName(teamID);
        if (playerTeamName != null) {
            p.setDisplayName(playerTeamName + "" + p.getName());
        }

        // Set the player tab name to display name
        p.setPlayerListName(p.getDisplayName());
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
            return p.getUniqueId().toString();

        for (String teamID : WaloConfig.getTeams()) {
            if (getTeamMembers(teamID).contains(p.getUniqueId().toString())) {
                return teamID;
            }
        }

        return p.getUniqueId().toString();
    }

    public static ArrayList<String> getAliveTeams() {
        ArrayList<String> aliveTeams = new ArrayList<>();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers())  {
            String teamOfPlayer = Teams.getTeamOfPlayer(onlinePlayer);
            if (!aliveTeams.contains(teamOfPlayer))
                aliveTeams.add(teamOfPlayer);
        }

        return aliveTeams;
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

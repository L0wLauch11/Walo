package me.lowlauch.walo;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class WaloConfig {
    static FileConfiguration mainConfig = Main.getInstance().getConfig();

    public static void reload() {
        Main.getInstance().reloadConfig();
    }

    public static void save() {
        Main.getInstance().saveConfig();
    }

    public static List<String> getPlayerMates(Player player) {
        String path = "mates." + player.getUniqueId().toString();
        List<String> mates = mainConfig.getStringList(path);

        return mates;
    }

    public static int getWorldBorderSize() {
        return mainConfig.getInt("worldborder.size");
    }

    public static int getWorldBorderShrinkSize() {
        return mainConfig.getInt("worldborder.shrinksize");
    }

    public static long getWorldBorderShrinkDelay()
    {
        return mainConfig.getLong("worldborder.shrinkdelay");
    }

    public static long getWorldBorderShrinkDuration()
    {
        return mainConfig.getLong("worldborder.shrinkduration");
    }

    public static String getDatabaseAPIUrl() {
        return mainConfig.getString("web-database-api.url");
    }

    public static String getDatabaseSecurityString() {
        return mainConfig.getString("web-database-api.access-token");
    }

    public static String getDiscordWebhookURL() {
        return mainConfig.getString("discord-webhook-url");
    }
    public static void addTeamInConfig(Player p) {
        String playerUUID = p.getUniqueId().toString();

        addTeamMember(playerUUID, playerUUID);
        setTeamName(playerUUID, "gommemode");
    }

    public static void setTeamName(String playerUUID, String newTeamName) {
        mainConfig.set("teams.player-teams." + playerUUID + ".name", newTeamName);
        save();
    }

    public static String getTeamName(String playerUUID) {
        return mainConfig.getString("teams.player-teams." + playerUUID + ".name");
    }

    public static Set<String> getTeams() {
        if (mainConfig.contains("teams.player-teams")) {
            return mainConfig.getConfigurationSection("teams.player-teams").getKeys(false);
        }

        return null;
    }

    public static List<String> getTeamMembers(String playerUUID) {
        return mainConfig.getStringList("teams.player-teams." + playerUUID + ".members");
    }

    public static void addTeamMember(String teamID, String playerUUID) {
        List<String> members = mainConfig.getStringList("teams.player-teams." + teamID + ".members");
        members.add(playerUUID);
        mainConfig.set("teams.player-teams." + teamID + ".members", members);
        save();
    }

    public static void removeTeamMember(String teamID, String playerUUID) {
        List<String> members = mainConfig.getStringList("teams.player-teams." + teamID + ".members");
        members.remove(playerUUID);
        mainConfig.set("teams.player-teams." + teamID + ".members", members);
        save();
    }

    public static void resetTeams() {
        mainConfig.set("teams.player_teams", null);
        save();
    }

    public static int getMaxTeamSize() {
        return mainConfig.getInt("teams.player-teams.max-allowed-size");
    }

    public static int getAutostartRequiredPlayers() {
        return mainConfig.getInt("autostart.required-players");
    }

    public static int getAutostartSeconds() {
        return mainConfig.getInt("autostart.seconds");
    }

    public static boolean getProtectionTimeEnabled() {
        return mainConfig.getBoolean("protection-time.enabled");
    }

    public static boolean getAllowSubsequentJoining() {
        return mainConfig.getBoolean("joining.allow-subsequent");
    }

    public static int getRejoinTimeout() {
        return mainConfig.getInt("joining.rejoin-timeout");
    }
}

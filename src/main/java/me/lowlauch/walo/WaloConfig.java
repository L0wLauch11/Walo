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

    public static void setValue(String path, Object value) {
        mainConfig.set(path, value);
    }

    public static List<String> getPlayerMates(Player player) {
        String path = "mates." + player.getUniqueId().toString();
        List<String> mates = mainConfig.getStringList(path);

        return mates;
    }

    public static void setPlayerMates(Player player, List<String> matesUUIDStringList) {
        String path = "mates." + player.getUniqueId().toString();
        setValue(path, matesUUIDStringList);
    }

    public static String getPlayerTeamTag(Player player) {
        return mainConfig.getString("tags." + player.getUniqueId().toString());
    }

    public static void setPlayerTeamTag(Player player, String teamTag) {
        mainConfig.set("tags." + player.getUniqueId().toString(), teamTag);
    }

    public static int getWorldBorderSize() {
        return mainConfig.getInt("worldborder.size");
    }

    public static int getWorldBorderShrinkSize() {
        return mainConfig.getInt("worldborder.shrinksize");
    }

    public static long getWorldBorderShinkDelay() // When the border starts to shrink
    {
        return mainConfig.getLong("worldborder.shrinkdelay");
    }

    public static long getWorldBorderShrinkDuration() // How long it takes to shrink to the shrink size
    {
        return mainConfig.getLong("worldborder.shrinkduration");
    }

    public static String getDatabaseAPIUrl() {
        return mainConfig.getString("web-database-api.url");
    }

    public static String getDatabaseSecurityString() {
        return mainConfig.getString("web-database-api.security-string");
    }

    public static void addTeamInConfig(Player p) {
        String playerUUID = p.getUniqueId().toString();

        addTeamMember(playerUUID, playerUUID);
        setTeamName(playerUUID, "gommemode");
    }

    public static void setTeamName(String playerUUID, String newTeamName) {
        mainConfig.set("teams." + playerUUID + ".name", newTeamName);
    }

    public static String getTeamName(String playerUUID) {
        return mainConfig.getString("teams." + playerUUID + ".name");
    }

    public static Set<String> getTeams() {
        if (mainConfig.contains("teams")) {
            return mainConfig.getConfigurationSection("teams").getKeys(false);
        }

        return null;
    }

    public static List<String> getTeamMembers(String playerUUID) {
        return mainConfig.getStringList("teams." + playerUUID + ".members");
    }

    public static void addTeamMember(String teamID, String playerUUID) {
        List<String> members = mainConfig.getStringList("teams." + teamID + ".members");
        members.add(playerUUID);
        mainConfig.set("teams." + teamID + ".members", members);
    }

    public static void removeTeamMember(String teamID, String playerUUID) {
        List<String> members = mainConfig.getStringList("teams." + teamID + ".members");
        members.remove(playerUUID);
        mainConfig.set("teams." + teamID + ".members", members);
    }
}

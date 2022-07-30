package me.lowlauch.walo;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

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

    public static String getMySQLHost() {
        return mainConfig.getString("mysql.host");
    }

    public static int getMySQLPort() {
        return mainConfig.getInt("mysql.port");
    }

    public static String getMySQLDatabase() {
        return mainConfig.getString("mysql.database");
    }

    public static String getMySQLUsername() {
        return mainConfig.getString("mysql.username");
    }

    public static String getMySQLPassword() {
        return mainConfig.getString("mysql.password");
    }
}

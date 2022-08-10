package me.lowlauch.walo.database;

import me.lowlauch.walo.WaloConfig;
import org.bukkit.entity.Player;

public class WaloDatabase {
    static String securityString = WaloConfig.getDatabaseSecurityString();

    public static void initWaloTable() {
        DatabaseRequest.request("secret=" + securityString + "&operation=inittable&uuid=foo&name=bar");
    }

    public static void createPlayer(Player p) {
        String playerUUID = p.getUniqueId().toString();
        String playerName = p.getName();

        DatabaseRequest.request("secret=" + securityString + "&operation=createplayer&uuid=" + playerUUID + "&name=" + playerName);
    }

    public static void addPlayerKill(Player p) {
        String playerUUID = p.getUniqueId().toString();
        String playerName = p.getName();

        DatabaseRequest.request("secret=" + securityString + "&operation=addkill&uuid=" + playerUUID + "&name=" + playerName);
    }

    public static String getPlayerKills(Player p) {
        String playerUUID = p.getUniqueId().toString();
        String playerName = p.getName();

        return DatabaseRequest.request("secret=" + securityString + "&operation=getkills&uuid=" + playerUUID + "&name=" + playerName);
    }

    public static void addPlayerPlaycount(Player p) {
        String playerUUID = p.getUniqueId().toString();
        String playerName = p.getName();

        DatabaseRequest.request("secret=" + securityString + "&operation=addplaycount&uuid=" + playerUUID + "&name=" + playerName);
    }
}

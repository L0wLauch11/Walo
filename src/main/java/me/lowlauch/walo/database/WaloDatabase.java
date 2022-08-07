package me.lowlauch.walo.database;

import me.lowlauch.walo.WaloConfig;
import org.bukkit.entity.Player;

import java.io.IOException;

public class WaloDatabase {
    static String securityString = WaloConfig.getDatabaseSecurityString();

    public static void initWaloTable() {
        try {
            DatabaseRequest.request("secret=" + securityString + "&operation=inittable&uuid=foo&name=bar");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createPlayer(Player p) {
        String playerUUID = p.getUniqueId().toString();
        String playerName = p.getName();

        try {
            DatabaseRequest.request("secret=" + securityString + "&operation=createplayer&uuid=" + playerUUID + "&name=" + playerName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addPlayerKill(Player p) {
        String playerUUID = p.getUniqueId().toString();
        String playerName = p.getName();

        try {
            DatabaseRequest.request("secret=" + securityString + "&operation=addkill&uuid=" + playerUUID + "&name=" + playerName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getPlayerKills(Player p) {
        String playerUUID = p.getUniqueId().toString();
        String playerName = p.getName();

        String kills;
        try {
            kills = DatabaseRequest.request("secret=" + securityString + "&operation=getkills&uuid=" + playerUUID + "&name=" + playerName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return kills;
    }
}

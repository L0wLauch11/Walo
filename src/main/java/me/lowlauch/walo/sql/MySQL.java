package me.lowlauch.walo.sql;


import me.lowlauch.walo.WaloConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {
    private static Connection connection;
    public static String table = "walo";

    public void setup() {
        String host = WaloConfig.getMySQLHost();
        int port = WaloConfig.getMySQLPort();
        String database = WaloConfig.getMySQLDatabase();
        String username = WaloConfig.getMySQLUsername();
        String password = WaloConfig.getMySQLPassword();

        try {
            synchronized (this) {
                if (getConnection() != null && !getConnection().isClosed()) {
                    return;
                }

                setConnection(DriverManager.getConnection("jdbc:mysql://" + host + ":"
                        + port + "/" + database, username, password));

                Bukkit.getLogger().info(ChatColor.GREEN + "MYSQL CONNECTED");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection conn) {
        connection = conn;
    }
}

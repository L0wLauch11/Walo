package me.lowlauch.Walo.SQL;


import me.lowlauch.Walo.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL
{
    private static Connection connection;
    public static String table = "walo";

    public void setup()
    {
        FileConfiguration config = Main.getInstance().getConfig();

        String host = config.getString("mysql.host");
        int port = config.getInt("mysql.port");
        String database = config.getString("mysql.database");
        String username = config.getString("mysql.username");
        String password = config.getString("mysql.password");

        try
        {
            synchronized (this)
            {
                if(getConnection() != null && !getConnection().isClosed())
                {
                    return;
                }

                Class.forName("com.mysql.jdbc.Driver");
                setConnection( DriverManager.getConnection("jdbc:mysql://" + host + ":"
                        + port + "/" + database, username, password));

                Bukkit.getLogger().info(ChatColor.GREEN + "MYSQL CONNECTED");
            }
        } catch(SQLException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public static Connection getConnection()
    {
        return connection;
    }

    public static void setConnection(Connection conn)
    {
        connection = conn;
    }
}

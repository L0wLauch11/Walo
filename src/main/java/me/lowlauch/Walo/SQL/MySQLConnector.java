package me.lowlauch.Walo.SQL;

import me.lowlauch.Walo.Main;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnector
{
    private final FileConfiguration config = Main.getInstance().getConfig();

    private String host = config.getString("mysql.host");
    private String port = config.getString("mysql.port");
    private String database = config.getString("mysql.database");
    private String username = config.getString("mysql.username");
    private String password = config.getString("mysql.password");

    private Connection connection;

    public boolean isConnected()
    {
        return (connection == null);
    }

    public void connect() throws ClassNotFoundException, SQLException
    {
        // Connect to MySQL DB
        if(!isConnected())
            connection = DriverManager.getConnection(String.format("jdbc:mysql://%s:%s/%s?useSSL=false", host, port, database),
                    username, password);
    }

    public void disconnect()
    {
        if(isConnected())
        {
            try
            {
                connection.close();
            } catch(SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    public Connection getConnection()
    {
        return connection;
    }
}

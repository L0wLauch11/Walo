package me.lowlauch.Walo;

import me.lowlauch.Walo.Commands.CommandsManager;
import me.lowlauch.Walo.SQL.Database;
import me.lowlauch.Walo.SQL.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.Objects;

public class Main extends JavaPlugin
{
    public static String prefix = "§f[§6Walo§f]§7 ";
    private static Main instance;
    private MySQL sql = new MySQL();
    public Database db = new Database();

    public static Main getInstance()
    {
        return instance;
    }

    public void onEnable()
    {
        instance = this;

        CommandsManager commandsManager = new CommandsManager();

        // Add all the commands
        Objects.requireNonNull(getCommand("walo")).setExecutor(commandsManager);

        // Set the difficulty to peaceful until game starts
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "difficulty peaceful");

        // Config stuff
        saveDefaultConfig();

        // Connect to MySQL
        sql.setup();
        db.createTable("UUID VARCHAR(100),NAME VARCHAR(100),KILLS INT(100)");

        // Add event listener
        getServer().getPluginManager().registerEvents(new EventListener(), this);

        // Info
        Info.runInfo();

        // Scoreboard
        ScoreboardHandler.updateScoreboard();
    }

    public void onDisable()
    {
        try
        {
            MySQL.getConnection().close();
        } catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        if(sql != null)
            sql = null;
    }
}

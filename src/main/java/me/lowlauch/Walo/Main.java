package me.lowlauch.Walo;

import me.lowlauch.Walo.SQL.Database;
import me.lowlauch.Walo.SQL.MySQLConnector;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.util.Objects;

public class Main extends JavaPlugin
{
    public static String prefix = "§f[§6Walo§f]§7 ";
    private static Main instance;
    public MySQLConnector sql;
    public Database db;

    public static Main getInstance()
    {
        return instance;
    }

    public void onEnable()
    {
        instance = this;
        sql = new MySQLConnector();
        db = new Database();

        Commands commands = new Commands();

        // Add all the commands
        Objects.requireNonNull(getCommand("walo")).setExecutor(commands);

        // Add event listener
        getServer().getPluginManager().registerEvents(new EventListener(), this);

        // Set the difficulty to peaceful until game starts
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "difficulty peaceful");

        // Config stuff
        saveDefaultConfig();

        // Connecto to MySQL
        try
        {
            sql.connect();
        } catch (ClassNotFoundException | SQLException e)
        {
            getLogger().info(prefix + "Could not connect to database");
        }

        if(sql.isConnected())
        {
            getLogger().info(prefix + "Successfully connected to database");
            db.createTable();
        }

        // Info
        // Task that runs every tick after 3 hours
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if(!Commands.started)
                {
                    double val = getConfig().getDouble("worldborder.shrinkdelay")/20/60;

                    // Broadcast some info
                    Bukkit.getServer().broadcastMessage(
                            prefix + "-------------------------------\n" +
                            prefix + "Info: \n" +
                            prefix + "§6Bordergröße in jede Richtung: §7" + getConfig().getInt("worldborder.size")/2 + "\n" +
                            prefix + "§6Border §7wird auf " + getConfig().getInt("worldborder.shrinksize")/2 + " in jede Richtung nach " + val +  " Minuten gesetzt.\n" +
                            prefix + "§6Schutzzeit: §710 Minuten\n" +
                            prefix + "§6Walo Discord: §7§lhttps://discord.gg/amJbWnUq9f\n" +
                            prefix + "-------------------------------");
                }
            }
        }.runTaskTimer(Main.getInstance(), 0L, 2000L);
    }

    public void onDisable()
    {
        sql.disconnect();
    }
}

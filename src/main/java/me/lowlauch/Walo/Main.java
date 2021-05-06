package me.lowlauch.Walo;

import me.lowlauch.Walo.Commands.CommandVariables;
import me.lowlauch.Walo.Commands.CommandsManager;
import me.lowlauch.Walo.SQL.Database;
import me.lowlauch.Walo.SQL.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

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
        // Task that runs every tick after 3 hours
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if(!CommandVariables.started)
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
        try
        {
            MySQL.getConnection().close();
        } catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
        sql = null;
    }
}

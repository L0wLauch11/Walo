package me.lowlauch.walo;

import me.lowlauch.walo.eventlistening.EventListenersManager;
import me.lowlauch.walo.commands.CommandsManager;
import me.lowlauch.walo.database.WaloDatabase;
import me.lowlauch.walo.tasks.AutostartTask;
import me.lowlauch.walo.tasks.InfoTask;
import me.lowlauch.walo.tasks.LagTask;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class Main extends JavaPlugin {
    public static String prefix = "§f[§6Walo§f]§7 ";
    private static Main instance;
    private static CommandsManager commandsManager;

    public static Main getInstance() {
        return instance;
    }

    public static CommandsManager getCommandsManager() {
        return commandsManager;
    }

    public void onEnable() {
        instance = this;
        commandsManager = new CommandsManager();

        // Add all the commands
        Objects.requireNonNull(getCommand("walo")).setExecutor(commandsManager);

        // Set the difficulty to peaceful until game starts
        for (World w : Bukkit.getServer().getWorlds()) {
            w.setDifficulty(Difficulty.PEACEFUL);
        }

        // Config stuff
        saveDefaultConfig();

        WaloDatabase.initWaloTable();

        // Add event listener
        EventListenersManager.registerEvents();

        getServer().getScheduler().scheduleSyncRepeatingTask(this, new LagTask(), 1L, 100L);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new AutostartTask(), 1L, 200L);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new InfoTask(), 1L, 2000L);
    }

    public void onDisable() {
        for (Player p : getServer().getOnlinePlayers()) {
            p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }
    }
}

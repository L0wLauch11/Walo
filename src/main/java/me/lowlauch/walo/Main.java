package me.lowlauch.walo;

import me.lowlauch.walo.eventlistening.EventListenersManager;
import me.lowlauch.walo.commands.CommandsManager;
import me.lowlauch.walo.database.WaloDatabase;
import me.lowlauch.walo.misc.Info;
import me.lowlauch.walo.misc.Lag;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class Main extends JavaPlugin {
    public static String prefix = "§f[§6Walo§f]§7 ";
    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;

        CommandsManager commandsManager = new CommandsManager();

        // Add all the commands
        Objects.requireNonNull(getCommand("walo")).setExecutor(commandsManager);

        // Set the difficulty to peaceful until game starts
        //Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "difficulty peaceful");
        for (World w : Bukkit.getServer().getWorlds()) {
            w.setDifficulty(Difficulty.PEACEFUL);
        }

        // Config stuff
        saveDefaultConfig();

        WaloDatabase.initWaloTable();

        // Add event listener
        EventListenersManager.registerEvents();

        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Lag(), 100L, 1L);

        // Info
        Info.runInfo();
    }

    public void onDisable() {
        for (Player p : getServer().getOnlinePlayers()) {
            p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }
    }
}

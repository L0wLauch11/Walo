package me.lowlauch.walo;

import me.lowlauch.walo.eventlistening.EventListenersManager;
import me.lowlauch.walo.commands.CommandsManager;
import me.lowlauch.walo.database.WaloDatabase;
import me.lowlauch.walo.misc.WorldUtil;
import me.lowlauch.walo.tasks.InfoTask;
import me.lowlauch.walo.tasks.LagTask;
import me.lowlauch.walo.tasks.LobbyScoreboardUpdaterTask;
import me.lowlauch.walo.tasks.ServerStartTask;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.file.Files;
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
        saveDefaultBadWords();

        WaloDatabase.initWaloTable();

        // Add event listener
        EventListenersManager.registerEvents();

        getServer().getScheduler().scheduleSyncRepeatingTask(this, new LagTask(), 1L, 100L);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new InfoTask(), 1L, 2000L);
        getServer().getScheduler().runTaskLater(this, new ServerStartTask(), 1L); // delay by a tick just to be safe
        LobbyScoreboardUpdaterTask.create();
    }

    public void onDisable() {
        for (Player p : getServer().getOnlinePlayers()) {
            p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }
    }

    public void saveDefaultBadWords() {
        File file = new File(this.getDataFolder().getAbsolutePath() + "/badwords.txt");

        if (file.exists()) {
            return;
        }

        InputStream input = getClass().getResourceAsStream("/badwords.txt");
        assert input != null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));

        FileWriter output;
        try {
            output = new FileWriter(file.getPath());
            BufferedWriter writer = new BufferedWriter(output);

            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line + "\n");
            }

            writer.close();
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

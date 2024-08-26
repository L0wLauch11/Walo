package me.lowlauch.walo.tasks;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.WaloConfig;
import me.lowlauch.walo.misc.WorldUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class GameRestartTask implements Runnable {
    private int gameRestartSeconds = 60;

    @Override
    public void run() {
        gameRestartSeconds--;

        // Warn players
        if (gameRestartSeconds <= 10) {
            Bukkit.getServer().broadcastMessage(Main.prefix + "Das Spiel ist vorbei. Starte in §6"
                    + gameRestartSeconds + " Sekunden neu.");
        }

        // Timer has reached its end -> restart
        if (gameRestartSeconds <= 0) {
            // Players need to be kicked to unload worlds
            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                p.kickPlayer("Das Spiel startet neu!");
            }

            // Delete teams
            WaloConfig.resetTeams();

            // Unban everyone
            BanList banList = Bukkit.getBanList(BanList.Type.NAME);
            for (BanEntry entry : banList.getBanEntries()) {
                banList.pardon(entry.getTarget());
            }

            // Restart server and delete worlds
            Bukkit.getServer().shutdown();

            String workingDirectory = new File(".").getAbsolutePath();
            String waloRestartCompanionPath = workingDirectory + "/WaloRestartCompanion.jar";
            File waloRestartCompanion = new File(waloRestartCompanionPath);

            if (!waloRestartCompanion.exists()) {
                Bukkit.getLogger().info("WaloRestartCompanion.jar needs to be in root folder in order to successfully restart the server!");
            } else {
                String worlds = "";
                for (World world : Bukkit.getWorlds()) {
                    worlds += world.getWorldFolder() + " ";
                }

                Bukkit.getLogger().info(worlds);

                try {
                    Process process = Runtime.getRuntime().exec("java -jar " + waloRestartCompanionPath + " " + worlds + " --restart");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

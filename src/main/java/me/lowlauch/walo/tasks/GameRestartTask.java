package me.lowlauch.walo.tasks;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.WaloConfig;
import org.bukkit.*;
import org.bukkit.entity.Player;

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

            // Delete all worlds, so that they will be regenerated
            for (World world : Bukkit.getServer().getWorlds()) {
                Bukkit.getServer().unloadWorld(world.getName(), false);

                boolean deletionSuccess = world.getWorldFolder().delete();

                if (deletionSuccess) {
                    Bukkit.getLogger().info("Welt " + world.getName() + " erfolgreich gelöscht!");
                }
            }

            // Delete teams
            WaloConfig.resetTeams();

            // Unban everyone
            BanList banList = Bukkit.getBanList(BanList.Type.NAME);
            for (BanEntry entry : banList.getBanEntries()) {
                banList.pardon(entry.getTarget());
            }

            // dispatchCommand sucks, but I've yet to find a better method to restart the server
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "restart");
        }
    }
}

package me.lowlauch.walo.tasks;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.WaloConfig;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;

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
            for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
                Bukkit.getBanList(BanList.Type.NAME).pardon(player.getName());
            }

            // dispatchCommand sucks, but I've yet to find a better method to restart the server
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "restart");
        }
    }
}

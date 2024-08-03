package me.lowlauch.walo.tasks;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.WaloConfig;
import me.lowlauch.walo.misc.GlobalVariables;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class AutostartTask implements Runnable {

    @Override
    public void run() {
        Bukkit.getLogger().info("AutostartTask ran");

        if (GlobalVariables.started) {
            return;
        }

        if (Bukkit.getServer().getOnlinePlayers().size() >= WaloConfig.getAutostartRequiredPlayers()) {
            Bukkit.getServer().broadcastMessage(Main.prefix + "Es sind genügend Spieler anwesend! Das spiel Startet in "
                    + WaloConfig.getAutostartSeconds());

            new BukkitRunnable() {
                @Override
                public void run() {
                    Main.getCommandsManager().executeSubcommand(Bukkit.getServer().getConsoleSender(), "start");
                }
            }.runTaskLater(Main.getInstance(), 20L * WaloConfig.getAutostartSeconds());
        }
    }
}

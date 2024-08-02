package me.lowlauch.walo.tasks;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.WaloConfig;
import org.bukkit.Bukkit;

public class AutostartTask implements Runnable {

    @Override
    public void run() {
        if (Bukkit.getServer().getOnlinePlayers().size() >= WaloConfig.getAutostartRequiredPlayers()) {
            Main.getCommandsManager().executeSubcommand(Bukkit.getServer().getConsoleSender(), "start");
        }
    }
}

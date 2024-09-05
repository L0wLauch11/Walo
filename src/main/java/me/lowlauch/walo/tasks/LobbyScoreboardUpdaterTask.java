package me.lowlauch.walo.tasks;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.ScoreboardHandler;
import me.lowlauch.walo.misc.GlobalVariables;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class LobbyScoreboardUpdaterTask {
    private static int taskId = -1;

    public static void create() {
        Runnable runnable = () -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                // Update
                if (!p.hasMetadata("no-scoreboard"))
                    ScoreboardHandler.updatePlayerScoreboard(p);
            }
        };

        taskId = Bukkit.getScheduler()
                .scheduleSyncRepeatingTask(Main.getInstance(), runnable, 0L, 20L);
    }

    public static void cancel() {
        if (taskId != -1) {
            Bukkit.getScheduler().cancelTask(taskId);
        }
    }
}

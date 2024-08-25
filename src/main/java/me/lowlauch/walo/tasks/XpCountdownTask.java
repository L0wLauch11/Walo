package me.lowlauch.walo.tasks;

import me.lowlauch.walo.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

// Displays a countdown in the player's XP bar
public class XpCountdownTask {
    private static int countdown = 0;
    private static int taskId = -1;

    public static void setCountdown(int seconds) {
        countdown = seconds;

        Runnable runnable = () -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.setLevel(countdown);
                p.setExp(1);
            }

            if (countdown <= 0) {
                for (Player p : Bukkit.getOnlinePlayers()) { // just to make sure
                    p.setExp(0);
                    p.setLevel(0);
                }
                cancel();
            }

            countdown--;
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
package me.lowlauch.walo;

import me.lowlauch.walo.misc.GlobalVariables;
import me.lowlauch.walo.misc.Lag;
import me.lowlauch.walo.misc.Ping;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.time.LocalTime;

public class ScoreboardHandler {
    private static int minutesTimer = -1;
    private static Objective objective;
    private static Scoreboard scoreboard;
    private static BukkitTask task;
    private static LocalTime playTime;

    public static void setScoreboard(Player p) {
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        objective = scoreboard.registerNewObjective("walo", "dummy");

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("§6§lWalo");

        p.setScoreboard(scoreboard);
    }

    public static void updateScoreboard() {
        if (task != null)
            task.cancel();

        task = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), () -> {
            minutesTimer++;
            playTime = LocalTime.ofSecondOfDay(minutesTimer * 60L);

            for (Player p : Bukkit.getOnlinePlayers()) {
                // Update
                if (!p.hasMetadata("no-scoreboard"))
                    updatePlayerScoreboard(p);
            }
        }, 0, 1200);
    }

    public static void updatePlayerScoreboard(Player p) {
        setScoreboard(p);

        int borderSize = WaloConfig.getWorldBorderSize() / 2;
        if (GlobalVariables.started) {
            long borderTimeInMinutes = WaloConfig.getWorldBorderShrinkDelay() / 20 / 60;
            GlobalVariables.borderTime = String.valueOf(borderTimeInMinutes - minutesTimer);

            String borderShrinkString = "in " + GlobalVariables.borderTime + " Minuten";

            if (Long.parseLong(GlobalVariables.borderTime) <= 0) {
                borderShrinkString = "jetzt";
            }
            
            // Set scoreboard values
            int shrinkSize = WaloConfig.getWorldBorderShrinkSize() / 2;
            String[] scores = {

                    "§aPing: §f" + Ping.getPing(p) + " ms§a TPS: §f" + (int) Math.ceil(Lag.getTPS()), " ",
                    GlobalVariables.protection ? (minutesTimer < 10 ? (10 - minutesTimer) + " Minuten" : "an") : "§caus", "§aSchutzzeit: ", "  ",
                    "+" + shrinkSize + "; -" + shrinkSize, "§aVerkleinert auf:", "   ",
                    borderShrinkString, "§aBorder verkleinert:", "    ",
                    "+" + borderSize + "; -" + borderSize, "§aBordergröße:", "     ",
                    "§aSpielzeit: §f" + playTime

            };

            int i = 0;
            for (String score : scores) {
                objective.getScore(score).setScore(++i);
            }

        } else {
            // Set scoreboard values
            String[] scores = {

                    "10 Minuten", "§aSchutzzeit: ", " ",
                    "+" + borderSize + "; -" + borderSize, "§aBordergröße:"

            };

            int i = 0;
            for (String score : scores) {
                objective.getScore(score).setScore(++i);
            }
        }

        p.setScoreboard(scoreboard);
    }
}

package me.lowlauch.walo;

import me.lowlauch.walo.misc.GlobalVariables;
import me.lowlauch.walo.tasks.LagTask;
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
        }, 0, 1200L);
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

            int shrinkSize = WaloConfig.getWorldBorderShrinkSize() / 2;
            String[] scores = {
                "§aPing: §f" + Ping.getPing(p) + " ms§a TPS: §f" + (int) Math.ceil(LagTask.getTPS()), " ",
                GlobalVariables.protection ? (minutesTimer < 10 ? (10 - minutesTimer) + " Minuten" : "an") : "§caus", "§aSchutzzeit: ", "  ",
                "+" + shrinkSize + "; -" + shrinkSize, "§aVerkleinert auf:", "   ",
                borderShrinkString, "§aBorder verkleinert:", "    ",
                "+" + borderSize + "; -" + borderSize, "§aBordergröße:", "     ", // <- as far as I can tell,
                "§aSpielzeit: §f" + playTime                                      // there needs to be +1 space for every line;
            };                                                                    // idk... I wrote this code a while ago

            int i = 0;
            for (String score : scores) {
                objective.getScore(score).setScore(++i);
            }

        } else { // Before game start
            String[] scores = {
                    "10 Minuten", "§aSchutzzeit: ", " ",
                    "+" + borderSize + "; -" + borderSize, "§aBordergröße:", "  ",
                    Bukkit.getServer().getOnlinePlayers().size() + "/" + WaloConfig.getAutostartRequiredPlayers(), "§aSpieler bis zum Start: "
            };

            int i = 0;
            for (String score : scores) {
                objective.getScore(score).setScore(++i);
            }
        }

        p.setScoreboard(scoreboard);
    }
}

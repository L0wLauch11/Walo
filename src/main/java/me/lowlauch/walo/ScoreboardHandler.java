package me.lowlauch.walo;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.time.LocalTime;

public class ScoreboardHandler
{
    private static int minutesTimer = 0;
    private static Objective objective;
    private static Scoreboard scoreboard;
    private static BukkitTask task;

    public static void setScoreboard(Player p)
    {
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        objective = scoreboard.registerNewObjective("walo", "dummy");

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("§6§lWalo");

        p.setScoreboard(scoreboard);
    }

    public static void updateScoreboard()
    {
        if(task == null)
            task = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), () -> {
                for(Player p : Bukkit.getOnlinePlayers())
                {
                    if(!p.hasMetadata("no-scoreboard"))
                        updatePlayerScoreboard(p);
                }
            }, 0, 1200);
    }

    public static void updatePlayerScoreboard(Player p)
    {
        setScoreboard(p);

        int borderSize = Main.getInstance().getConfig().getInt("worldborder.size")/2;
        if(GlobalVariables.started)
        {
            // Update timer
            minutesTimer++;
            LocalTime timeOfDay = LocalTime.ofSecondOfDay(minutesTimer * 60L);

            // Set scoreboard values
            objective.getScore("§aSpielzeit:").setScore(14);
            objective.getScore(String.valueOf(timeOfDay)).setScore(13);

            objective.getScore("    ").setScore(12);

            objective.getScore("§aBordergröße:").setScore(11);
            objective.getScore("+" + borderSize + "; -" + borderSize).setScore(10);

            objective.getScore("   ").setScore(9);

            objective.getScore("§aBorder verkleinert:").setScore(8);
            objective.getScore(GlobalVariables.borderTime.split("\\.")[0] + " Uhr").setScore(7);

            objective.getScore("  ").setScore(6);

            int shrinkSize = Main.getInstance().getConfig().getInt("worldborder.shrinksize")/2;
            objective.getScore("§aVerkleinerung auf:").setScore(5);
            objective.getScore("+" + shrinkSize + "; -" + shrinkSize).setScore(4);

            objective.getScore(" ").setScore(3);

            objective.getScore("§aSchutzzeit: ").setScore(2);


            if(GlobalVariables.protection)
            {
                if(minutesTimer < 10)
                    objective.getScore((10 - minutesTimer) + " Minuten").setScore(1);
                else
                    objective.getScore("an").setScore(1);
            } else
            {
                objective.getScore("§caus!").setScore(1);
            }
        } else
        {
            // Set scoreboard values
            objective.getScore("§aBordergröße:").setScore(5);
            objective.getScore("+" + borderSize + "; -" + borderSize).setScore(4);

            objective.getScore(" ").setScore(3);

            objective.getScore("§aSchutzzeit: ").setScore(2);
            objective.getScore("10 Minuten").setScore(1);
        }

        p.setScoreboard(scoreboard);
    }
}

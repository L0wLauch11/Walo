package me.lowlauch.walo;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.time.LocalTime;

public class ScoreboardHandler
{
    private static int minutesTimer = 0;
    private static LocalTime timeOfDay;

    public static void setScoreboard(Player p)
    {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("walo", "dummy");

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("§6§lWalo");
    }

    public static void updateScoreboard()
    {
        Bukkit.getScheduler().runTaskTimer(Main.getInstance(), () -> {
            for(Player p : Bukkit.getOnlinePlayers())
            {
                Scoreboard pScoreboard = p.getScoreboard();

                if(pScoreboard == null)
                {
                    setScoreboard(p);
                    pScoreboard = p.getScoreboard();
                }

                Objective objective = pScoreboard.getObjective("main-content");

                if(GlobalVariables.started)
                {
                    // Update timer
                    minutesTimer++;
                    timeOfDay = LocalTime.ofSecondOfDay(minutesTimer*60L);

                    // Set scoreboard values
                    objective.getScore("Spielzeit:").setScore(1);
                    objective.getScore(String.valueOf(timeOfDay)).setScore(2);

                    objective.getScore("Border kommt um:").setScore(4);
                    objective.getScore(GlobalVariables.borderTime).setScore(5);

                    int shrinkSize = Main.getInstance().getConfig().getInt("worldborder.shrinksize")/2;
                    objective.getScore("Verkleinerung auf:").setScore(7);
                    objective.getScore(String.valueOf(shrinkSize)).setScore(8);

                    objective.getScore("Schutzzeit: ").setScore(10);
                    objective.getScore("10 Minuten").setScore(11);

                    // Set empty spaces in between
                    for(int i = 0;  i <= 9; i += 3)
                        objective.getScore("").setScore(i);

                } else
                {
                    int borderSize = Main.getInstance().getConfig().getInt("worldborder.size")/2;

                    // Set scoreboard values
                    objective.getScore("Bordergröße:").setScore(1);
                    objective.getScore("+" + borderSize + "; -" + borderSize).setScore(2);

                    objective.getScore("Border kommt um:").setScore(4);
                    objective.getScore(GlobalVariables.borderTime).setScore(5);
                }
            }
        }, 0, 1200);
    }
}

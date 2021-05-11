package me.lowlauch.Walo;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardHandler
{
    public void setScoreboard(Player p)
    {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    }
}

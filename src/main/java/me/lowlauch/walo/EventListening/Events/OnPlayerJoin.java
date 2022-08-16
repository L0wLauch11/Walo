package me.lowlauch.walo.EventListening.Events;

import me.lowlauch.callable_di_disabler.DisableDamageIndicator;
import me.lowlauch.walo.Main;
import me.lowlauch.walo.ScoreboardHandler;
import me.lowlauch.walo.Teams.TeamSettingsItems.TeamsInventoryItem;
import me.lowlauch.walo.Teams.Teams;
import me.lowlauch.walo.WaloConfig;
import me.lowlauch.walo.database.WaloDatabase;
import me.lowlauch.walo.misc.GlobalVariables;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static me.lowlauch.walo.misc.GlobalVariables.damageIndicatorDisabler;

public class OnPlayerJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        // SQL
        Player p = e.getPlayer();
        WaloDatabase.createPlayer(p);

        // Scoreboard
        ScoreboardHandler.updatePlayerScoreboard(p);

        // Schedule damage indicator disable
        if (damageIndicatorDisabler) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> DisableDamageIndicator.sendDisable(p), 300L);
        }

        if (p.hasMetadata("no-scoreboard") && p.getScoreboard() != null)
            p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

        // Change name
        String playerTeamName = Teams.getTeamName(Teams.getTeamOfPlayer(p));
        if (playerTeamName != null) {
            p.setDisplayName(playerTeamName + "" + p.getName());
        }

        // Set the player tab name to display name
        p.setPlayerListName(e.getPlayer().getDisplayName());

        // Add Walo Panel Item to inventory
        if (!GlobalVariables.started) {
            p.getInventory().clear();
            TeamsInventoryItem.addItemToPlayerInventory(p);
        }

        // Set the player to the right gamemode
        if (!GlobalVariables.started)
            p.setGameMode(GameMode.ADVENTURE);
        else if (p.getGameMode().equals(GameMode.ADVENTURE))
            p.setGameMode(GameMode.SURVIVAL);

        // Death message
        if (p.isBanned())
            p.kickPlayer("Du bist tot oder hast combat-logging betrieben.");
    }
}

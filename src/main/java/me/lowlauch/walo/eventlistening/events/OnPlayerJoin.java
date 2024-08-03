package me.lowlauch.walo.eventlistening.events;

import me.lowlauch.callable_di_disabler.DisableDamageIndicator;
import me.lowlauch.walo.Main;
import me.lowlauch.walo.ScoreboardHandler;
import me.lowlauch.walo.WaloConfig;
import me.lowlauch.walo.teams.teamsettingsitems.TeamsInventoryItem;
import me.lowlauch.walo.teams.Teams;
import me.lowlauch.walo.database.WaloDatabase;
import me.lowlauch.walo.misc.GlobalVariables;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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

        if (!GlobalVariables.started) {
            // Set the player to the right gamemode
            p.setGameMode(GameMode.ADVENTURE);

            // Add Walo Panel Item to inventory
            p.getInventory().clear();
            TeamsInventoryItem.addItemToPlayerInventory(p);

        } else if (p.getGameMode().equals(GameMode.ADVENTURE)) {
            p.setGameMode(GameMode.SURVIVAL);
        }

        // Slowness and blindness on join
        if (GlobalVariables.started) {
            // Handle logout timer ban
            long currentTime = System.currentTimeMillis();
            long rejoinTimeout = WaloConfig.getRejoinTimeout() * 60000L;
            if (currentTime - GlobalVariables.playerLeaveTimestamps.get(p.getUniqueId()) >= rejoinTimeout) {
                String banString = Main.prefix + "Du bist zu lange offline gewesen!";
                Bukkit.getBanList(BanList.Type.NAME).addBan(p.getName(), banString, null, "Tot");
                p.kickPlayer(banString);
            }

            // Handle subsequent joining after game start
            if (!WaloConfig.getAllowSubsequentJoining()
                    && !GlobalVariables.startPlayersUUID.contains(p.getUniqueId().toString())) {
                p.kickPlayer("Sorry, das Spiel ist gerade am laufen.");
            }

            int seconds = 10;

            p.sendMessage(Main.prefix + ChatColor.RED + "Du bekommst nach dem einloggen " + seconds + " Sekunden lang einen Debuff!");

            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, seconds * 20, 1));
            p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, seconds * 20, 5));
            p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, seconds * 20, 1));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, seconds * 20, 2));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, seconds * 20, 2));
        }

        // Death message
        if (p.isBanned())
            p.kickPlayer("Du bist tot oder hast combat-logging betrieben.");
    }
}

package me.lowlauch.walo.eventlistening.events;

import me.lowlauch.callable_di_disabler.DisableDamageIndicator;
import me.lowlauch.walo.Main;
import me.lowlauch.walo.ScoreboardHandler;
import me.lowlauch.walo.WaloConfig;
import me.lowlauch.walo.misc.WorldUtil;
import me.lowlauch.walo.tasks.XpCountdownTask;
import me.lowlauch.walo.teams.teamsettingsitems.TeamsInventoryItem;
import me.lowlauch.walo.teams.Teams;
import me.lowlauch.walo.database.WaloDatabase;
import me.lowlauch.walo.misc.GlobalVariables;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

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

        // Teleport player inside border
        double blocksInset = 3;
        double worldBorderSize = p.getWorld().getWorldBorder().getSize();
        Location worldBorderCenter = p.getWorld().getWorldBorder().getCenter();

        double newX = p.getLocation().getX();
        double newY = p.getLocation().getY();
        double newZ = p.getLocation().getZ();

        double[] worldBorderBoundsX = new double[2];
        double[] worldBorderBoundsZ = new double[2];
        worldBorderBoundsX[0] = worldBorderCenter.getX() - worldBorderSize / 2;
        worldBorderBoundsX[1] = worldBorderCenter.getX() + worldBorderSize / 2;
        worldBorderBoundsZ[0] = worldBorderCenter.getZ() - worldBorderSize / 2;
        worldBorderBoundsZ[1] = worldBorderCenter.getZ() + worldBorderSize / 2;

        // X coordinate
        if (p.getLocation().getX() < worldBorderBoundsX[0]) {
            newX = worldBorderBoundsX[0] + blocksInset;
        }

        if (p.getLocation().getX() > worldBorderBoundsX[1]) {
            newX = worldBorderBoundsX[1] - blocksInset;
        }

        // Z coordinate
        if (p.getLocation().getZ() < worldBorderBoundsZ[0]) {
            newZ = worldBorderBoundsZ[0] + blocksInset;
        }

        if (p.getLocation().getZ() > worldBorderBoundsZ[1]) {
            newZ = worldBorderBoundsZ[1] - blocksInset;
        }

        // Y coordinate (y is up axis don't forget)
        newY = GlobalVariables.started ? newY : WorldUtil.calculateFloorY(60, p);

        p.teleport(new Location(p.getWorld(), newX, newY, newZ));

        // Set the player tab name to display name
        p.setPlayerListName(e.getPlayer().getDisplayName());

        if (!GlobalVariables.started) {
            p.setGameMode(GameMode.ADVENTURE);
            p.setTotalExperience(0);
            p.setExp(0);

            e.setJoinMessage(Main.prefix + p.getName() + " ist beigetreten! §7("
                    + Bukkit.getServer().getOnlinePlayers().size()
                    + "/" + WaloConfig.getAutostartRequiredPlayers() + ")");

            // Add Walo Panel Item to inventory
            p.getInventory().clear();
            TeamsInventoryItem.addItemToPlayerInventory(p);

            // Autostart check
            if (!GlobalVariables.autostartInitiated
                    && Bukkit.getServer().getOnlinePlayers().size() >= WaloConfig.getAutostartRequiredPlayers()) {
                Bukkit.getServer().broadcastMessage(Main.prefix + "Es sind genügend Spieler anwesend! Das Spiel startet in §6"
                        + WaloConfig.getAutostartSeconds() + " §7Sekunden!");

                XpCountdownTask.setCountdown(WaloConfig.getAutostartSeconds());

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Main.getCommandsManager().executeSubcommand(Bukkit.getServer().getConsoleSender(), "start");
                    }
                }.runTaskLater(Main.getInstance(), 20L * WaloConfig.getAutostartSeconds());

                GlobalVariables.autostartInitiated = true;
            }
        } else if (p.getGameMode().equals(GameMode.ADVENTURE)) {
            p.setGameMode(GameMode.SURVIVAL);
        }

        // Slowness and blindness on join
        if (GlobalVariables.started) {
            // Handle subsequent joining after game start
            if (!WaloConfig.getAllowSubsequentJoining()
                    && !GlobalVariables.startPlayersUUID.contains(p.getUniqueId().toString())) {
                p.kickPlayer(Main.prefix + "Sorry, das Spiel ist gerade am laufen.");
            }

            // Handle logout timer ban
            long currentTime = System.currentTimeMillis();
            long rejoinTimeout = WaloConfig.getRejoinTimeout() * 60000L; // convert to millis
            if (GlobalVariables.playerLeaveTimestamps.get(p.getUniqueId()) != null
                    && currentTime - GlobalVariables.playerLeaveTimestamps.get(p.getUniqueId()) >= rejoinTimeout) {
                String banString = Main.prefix + "Leider bist du zu lange offline geblieben und deswegen AUSGESCHIEDEN!";
                Bukkit.getBanList(BanList.Type.NAME).addBan(p.getName(), banString, null, "Tot");
                p.kickPlayer(banString);
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

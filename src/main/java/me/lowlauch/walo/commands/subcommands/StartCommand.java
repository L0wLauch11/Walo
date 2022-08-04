package me.lowlauch.walo.commands.subcommands;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.ScoreboardHandler;
import me.lowlauch.walo.WaloConfig;
import me.lowlauch.walo.commands.SubCommand;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static me.lowlauch.walo.misc.GlobalVariables.*;

public class StartCommand implements SubCommand {
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        timer = -1;

        seconds = 10;

        // Adds a routine that gets executed every second and stops until the timer exceeds the seconds
        // amount and then starts the game
        for (int i = 0; i <= seconds * 20; i += 20) {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
                public void run() {
                    timer++;

                    if (seconds - timer != 0) {
                        for (Player p : Bukkit.getOnlinePlayers())
                            p.playSound(p.getLocation(), Sound.NOTE_PLING, 10, 1);

                        Bukkit.broadcastMessage(Main.prefix + "Walo startet in §6" + (seconds - timer) + "§7 Sekunden!");
                    }

                    if (timer >= seconds) {
                        // we don't talk about this
                        // Warn the player 1 seconds before the timer runs out
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> Bukkit.getServer().broadcastMessage(Main.prefix + "Die §6Schutzzeit§7 endet in 1 Sekunde!"), 11980L);

                        // Warn the player 2 seconds before the timer runs out
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> Bukkit.getServer().broadcastMessage(Main.prefix + "Die §6Schutzzeit§7 endet in 2 Sekunden!"), 11960L);

                        // Warn the player 3 seconds before the timer runs out
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> Bukkit.getServer().broadcastMessage(Main.prefix + "Die §6Schutzzeit§7 endet in 3 Sekunden!"), 11940L);

                        // Warn the player 4 seconds before the timer runs out
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> Bukkit.getServer().broadcastMessage(Main.prefix + "Die §6Schutzzeit§7 endet in 4 Sekunden!"), 11920L);

                        // Warn the player 5 seconds before the timer runs out
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> Bukkit.getServer().broadcastMessage(Main.prefix + "Die §6Schutzzeit§7 endet in 5 Sekunden!"), 11900L);

                        // Warn the player 60 seconds before the timer runs out
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> Bukkit.getServer().broadcastMessage(Main.prefix + "Die §6Schutzzeit§7 endet in einer Minute!"), 10800L);

                        // Warn the player 5 minutes before the timer runs out
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> Bukkit.getServer().broadcastMessage(Main.prefix + "Die §6Schutzzeit§7 endet in 5 Minuten!"), 6000L);

                        // Toggle protection time off in 10 minutes
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
                            // Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "walo protection false");
                            new ProtectionCommand().execute(Bukkit.getServer().getConsoleSender(), new String[]{"protection", "false"});
                        }, 11999L);

                        //// DEACTIVATE NETHER AND SHRINK BORDER
                        // Warn player about border shrink
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> Bukkit.getServer().broadcastMessage(Main.prefix + "Die Border wird in §45 Minuten§7 auf §4100 Blöcke§7 in jede richtung gesetzt!\n" +
                                Main.prefix + "Der Nether wird in §45 Minuten§7 §4§ldeaktiviert!\n" +
                                Main.prefix + "§4§lDIES IST DIE EINZIGE WARNUNG!!!"), WaloConfig.getWorldBorderShinkDelay() - 6000L /*3 hours - 5 minutes 210000L*/);

                        // Shrink border
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
                            // Set worldborder in overworld
                            World world = Bukkit.getWorld("world");
                            WorldBorder worldBorder = world.getWorldBorder();
                            worldBorder.setCenter(0, 0);
                            worldBorder.setSize(WaloConfig.getWorldBorderShrinkSize(), 400L);

                            // Set worldborder in nether
                            World nether = Bukkit.getWorld("world_nether");
                            WorldBorder netherBorder = nether.getWorldBorder();
                            netherBorder.setCenter(0, 0);
                            netherBorder.setSize(worldBorder.getSize());
                            netherBorder.setSize(0, 400L);

                            // set 0, 0 to lava
                            for (int i1 = 0; i1 < 255; i1++) {
                                Block block = nether.getBlockAt(0, i1, 0);
                                if (block.getType() != Material.AIR)
                                    block.setType(Material.LAVA);

                                block = nether.getBlockAt(1, i1, 0);
                                if (block.getType() != Material.AIR)
                                    block.setType(Material.LAVA);

                                block = nether.getBlockAt(-1, i1, -1);
                                if (block.getType() == Material.AIR)
                                    block.setType(Material.LAVA);

                                block = nether.getBlockAt(0, i1, -1);
                                if (block.getType() != Material.AIR)
                                    block.setType(Material.LAVA);
                            }

                            Bukkit.getServer().broadcastMessage(Main.prefix + "Die Border wird auf §4" + WaloConfig.getWorldBorderShrinkSize() / 2 + " Blöcke§7 in jede Richtung gesetzt!" +
                                    "\n" + Main.prefix + "Im Nether auf §40 Blöcke!");
                        }, WaloConfig.getWorldBorderShinkDelay());

                        // Start Walo
                        // Loop through all players and heal them and all that other stuff
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            p.setHealth(20.0f);
                            p.setSaturation(20.0f);
                            p.setFoodLevel(20);
                            p.setGameMode(GameMode.SURVIVAL);
                            p.setWalkSpeed(0.2f);
                            p.setLevel(0);
                            p.setExp(0);
                            p.getInventory().clear();
                            p.updateInventory();

                            p.playSound(p.getLocation(), Sound.NOTE_PLING, 10, 1);
                        }

                        Player playerComSender = (Player) commandSender;
                        World world = playerComSender.getWorld();

                        // Some properties change
                        world.setThundering(false);
                        world.setStorm(false);
                        world.setDifficulty(Difficulty.NORMAL);
                        world.setTime(0);

                        WorldBorder worldBorder = world.getWorldBorder();
                        worldBorder.setCenter(0, 0);
                        worldBorder.setSize(WaloConfig.getWorldBorderSize(), 0);

                        protection = true;
                        started = true;

                        // Calculate border time
                        long borderTimeInMinutes = WaloConfig.getWorldBorderShinkDelay() / 20 / 60;
                        borderTime = Long.toString(borderTimeInMinutes);

                        // Scoreboard
                        ScoreboardHandler.updateScoreboard();

                        Bukkit.broadcastMessage(Main.prefix + "Walo wurde §6gestartet§7.");
                    }
                }
            }, i);// 60 L == 3 sec, 20 ticks == 1 sec
        }
    }

    @Override
    public String getName() {
        return "start";
    }

    @Override
    public boolean requiresOp() {
        return true;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public int requiredArguments() {
        return 0;
    }
}

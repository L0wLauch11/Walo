package me.lowlauch.walo.commands.subcommands;

import me.lowlauch.callable_di_disabler.DisableDamageIndicator;
import me.lowlauch.walo.Main;
import me.lowlauch.walo.ScoreboardHandler;
import me.lowlauch.walo.WaloConfig;
import me.lowlauch.walo.commands.SubCommand;
import me.lowlauch.walo.database.WaloDatabase;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.lowlauch.walo.misc.GlobalVariables.*;

public class StartCommand implements SubCommand {
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        timer = -1;
        seconds = 10;

        if (damageIndicatorDisabler) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                DisableDamageIndicator.sendDisable(p);
            }
        }

        // Adds a routine that gets executed every second and stops until the timer exceeds the seconds
        // amount and then starts the game
        for (int i = 0; i <= seconds * 20; i += 20) {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
                timer++;

                // Start countdown
                if (seconds - timer != 0) {
                    for (Player p : Bukkit.getOnlinePlayers())
                        p.playSound(p.getLocation(), Sound.NOTE_PLING, 10, 1);

                    String plural = "n!";
                    if (seconds - timer == 1) {
                        plural = "!"; // remove plural
                    }

                    Bukkit.broadcastMessage(Main.prefix + "Walo startet in §6" + (seconds - timer) + "§7 Sekunde" + plural);
                }

                // Start game
                if (timer >= seconds) {
                    addProtectionTimeWarnings(new int[]{1, 2, 3, 4, 5, 60, 60*5});

                    // Schedule: Toggle protection time off in 10 minutes
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
                        new ProtectionCommand().execute(Bukkit.getServer().getConsoleSender(), new String[]{"protection", "false"});
                    }, 11999L);

                    // Schedule: Warn player about nether border shrink
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () ->
                            Bukkit.getServer().broadcastMessage(
                                    Main.prefix + "Die Border wird in §45 Minuten§7 auf §4100 Blöcke§7 in jede richtung gesetzt!\n" +
                                    Main.prefix + "Der Nether wird in §45 Minuten§7 §4§ldeaktiviert!\n" +
                                    Main.prefix + "§4§lDies ist die einzige Warnung!"
                            ), WaloConfig.getWorldBorderShrinkDelay() - 6000L /* = 5 minutes*/
                    );

                    // Schedule: Shrink border
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
                        // Set worldborder in overworld
                        World world = Bukkit.getWorld("world");
                        WorldBorder worldBorder = world.getWorldBorder();
                        worldBorder.setCenter(0, 0);
                        worldBorder.setSize(WaloConfig.getWorldBorderShrinkSize(), WaloConfig.getWorldBorderShrinkDuration());

                        // Set worldborder in nether
                        World nether = Bukkit.getWorld("world_nether");
                        WorldBorder netherBorder = nether.getWorldBorder();
                        netherBorder.setCenter(0, 0);
                        netherBorder.setSize(worldBorder.getSize());
                        netherBorder.setSize(0, WaloConfig.getWorldBorderShrinkDuration());

                        // set 0, 0 to lava
                        for (int i1 = 0; i1 < 255; i1++) {
                            Block block = nether.getBlockAt(0, i1, 0);
                            if (block.getType() == Material.AIR || block.getType() == Material.NETHERRACK)
                                block.setType(Material.LAVA);

                            block = nether.getBlockAt(1, i1, 0);
                            if (block.getType() == Material.AIR || block.getType() == Material.NETHERRACK)
                                block.setType(Material.LAVA);

                            block = nether.getBlockAt(-1, i1, -1);
                            if (block.getType() == Material.AIR || block.getType() == Material.NETHERRACK)
                                block.setType(Material.LAVA);

                            block = nether.getBlockAt(0, i1, -1);
                            if (block.getType() == Material.AIR || block.getType() == Material.NETHERRACK)
                                block.setType(Material.LAVA);
                        }

                        Bukkit.getServer().broadcastMessage(
                                Main.prefix + "Die Border wird auf §4" + WaloConfig.getWorldBorderShrinkSize() / 2
                                        + " Blöcke§7 in jede Richtung gesetzt!"
                                        + "\n" + Main.prefix + "Im Nether auf §40 Blöcke!"
                        );
                    }, WaloConfig.getWorldBorderShrinkDelay());

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

                        // Only players who were here since the start can rejoin later
                        startPlayersUUID.add(p.getUniqueId().toString());

                        // Database stuff
                        if (!statsDisabled) {
                            WaloDatabase.addPlayerPlaycount(p);
                        }
                    }

                    World world = Bukkit.getServer().getWorld("world");

                    // Some properties change
                    world.setThundering(false);
                    world.setStorm(false);
                    world.setDifficulty(Difficulty.NORMAL);
                    world.setTime(0);

                    WorldBorder worldBorder = world.getWorldBorder();
                    worldBorder.setCenter(0, 0);
                    worldBorder.setSize(WaloConfig.getWorldBorderSize(), 0);

                    if (WaloConfig.getProtectionTimeEnabled()) {
                        protection = true;
                    }

                    // Calculate border time
                    long borderTimeInMinutes = WaloConfig.getWorldBorderShrinkDelay() / 20 / 60;
                    borderTime = Long.toString(borderTimeInMinutes);

                    ScoreboardHandler.updateScoreboard();

                    Bukkit.broadcastMessage(Main.prefix + "Walo wurde §6gestartet§7.");
                    started = true;
                }
            }, i);// 60 L == 3 sec, 20 ticks == 1 sec
        }
    }

    public void addProtectionTimeWarnings(int[] seconds) {
        long tenMinutesInTicks = 12000L;

        for (int time : seconds) {
            long secondInTicks = time * 20L;
            String message;
            String secondsOrMinutes = "Sekunden";
            String secondOrMinute = "Sekunde";

            if (time % 60 == 0) {
                secondsOrMinutes = "Minuten";
                secondOrMinute = "Minute";

                time /= 60;
            }

            if (time == 1) {
                message = Main.prefix + "Die §6Schutzzeit§7 endet in " + time + " " + secondOrMinute + "!";
            } else {
                message = Main.prefix + "Die §6Schutzzeit§7 endet in " + time + " " + secondsOrMinutes + "!";
            }

            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(),
                    () -> Bukkit.getServer().broadcastMessage(message),
                    tenMinutesInTicks - secondInTicks);
        }
    }

    @Override
    public String getName() {
        return "start";
    }

    @Override
    public String getHelp() {
        return ": §7Startet das Spiel. Leitet auch einige wichtige Prozesse ein, wie das verkleinern der Border etc...";
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

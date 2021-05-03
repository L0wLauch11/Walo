package me.lowlauch.Walo;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class Commands implements CommandExecutor
{
    public static boolean started = false;
    public static boolean protection = false;
    public static boolean statsDisabled = false;
    public static boolean emeraldsDropDiamonds = false;

    private static int timer = -1;
    private static CommandSender comSender;
    private static int seconds;

    public boolean onCommand(final CommandSender commandSender, Command command, String commandLabel, String[] args)
    {
        // Normal commands
        if(commandLabel.equalsIgnoreCase("walo"))
        {
            if (args.length >= 1)
            {
                if(args[0].equalsIgnoreCase("stats"))
                {
                    Main.getInstance().reloadConfig();

                    Player checkStatsPlayer = null;
                    
                    if(args.length != 1)
                    {
                        // Display stats for another player
                        checkStatsPlayer = Bukkit.getPlayer(args[1]);
                        
                    } else
                    {
                        // Show the stats for himself
                        checkStatsPlayer = (Player) commandSender;
                    }

                    if(checkStatsPlayer != null)
                    {
                        int kills = Main.getInstance().db.getKills(checkStatsPlayer.getUniqueId());

                        // Send him the stats for this player
                        commandSender.sendMessage(Main.prefix + "Stats für: §a" + checkStatsPlayer.getName()
                                + "\n§6Kills: §7" + kills);
                    } else
                        commandSender.sendMessage(Main.prefix + "Dieser Spieler ist nicht online, oder existiert nicht.");

                    return true;
                }
            }
        }

        // Op commands
        if(commandLabel.equalsIgnoreCase("walo") && commandSender.isOp())
        {
            if (args.length >= 1)
            {
                switch (args[0])
                {
                    case "help":
                        if(commandSender.isOp())
                        {
                            commandSender.sendMessage("§7/walo §6start §7- §aStartet das Walo.\n" +
                                    "§7/walo §6resume §7- §aSetzt Walo fort, falls was schiefgegangen ist.\n" +
                                    "§7/walo §6stats <Spieler> §7- §aZeigt die Stats eines Spielers an.\n" +
                                    "§7/walo §6setupborder §7- §aSetzt die Border auf 25, 25.\n" +
                                    "§7/walo §6disablestats §7- §aDeaktiviert temporär Stats.\n" +
                                    "§7/walo §6protection §7- §aToggled manuell die Schutzzeit.\n" +
                                    "§7/walo §6addmate <Spieler1> <Spieler2> §7- §aAdded einen Mate zu dem Team.\n" +
                                    "§7/walo §6teamtag <String> §7- §aSetzt einen Team-Tag Text.");
                        }
                    break;

                    case "setupborder":
                        // Set worldborder to lobby
                        WorldBorder worldBorder = Bukkit.getWorld("world").getWorldBorder();

                        worldBorder.setCenter(0, 0);
                        worldBorder.setSize(25, 0);
                        break;

                    case "start":
                        comSender = commandSender;
                        timer = -1;

                        seconds = 10;

                        // Adds a routine that gets executed every second and stops until the timer exceeds the seconds
                        // amount and then starts the game
                        for(int i = 0; i <= seconds*20; i += 20)
                        {
                            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
                                public void run() {
                                    timer++;

                                    if(seconds-timer != 0)
                                    {
                                        for (Player p : Bukkit.getOnlinePlayers())
                                            p.playSound(p.getLocation(), Sound.NOTE_PLING, 10, 1);

                                        Bukkit.broadcastMessage(Main.prefix + "Walo startet in §6" + (seconds - timer) + "§7 Sekunden!");
                                    }

                                    if(timer >= seconds)
                                    {
                                        // This code should be optimized
                                        // Warn the player 1 seconds before the timer runs out
                                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable()
                                        {
                                            public void run()
                                            {
                                                Bukkit.getServer().broadcastMessage(Main.prefix + "Die §6Schutzzeit§7 endet in 1 Sekunde!");
                                            }
                                        }, 11980L);

                                        // Warn the player 2 seconds before the timer runs out
                                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable()
                                        {
                                            public void run()
                                            {
                                                Bukkit.getServer().broadcastMessage(Main.prefix + "Die §6Schutzzeit§7 endet in 2 Sekunden!");
                                            }
                                        }, 11960L);

                                        // Warn the player 3 seconds before the timer runs out
                                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable()
                                        {
                                            public void run()
                                            {
                                                Bukkit.getServer().broadcastMessage(Main.prefix + "Die §6Schutzzeit§7 endet in 3 Sekunden!");
                                            }
                                        }, 11940L);

                                        // Warn the player 4 seconds before the timer runs out
                                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable()
                                        {
                                            public void run()
                                            {
                                                Bukkit.getServer().broadcastMessage(Main.prefix + "Die §6Schutzzeit§7 endet in 4 Sekunden!");
                                            }
                                        }, 11920L);

                                        // Warn the player 5 seconds before the timer runs out
                                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable()
                                        {
                                            public void run()
                                            {
                                                Bukkit.getServer().broadcastMessage(Main.prefix + "Die §6Schutzzeit§7 endet in 5 Sekunden!");
                                            }
                                        }, 11900L);

                                        // Warn the player 60 seconds before the timer runs out
                                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable()
                                        {
                                            public void run()
                                            {
                                                Bukkit.getServer().broadcastMessage(Main.prefix + "Die §6Schutzzeit§7 endet in einer Minute!");
                                            }
                                        }, 10800L);

                                        // Warn the player 5 minutes before the timer runs out
                                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable()
                                        {
                                            public void run()
                                            {
                                                Bukkit.getServer().broadcastMessage(Main.prefix + "Die §6Schutzzeit§7 endet in 5 Minuten!");
                                            }
                                        }, 6000L);

                                        // Toggle protection time off in 10 minutes
                                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable()
                                        {
                                            public void run()
                                            {
                                                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "walo protection false");
                                            }
                                        }, 12000L);

                                        //// DEACTIVATE NETHER AND SHRINK BORDER
                                        // Warn player about border shrink
                                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable()
                                        {
                                            public void run()
                                            {
                                                Bukkit.getServer().broadcastMessage(Main.prefix + "Die Border wird in §45 Minuten§7 auf §4100 Blöcke§7 in jede richtung gesetzt!\n" +
                                                        Main.prefix + "Der Nether wird in §45 Minuten§7 §4§ldeaktiviert!\n" +
                                                        Main.prefix + "§4§lDIES IST DIE EINZIGE WARNUNG!!!");
                                            }
                                        }, Main.getInstance().getConfig().getLong("worldborder.shrinkdelay") - 6000L /*3 hours - 5 minutes 210000L*/);

                                        // Shrink border
                                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable()
                                        {
                                            public void run()
                                            {

                                                // Set worldborder in overworld
                                                World world = Bukkit.getWorld("world");
                                                WorldBorder worldBorder = world.getWorldBorder();
                                                worldBorder.setCenter(0, 0);
                                                worldBorder.setSize(Main.getInstance().getConfig().getDouble("worldborder.shrinksize"), 400L);

                                                // Set worldborder in nether
                                                World nether = Bukkit.getWorld("world_nether");
                                                WorldBorder netherBorder = nether.getWorldBorder();
                                                netherBorder.setCenter(0, 0);
                                                netherBorder.setSize(worldBorder.getSize());
                                                netherBorder.setSize(0, 400L);

                                                // set 0, 0 to lava
                                                for(int i = 0; i < 255; i++)
                                                {
                                                    Block block = nether.getBlockAt(0, i, 0);
                                                    if(block.getType() != Material.AIR)
                                                        block.setType(Material.LAVA);

                                                    block = nether.getBlockAt(1, i, 0);
                                                    if(block.getType() != Material.AIR)
                                                        block.setType(Material.LAVA);

                                                    block = nether.getBlockAt(-1, i, -1);
                                                    if(block.getType() == Material.AIR)
                                                        block.setType(Material.LAVA);

                                                    block = nether.getBlockAt(0, i, -1);
                                                    if(block.getType() != Material.AIR)
                                                        block.setType(Material.LAVA);
                                                }

                                                Bukkit.getServer().broadcastMessage(Main.prefix + "Die Border wird auf §4" + Main.getInstance().getConfig().getInt("worldborder.shrinksize") / 2 + " Blöcke§7 in jede Richtung gesetzt!" +
                                                        "\n" + Main.prefix + "Im Nether auf §40 Blöcke!");
                                            }
                                        }, Main.getInstance().getConfig().getLong("worldborder.shrinkdelay"));

                                        // Start Walo
                                        // Loop through all players and heal them and all that other stuff
                                        for (Player p : Bukkit.getOnlinePlayers())
                                        {
                                            p.setHealth(20.0f);
                                            p.setSaturation(20.0f);
                                            p.setFoodLevel(20);
                                            p.setGameMode(GameMode.SURVIVAL);
                                            p.setWalkSpeed(0.2f);
                                            p.setLevel(0);
                                            p.setExp(0);

                                            p.playSound(p.getLocation(), Sound.NOTE_PLING, 10, 1);
                                        }

                                        // Some properties change
                                        Bukkit.dispatchCommand(comSender, "weather clear");
                                        Bukkit.dispatchCommand(comSender, "difficulty normal");
                                        Bukkit.dispatchCommand(comSender, "time set day");
                                        Bukkit.dispatchCommand(comSender, "walo protection true");
                                        Bukkit.dispatchCommand(comSender, "clear @a");
                                        Bukkit.dispatchCommand(comSender, "worldborder set " + Main.getInstance().getConfig().getInt("worldborder.size") + " 0");

                                        started = true;
                                        Bukkit.broadcastMessage(Main.prefix + "Walo wurde §6gestartet§7.");
                                    }
                                }
                            }, i);// 60 L == 3 sec, 20 ticks == 1 sec
                        }
                        break;

                    case "disablestats":
                        statsDisabled = true;
                        Bukkit.getServer().broadcastMessage(Main.prefix + "In diesem Game werden §4§lkeine §7Stats aufgezeichnet!");
                        break;

                    case "resume":
                        comSender = commandSender;

                        // Loop through all players and heal them and all that other stuff
                        for (Player p : Bukkit.getOnlinePlayers())
                        {
                            p.setHealth(20.0f);
                            p.setSaturation(20.0f);
                            p.setFoodLevel(20);
                            p.setGameMode(GameMode.SURVIVAL);
                            p.setWalkSpeed(0.2f);

                            p.playSound(p.getLocation(), Sound.NOTE_PLING, 10, 1);
                        }

                        // Some properties change
                        Bukkit.dispatchCommand(comSender, "difficulty normal");

                        started = true;
                        Bukkit.broadcastMessage(Main.prefix + "Walo wurde §6fortgesetzt§7.");
                        break;

                    case "stop":
                        for (Player p : Bukkit.getOnlinePlayers())
                            p.setGameMode(GameMode.ADVENTURE);

                        started = false;
                        Bukkit.broadcastMessage(Main.prefix + "Walo wurde §4gestoppt§7.");
                        break;

                    case "protection":
                        if(args.length >= 2)
                        {
                            // Toggle protection period
                            try
                            {
                                protection = Boolean.parseBoolean(args[1]);
                            } catch(Exception ex)
                            {
                                // Let the player know if something failed
                                commandSender.sendMessage(Main.prefix + "Du musst true oder false eingeben!: " + ex.toString());
                            }

                            String string = "Die Schutzzeit ist " + protection;

                            string = string.replaceAll("false", "§caus");
                            string = string.replaceAll("true", "§aan");

                            Bukkit.broadcastMessage(Main.prefix + string + "§7!");
                        } else
                        {
                            // Set protection period to true or false
                            protection = !protection;

                            String string = "Die Schutzzeit ist " + protection;

                            string = string.replaceAll("false", "§caus");
                            string = string.replaceAll("true", "§aan");

                            Bukkit.broadcastMessage(Main.prefix + string + "§7!");
                        }
                        break;

                    case "addmate":
                        if(args.length == 3)
                        {
                            Player player = Bukkit.getServer().getPlayer(args[1]);
                            Player destinationPlayer = Bukkit.getServer().getPlayer(args[2]);

                            // Don't let the player add himself as mate
                            assert player != null;
                            assert destinationPlayer != null;
                            if(!player.getName().equals(destinationPlayer.getName()))
                            {
                                // Set the argument as mate in both the lists
                                String path = "mates." + player.getUniqueId().toString();
                                List<String> mates = Main.getInstance().getConfig().getStringList(path);

                                String pathDestinationPlayer = "mates." + destinationPlayer.getUniqueId().toString();
                                List<String> matesDestinationPlayer = Main.getInstance().getConfig().getStringList(pathDestinationPlayer);

                                mates.add(destinationPlayer.getUniqueId().toString());
                                matesDestinationPlayer.add(player.getUniqueId().toString());

                                Main.getInstance().getConfig().set(pathDestinationPlayer, matesDestinationPlayer);
                                Main.getInstance().getConfig().set(path, mates);

                                // Inform both players that they are now mates
                                commandSender.sendMessage(Main.prefix + "Zu §6" + player.getName() + "§7 seinen Mates wurde §6" + destinationPlayer.getName() + "§7 hinzugefügt.");

                                player.sendMessage(Main.prefix + "§6" + destinationPlayer.getName() + "§7 ist jetzt einer deiner Mates.");
                                destinationPlayer.sendMessage(Main.prefix + "§6" + player.getName() + "§7 ist jetzt einer deiner Mates.");

                                // Save the config
                                Main.getInstance().saveConfig();
                                Main.getInstance().reloadConfig();
                            } else
                            {
                                commandSender.sendMessage(Main.prefix + "Du kannst dich nicht selber als Mate adden!");
                            }
                        } else
                        {
                            commandSender.sendMessage(Main.prefix + "Du musst: §c/walo addmate <Spieler> <Team-Mate>§7 machen");
                        }
                        break;

                    case "teamtag":
                        if(args.length == 3)
                        {
                            // Save custom tag under variable
                            String customTag = args[2];

                            // Replace & with the color code symbol
                            customTag = customTag.replaceAll("&", "§");

                            // Sets a custom tag for a player and mates
                            Player p = Bukkit.getPlayer(args[1]);

                            List<String> playerMates = Main.getInstance().getConfig().getStringList("mates." + p.getUniqueId().toString());

                            int length = playerMates.size();

                            String finalName = customTag + " §r" + p.getName();

                            p.setDisplayName(finalName);
                            p.setPlayerListName(finalName);
                            Main.getInstance().getConfig().set("tags." + p.getUniqueId().toString(), finalName);

                            p.sendMessage(Main.prefix + "Euer Team-Tag ist jetzt: " + customTag);

                            if(!customTag.equals("reset"))
                            {
                                // Loop through all mates and change their tag
                                for (String playerMate : playerMates)
                                {
                                    Player changeTagPlayer = Bukkit.getPlayer(UUID.fromString(playerMate));
                                    finalName = customTag + " §r" + changeTagPlayer.getName();

                                    // Save tag under config
                                    Main.getInstance().getConfig().set("tags." + changeTagPlayer.getUniqueId().toString(), finalName);

                                    changeTagPlayer.setDisplayName(finalName);
                                    changeTagPlayer.setPlayerListName(finalName);

                                    changeTagPlayer.sendMessage(Main.prefix + "Euer Team-Tag ist jetzt: " + customTag);
                                }

                                Main.getInstance().saveConfig();
                                Main.getInstance().reloadConfig();
                            } else
                            {
                                p.setDisplayName(p.getName());
                                p.setPlayerListName(p.getName());

                                p.sendMessage(Main.prefix + "Euer Team-Tag ist jetzt resettet worden :(");

                                // Save tag under config
                                Main.getInstance().getConfig().set("tags." + p.getUniqueId().toString(), p.getName());


                                for (String playerMate : playerMates)
                                {
                                    Player changeTagPlayer = Bukkit.getPlayer(UUID.fromString(playerMate));
                                    changeTagPlayer.setDisplayName(changeTagPlayer.getName());
                                    changeTagPlayer.setPlayerListName(changeTagPlayer.getName());

                                    // Save tag under config
                                    Main.getInstance().getConfig().set("tags." + changeTagPlayer.getUniqueId().toString(), changeTagPlayer.getName());

                                    changeTagPlayer.sendMessage(Main.prefix + "Euer Team-Tag ist jetzt resettet worden :(");
                                }

                                Main.getInstance().saveConfig();
                                Main.getInstance().reloadConfig();
                            }
                        } else
                            commandSender.sendMessage("nicht genug argumente");

                        break;

                    case "bc":
                        if(args.length > 1)
                        {
                            String message = "";
                            for(int i = 1; i < args.length; i++)
                            {
                                message += args[i] + " ";
                            }
                            message = message.replaceAll("&", "§");

                            Bukkit.getServer().broadcastMessage(Main.prefix + message);
                        } else
                        {
                            commandSender.sendMessage(Main.prefix + "Du musst: §c/walo bc <deine message>");
                        }
                        break;

                    case "emeralds=diamonds":
                        emeraldsDropDiamonds = !emeraldsDropDiamonds;

                        String string = "§2Emeralds §7droppen jetzt" + emeraldsDropDiamonds + "§bDiamonds!";

                        string.replaceAll("false", "§4keine ");
                        string.replaceAll("true", "");

                        Bukkit.getServer().broadcastMessage(string);
                        break;

                    default:
                        commandSender.sendMessage(Main.prefix + "Diesen Command gibt es nicht.");
                        break;
                }
            }

        } else
        {
            commandSender.sendMessage(Main.prefix + "Diesen Command gibt es nicht.");
        }
        return true;
    }
}

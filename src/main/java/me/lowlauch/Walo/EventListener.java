package me.lowlauch.Walo;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class EventListener implements Listener
{
    @EventHandler
    public void onPlayerTravel(final PlayerTeleportEvent e)
    {
        // If the nether is disabled then stop the event
        if(!Commands.started && e.getTo().getWorld().getName().toLowerCase().contains("nether"))
        {
            e.getPlayer().sendMessage(Main.prefix + "§c>:(");

            if(e.getFrom() != null)
                e.getPlayer().teleport(new Location(e.getFrom().getWorld(), e.getFrom().getX(), e.getFrom().getY(), e.getFrom().getZ()));
            e.setCancelled(true);
        } else {
            // Spawn a protecting bedrock ring
            if (e.getTo().getWorld().getName().toLowerCase().contains("nether") && !e.getFrom().getWorld().getName().toLowerCase().contains("nether") && e.getPlayer().getGameMode().equals(GameMode.SURVIVAL))
            {
                final double playerX = e.getTo().getX();
                final double playerY = e.getTo().getY();
                final double playerZ = e.getTo().getZ();

                final int rad = 15;
                // Make a protecting bedrock circle under the player
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
                    public void run() {
                        for (int i = -rad; i < rad; i++)
                        {
                            for (int j = -rad; j < rad; j++)
                            {
                                for(int k = -rad; k < rad; k++)
                                {
                                    Block blockAtCurrentPos = e.getTo().getWorld().getBlockAt(new Location(e.getTo().getWorld(), playerX + i, playerY + j, playerZ + k));

                                    // Replace lava fire, and beds with nothing
                                    if(blockAtCurrentPos.getType().equals(Material.LAVA) ||
                                            blockAtCurrentPos.getType().equals(Material.STATIONARY_LAVA) ||
                                            blockAtCurrentPos.getType().equals(Material.FIRE) ||
                                            blockAtCurrentPos.getType().equals(Material.BED))
                                    {
                                        blockAtCurrentPos.setType(Material.AIR);
                                    }
                                }

                                // Spawn a protecting bedrock floor under players feet
                                e.getTo().getWorld().getBlockAt(new Location(e.getTo().getWorld(), playerX + i, playerY - 2, playerZ + j)).setType(Material.BEDROCK);
                            }
                        }
                    }
                }, 10L);// 60 L == 3 sec, 20 ticks == 1 sec
            }
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent e)
    {
        // Disable Golden OP Apples
        Material itemType = e.getRecipe().getResult().getType();
        byte itemData = e.getRecipe().getResult().getData().getData();
        if(itemType == Material.ENDER_CHEST || itemType == Material.HOPPER || (itemType == Material.GOLDEN_APPLE && itemData==1))
        {
            e.getInventory().setResult(new ItemStack(Material.AIR));
            for(HumanEntity he:e.getViewers())
            {
                if(he instanceof Player)
                {
                    he.sendMessage(Main.prefix + "§6OP Apples§7 sind §cverboten§7!");
                }
            }
        }
    }

    @EventHandler
    public void onBrew(BrewEvent e)
    {
        // Disable Strength Potions
        if(e.getContents().contains(Material.BLAZE_POWDER)
        || e.getContents().contains(Material.FERMENTED_SPIDER_EYE))
            e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        // Change name
        String customTag = Main.getInstance().getConfig().getString("tags." + e.getPlayer().getUniqueId().toString());
        e.getPlayer().setDisplayName(customTag);

        // Set the player tab name to display name
        e.getPlayer().setPlayerListName(e.getPlayer().getDisplayName());

        // Set the player to gamemode 2 if game hasn't started
        if(!Commands.started)
            e.getPlayer().setGameMode(GameMode.ADVENTURE);

        if(e.getPlayer().isBanned())
            e.getPlayer().kickPlayer("Du bist tot oder hast combat-logging betrieben.");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDisconnect(PlayerQuitEvent e)
    {
        // Combat logging protection
        if(e.getPlayer().getHealth() < 10.0f && e.getPlayer().getHealth() != 0.0f && Commands.started)
        {
            Bukkit.getBanList(BanList.Type.NAME).addBan(e.getPlayer().getName(), Main.prefix + "Du hast gelefted wie du wenige leben hattest", null, "Tot");
            Bukkit.broadcastMessage(Main.prefix + "§6" + e.getPlayer().getName() + " §7hat geleftet wie dieser Spieler wenige leben hatte! §cAusgeschieden§7!");
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e)
    {
        if(!Commands.started && e.getEntity() instanceof Player)
        {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDamageByEntity(EntityDamageByEntityEvent e)
    {
        if(Commands.protection)
        {
            if (e.getDamager() instanceof Player && e.getEntity() instanceof Player)
            {
                // If game hasn't started yet cancel the event
                e.getDamager().sendMessage(Main.prefix + "Die Schutzzeit ist noch an!");
                e.setCancelled(true);
            }
        }

        // This only applies to players
        if(Commands.started)
        {
            if(e.getEntity() instanceof Player && e.getDamager() instanceof Player)
            {
                Main.getInstance().reloadConfig();

                // Stop team mates from damaging each other
                Player victim = (Player) e.getEntity();
                Player damager = (Player) e.getDamager();

                List<String> victimMates;
                List<String> damagerMates;

                victimMates = Main.getInstance().getConfig().getStringList("mates." + victim.getUniqueId().toString());
                damagerMates = Main.getInstance().getConfig().getStringList("mates." + damager.getUniqueId().toString());

                int length = Math.max(victimMates.size(), damagerMates.size());

                // Check if he is team mate
                for(int i = 0; i < length; i++)
                {
                    if(victimMates.get(i).equals(damager.getUniqueId().toString()) || damagerMates.get(i).equals(victim.getUniqueId().toString()))
                        e.setCancelled(true);
                }
            }
        } else
        {
            if (e.getDamager() instanceof Player && e.getEntity() instanceof Player)
            {
                // If game hasn't started yet cancel the event
                e.getDamager().sendMessage(Main.prefix + "Das Spiel hat noch nicht gestartet!");
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerDie(PlayerDeathEvent e)
    {
        // Ban the player if he dies and the game has started
        if(Commands.started)
        {
            // Change the message a bit
            String deathMessage;
            Player killer = e.getEntity().getKiller();

            if(killer != null)
                deathMessage = Main.prefix + "§6" + e.getEntity().getName() + "§4 wurde von §6" + killer.getName() + "§4 getötet.";
            else
                deathMessage = Main.prefix + "§6" + e.getEntity().getName() + "§4 ist gestorben.";

            e.setDeathMessage(deathMessage);

            // Ban the player
            Bukkit.getBanList(BanList.Type.NAME).addBan(e.getEntity().getName(), Main.prefix + "Du bist gestorben", null, "Tot");
            e.getEntity().kickPlayer(Main.prefix + "§cDu bist gestorben.");

            // Give the killer a kill in stats
            if(Commands.statsDisabled)
            {
                e.getEntity().getKiller().sendMessage(Main.prefix + "Du hast §4keinen Kill§7 in den Stats bekommen!");
            } else
            {
                String path = "stats.kills." + e.getEntity().getKiller().getUniqueId().toString();
                Main.getInstance().getConfig().set(path, Main.getInstance().getConfig().getInt(path) + 1);
            }

            // THIS DOES NOT WORK YET!
            /*
            * // Check if only one team is alive
            if(Bukkit.getOnlinePlayers().size() == 1)
            {
                Player wonPlayer = (Player) Bukkit.getOnlinePlayers().toArray()[0];

                List<String> mates = Main.getInstance().getConfig().getStringList("mates." + wonPlayer.getUniqueId().toString());
                int length = mates.size();
                String winpath;

                // Give all his mates a win in stats
                for(int i = 0; i < length; i++)
                {
                    // Give the killer a kill in stats
                    winpath = "stats.wins." + mates.get(i);
                    Main.getInstance().getConfig().set(winpath, Main.getInstance().getConfig().getInt(winpath)+1);
                }

                // Give the last player alive a win
                winpath = "stats.wins." + wonPlayer.getUniqueId().toString();
                Main.getInstance().getConfig().set(winpath, Main.getInstance().getConfig().getInt(winpath)+1);

                Bukkit.getServer().broadcastMessage(Main.prefix + "§6" + wonPlayer.getName() + " §7 und sein Team haben §eWalo§7 gewonnen!");
            }

            // Check if only one team is alive
            for(Player p : Bukkit.getOnlinePlayers())
            {
                List<String> mates = Main.getInstance().getConfig().getStringList("mates." + p.getUniqueId().toString());

                int length = mates.size();
                int mateCount = 0;

                // Check if he is team mate
                for(int i = 0; i < length; i++)
                {
                    Player checkPlayer = (Player) Bukkit.getOnlinePlayers().toArray()[i];

                    if(mates.get(i).contains(checkPlayer.getUniqueId().toString()))
                        mateCount++;
                }

                // Someone won
                if(mateCount >= Bukkit.getOnlinePlayers().toArray().length);
                {
                    String winpath;

                    // Give all his mates a win in stats
                    for(int i = 0; i < length; i++)
                    {
                        // Give the killer a kill in stats
                        winpath = "stats.wins." + mates.get(i);
                        Main.getInstance().getConfig().set(winpath, Main.getInstance().getConfig().getInt(winpath)+1);
                    }

                    // Give the last player alive a win
                    winpath = "stats.wins." + p.getUniqueId().toString();
                    Main.getInstance().getConfig().set(winpath, Main.getInstance().getConfig().getInt(winpath)+1);

                    Bukkit.getServer().broadcastMessage(Main.prefix + "§6" + p.getName() + " §7 und sein Team haben §eWalo§7 gewonnen!");
                }
            }
            * */

            Main.getInstance().saveConfig();
        }
    }

    @EventHandler
    public void onPlayerBlockBreak(BlockBreakEvent e)
    {
        if(e.getPlayer().getWorld().getName().contains("nether") && e.getBlock().getType() == Material.OBSIDIAN)
        {
            e.getPlayer().sendMessage(Main.prefix + "Du kannst kein Obsidian im Nether abbauen!");
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e)
    {
        // Better chat formatting
        String message = e.getMessage();
        e.setFormat(e.getPlayer().getDisplayName() + "§7: " + message);
        e.setMessage("");
    }
}

package me.lowlauch.Walo;

import me.lowlauch.Walo.Commands.CommandVariables;
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

import java.util.List;
import java.util.UUID;

public class EventListener implements Listener
{
    @EventHandler
    public void onPlayerTravel(final PlayerTeleportEvent e)
    {
        // If the nether is disabled then stop the event
        if(!CommandVariables.started && e.getTo().getWorld().getName().toLowerCase().contains("nether"))
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
        ItemStack itemStack = e.getRecipe().getResult();
        Material itemType = itemStack.getType();

        ItemStack enchantedGoldenApple = new ItemStack(Material.GOLDEN_APPLE, 1, (short)1);

        if(itemStack.equals(enchantedGoldenApple))
        {
            e.getInventory().setResult(new ItemStack(Material.AIR));
            for(HumanEntity he : e.getViewers())
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
        // SQL
        Player p = e.getPlayer();
        Main.getInstance().db.createPlayer(p, "UUID,NAME,KILLS", String.format("%s,%s,%d",
                e.getPlayer().getUniqueId(), e.getPlayer().getName(), 0));

        // Change name
        String customTag = Main.getInstance().getConfig().getString("tags." + p.getUniqueId().toString());
        p.setDisplayName(customTag);

        // Set the player tab name to display name
        p.setPlayerListName(e.getPlayer().getDisplayName());

        // Set the player to the right gamemode
        if(!CommandVariables.started)
            p.setGameMode(GameMode.ADVENTURE);
        else if(p.getGameMode().equals(GameMode.ADVENTURE))
            p.setGameMode(GameMode.SURVIVAL);

        // Death message
        if(p.isBanned())
            p.kickPlayer("Du bist tot oder hast combat-logging betrieben.");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDisconnect(PlayerQuitEvent e)
    {
        Player p = e.getPlayer();
        EntityDamageEvent damageCause = p.getLastDamageCause();

        // Combat logging protection
        if(p.getHealth() < 10.0f && p.getHealth() != 0.0f && CommandVariables.started)
        {
            // Count the kill towards the player that last damaged them
            if(damageCause instanceof EntityDamageByEntityEvent)
            {
                // Ban the player if he dies and the game has started
                // Change the message a bit
                String deathMessage;
                Player killer = (Player) ((EntityDamageByEntityEvent) p.getLastDamageCause()).getDamager();

                deathMessage = Main.prefix + "§6" + p.getName() + "§4 hatte zu viel Angst vor §6" + killer.getName() + ".";

                Bukkit.getServer().broadcastMessage(deathMessage);

                // Ban the player
                Bukkit.getBanList(BanList.Type.NAME).addBan(p.getName(), Main.prefix + "Du bist gestorben", null, "Tot");
                p.kickPlayer(Main.prefix + "§cDu bist gestorben.");

                // Give the killer a kill in stats
                if(CommandVariables.statsDisabled)
                {
                    killer.sendMessage(Main.prefix + "Du hast §4keinen Kill§7 in den Stats bekommen!");
                } else
                {
                    // Add kill to database
                    UUID uuid = killer.getUniqueId();
                    Main.getInstance().db.setInt(uuid, "KILLS", Main.getInstance().db.getInt(uuid, "KILLS")+1);
                    Main.getInstance().db.orderBy("KILLS");
                }

            } else
            {
                // Ban Player
                Bukkit.getBanList(BanList.Type.NAME).addBan(e.getPlayer().getName(), Main.prefix + "Du hast gelefted wie du wenige leben hattest", null, "Tot");
                Bukkit.broadcastMessage(Main.prefix + "§6" + e.getPlayer().getName() + " §7hat geleftet wie dieser Spieler wenige leben hatte! §cAusgeschieden§7!");
            }
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e)
    {
        // Cancel damage before game has started
        if(!CommandVariables.started && e.getEntity() instanceof Player)
        {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDamageByEntity(EntityDamageByEntityEvent e)
    {
        if(CommandVariables.protection)
        {
            if (e.getDamager() instanceof Player && e.getEntity() instanceof Player)
            {
                // If game hasn't started yet cancel the event
                e.getDamager().sendMessage(Main.prefix + "Die Schutzzeit ist noch an!");
                e.setCancelled(true);
            }
        }

        // This only applies to players
        if(CommandVariables.started)
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
        if(CommandVariables.started)
        {
            Player p = e.getEntity();
            String deathMessage;
            Player killer = p.getKiller();

            // Player death
            if(killer != null)
            {
                deathMessage = Main.prefix + "§6" + p.getName() + "§4 wurde von §6" + killer.getName() + "§4 getötet.";

                // Ban the player
                Bukkit.getBanList(BanList.Type.NAME).addBan(p.getName(), Main.prefix + "Du bist gestorben", null, "Tot");
                p.kickPlayer(Main.prefix + "§cDu bist gestorben.");

                // Give the killer a kill in stats
                if(CommandVariables.statsDisabled)
                {
                    killer.sendMessage(Main.prefix + "Du hast §4keinen Kill§7 in den Stats bekommen!");
                } else
                {
                    /*  Old way of saving kills, without a database

                        String path = "stats.kills." + e.getEntity().getKiller().getUniqueId().toString();
                        Main.getInstance().getConfig().set(path, Main.getInstance().getConfig().getInt(path) + 1);

                    */

                    // Add kill to database
                    UUID uuid = killer.getUniqueId();
                    Main.getInstance().db.setInt(uuid, "KILLS", Main.getInstance().db.getInt(uuid, "KILLS")+1);
                    Main.getInstance().db.orderBy("KILLS");
                }
            } else
                // Normal death
                deathMessage = Main.prefix + "§6" + p.getName() + "§4 ist gestorben.";

            // Change death message
            e.setDeathMessage(deathMessage);
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

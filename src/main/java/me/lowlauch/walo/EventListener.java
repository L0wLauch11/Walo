package me.lowlauch.walo;

import me.lowlauch.walo.database.WaloDatabase;
import me.lowlauch.walo.misc.GlobalVariables;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
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
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class EventListener implements Listener {
    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent e) {
        // Disable dropping if game has not started
        if (!GlobalVariables.started) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerTravel(final PlayerTeleportEvent e) {
        boolean destinationIsNether = e.getTo().getWorld().getEnvironment().equals(World.Environment.NETHER);
        boolean originIsNether = e.getFrom().getWorld().getEnvironment().equals(World.Environment.NETHER);

        // If the nether is disabled then stop the event
        if (!GlobalVariables.started && destinationIsNether) {
            e.getPlayer().sendMessage(Main.prefix + "§cDer Nether ist erst nach dem Spielstart verfügbar!");
            if (e.getFrom() != null)
                e.getPlayer().teleport(new Location(e.getFrom().getWorld(), e.getFrom().getX(), e.getFrom().getY(), e.getFrom().getZ()));
            e.setCancelled(true);

            return;
        }

        if (!destinationIsNether || originIsNether || !(e.getPlayer().getGameMode().equals(GameMode.SURVIVAL)))
            return;

        // Spawn a protecting bedrock ring
        final double playerX = e.getTo().getX();
        final double playerY = e.getTo().getY();
        final double playerZ = e.getTo().getZ();

        final int rad = 15;
        // Make a protecting bedrock circle under the player
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
            for (int i = -rad; i < rad; i++) {
                for (int j = -rad; j < rad; j++) {
                    for (int k = -rad; k < rad; k++) {
                        Block blockAtCurrentPos = e.getTo().getWorld().getBlockAt(new Location(e.getTo().getWorld(), playerX + i, playerY + j, playerZ + k));

                        // Replace unfair objects with nothing
                        Material[] unfairBlocks = {

                                Material.LAVA,
                                Material.STATIONARY_LAVA,
                                Material.FIRE,
                                Material.BED

                        };

                        for (Material unfairBlock : unfairBlocks) {
                            if (blockAtCurrentPos.getType().equals(unfairBlock)) {
                                blockAtCurrentPos.setType(Material.AIR);
                            }
                        }

                    }

                    // Spawn a protecting bedrock floor under players feet
                    e.getTo().getWorld().getBlockAt(new Location(e.getTo().getWorld(), playerX + i, playerY - 2, playerZ + j)).setType(Material.BEDROCK);
                }
            }
        }, 10L);// 60 L == 3 sec, 20 ticks == 1 sec
    }

    @EventHandler
    public void onCraft(CraftItemEvent e) {
        // Disable Golden OP Apples
        ItemStack itemStack = e.getRecipe().getResult();
        ItemStack enchantedGoldenApple = new ItemStack(Material.GOLDEN_APPLE, 1, (short) 1);

        if (itemStack.equals(enchantedGoldenApple)) {
            e.getInventory().setResult(new ItemStack(Material.AIR));
            for (HumanEntity he : e.getViewers()) {
                if (he instanceof Player) {
                    he.sendMessage(Main.prefix + "§6OP Apples§7 sind §cverboten§7!");
                }
            }
        }
    }

    @EventHandler
    public void onBrew(BrewEvent e) {
        // Disable Strength Potions
        if (e.getContents().contains(Material.BLAZE_POWDER)
                || e.getContents().contains(Material.FERMENTED_SPIDER_EYE))
            e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        // SQL
        Player p = e.getPlayer();
        WaloDatabase.createPlayer(p);

        // Scoreboard
        ScoreboardHandler.updatePlayerScoreboard(p);

        if (p.hasMetadata("no-scoreboard") && p.getScoreboard() != null)
            p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

        // Change name
        String customTag = WaloConfig.getPlayerTeamTag(p);
        p.setDisplayName(customTag);

        // Set the player tab name to display name
        p.setPlayerListName(e.getPlayer().getDisplayName());

        // Set the player to the right gamemode
        if (!GlobalVariables.started)
            p.setGameMode(GameMode.ADVENTURE);
        else if (p.getGameMode().equals(GameMode.ADVENTURE))
            p.setGameMode(GameMode.SURVIVAL);

        // Death message
        if (p.isBanned())
            p.kickPlayer("Du bist tot oder hast combat-logging betrieben.");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDisconnect(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        EntityDamageEvent damageCause = p.getLastDamageCause();

        // Combat logging protection
        if (p.getHealth() >= 10.0f || p.getHealth() == 0.0f || !GlobalVariables.started)
            return;

        // Drop Inventory
        for (ItemStack itemStack : p.getInventory()) {
            if (itemStack != null && !itemStack.getType().equals(Material.AIR))
                p.getWorld().dropItemNaturally(p.getLocation(), itemStack);
        }
        p.getInventory().clear();

        // Drop Armor Contents
        ItemStack[] armorContents = p.getInventory().getArmorContents();
        for (ItemStack itemStack : armorContents) {
            if (itemStack != null && !itemStack.getType().equals(Material.AIR))
                p.getWorld().dropItemNaturally(p.getLocation(), itemStack);
        }
        p.getInventory().setArmorContents(null);

        // Count the kill towards the player that last damaged them
        if (damageCause instanceof EntityDamageByEntityEvent) {
            // Ban the player if he dies and the game has started
            // Change the message a bit
            String deathMessage;
            Entity killer = ((EntityDamageByEntityEvent) p.getLastDamageCause()).getDamager();

            if (!(killer instanceof Player)) {
                // Ban Player
                Bukkit.getBanList(BanList.Type.NAME).addBan(e.getPlayer().getName(), Main.prefix + "Du hast gelefted wie du wenige leben hattest", null, "Tot");
                p.kickPlayer(Main.prefix + "§cDu bist gestorben.");

                deathMessage = Main.prefix + "§6" + e.getPlayer().getName() + " §7hat geleftet wie dieser Spieler wenige leben hatte! §cAusgeschieden§7!";
                e.setQuitMessage(deathMessage);

                return;
            }

            deathMessage = Main.prefix + "§6" + p.getName() + "§4 hatte zu viel Angst vor §6" + killer.getName() + ".";
            e.setQuitMessage(deathMessage);

            // Ban the player
            Bukkit.getBanList(BanList.Type.NAME).addBan(p.getName(), Main.prefix + "Du bist gestorben", null, "Tot");
            p.kickPlayer(Main.prefix + "§cDu bist gestorben.");

            // Give the killer a kill in stats
            if (GlobalVariables.statsDisabled) {
                killer.sendMessage(Main.prefix + "Du hast §4keinen Kill§7 in den Stats bekommen!");
            } else {
                // Add kill to database
                WaloDatabase.addPlayerKill((Player) killer);
            }

        } else {
            // Ban Player
            Bukkit.getBanList(BanList.Type.NAME).addBan(e.getPlayer().getName(), Main.prefix + "Du hast gelefted wie du wenige leben hattest", null, "Tot");
            p.kickPlayer(Main.prefix + "§cDu bist gestorben.");
            Bukkit.broadcastMessage(Main.prefix + "§6" + e.getPlayer().getName() + " §7hat geleftet wie dieser Spieler wenige leben hatte! §cAusgeschieden§7!");
        }

    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        // Cancel damage before game has started
        if (!GlobalVariables.started && e.getEntity() instanceof Player) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDamageByEntity(EntityDamageByEntityEvent e) {
        // This only applies to players
        if (!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player)) {
            return;
        }

        if (!GlobalVariables.started) {
            e.getDamager().sendMessage(Main.prefix + "Das Spiel hat noch nicht gestartet!");
            e.setCancelled(true);

            return;
        }

        if (GlobalVariables.protection) {
            e.getDamager().sendMessage(Main.prefix + "Die Schutzzeit ist noch an!");
            e.setCancelled(true);

            return;
        }

        // Stop team mates from damaging each other
        WaloConfig.reload();

        Player victim = (Player) e.getEntity();
        Player damager = (Player) e.getDamager();

        List<String> victimMates;
        List<String> damagerMates;

        victimMates = WaloConfig.getPlayerMates(victim);
        damagerMates = WaloConfig.getPlayerMates(damager);

        int length = Math.max(victimMates.size(), damagerMates.size());

        // Check if he is team mate
        for (int i = 0; i < length; i++) {
            if (victimMates.get(i).equals(damager.getUniqueId().toString()) || damagerMates.get(i).equals(victim.getUniqueId().toString()))
                e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDie(PlayerDeathEvent e) {
        if (!GlobalVariables.started)
            return;

        // Ban the player if he dies and the game has started
        Player p = e.getEntity();
        String deathMessage;
        Player killer = p.getKiller();

        // Player death
        if (killer != null) {
            deathMessage = Main.prefix + "§6" + p.getName() + "§4 wurde von §6" + killer.getName() + "§4 getötet.";

            // Ban the player
            Bukkit.getBanList(BanList.Type.NAME).addBan(p.getName(), Main.prefix + "Du bist gestorben", null, "Tot");
            p.kickPlayer(Main.prefix + "§cDu bist gestorben.");

            // Give the killer a kill in stats
            if (GlobalVariables.statsDisabled) {
                killer.sendMessage(Main.prefix + "Du hast §4keinen Kill§7 in den Stats bekommen!");
            } else {
                /*  Old way of saving kills, without a database

                    String path = "stats.kills." + e.getEntity().getKiller().getUniqueId().toString();
                    Main.getInstance().getConfig().set(path, Main.getInstance().getConfig().getInt(path) + 1);

                */

                // Add kill to database
                WaloDatabase.addPlayerKill(killer);
            }
        } else {
            // Normal death
            deathMessage = Main.prefix + "§6" + p.getName() + "§4 ist gestorben.";

            // Ban the player
            Bukkit.getBanList(BanList.Type.NAME).addBan(p.getName(), Main.prefix + "Du bist gestorben", null, "Tot");
            p.kickPlayer(Main.prefix + "§cDu bist gestorben.");
        }

        // Change death message
        e.setDeathMessage(deathMessage);
    }

    @EventHandler
    public void onPlayerBlockBreak(BlockBreakEvent e) {
        if (e.getPlayer().getWorld().getName().contains("nether") && e.getBlock().getType() == Material.OBSIDIAN) {
            e.getPlayer().sendMessage(Main.prefix + "Du kannst kein Obsidian im Nether abbauen!");
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        // Better chat formatting
        String message = e.getMessage();
        e.setFormat(e.getPlayer().getDisplayName() + "§7: " + message);
        e.setMessage("");
    }
}

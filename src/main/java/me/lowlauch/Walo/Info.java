package me.lowlauch.Walo;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

public class Info
{
    public static void runInfo()
    {
        final FileConfiguration config = Main.getInstance().getConfig();

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if(!GlobalVariables.started)
                {
                    double val = Main.getInstance().getConfig().getDouble("worldborder.shrinkdelay")/20/60;

                    // Broadcast some info
                    Bukkit.getServer().broadcastMessage(
                        Main.prefix + "-------------------------------\n" +
                        Main.prefix + "Info: \n" +
                        Main.prefix + "§6Bordergröße in jede Richtung: §7" + config.getInt("worldborder.size")/2 + "\n" +
                        Main.prefix + "§6Border §7wird auf " + config.getInt("worldborder.shrinksize")/2 + " in jede Richtung nach " + val +  " Minuten gesetzt.\n" +
                        Main.prefix + "§6Schutzzeit: §710 Minuten\n" +
                        Main.prefix + "§6Walo Discord: §7§lhttps://discord.gg/amJbWnUq9f\n" +
                        Main.prefix + "-------------------------------"
                    );
                }
            }
        }.runTaskTimer(Main.getInstance(), 0L, 2000L);
    }
}

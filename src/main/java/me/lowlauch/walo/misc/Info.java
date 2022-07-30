package me.lowlauch.walo.misc;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.WaloConfig;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class Info {
    public static void runInfo() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!GlobalVariables.started) {
                    // Broadcast some info
                    Bukkit.getServer().broadcastMessage(
                            Main.prefix + "-------------------------------\n" +
                                    Main.prefix + "Info: \n" +
                                    Main.prefix + "§6Bordergröße in jede Richtung: §7" + WaloConfig.getWorldBorderSize() / 2 + "\n" +
                                    Main.prefix + "§6Border §7wird auf " + WaloConfig.getWorldBorderShrinkSize() / 2 + " in jede Richtung nach " + WaloConfig.getWorldBorderShinkDelay() / 60 / 20 + " Minuten gesetzt.\n" +
                                    Main.prefix + "§6Schutzzeit: §710 Minuten\n" +
                                    Main.prefix + "§6Walo Discord: §7§lhttps://discord.gg/amJbWnUq9f\n" +
                                    Main.prefix + "-------------------------------"
                    );
                }
            }
        }.runTaskTimer(Main.getInstance(), 0L, 2000L);
    }
}

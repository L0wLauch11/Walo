package me.lowlauch.walo.tasks;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.WaloConfig;
import me.lowlauch.walo.misc.GlobalVariables;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class InfoTask implements Runnable {
    @Override
    public void run() {
        if (GlobalVariables.started) {
            return;
        }

        Bukkit.getLogger().info("InfoTask ran");
        String borderSize = String.valueOf(WaloConfig.getWorldBorderSize() / 2);

        // Broadcast some info
        Bukkit.getServer().broadcastMessage(
            Main.prefix + "-------------------------------\n" +
                    Main.prefix + "Info: \n" +
                    Main.prefix + "§6Bordergröße: §7-" + borderSize + " +" + borderSize + "\n" +
                    Main.prefix + "§6Border §7wird auf " + WaloConfig.getWorldBorderShrinkSize() / 2 + " in jede Richtung nach " + WaloConfig.getWorldBorderShrinkDelay() / 60 / 20 + " Minuten gesetzt.\n" +
                    Main.prefix + "§6Schutzzeit: §710 Minuten\n" +
                    Main.prefix + "§6Benötigte Spieler: §7" + Bukkit.getServer().getOnlinePlayers().size() + "/" + WaloConfig.getAutostartRequiredPlayers() + "\n" +
                    Main.prefix + "\n" +
                    Main.prefix + "§7Ich entwickle dieses Plugin in meiner Freizeit. Ich würde mich über Spenden freuen :)\n" +
                    Main.prefix + "§6§nhttps://ko-fi.com/lowlauch\n" +
                    Main.prefix + "-------------------------------"
        );
    }
}

package me.lowlauch.walo.misc;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

public class GlobalVariables {
    public static boolean started = false;
    public static boolean protection = false;
    public static boolean statsDisabled = false;

    public static int timer = -1;
    public static int seconds;
    public static String borderTime;
    public static boolean damageIndicatorDisabler = (Bukkit.getServer().getPluginManager().getPlugin("callable_di_disabler") != null);

    public static Location netherPortalLocation = new Location(Bukkit.getWorld("world_nether"), 0, 0, 0);
}

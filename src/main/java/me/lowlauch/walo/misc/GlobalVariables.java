package me.lowlauch.walo.misc;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.*;

public class GlobalVariables {
    public static boolean started = false;
    public static boolean protection = false;
    public static boolean statsDisabled = false;
    public static boolean autostartInitiated = false;
    public static boolean damageIndicatorDisabler = (Bukkit.getServer().getPluginManager().getPlugin("callable_di_disabler") != null);

    public static int timer = -1;
    public static int seconds;
    public static String borderTime;

    public static Location netherPortalLocation = new Location(Bukkit.getWorld("world_nether"), 0, 0, 0);
    public static ArrayList<String> startPlayersUUID = new ArrayList<>();
    public static HashMap<UUID, Long> playerLeaveTimestamps = new HashMap<>();
}

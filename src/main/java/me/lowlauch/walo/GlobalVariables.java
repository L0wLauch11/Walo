package me.lowlauch.walo;

import org.bukkit.command.CommandSender;

import java.time.LocalTime;

public class GlobalVariables
{
    public static boolean started = false;
    public static boolean protection = false;
    public static boolean statsDisabled = false;

    public static int timer = -1;
    public static CommandSender comSender;
    public static int seconds;

    private static final long borderTimeInSeconds = (long) Main.getInstance().getConfig().getDouble("worldborder.shrinkdelay")/20;
    public static String borderTime = LocalTime.now().plusSeconds(borderTimeInSeconds).toString();
}

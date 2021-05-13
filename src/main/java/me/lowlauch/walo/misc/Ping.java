package me.lowlauch.walo.misc;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Ping
{
    static String serverName  = Bukkit.getServer().getClass().getPackage().getName(),
            serverVersion = serverName.substring(serverName.lastIndexOf(".") + 1);

    public static int getPing(Player p)
    {
        try
        {
            Class<?> CPClass = Class.forName("org.bukkit.craftbukkit." + serverVersion + ".entity.CraftPlayer");
            Object CraftPlayer = CPClass.cast(p);

            Method getHandle = CraftPlayer.getClass().getMethod("getHandle");
            Object EntityPlayer = getHandle.invoke(CraftPlayer);

            Field ping = EntityPlayer.getClass().getDeclaredField("ping");

            return ping.getInt(EntityPlayer);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return 0;
    }
}

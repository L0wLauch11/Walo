package me.lowlauch.walo.EventListening;

import me.lowlauch.walo.EventListening.Events.*;
import me.lowlauch.walo.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class EventListenersManager {
    public static void registerEvents() {
        ArrayList<Listener> events = new ArrayList<>();

        events.add(new OnBrew());
        events.add(new OnCraft());
        events.add(new OnInventoryClick());
        events.add(new OnPlayerBlockBreak());
        events.add(new OnPlayerChat());
        events.add(new OnPlayerDamage());
        events.add(new OnPlayerDamageByEntity());
        events.add(new OnPlayerDie());
        events.add(new OnPlayerDisconnect());
        events.add(new OnPlayerDrop());
        events.add(new OnPlayerJoin());
        events.add(new OnPlayerTravel());

        for (Listener event : events) {
            Bukkit.getServer().getPluginManager().registerEvents(event, Main.getInstance());
        }

    }
}

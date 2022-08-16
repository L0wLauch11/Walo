package me.lowlauch.walo.eventlistening;

import me.lowlauch.walo.eventlistening.events.*;
import me.lowlauch.walo.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.ArrayList;

public class EventListenersManager {
    public static void registerEvents() {
        ArrayList<Listener> events = new ArrayList<>();

        events.add(new OnBrew());
        events.add(new OnCraft());
        events.add(new OnPlayerInteract());
        events.add(new OnPlayerBlockBreak());
        events.add(new OnPlayerChat());
        events.add(new OnPlayerDamage());
        events.add(new OnPlayerDamageByEntity());
        events.add(new OnPlayerDie());
        events.add(new OnPlayerDisconnect());
        events.add(new OnPlayerDrop());
        events.add(new OnPlayerJoin());
        events.add(new OnPlayerTravel());
        events.add(new OnInventoryClick());

        for (Listener event : events) {
            Bukkit.getServer().getPluginManager().registerEvents(event, Main.getInstance());
        }

    }
}

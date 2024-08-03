package me.lowlauch.walo.tasks;

import me.lowlauch.walo.commands.subcommands.SetupBorderCommand;
import org.bukkit.Bukkit;

public class ServerStartTask implements Runnable {

    @Override
    public void run() {
        new SetupBorderCommand().execute(Bukkit.getConsoleSender(), new String[]{});
    }
}

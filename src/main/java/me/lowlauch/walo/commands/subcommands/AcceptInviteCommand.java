package me.lowlauch.walo.commands.subcommands;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.teams.Teams;
import me.lowlauch.walo.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AcceptInviteCommand implements SubCommand {
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        Player inviter = Bukkit.getPlayer(args[1]);

        if (!Bukkit.getOnlinePlayers().contains(inviter)) {
            commandSender.sendMessage(Main.prefix + args[1] + " ist nicht online.");
            return;
        }

        Player p = (Player) commandSender;
        String teamID = Teams.getTeamOfPlayer(inviter);
        Teams.joinTeam(teamID, p.getUniqueId().toString());

        p.sendMessage(Main.prefix + "Du bist dem Team beigetreten!");
        inviter.sendMessage(Main.prefix + ChatColor.GOLD + p.getName() + ChatColor.GRAY + " ist deinem Team beigetreten!");
    }

    @Override
    public String getName() {
        return "acceptinvite";
    }

    @Override
    public boolean requiresOp() {
        return false;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public int requiredArguments() {
        return 1;
    }
}

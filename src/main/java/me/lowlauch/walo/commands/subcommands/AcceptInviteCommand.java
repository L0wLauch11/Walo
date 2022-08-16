package me.lowlauch.walo.commands.subcommands;

import me.lowlauch.walo.Teams.Teams;
import me.lowlauch.walo.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AcceptInviteCommand implements SubCommand {
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        Player inviter = Bukkit.getPlayer(args[1]);
        Player p = (Player) commandSender;
        String teamID = Teams.getTeamOfPlayer(inviter);
        Teams.joinTeam(teamID, p.getUniqueId().toString());
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

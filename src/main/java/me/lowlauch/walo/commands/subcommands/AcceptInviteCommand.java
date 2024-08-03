package me.lowlauch.walo.commands.subcommands;

import me.lowlauch.walo.Main;
import me.lowlauch.walo.WaloConfig;
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
        Player p = (Player) commandSender;

        if (!p.hasMetadata("invited-by-" + inviter.getName())) {
            commandSender.sendMessage(Main.prefix + "§cDu wurdest von " + inviter.getName() + " nicht eingeladen!");
            return;
        }

        if (!Bukkit.getOnlinePlayers().contains(inviter)) {
            commandSender.sendMessage(Main.prefix + args[1] + " ist nicht online.");
            return;
        }

        if (WaloConfig.getTeamMembers(inviter.getUniqueId().toString()).size() >= WaloConfig.getMaxTeamSize()) {
            commandSender.sendMessage(Main.prefix + "§cDieses Team ist bereits voll!");
            return;
        }

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
    public String getHelp() {
        return "<Spieler>: §7nimm eine Einladung zu einem Team an.";
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

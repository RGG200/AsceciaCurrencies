package me.rgn.asceciacurrencies.api.versions;

import org.bukkit.command.CommandSender;

public interface Team {
    public boolean addTeamMember(CommandSender s, String playername);
    public boolean inviteMember(CommandSender s, String playername);
    public boolean setTeamMemberPermission(CommandSender s, String playername, String Permission, Boolean allowordeny);
    public boolean teamList(CommandSender s, String name);
    public boolean kickTeamMember(CommandSender s, String playername);
    public boolean leaveTeam(CommandSender s);
    public boolean getTeamMemberPermissions(CommandSender s, String playername);

}

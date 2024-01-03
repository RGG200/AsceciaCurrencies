package me.rgn.asceciacurrencies.api.versions;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface Team {
    public boolean addTeamMember(Player p, String playerid);
    public boolean inviteMember(Player p, String playerid);
    public boolean setTeamMemberPermission(Player p, String playerid, String Permission, Boolean allowordeny);
    public boolean teamList(Player p, String name);
    public boolean kickTeamMember(Player p, String playerid);
    public boolean leaveTeam(Player p);
    public boolean getTeamMemberPermissions(Player p, String playerid);

}

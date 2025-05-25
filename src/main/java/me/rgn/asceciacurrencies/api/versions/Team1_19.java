package me.rgn.asceciacurrencies.api.versions;

import me.rgn.asceciacurrencies.api.CurrenciesAPI;
import me.rgn.asceciacurrencies.files.CurrenciesConfig;
import me.rgn.asceciacurrencies.files.LanguageConfig;
import me.rgn.asceciacurrencies.files.PlayersConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Team1_19 implements Team {
    public boolean addTeamMember(Player p, String playerid){
        for(String currencies: CurrenciesConfig.get().getKeys(false)){
            if(PlayersConfig.get().get(playerid + ".team") == null && !p.getUniqueId().toString().equals(CurrenciesConfig.get().getString(PlayersConfig.get().get(playerid + ".team") + ".author"))){
                if(PlayersConfig.get().getString(playerid + ".invite") == null){
                    p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-16_4"));
                }
                else if(PlayersConfig.get().getString(playerid + ".invite").equals(currencies)){
                    CurrenciesConfig.get().set(currencies + ".team." + playerid + ".rename", false);
                    CurrenciesConfig.get().set(currencies + ".team." + playerid + ".mint", true);
                    CurrenciesConfig.get().set(currencies + ".team." + playerid + ".deposit", true);
                    CurrenciesConfig.get().set(currencies + ".team." + playerid + ".description", false);
                    PlayersConfig.get().set(playerid + ".invite", null);
                    PlayersConfig.get().set(playerid + ".team", currencies);
                    Bukkit.getServer().broadcastMessage(ChatColor.GREEN + CurrenciesAPI.playersConfig.get().getString(playerid + ".name") + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-16_8") + " " + currencies);
                }else{
                    p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-16_3"));
                }
            }else{
                p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-16_3"));
            }
        }
        PlayersConfig.save();
        PlayersConfig.reload();
        CurrenciesConfig.save();
        CurrenciesConfig.reload();
        return true;
    }
    public boolean inviteMember(Player p, String playername){
        String playerid = Bukkit.getOfflinePlayer(UUID.fromString(playername)).getUniqueId().toString();
        if(PlayersConfig.get().getString(playerid + ".team") == null){
            for(String currencies: CurrenciesConfig.get().getKeys(false)){
                if(CurrenciesConfig.get().getString(currencies + ".author").equals(p.getUniqueId().toString())){
                    if(PlayersConfig.get().getString(playerid + ".invite") != null){
                        if(!PlayersConfig.get().getString(playerid + ".invite").equals(currencies)){
                            PlayersConfig.get().set(playerid + ".invite", currencies);
                            Bukkit.getServer().broadcastMessage(ChatColor.GREEN + CurrenciesAPI.playersConfig.get().getString(playerid + ".name") + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-16_1")  + currencies);
                        }else{
                            p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-16_6"));
                        }
                    }else{
                        PlayersConfig.get().set(playerid + ".invite", currencies);
                    }
                }
            }
        }else{
            p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-16_5"));
        }

        PlayersConfig.save();
        PlayersConfig.reload();
        CurrenciesConfig.save();
        CurrenciesConfig.reload();

        return true;
    }

    public boolean setTeamMemberPermission(Player p, String playerid, String Permission, Boolean allowordeny){
        if(PlayersConfig.get().get(playerid + ".team") != null){
            for(String currencies: CurrenciesConfig.get().getKeys(false)){
                if (CurrenciesConfig.get().getString(currencies + ".author").equals(p.getUniqueId().toString().toString())){
                    if(CurrenciesConfig.get().contains(currencies + ".team." + playerid + "." + Permission)){
                        CurrenciesConfig.get().set(currencies + ".team." + playerid + "." + Permission, allowordeny);
                        p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-15"));
                    }else{
                        p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-16_1"));
                    }
                }else{
                    p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-16_2"));
                }
            }
            CurrenciesConfig.save();
            CurrenciesConfig.reload();
        }
        return true;
    }
    public boolean teamList(Player p, String name){
        if(CurrenciesConfig.get().contains(name)){
            p.sendMessage(ChatColor.AQUA + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-16"));
            for (String player: CurrenciesConfig.get().getConfigurationSection(name + ".team").getKeys(false)){
                p.sendMessage("     " + ChatColor.GREEN + "- " + Bukkit.getServer().getOfflinePlayer(UUID.fromString(player)).getName());
                PlayersConfig.get().set(player + ".team", name);
            }
        }else{
            p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-4_1"));
        }
        PlayersConfig.save();
        PlayersConfig.reload();
        return true;
    }
    public boolean kickTeamMember(Player p, String playerid){
        final List<?> team = new ArrayList<>();
        if(PlayersConfig.get().get(playerid + ".team") != null && !p.getUniqueId().toString().equals(CurrenciesConfig.get().getString(PlayersConfig.get().getString(p.getUniqueId().toString() + ".team") + ".author"))){
            if (CurrenciesConfig.get().getString(PlayersConfig.get().getString(playerid + ".team") + ".author").equals(p.getUniqueId().toString().toString())){
                CurrenciesConfig.get().set(PlayersConfig.get().getString(playerid + ".team") + ".team." + playerid, null);
                PlayersConfig.get().set(playerid + ".team", null);
                p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-16_2"));
            }
        }else{
            p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-16_2"));
        }
        PlayersConfig.save();
        PlayersConfig.reload();
        CurrenciesConfig.save();
        CurrenciesConfig.reload();
        return true;
    }
    public boolean leaveTeam(Player p){
        if(PlayersConfig.get().get(p.getUniqueId().toString() + ".team") != null && !p.getUniqueId().toString().equals(CurrenciesConfig.get().getString(PlayersConfig.get().getString(p.getUniqueId().toString() + ".team") + ".author"))) {
            CurrenciesConfig.get().set(PlayersConfig.get().getString(p.getUniqueId().toString() + ".team") + ".team." + p.getUniqueId().toString(), null);
            PlayersConfig.get().set(p.getUniqueId().toString() + ".team", null);
            p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-16_9"));
        }else{
            p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-16_7"));
        }
        PlayersConfig.save();
        PlayersConfig.reload();
        CurrenciesConfig.save();
        CurrenciesConfig.reload();
        return true;
    }
    public boolean getTeamMemberPermissions(Player p, String playerid){
        final List<?> team = new ArrayList<>();
        if(PlayersConfig.get().getString(playerid + ".team") != null) {
            if (PlayersConfig.get().getString(playerid + ".team").equals(CurrenciesAPI.playersConfig.get().getString(p.getUniqueId() + ".team"))) {
                String currencies = PlayersConfig.get().getString(playerid + ".team");
                p.sendMessage(ChatColor.AQUA + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-16_3") + CurrenciesAPI.playersConfig.get().getString(playerid + ".name") + "\n " + ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-16_4") + CurrenciesConfig.get().getBoolean(currencies + ".team." + playerid + ".mint") + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-16_5") + CurrenciesConfig.get().getBoolean(currencies + ".team." + playerid + ".deposit") + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-16_6") + CurrenciesConfig.get().getBoolean(currencies + ".team." + playerid + ".rename") + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-16_7") + CurrenciesConfig.get().getBoolean(currencies + ".team." + playerid + ".description"));
            }else{
                p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-16_2"));
            }
        }else{
            p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-16_2"));
        }
        return true;
    }
}
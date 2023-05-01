package me.rgn.asceciacurrencies.api;

import me.rgn.asceciacurrencies.files.CurrenciesConfig;
import me.rgn.asceciacurrencies.files.LanguageConfig;
import me.rgn.asceciacurrencies.files.PlayersConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class Team {
    public static boolean addTeamMember(CommandSender s, String playername){
        for(String currencies: CurrenciesConfig.get().getKeys(false)){
            if(PlayersConfig.get().get(playername + ".team") == null && s.getName().equals(CurrenciesConfig.get().getString(PlayersConfig.get().getString(s.getName() + ".team") + ".author"))){
                if(PlayersConfig.get().getString(playername + ".invite") == null){
                    s.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-16_4"));
                }
                else if(PlayersConfig.get().getString(playername + ".invite").equals(currencies)){
                    CurrenciesConfig.get().set(currencies + ".team." + playername + ".rename", false);
                    CurrenciesConfig.get().set(currencies + ".team." + playername + ".mint", true);
                    CurrenciesConfig.get().set(currencies + ".team." + playername + ".deposit", true);
                    CurrenciesConfig.get().set(currencies + ".team." + playername + ".description", false);
                    PlayersConfig.get().set(playername + ".invite", null);
                    PlayersConfig.get().set(playername + ".team", currencies);
                    Bukkit.getServer().broadcastMessage(ChatColor.GREEN + playername + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-16_8") + " " + currencies);
                }else{
                    s.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-16_3"));
                }
            }else{
                s.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-16_3"));
            }
        }
        PlayersConfig.save();
        PlayersConfig.reload();
        CurrenciesConfig.save();
        CurrenciesConfig.reload();
        return true;
    }
    public static boolean inviteMember(CommandSender s, String playername){
        if(PlayersConfig.get().getString(playername + ".team") == null){
            for(String currencies: CurrenciesConfig.get().getKeys(false)){
                if(CurrenciesConfig.get().getString(currencies + ".author").equals(s.getName())){
                    if(PlayersConfig.get().getString(playername + ".invite") != null){
                        if(!PlayersConfig.get().getString(playername + ".invite").equals(currencies)){
                            PlayersConfig.get().set(playername + ".invite", currencies);
                        }else{
                            s.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-16_6"));
                        }
                    }
                    Bukkit.getServer().broadcastMessage(ChatColor.GREEN + playername + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-16_1")  + currencies);
                }
            }
        }else{
            s.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-16_5"));
        }

        PlayersConfig.save();
        PlayersConfig.reload();
        CurrenciesConfig.save();
        CurrenciesConfig.reload();

        return true;
    }

    public static boolean setTeamMemberPermission(CommandSender s, String playername, String Permission, Boolean allowordeny){
        if(PlayersConfig.get().get(playername + ".team") != null){
            for(String currencies: CurrenciesConfig.get().getKeys(false)){
                if (CurrenciesConfig.get().getString(currencies + ".author").equals(s.getName().toString())){
                    if(CurrenciesConfig.get().contains(currencies + ".team." + playername + "." + Permission)){
                        CurrenciesConfig.get().set(currencies + ".team." + playername + "." + Permission, allowordeny);
                        s.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-15"));
                    }else{
                        s.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-16_1"));
                    }
                }else{
                    s.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-16_2"));
                }
            }
            CurrenciesConfig.save();
            CurrenciesConfig.reload();
        }
        return true;
    }
    public static boolean teamList(CommandSender s, String name){
        if(CurrenciesConfig.get().contains(name)){
            s.sendMessage(ChatColor.AQUA + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-16"));
            for (String player: PlayersConfig.get().getKeys(false)){
                if (CurrenciesConfig.get().contains(name + ".team." + player)){
                    s.sendMessage("     " + ChatColor.GREEN + "- " + player );
                    PlayersConfig.get().set(player + ".team", name);
                }
            }
        }else{
            s.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-4_1"));
        }
        PlayersConfig.save();
        PlayersConfig.reload();
        return true;
    }
    public static boolean kickTeamMember(CommandSender s, String playername){
        final List<?> team = new ArrayList<>();
        if(PlayersConfig.get().get(playername + ".team") != null && !s.getName().equals(CurrenciesConfig.get().getString(PlayersConfig.get().getString(s.getName() + ".team") + ".author"))){
            if (CurrenciesConfig.get().getString(PlayersConfig.get().getString(playername + ".team") + ".author").equals(s.getName().toString())){
                CurrenciesConfig.get().set(PlayersConfig.get().getString(playername + ".team") + ".team." + playername, null);
                PlayersConfig.get().set(playername + ".team", null);
                s.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-16_2"));
            }
        }else{
            s.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-16_2"));
        }
        PlayersConfig.save();
        PlayersConfig.reload();
        CurrenciesConfig.save();
        CurrenciesConfig.reload();
        return true;
    }
    public static boolean leaveTeam(CommandSender s){
        if(PlayersConfig.get().get(s.getName() + ".team") != null && !s.getName().equals(CurrenciesConfig.get().getString(PlayersConfig.get().getString(s.getName() + ".team") + ".author"))) {
            CurrenciesConfig.get().set(PlayersConfig.get().getString(s.getName() + ".team") + ".team." + s.getName(), null);
            PlayersConfig.get().set(s.getName() + ".team", null);
            s.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-16_9"));
        }else{
            s.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-16_7"));
        }
        PlayersConfig.save();
        PlayersConfig.reload();
        CurrenciesConfig.save();
        CurrenciesConfig.reload();
        return true;
    }
    public static boolean getTeamMemberPermissions(CommandSender s, String playername){
        final List<?> team = new ArrayList<>();
        if(PlayersConfig.get().get(playername + ".team") != null){
            for(String currencies: CurrenciesConfig.get().getKeys(false)){
                if (CurrenciesConfig.get().get(currencies + ".team." + playername) != null){
                    s.sendMessage(ChatColor.AQUA + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-16_3") + playername + "\n " + ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-16_4") + CurrenciesConfig.get().getString(currencies + ".team." + playername + ".mint") + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-16_5") + CurrenciesConfig.get().getString(currencies + ".team." + playername + ".deposit") + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-16_6") + CurrenciesConfig.get().getString(currencies + ".team." + playername + ".rename") + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-16_7") + CurrenciesConfig.get().getString(currencies + ".team." + playername + ".description"));
                }
            }
        }else{
            s.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-16_2"));
        }
        return true;
    }
}

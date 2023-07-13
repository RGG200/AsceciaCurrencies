package me.rgn.asceciacurrencies.Listeners;

import me.rgn.asceciacurrencies.api.CurrenciesAPI;
import me.rgn.asceciacurrencies.files.PlayersConfig;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        if(CurrenciesAPI.playersConfig.get().getString(e.getPlayer().getName() + ".invite") == null && CurrenciesAPI.playersConfig.get().getString(e.getPlayer().getName() + ".team") == null){
            e.getPlayer().sendMessage(ChatColor.GOLD + CurrenciesAPI.languageConfig.get().getString(CurrenciesAPI.languageConfig.get().getString("language") + ".info-0"));
        }else if(CurrenciesAPI.playersConfig.get().getString(e.getPlayer().getName() + ".invite") != null){
            e.getPlayer().sendMessage(ChatColor.GOLD + CurrenciesAPI.languageConfig.get().getString(CurrenciesAPI.languageConfig.get().getString("language") + ".info-0_1") + CurrenciesAPI.playersConfig.get().getString(e.getPlayer().getName() + ".invite"));
        }
        if(!PlayersConfig.get().contains(e.getPlayer().getUniqueId() + ".name")){
            PlayersConfig.get().set(e.getPlayer().getUniqueId() + ".name", e.getPlayer().getName());
        }
    }
}

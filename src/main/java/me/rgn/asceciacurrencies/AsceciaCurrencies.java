package me.rgn.asceciacurrencies;

import me.rgn.asceciacurrencies.files.CurrenciesConfig;
import me.rgn.asceciacurrencies.files.LanguageConfig;
import me.rgn.asceciacurrencies.messages.Messages;
import me.rgn.asceciacurrencies.commands.*;
import me.rgn.asceciacurrencies.files.PlayersConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class AsceciaCurrencies extends JavaPlugin {
    @Override
    public void onEnable() {
        //init configs
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        CurrenciesConfig.setup();
        CurrenciesConfig.get().options().copyDefaults(true);
        LanguageConfig.setup();
        LanguageConfig.get().options().copyDefaults(true);
        CurrenciesConfig.save();
        PlayersConfig.setup();
        PlayersConfig.get().options().copyDefaults(true);
        PlayersConfig.save();
        Messages.mlist();
        //getting command
        getCommand("Currencies").setExecutor(new Currencies());
        //printing success
        System.out.println("[Ascecia-Currencies]: Plugin Loaded !");
    }
    public void onLoad(){
        Bukkit.getServer().broadcastMessage(ChatColor.GOLD + "[ Ascecia-Currencies ]: Version 1.0-Snapshot \n Thanks for using Ascecia-Currencies !!!");
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                for (String currencies : CurrenciesConfig.get().getKeys(false)) {
                    double cEcoAct = CurrenciesConfig.get().getDouble(currencies + "economic-activity");
                    CurrenciesConfig.get().set(currencies + ".economic-activity", cEcoAct - (1 / 2563842));
                }
                }
        },0L, 20L);
    }

}
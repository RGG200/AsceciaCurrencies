package me.rgn.asceciacurrencies;

import me.rgn.asceciacurrencies.files.CurrenciesConfig;
import me.rgn.asceciacurrencies.files.LanguageConfig;
import me.rgn.asceciacurrencies.messages.Messages;
import me.rgn.asceciacurrencies.commands.*;
import me.rgn.asceciacurrencies.files.PlayersConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;


public final class AsceciaCurrencies extends JavaPlugin {
    @Override
    public void onEnable() {
        //init configs
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        CurrenciesConfig.setup();
        CurrenciesConfig.get().options().copyDefaults(true);
        CurrenciesConfig.save();
        LanguageConfig.setup();
        LanguageConfig.get().options().copyDefaults(true);
        PlayersConfig.setup();
        PlayersConfig.get().options().copyDefaults(true);
        PlayersConfig.save();
        Messages.mlist();
        //getting command
        getCommand("Currencies").setExecutor(new Currencies());
        //printing success
        System.out.println("[Ascecia-Currencies]: Plugin Loaded !");
        getServer().broadcastMessage(ChatColor.GOLD + "[ Ascecia-Currencies ]: Version 1.0-RC1 \n Thanks for using Ascecia-Currencies !!!");
        //does stuff
        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                for (String currencies : CurrenciesConfig.get().getKeys(false)){
                    double cMarketAmount = CurrenciesConfig.get().getDouble(currencies + ".amount");
                    double cValue = CurrenciesConfig.get().getDouble(currencies + ".totalvalue");
                    double cEcoAct = CurrenciesConfig.get().getDouble(currencies + ".economic-activity");
                    CurrenciesConfig.get().set(currencies + ".economic-activity", cEcoAct - 0.00000005);
                    CurrenciesConfig.get().set(currencies + ".totalvalue", cValue*cEcoAct);
                    CurrenciesConfig.get().set(currencies + ".power", (cValue/cMarketAmount)*cEcoAct);
                    CurrenciesConfig.save();
                }
            }
        }, 0L, 20L);
    }

}
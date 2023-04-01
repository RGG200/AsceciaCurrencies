package me.rgn.asceciacurrencies;

import me.rgn.asceciacurrencies.files.CurrenciesConfig;
import me.rgn.asceciacurrencies.files.LanguageConfig;
import me.rgn.asceciacurrencies.messages.Messages;
import me.rgn.asceciacurrencies.commands.*;
import me.rgn.asceciacurrencies.files.PlayersConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public final class AsceciaCurrencies extends JavaPlugin implements TabCompleter {

    public static JavaPlugin plugin;

    @Override
    public void onEnable() {
        plugin = this;
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
        getCommand("Currencies").setTabCompleter(new Currencies());
        //printing success
        System.out.println("[Ascecia-Currencies]: Plugin Loaded !");
        //does stuff
        BukkitScheduler economic_evolution = getServer().getScheduler();
        BukkitScheduler economy_check = getServer().getScheduler();
        economy_check.scheduleSyncRepeatingTask(this, new Runnable(){
            @Override
            public void run() {
                for (String currencies : CurrenciesConfig.get().getKeys(false)) {
                    double cMarketAmount = CurrenciesConfig.get().getDouble(currencies + ".amount");
                    double cValue = CurrenciesConfig.get().getDouble(currencies + ".totalvalue");
                    double cEcoAct = CurrenciesConfig.get().getDouble(currencies + ".economic-activity");
                    if (cEcoAct < 0.2) {
                        CurrenciesConfig.get().set(currencies + ".economic-activity", 0.21);
                    }
                    CurrenciesConfig.save();
                }
            }
        },0L, 20L);
        economic_evolution.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                for (String currencies : CurrenciesConfig.get().getKeys(false)) {
                    double cMarketAmount = CurrenciesConfig.get().getDouble(currencies + ".amount");
                    double cValue = CurrenciesConfig.get().getDouble(currencies + ".totalvalue");
                    double cEcoAct = CurrenciesConfig.get().getDouble(currencies + ".economic-activity");
                    CurrenciesConfig.get().set(currencies + ".economic-activity", cEcoAct - 0.01);
                    if (cEcoAct < 0.2) {
                        CurrenciesConfig.get().set(currencies + ".economic-activity", 0.21);
                    }
                    CurrenciesConfig.get().set(currencies + ".totalvalue", cValue * cEcoAct);
                    CurrenciesConfig.get().set(currencies + ".power", Double.valueOf(Math.round((cValue / cMarketAmount) * 1000)) / 1000);
                    CurrenciesConfig.save();
                }
            }
        }, 0L, 576000L);

    }
}

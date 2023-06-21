package me.rgn.asceciacurrencies;

import me.rgn.asceciacurrencies.Listeners.JoinListener;
import me.rgn.asceciacurrencies.api.CurrenciesAPI;
import me.rgn.asceciacurrencies.api.versions.*;
import me.rgn.asceciacurrencies.files.CurrenciesConfig;
import me.rgn.asceciacurrencies.files.LanguageConfig;
import me.rgn.asceciacurrencies.commands.*;
import me.rgn.asceciacurrencies.files.PlayersConfig;
import me.rgn.asceciacurrencies.message.Messages;
import me.rgn.asceciacurrencies.web.AsceciaWebServerController;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.logging.Logger;

public final class AsceciaCurrencies extends JavaPlugin implements TabCompleter {
    public static JavaPlugin plugin;
    @Override
    public void onEnable() {
        //Hook into Vault
        String sVersion = Bukkit.getBukkitVersion().split("-")[0];
        switch (sVersion){
            case "1.20.1":
                CurrenciesAPI.currency = new Currency1_19();
                CurrenciesAPI.team = new Team1_19();
                break;
            case "1.20":
                CurrenciesAPI.currency = new Currency1_19();
                CurrenciesAPI.team = new Team1_19();
                break;
            case "1.19.4":
                CurrenciesAPI.currency = new Currency1_19();
                CurrenciesAPI.team = new Team1_19();
                break;
            case "1.19.3":
                CurrenciesAPI.currency = new Currency1_19();
                CurrenciesAPI.team = new Team1_19();
                break;
            case "1.19.2":
                CurrenciesAPI.currency = new Currency1_19();
                CurrenciesAPI.team = new Team1_19();
                break;
            case "1.19.1":
                CurrenciesAPI.currency = new Currency1_19();
                CurrenciesAPI.team = new Team1_19();
                break;
            case "1.19":
                CurrenciesAPI.currency = new Currency1_19();
                CurrenciesAPI.team = new Team1_19();
                break;
            case "1.18.2":
                CurrenciesAPI.currency = new Currency1_19();
                CurrenciesAPI.team = new Team1_19();
                break;
            case "1.18.1":
                CurrenciesAPI.currency = new Currency1_19();
                CurrenciesAPI.team = new Team1_19();
                break;
            case "1.18":
                CurrenciesAPI.currency = new Currency1_19();
                CurrenciesAPI.team = new Team1_19();
                break;
            default:
                getLogger().severe("[Ascecia-Currencies]: Unsupported version disabling plugin...");
                Bukkit.getPluginManager().disablePlugin(this);
                break;
        }
        plugin = this;
        //init configs
        plugin.getConfig().addDefault("economic-rate", 20);
        Bukkit.getLogger().info("[Ascecia-Currencies]: Registering Events... !");
        getServer().getPluginManager().registerEvents(new JoinListener(), plugin);
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        CurrenciesConfig.setup();
        CurrenciesConfig.get().options().copyDefaults(true);
        CurrenciesConfig.save();
        LanguageConfig.setup();
        Messages.mlist();
        LanguageConfig.get().options().copyDefaults(true);
        PlayersConfig.setup();
        PlayersConfig.get().options().copyDefaults(true);
        PlayersConfig.save();
        CurrenciesAPI.currency.reloadConfig(Bukkit.getConsoleSender());
        //getting command
        getCommand("Currencies").setExecutor(new Currencies());
        getCommand("Currencies").setTabCompleter(new Currencies());
        //printing success
        Bukkit.getLogger().info("[Ascecia-Currencies]: Plugin Loaded !");
        //does stuff
        BukkitScheduler economic_evolution = getServer().getScheduler();
        BukkitScheduler economy_check = getServer().getScheduler();
        BukkitScheduler invites = getServer().getScheduler();
        AsceciaWebServerController socketController = new AsceciaWebServerController(29007);
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, socketController::transferData, 20, 20 * 5);
        economy_check.scheduleSyncRepeatingTask(this, new Runnable(){
            @Override
            public void run() {
                for (String currencies : CurrenciesConfig.get().getKeys(false)) {
                    double cMarketAmount = CurrenciesConfig.get().getDouble(currencies + ".amount");
                    double cValue = CurrenciesConfig.get().getDouble(currencies + ".totalvalue");
                    double cEcoAct = CurrenciesConfig.get().getDouble(currencies + ".economic-activity");
                    CurrenciesConfig.get().set(currencies + ".power", (Double.valueOf(Math.round((cValue / cMarketAmount) * 1000)) / 1000)*cEcoAct);
                    if (cEcoAct <= 0.1) {
                        CurrenciesConfig.get().set(currencies + ".economic-activity", 0.101);
                    }
                    CurrenciesConfig.save();
                }
            }
        },0L, 20L*60);
        economic_evolution.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                for (String currencies : CurrenciesConfig.get().getKeys(false)) {
                    double cMarketAmount = CurrenciesConfig.get().getDouble(currencies + ".amount");
                    double cValue = CurrenciesConfig.get().getDouble(currencies + ".totalvalue");
                    double cEcoAct = CurrenciesConfig.get().getDouble(currencies + ".economic-activity");
                    CurrenciesConfig.get().set(currencies + ".economic-activity", cEcoAct - 0.01);
                    if (cEcoAct <= 0.1) {
                        CurrenciesConfig.get().set(currencies + ".economic-activity", 0.101);
                    }
                    CurrenciesConfig.get().set(currencies + ".totalvalue", cValue);
                    CurrenciesConfig.get().set(currencies + ".power", (Double.valueOf(Math.round((cValue / cMarketAmount) * 1000)) / 1000)*cEcoAct);
                    CurrenciesConfig.save();
                }
            }
        }, 0L, 20L*3600*8*20/plugin.getConfig().getInt("economic_rate"));
    }
}

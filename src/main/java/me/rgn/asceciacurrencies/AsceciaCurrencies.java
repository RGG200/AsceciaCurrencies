package me.rgn.asceciacurrencies;

import me.rgn.asceciacurrencies.files.CurrenciesConfig;
import me.rgn.asceciacurrencies.files.LanguageConfig;
import me.rgn.asceciacurrencies.commands.*;
import me.rgn.asceciacurrencies.files.PlayersConfig;
import me.rgn.asceciacurrencies.message.Messages;
import me.rgn.asceciacurrencies.web.AsceciaWebServerController;
import org.bukkit.Bukkit;
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
        Messages.mlist();
        LanguageConfig.get().options().copyDefaults(true);
        PlayersConfig.setup();
        PlayersConfig.get().options().copyDefaults(true);
        PlayersConfig.save();
        //getting command
        getCommand("Currencies").setExecutor(new Currencies());
        getCommand("Currencies").setTabCompleter(new Currencies());
        //printing success
        System.out.println("[Ascecia-Currencies]: Plugin Loaded !");
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
                    CurrenciesConfig.get().set(currencies + ".totalvalue", cValue);
                    CurrenciesConfig.get().set(currencies + ".power", (Double.valueOf(Math.round((cValue / cMarketAmount) * 1000)) / 1000)*cEcoAct);
                    CurrenciesConfig.save();
                }
            }
        }, 0L, 576000L);
        invites.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                for (String player : PlayersConfig.get().getKeys(false)){
                    PlayersConfig.get().set(player + ".invite", null);
                }
                PlayersConfig.save();
                PlayersConfig.reload();
                Bukkit.getServer().broadcastMessage(ChatColor.AQUA + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".info-0"));
            }
        },0L, 200 * 20);

    }
}

package me.rgn.asceciacurrencies;

import me.lokka30.treasury.api.common.service.ServicePriority;
import me.lokka30.treasury.api.common.service.ServiceRegistry;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.rgn.asceciacurrencies.Listeners.JoinListener;
import me.rgn.asceciacurrencies.api.CurrenciesAPI;
import me.rgn.asceciacurrencies.api.versions.*;
import me.rgn.asceciacurrencies.economy.ACEconomyProvider;
import me.rgn.asceciacurrencies.economy.ACurrency;
import me.rgn.asceciacurrencies.economy.APlayerAccount;
import me.rgn.asceciacurrencies.files.CurrenciesConfig;
import me.rgn.asceciacurrencies.files.LanguageConfig;
import me.rgn.asceciacurrencies.commands.*;
import me.rgn.asceciacurrencies.files.PlayersConfig;
import me.rgn.asceciacurrencies.message.Messages;
import me.rgn.asceciacurrencies.web.ACWebServer;
import me.rgn.asceciacurrencies.web.ACWebServerController;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.logging.Logger;
import static me.rgn.asceciacurrencies.api.versions.Currency1_19.CurrenciesMap;
public final class AsceciaCurrencies extends JavaPlugin implements TabCompleter {

    public static JavaPlugin plugin;

    public HashMap<String, APlayerAccount> PlayersMap = new HashMap<String, APlayerAccount>();

    final EconomyProvider econProvider = new ACEconomyProvider();


    @Override
    public void onEnable() {
        String sVersion = Bukkit.getBukkitVersion().split("-")[0];
        if(getServer().getPluginManager().getPlugin("Treasury") != null){
            ServiceRegistry.INSTANCE.registerService(
                    EconomyProvider.class,
                    econProvider,
                    "AsceciaCurrencies",
                    ServicePriority.NORMAL
            );
            switch (sVersion){
                case "1.20.1", "1.19", "1.19.2", "1.19.1", "1.18.2", "1.18.1", "1.18", "1.19.3", "1.19.4", "1.20":
                    CurrenciesAPI.currency = new Currency1_19();
                    CurrenciesAPI.team = new Team1_19();
                    break;
                default:
                    getLogger().severe("[Ascecia-Currencies]: Unsupported version disabling plugin...");
                    Bukkit.getPluginManager().disablePlugin(this);
                    break;
            }
        }else{
            getLogger().warning("Treasury is not installed disabling treasury support.");
            switch (sVersion){
                case "1.20.1", "1.19", "1.19.2", "1.19.1", "1.18.2", "1.18.1", "1.18", "1.19.3", "1.19.4", "1.20":
                    CurrenciesAPI.currency = new Currency1_19NT();
                    CurrenciesAPI.team = new Team1_19();
                    break;
                default:
                    getLogger().severe("[Ascecia-Currencies]: Unsupported version disabling plugin...");
                    Bukkit.getPluginManager().disablePlugin(this);
                    break;
            }
        }
        plugin = this;
        //init configs
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        Bukkit.getLogger().info("[Ascecia-Currencies]: Registering Events... !");
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
        for(String player: PlayersConfig.get().getKeys(false)){
            OfflinePlayer p = getServer().getOfflinePlayer(player);
            PlayersConfig.get().set(p.getUniqueId().toString(), PlayersConfig.get().get(p.getName()));
            for (String currency: CurrenciesConfig.get().getKeys(false)) {
                if(CurrenciesConfig.get().getString(currency + ".author").equals(p.getName())){
                    CurrenciesConfig.get().set(currency + ".author", p.getUniqueId().toString());
                }
            }
            PlayersConfig.get().set(p.getName(),null);
        }
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
        }, 0L, 20L*3600*8);
        if(getServer().getPluginManager().getPlugin("Treasury") != null){
            treasuryHook();
        }
        ACWebServerController socketController = new ACWebServerController(29007);
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, socketController::transferData, 20, 20 * 15);
    }
    public void treasuryHook(){
        getConfig().set("defaultcurrency" + ".power", 1.0);
        getConfig().set("defaultcurrency" + ".amount", 1.0);
        getConfig().set("defaultcurrency" + ".decimal", 2);
        getConfig().set("defaultcurrency" + ".totalvalue", 1.0);
        getConfig().set("defaultcurrency" + ".economic-activity", 1.0);
        getConfig().set("defaultcurrency" + ".description", "defaultDescription");
        getConfig().set("defaultcurrency" + ".peers", 1);
        getConfig().set("defaultcurrency" + ".author", "default");
        getConfig().set("defaultcurrency" + ".convert-fee", 0);
        CurrenciesMap.put("defaultcurrency", new ACurrency());
        CurrenciesMap.get("defaultcurrency").name = "defaultcurrency";
        CurrenciesMap.get("defaultcurrency").symbol = "defaultcurrency".substring(0, 1);
        CurrenciesMap.get("defaultcurrency").conversionRATE = BigDecimal.valueOf(getConfig().getDouble("defaultcurrency" + ".power") * (1));
        BukkitScheduler treasury_hook = getServer().getScheduler();
        treasury_hook.scheduleSyncRepeatingTask(this, new Runnable(){
            @Override
            public void run() {
                if(CurrenciesConfig.get().getKeys(false).size() > 0){
                    for(String player: PlayersConfig.get().getKeys(false)){
                        if(PlayersMap.containsKey(player)){
                            PlayersMap.get(player).player_id = player;
                        }else if(PlayersMap.containsKey(player) && PlayersConfig.get().contains(player)){
                            PlayersMap.remove(player);
                        }
                        else{
                            PlayersMap.put(player, new APlayerAccount());
                        }
                    }
                    for(String currency: CurrenciesConfig.get().getKeys(false)){
                        if(CurrenciesMap.containsKey(currency)){
                            CurrenciesMap.get(currency).name = currency;
                            CurrenciesMap.get(currency).symbol = currency.substring(0, 0);
                            CurrenciesMap.get(currency).conversionRATE = BigDecimal.valueOf(CurrenciesConfig.get().getDouble(currency + ".power") * (1 + CurrenciesConfig.get().getDouble(currency + ".convert-fee")));
                        }else{
                            CurrenciesMap.put(currency, new ACurrency());
                        }
                    }
                }
            }
        },0L, 20L);
    }
}

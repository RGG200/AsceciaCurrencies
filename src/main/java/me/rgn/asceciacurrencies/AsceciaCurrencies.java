package me.rgn.asceciacurrencies;

import me.rgn.asceciacurrencies.api.CurrenciesAPI;
import me.rgn.asceciacurrencies.commands.*;
import me.rgn.asceciacurrencies.files.CustomConfig;
import me.rgn.asceciacurrencies.files.PlayersConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Currency;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public final class AsceciaCurrencies extends JavaPlugin {
    public static int time = 0;
    @Override
    public void onEnable() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        CustomConfig.setup();
        CustomConfig.get().options().copyDefaults(true);
        CustomConfig.save();
        PlayersConfig.setup();
        PlayersConfig.get().options().copyDefaults(true);
        PlayersConfig.save();
        getCommand("Currencies").setExecutor(new Currencies());
        System.out.println("[Ascecia-Currencies]: Plugin Loaded !");
        Bukkit.getServer().broadcastMessage(ChatColor.GOLD + "[ Ascecia-Currencies ]: Version 1.0-Snapshot \n Thanks for using Ascecia-Currencies !!!");
        int period = 1000;
        int delay = 2100000000;
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask()
    {
            public void run(){
                time ++;
                for (String currencies : CustomConfig.get().getKeys(false)){
                    double cAmount = CustomConfig.get().getDouble(currencies + ".amount");
                    double cValue = CustomConfig.get().getDouble(currencies + ".total-value");
                    double cEcoDev = CustomConfig.get().getDouble(currencies + ".economic-development");
                    double cEcoAct = CustomConfig.get().getDouble(currencies + ".economic-activity");
                    CustomConfig.get().set(currencies + ".economic-activity", cEcoAct-(1/36000));
                    CustomConfig.get().set(currencies + ".economic-development", cEcoDev+(time*(cAmount/cValue))/5184000);
                    CustomConfig.get().set(currencies + ".power", ((cValue/cAmount)*cEcoAct)*cEcoDev);
                    CustomConfig.save();
                }
            }
        },delay, period);
    }
}
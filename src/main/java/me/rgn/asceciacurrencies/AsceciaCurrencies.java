package me.rgn.asceciacurrencies;

import me.rgn.asceciacurrencies.files.LanguageConfig;
import me.rgn.asceciacurrencies.messages.Messages;
import me.rgn.asceciacurrencies.commands.*;
import me.rgn.asceciacurrencies.files.CustomConfig;
import me.rgn.asceciacurrencies.files.PlayersConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class AsceciaCurrencies extends JavaPlugin {
    @Override
    public void onEnable() {
        //init configs
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        CustomConfig.setup();
        CustomConfig.get().options().copyDefaults(true);
        LanguageConfig.setup();
        LanguageConfig.get().options().copyDefaults(true);
        CustomConfig.save();
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
    }

}
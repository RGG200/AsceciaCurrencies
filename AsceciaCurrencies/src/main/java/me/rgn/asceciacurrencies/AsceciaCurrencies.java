package me.rgn.asceciacurrencies;

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

public final class AsceciaCurrencies extends JavaPlugin {
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
    }
}
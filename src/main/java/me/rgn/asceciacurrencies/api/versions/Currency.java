package me.rgn.asceciacurrencies.api.versions;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface Currency {
    public boolean add(Player p, String name, double amount);
    public boolean create(Player p, String name);
    public boolean delete(Player p, String name);
    public boolean deposit(Player p,String currencies, double itemamount);
    public boolean description(Player p, String description);
    public boolean setMintMaterialPrice(String mm, double amount, CommandSender sender);
    public boolean forceDelete(String name, CommandSender sender);
    public boolean giveOwnership(Player author, String name, String new_author);
    public boolean info(CommandSender p, String name);
    public boolean language(String language, CommandSender sender);
    public boolean list(CommandSender p);
    public boolean mint(Player p,String currencies, double amount, double ia);
    public boolean pay(Player p,Player target, String name, double amount);
    public boolean payOffline(Player p, OfflinePlayer target, String name, double amount);
    public boolean remove(Player p, String name, double amount);
    public boolean reloadConfig(CommandSender sender);
    public boolean rename (Player p,String currencies, String newName);
    public boolean setOrePrice(String ore, double amount, CommandSender sender);
    public boolean showAccount(Player p);
    public boolean showAccountOther(CommandSender s, OfflinePlayer op);
    public boolean top(Boolean all, String name, CommandSender p);
    public boolean withdraw(Player p, String name, double amount);
    public boolean wallet(CommandSender s, Player p);
}

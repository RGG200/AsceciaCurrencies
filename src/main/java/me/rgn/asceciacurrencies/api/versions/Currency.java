package me.rgn.asceciacurrencies.api.versions;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface Currency {
    public boolean add(Player p, String name, double amount);
    public boolean create(Player p, String name);
    public boolean delete(Player p, String name);
    public boolean forceDelete(String name, CommandSender sender);
    public boolean description(Player p, String description);
    public boolean info(Player p, String name);
    public boolean list(Player p);
    public boolean language(String language, CommandSender sender);
    public boolean mint(Player p,String currencies, double amount);
    public boolean setOrePrice(String ore, double amount, CommandSender sender);
    public boolean deposit(Player p,String currencies, double itemamount);
    public boolean pay(Player p,Player target, String name, double amount);
    public boolean remove(Player p, String name, double amount);
    public boolean reloadConfig(CommandSender sender);
    public boolean rename (Player p,String currencies, String newName);
    public boolean withdraw(Player p, String name, double amount);
    public boolean wallet(Player p);
    public boolean top(Boolean all, String name, Player p);
    public boolean giveOwnership(Player author, String name, String new_author);
}

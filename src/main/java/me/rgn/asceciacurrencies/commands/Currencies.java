package me.rgn.asceciacurrencies.commands;
import me.rgn.asceciacurrencies.api.CurrenciesAPI;
import me.rgn.asceciacurrencies.files.LanguageConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Currencies implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        CurrenciesAPI cAPI = new CurrenciesAPI();
        if (args.length > 0){
        if (args[0].equals("force") && args[1].equals("delete")){
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (p.hasPermission("asceciacurrencies.admin")) {
                    if (args.length > 3) {
                        p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-3"));
                    } else {
                        CurrenciesAPI.currency.forceDelete(args[1]);
                    }
                }
            }
        }
        if (sender instanceof Player) {
            //init variable
            Player p = (Player) sender;
            if (p.hasPermission("asceciacurrencies.players")) {
                UUID uuid = p.getUniqueId();
                String id = uuid.toString();
                //Create
                if (args[0].equals("create")) {
                    if (args.length < 2 || args.length > 2) {
                        p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-0"));
                    } else {
                        CurrenciesAPI.currency.create(p, args[1]);
                    }
                }

                //delete
                else if (args[0].equals("delete") || args[0].equals("del")) {
                    if (args.length < 2 || args.length > 2) {
                        p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-1"));
                    } else {
                        CurrenciesAPI.currency.delete(p, args[1]);
                    }
                }
                //description
                else if (args[0].equals("description") || args[0].equals("desc")) {
                    String message = "";
                    for (int i = 1; i < args.length; i++) {
                        message += args[i] + " ";
                    }
                    message = message.trim();
                    CurrenciesAPI.currency.description(p, message);
                }
                //withdraw
                else if (args[0].equals("withdraw") || args[0].equals("wd")) {
                    if (args.length < 3 || args.length > 3) {
                        p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-10"));
                    } else {
                        CurrenciesAPI.currency.withdraw(p, args[1], Double.valueOf(args[2]));
                    }
                }
                //info
                else if (args[0].equals("info")) {
                    if (args.length == 2) {
                        CurrenciesAPI.currency.info(p, args[1]);
                    } else {
                        p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-4"));
                    }
                }
                //list
                else if (args[0].equals("list")) {
                    CurrenciesAPI.currency.list(p);
                } else if (args[0].equals("mint")) {
                    if (args.length < 2 || args.length > 2) {
                        p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-6"));
                    } else {
                        CurrenciesAPI.currency.mint(p, Double.valueOf(args[1]));
                    }
                }
                //mint
                else if (args[0].equals("deposit") || args[0].equals("depo")) {
                    int itemamount = p.getInventory().getItemInMainHand().getAmount();
                    CurrenciesAPI.currency.deposit(p, itemamount);
                }
                //pay
                else if (args[0].equals("pay")) {
                    if (args.length < 4 || args.length > 4) {
                        p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-9"));
                    } else {
                        Player target = Bukkit.getServer().getPlayer(args[1]);
                        CurrenciesAPI.currency.pay(p, target, args[2], Double.valueOf(args[3]));
                    }

                } else if (args[0].equals("wallet") || args[0].equals("wal")) {
                    CurrenciesAPI.currency.wallet(p);
                } else {
                    p.sendMessage(ChatColor.YELLOW + cAPI.languageConfig.get().getString(cAPI.languageConfig.get().getString("language") + ".message-7"));
                }
            }
            }
        }else{
            if (sender instanceof Player p){
                p.sendMessage(ChatColor.YELLOW + cAPI.languageConfig.get().getString(cAPI.languageConfig.get().getString("language") + ".message-7"));
            }
        }
        return true;
    }
}
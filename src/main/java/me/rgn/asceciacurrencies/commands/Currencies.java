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
                 if (args[0].equals("help")) {
                    p.sendMessage(ChatColor.GOLD + "| Ascecia Currencies | Help | \n \n /currencies create (name) - creates a currency  \n \n /currencies delete/del (name) - deletes your currency \n \n /currencies description/desc (description) - sets the description of your currency your currency \n \n /currencies withdraw/wd (name) (amount) - turn back an amount of your currency into iron \n \n /currencies info (name) - gives you info about a currency \n \n /currencies list - gives you a list of all currencies available \n \n /currencies mint (amount) - makes an amount of currency \n \n /currencies deposit/depo - deposits the amount of ores you're holding in your hand into your currency to increase its power \n \n /currencies pay (playername) (name) (amount) - pays the target with an amount of currency \n \n /currencies wallet/wal - give you details about your wallet");
                }
                //Create
                else if (args[0].equals("create")) {
                    if (args.length < 2 || args.length > 2) {
                        p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-0"));
                    } else {
                        CurrenciesAPI.currency.create(p, args[1]);
                    }
                }

                //delete
                else if (args[0].equals("delete") || args[0].equals("del") ) {
                    if (args.length < 2  || args.length > 2) {
                        p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-1"));
                    } else {
                        CurrenciesAPI.currency.delete(p, args[1]);
                    }
                }
                //description
                 else if (args[0].equals("description") || args[0].equals("desc") ) {
                     String message = "";
                     for (int i = 1; i < args.length; i++) {
                         message += args[i] + " ";
                     }
                     message = message.trim();
                     CurrenciesAPI.currency.description(p, message);
                 }
                //withdraw
                else if (args[0].equals("withdraw") || args[0].equals("wd") ) {
                    if (args.length < 3 || args.length > 3) {
                        p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-10"));
                    }else  {
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
                else if (args[0].equals("deposit") || args[0].equals("depo") ) {
                    int itemamount = p.getInventory().getItemInMainHand().getAmount();
                    CurrenciesAPI.currency.deposit(p, itemamount);
                }
                //pay
                else if (args[0].equals("pay")) {
                    if (args.length < 4 || args.length > 4 ) {
                        p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-9"));
                    } else {
                        Player target = Bukkit.getServer().getPlayer(args[1]);
                        CurrenciesAPI.currency.pay(p, target, args[2], Double.valueOf(args[3]));
                    }

                } else if (args[0].equals("wallet") || args[0].equals("wal") ) {
                    CurrenciesAPI.currency.wallet(p);
                } else {
                    p.sendMessage(ChatColor.GOLD + "| Ascecia Currencies | Help | \n \n /currencies create (name) - creates a currency  \n \n /currencies delete (name) - deletes your currency \n \n /currencies withdraw (name) (amount) - turn back an amount of your currency into iron \n \n /currencies info (name) - gives you info about a currency \n \n /currencies list - gives you a list of all currencies available \n \n /currencies mint (amount) - makes an amount of currency \n \n /currencies deposit - deposit the amount of ores you're holding in your hand into your currency to increase its power \n \n /currencies pay (playername) (name) (amount) - pays the target with an amount of currency \n \n /currencies wallet - give you details about your wallet");
                }
                if (args[0] == null){
                    p.sendMessage(ChatColor.GOLD + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-7"));
                }

            }
        }
        return true;
    }
}
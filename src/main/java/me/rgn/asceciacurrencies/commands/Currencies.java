package me.rgn.asceciacurrencies.commands;
import me.rgn.asceciacurrencies.Currency;
import me.rgn.asceciacurrencies.api.CurrenciesAPI;
import me.rgn.asceciacurrencies.files.CustomConfig;
import me.rgn.asceciacurrencies.files.PlayersConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.Console;
import java.util.UUID;

public class Currencies implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        CurrenciesAPI cAPI = new CurrenciesAPI();
        if (args[0] == "forcedelete"){
            if (sender instanceof Player){
                Player p = (Player) sender;
                if (p.hasPermission("asceciacurrencies.admin")){
                    if (args.length > 1) {
                        CurrenciesAPI.Currencies.ForceDelete(args[1]);
                    }else {
                        p.sendMessage(ChatColor.DARK_RED + "[Currencies]: /currencies forcedelete (currencyname)");
                    }
                }
                if (args.length > 2){
                    p.sendMessage(ChatColor.DARK_RED + "[Currencies]: /currencies forcedelete (currencyname)");
                }
            }else {
                CurrenciesAPI.Currencies.ForceDelete(args[1]);
            }
        }
        if (sender instanceof Player) {
            //init variable
            Player p = (Player) sender;
            if (p.hasPermission("asceciacurrencies.players")) {
                UUID uuid = p.getUniqueId();
                String id = uuid.toString();
                 if (args[0].equals("help") || args.length == 0) {
                    p.sendMessage(ChatColor.GOLD + "| Ascecia Currencies | Help | \n \n /currencies create (name) - creates a currency  \n \n /currencies delete (name) - deletes your currency \n \n /currencies withdraw (name) (amount) - turn back an amount of your currency into iron \n \n /currencies info (name) - gives you info about a currency \n \n /currencies list - gives you a list of all currencies available \n \n /currencies mint (amount) - makes an amount of currency \n \n /currencies deposit - deposit the amount of ores you're holding in your hand into your currency to increase its power \n \n /currencies pay (playername) (name) (amount) - pays the target with an amount of currency \n \n /currencies wallet - give you details about your wallet");
                }
                //Create
                else if (args[0].equals("create")) {
                    if (args.length < 2) {
                        p.sendMessage(ChatColor.DARK_RED + "[Currencies]: /currencies create (currencyname)");
                    } else {
                        CurrenciesAPI.Currencies.Create(p, args[1]);
                    }
                    if (args.length > 2){
                        p.sendMessage(ChatColor.DARK_RED + "[Currencies]: /currencies create (currencyname)");
                    }
                }

                //delete
                else if (args[0].equals("delete")) {
                    if (args.length < 2) {
                        p.sendMessage(ChatColor.DARK_RED + "[Currencies]: /currencies delete (currencyname)");
                    } else {
                        CurrenciesAPI.Currencies.Delete(p, args[1]);
                    }
                }
                //withdraw
                else if (args[0].equals("withdraw")) {
                    if (args.length < 2) {
                        p.sendMessage(ChatColor.DARK_RED + "[Currencies]: /currencies withdraw (currencyname) (amount)");
                    } else {
                        if (args.length < 3) {
                            p.sendMessage(ChatColor.DARK_RED + "[Currencies]: /currencies withdraw (currencyname) (amount)");
                        }
                        CurrenciesAPI.Currencies.Withdraw(p, args[1], args[2]);
                    }
                     if (args.length > 3) {
                         p.sendMessage(ChatColor.DARK_RED + "[Currencies]: /currencies withdraw (currencyname) (amount)");
                     }
                }
                //info
                else if (args[0].equals("info")) {
                    if (args.length > 1) {
                        CurrenciesAPI.Currencies.Info(p, args[1]);
                    } else {
                        p.sendMessage(ChatColor.DARK_RED + "[Currencies]: /currencies info (currencyname)");
                    }
                    if (args.length > 2) {
                        p.sendMessage(ChatColor.DARK_RED + "[Currencies]: /currencies info (currencyname)");
                    }
                }
                //list
                else if (args[0].equals("list")) {
                    if (args.length == 1) {
                        CurrenciesAPI.Currencies.List(p);
                    }else {
                        p.sendMessage(ChatColor.DARK_RED + "[Currencies]: /currencies list");
                    }
                } else if (args[0].equals("mint")) {
                    if (args.length < 2) {
                        p.sendMessage(ChatColor.DARK_RED + "[Currencies]: /currencies mint (amount)");
                    } else {
                        CurrenciesAPI.Currencies.Mint(p, args[1]);
                    }if (args.length > 2) {
                         p.sendMessage(ChatColor.DARK_RED + "[Currencies]: /currencies mint (amount)");
                     }
                }
                //mint
                else if (args[0].equals("deposit")) {
                    if (args.length == 1) {
                        int itemamount = p.getInventory().getItemInMainHand().getAmount();
                        CurrenciesAPI.Currencies.Deposit(p, itemamount);
                    }else {
                        p.sendMessage(ChatColor.DARK_RED + "[Currencies]: /currencies deposit");
                    }
                }
                //pay
                else if (args[0].equals("pay")) {
                    if (args.length < 2) {
                        p.sendMessage(ChatColor.DARK_RED + "[Currencies]: /currencies pay (playername) (currencyname) (amount)");
                    } else {
                        if (args.length < 3) {
                            p.sendMessage(ChatColor.DARK_RED + "[Currencies]: /currencies pay (playername) (currencyname) (amount)");
                        } else {
                            if (args.length < 4) {
                                p.sendMessage(ChatColor.DARK_RED + "[Currencies]: /currencies pay (playername) (currencyname) (amount)");
                            } else {
                                Player target = Bukkit.getServer().getPlayer(args[1]);
                                CurrenciesAPI.Currencies.Pay(p, target, args[2], args[3]);
                            }
                        }
                    }
                     if (args.length > 4) {
                         p.sendMessage(ChatColor.DARK_RED + "[Currencies]: /currencies pay (playername) (currencyname) (amount)");
                     }
                } else if (args[0].equals("wallet")) {
                    CurrenciesAPI.Currencies.Wallet(p);
                } else {
                    p.sendMessage(ChatColor.GOLD + "| Ascecia Currencies | Help | \n \n /currencies create (name) - creates a currency  \n \n /currencies delete (name) - deletes your currency \n \n /currencies withdraw (name) (amount) - turn back an amount of your currency into iron \n \n /currencies info (name) - gives you info about a currency \n \n /currencies list - gives you a list of all currencies available \n \n /currencies mint (amount) - makes an amount of currency \n \n /currencies deposit - deposit the amount of ores you're holding in your hand into your currency to increase its power \n \n /currencies pay (playername) (name) (amount) - pays the target with an amount of currency \n \n /currencies wallet - give you details about your wallet");
                }

            }
        }
        return true;
    }
}
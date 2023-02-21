package me.rgn.asceciacurrencies.commands;
import me.rgn.asceciacurrencies.api.CurrenciesAPI;
import me.rgn.asceciacurrencies.files.CurrenciesConfig;
import me.rgn.asceciacurrencies.files.LanguageConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import javax.annotation.processing.Completions;
import java.util.*;

public class Currencies implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        CurrenciesAPI cAPI = new CurrenciesAPI();
        if (args.length > 0) {
            if (args[0].equals("force") && args[1].equals("delete")) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (p.hasPermission("asceciacurrencies.admin.forcemanage")) {
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
                UUID uuid = p.getUniqueId();
                String id = uuid.toString();
                //Create
                if (args[0].equals("create")) {
                    if (p.hasPermission("asceciacurrencies.player.manage")) {
                        if (args.length < 2 || args.length > 2) {
                            p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-0"));
                        } else {
                            CurrenciesAPI.currency.create(p, args[1]);
                        }
                    } else {
                        p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-11"));
                    }
                }

                //delete
                else if (args[0].equals("delete") || args[0].equals("del")) {
                    if (p.hasPermission("asceciacurrencies.player.manage")) {
                        if (args.length < 2 || args.length > 2) {
                            p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-1"));
                        } else {
                            CurrenciesAPI.currency.delete(p, args[1]);
                        }
                    } else {
                        p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-11"));
                    }
                }
                //description
                else if (args[0].equals("description") || args[0].equals("desc")) {
                    if (p.hasPermission("asceciacurrencies.player.infos")) {
                        String message = "";
                        for (int i = 1; i < args.length; i++) {
                            message += args[i] + " ";
                        }
                        message = message.trim();
                        CurrenciesAPI.currency.description(p, message);
                    } else {
                        p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-11"));
                    }
                }
                //withdraw
                else if (args[0].equals("withdraw") || args[0].equals("wd")) {
                    if (p.hasPermission("asceciacurrencies.player.withdraw")) {
                        if (args.length < 3 || args.length > 3) {
                            p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-10"));
                        } else {
                            CurrenciesAPI.currency.withdraw(p, args[1], Double.valueOf(args[2]));
                        }
                    } else {
                        p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-11"));
                    }
                }
                //info
                else if (args[0].equals("info")) {
                    if (p.hasPermission("asceciacurrencies.player.infos")) {
                        if (args.length == 2) {
                            CurrenciesAPI.currency.info(p, args[1]);
                        } else {
                            p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-4"));
                        }
                    } else {
                        p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-11"));
                    }
                }
                //list
                else if (args[0].equals("list")) {
                    if (p.hasPermission("asceciacurrencies.player.infos")) {
                        CurrenciesAPI.currency.list(p);
                    } else {
                        p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-11"));
                    }
                } else if (args[0].equals("mint")) {
                    if (p.hasPermission("asceciacurrencies.player.mint")) {
                        if (args.length < 2 || args.length > 2) {
                            p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-6"));
                        } else {
                            CurrenciesAPI.currency.mint(p, Double.valueOf(args[1]));
                        }
                    } else {
                        p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-11"));
                    }
                }
                //mint
                else if (args[0].equals("deposit") || args[0].equals("depo")) {
                    if (p.hasPermission("asceciacurrencies.player.deposit")) {
                        int itemamount = p.getInventory().getItemInMainHand().getAmount();
                        CurrenciesAPI.currency.deposit(p, itemamount);
                    } else {
                        p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-11"));
                    }
                }
                //pay
                else if (args[0].equals("pay")) {
                    if (p.hasPermission("asceciacurrencies.player.pay")) {
                        if (args.length < 4 || args.length > 4) {
                            p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-9"));
                        } else {
                            Player target = Bukkit.getServer().getPlayer(args[1]);
                            CurrenciesAPI.currency.pay(p, target, args[2], Double.valueOf(args[3]));
                        }
                    } else {
                        p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-11"));
                    }

                } else if (args[0].equals("wallet") || args[0].equals("wal")) {
                    if (p.hasPermission("asceciacurrencies.player.wallet")) {
                        CurrenciesAPI.currency.wallet(p);
                        p.sendMessage(ChatColor.YELLOW + cAPI.languageConfig.get().getString(cAPI.languageConfig.get().getString("language") + ".message-7"));
                    } else {
                        p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-11"));
                    }
                }
            }
        } else {
            if (sender instanceof Player p) {
                p.sendMessage(ChatColor.YELLOW + cAPI.languageConfig.get().getString(cAPI.languageConfig.get().getString("language") + ".message-7"));
            }
        }
        return true;
    }
    private static final String[] COMMANDS = {"create", "delete", "mint", "description", "info", "list", "deposit", "withdraw", "wallet", "pay"};
    private static final String[] CURRENCIES = {};
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String CommandLabel, String[] args){
        if(cmd.getName().equals("currencies")){
            // create new array
            final List<String> completions = new ArrayList<>();
            if (args.length == 1) {
                //if first character is matching the subcommand
                StringUtil.copyPartialMatches(args[0], Arrays.asList(COMMANDS), completions);
                //SORT THE LIST
                Collections.sort(completions);
            }
            return completions;
        }
        return null;
    }

}

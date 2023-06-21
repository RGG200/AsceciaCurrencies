package me.rgn.asceciacurrencies.commands;
import me.rgn.asceciacurrencies.AsceciaCurrencies;
import me.rgn.asceciacurrencies.api.CurrenciesAPI;
import me.rgn.asceciacurrencies.files.CurrenciesConfig;
import me.rgn.asceciacurrencies.files.LanguageConfig;
import me.rgn.asceciacurrencies.files.PlayersConfig;
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
        if (args.length != 0) {
            if (args[0].equals("force-delete")) {
                CommandSender s = sender;
                if (s.hasPermission("asceciacurrencies.admin.forcemanage")) {
                    if (args.length != 2) {
                        s.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-3"));
                    } else {
                        CurrenciesAPI.currency.forceDelete(args[1], s);
                    }
                }
            }else if (args[0].equals("config")){
                CommandSender s = sender;
                if (args.length == 1){
                    s.sendMessage(ChatColor.DARK_RED + "/currencies config (language/reload/ore)");
                }else{
                    switch (args[1]){
                        case "language":
                            if (s.hasPermission("asceciacurrencies.admin.language")) {
                                if(args.length == 3 && CurrenciesAPI.languageConfig.get().contains(args[1])){
                                    CurrenciesAPI.currency.language(args[2], s);
                                }else{
                                    s.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-14"));
                                }
                            } else {
                                s.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-11"));
                            }
                            break;
                        case "reload":
                            if (s.hasPermission("asceciacurrencies.admin.reloadconfig")) {
                                CurrenciesAPI.currency.reloadConfig(s);
                            }else{
                                s.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-11"));
                            }
                            break;
                        case "ore":
                            if(args.length == 4){
                                if (s.hasPermission("asceciacurrencies.admin.ore")) {
                                    CurrenciesAPI.currency.setOrePrice(args[2], Double.valueOf(args[3]), s);
                                }
                            }else{
                                s.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-15_1"));
                            }
                    }
                }
            }else if (sender instanceof Player) {
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
                //convert
                if(args[0].equals("convert")){
                    if(p.hasPermission("asceciacurrencies.player.convert")) {
                        if (args.length < 4 || args.length > 4) {
                            p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-18"));
                        }else{
                            CurrenciesAPI.currency.convertCurrency(p, args[1], args[2], Double.valueOf(args[3]));
                        }
                    }
                }
                // give ownership
                if (args[0].equals("give-ownership")) {
                    if (p.hasPermission("asceciacurrencies.player.manage")) {
                        if (args.length < 3 || args.length > 3) {
                            p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-17"));
                        } else {
                            CurrenciesAPI.currency.giveOwnership( p, args[1], args[2]);
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
                }else if (args[0].equals("config")){
                    CommandSender s = sender;
                    if (args.length == 1){
                        s.sendMessage(ChatColor.DARK_RED + "/currencies config (language/reload/ore)");
                    }else{
                        switch (args[1]){
                            case "language":
                                if (s.hasPermission("asceciacurrencies.admin.language")) {
                                    if(args.length == 3 && CurrenciesAPI.languageConfig.get().contains(args[1])){
                                        CurrenciesAPI.currency.language(args[2], s);
                                    }else{
                                        s.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-14"));
                                    }
                                } else {
                                    s.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-11"));
                                }
                                break;
                            case "reload":
                                if (s.hasPermission("asceciacurrencies.admin.reloadconfig")) {
                                    CurrenciesAPI.currency.reloadConfig(s);
                                }else{
                                    s.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-11"));
                                }
                                break;
                            case "ore":
                                if(args.length == 4){
                                    if (s.hasPermission("asceciacurrencies.admin.ore")) {
                                        CurrenciesAPI.currency.setOrePrice(args[2], Double.valueOf(args[3]), s);
                                    }
                                }else{
                                    s.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-15_1"));
                                }
                        }
                    }

                } else if (args[0].equals("mint")) {
                    if (p.hasPermission("asceciacurrencies.player.mint")) {
                        if (args.length < 3 || args.length > 3) {
                            p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-6"));
                        } else {
                            CurrenciesAPI.currency.mint(p, args[1], Double.valueOf(args[2]));
                        }
                    } else {
                        p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-11"));
                    }
                }
                //mint
                else if (args[0].equals("deposit") || args[0].equals("depo")) {
                    if(args.length == 2){
                        if (p.hasPermission("asceciacurrencies.player.deposit")) {
                            int itemamount = p.getInventory().getItemInMainHand().getAmount();
                            CurrenciesAPI.currency.deposit(p, args[1], itemamount);
                        } else {
                            p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-11"));
                        }
                    }else{
                        p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-8_2"));
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

                }//rename
                else if (args[0].equals("rename")) {
                    if (p.hasPermission("asceciacurrencies.player.rename")) {
                        if (args.length != 3) {
                            p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-12_1"));
                        } else {
                            CurrenciesAPI.currency.rename(p, args[1], args[2]);
                        }
                    } else {
                        p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-11"));
                    }

                } else if (args[0].equals("wallet") || args[0].equals("wal")) {
                    if (p.hasPermission("asceciacurrencies.player.wallet")) {
                        CurrenciesAPI.currency.wallet(p);
                    } else {
                        p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-11"));
                    }
                }else if(args[0].equals("top")){
                    if(args.length == 2 || args.length == 3){
                        if(p.hasPermission("asceciacurrencies.player.top")){
                            if(args[1].equals("all")){
                                cAPI.currency.top(true, "args[2]", p);
                            }else if(args[1].equals("one")){
                                if(cAPI.currenciesConfig.get().contains(args[2])){
                                    cAPI.currency.top(false, args[2], p);
                                }else{
                                    p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-13_1"));
                                }
                            }else{
                                p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-13"));
                            }
                        }else{
                            p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-11"));
                        }
                    }else{
                        p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-13"));
                    }
                }else if (args[0].equals("team")){
                    CommandSender s = sender;
                        if (s.hasPermission("asceciacurrencies.player.team")) {
                            if(args.length == 2 && args[1].equals("leave") || args.length == 2 && args[1].equals("join")){
                                switch (args[1]){
                                    case "leave":
                                        CurrenciesAPI.team.leaveTeam(s);
                                        break;
                                    case "join":
                                        CurrenciesAPI.team.addTeamMember(s, s.getName());
                                        break;
                                }
                            }else if(args.length == 3){
                                switch (args[1]){
                                    case "join":
                                        CurrenciesAPI.team.addTeamMember(s, s.getName());
                                        break;
                                    case "kick":
                                        CurrenciesAPI.team.kickTeamMember(s, args[2]);
                                        break;
                                    case "list":
                                        CurrenciesAPI.team.teamList(s, args[2]);
                                        break;
                                    case "permissions", "perms":
                                        CurrenciesAPI.team.getTeamMemberPermissions(s, args[2]);
                                        break;
                                    case "invite":
                                        CurrenciesAPI.team.inviteMember(s, args[2]);
                                        break;
                                }
                            }else if(args.length == 5){
                                switch (args[1]){
                                    case "set":
                                        CurrenciesAPI.team.setTeamMemberPermission(s, args[2], args[3], Boolean.valueOf(args[4]));
                                        break;
                                }
                            }else{
                                s.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-16"));
                            }
                        }else{
                            p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-11"));
                        }
                }else {
                    if (sender instanceof Player && !args[0].equals("create") && !args[0].equals("convert")) {
                        p.sendMessage(ChatColor.YELLOW + cAPI.languageConfig.get().getString(cAPI.languageConfig.get().getString("language") + ".message-7"));
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
    private static final String[] COMMANDS = {"create", "delete", "del", "mint", "description", "desc", "info", "list", "deposit", "depo", "withdraw", "wd", "wal", "wallet", "pay", "rename", "top", "team", "give-ownership", "convert"};
    private static final String[] COMMANDSPlUS = {"create", "delete", "del", "mint", "description", "desc", "info", "list", "deposit", "depo", "withdraw", "wd", "wal", "wallet", "pay", "rename", "top", "team", "force-delete", "config", "give-ownership", "convert"};
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String CommandLabel, String[] args){
        if(cmd.getName().equals("currencies")){
            // create new array
            final List<String> completions = new ArrayList<>();
            if (args.length == 1) {
                //if first character is matching the subcommand
                if(sender.hasPermission("asceciacurrencies.admin")){
                    StringUtil.copyPartialMatches(args[0], Arrays.asList(COMMANDSPlUS), completions);
                }else{
                    StringUtil.copyPartialMatches(args[0], Arrays.asList(COMMANDS), completions);
                }
                //SORT THE LIST
                Collections.sort(completions);
                return completions;
            }
            else if (args.length == 2) {
                switch (args[0].toLowerCase()){
                    case "delete","del", "info", "withdraw","wd", "mint", "depo", "deposit", "rename":
                        for (String currencies: CurrenciesConfig.get().getKeys(false)) {
                            completions.add(currencies);
                        }
                        break;
                    case "give-ownership", "convert":
                        for (String currencies: CurrenciesConfig.get().getKeys(false)) {
                            completions.add(currencies);
                        }
                        break;
                    case "config":
                        if(sender.hasPermission("asceciacurrencies.admin.reload-config")){
                            completions.add("reload");
                            completions.add("language");
                            completions.add("ore");
                        }
                        break;
                    case "force-delete":
                        if(sender.hasPermission("asceciacurrencies.admin.forcemanage")) {
                            for (String currencies : CurrenciesConfig.get().getKeys(false)) {
                                completions.add(currencies);
                            }
                        }
                        break;
                    case "pay":
                        for (Player player: Bukkit.getServer().getOnlinePlayers()){
                            String pName = player.getName();
                            completions.add(pName);
                        }
                        break;
                    case "top":
                        completions.add("all");
                        completions.add("one");
                        break;
                    case "team":
                        completions.add("set");
                        completions.add("invite");
                        completions.add("join");
                        completions.add("kick");
                        completions.add("leave");
                        completions.add("list");
                        completions.add("perms");
                        completions.add("permissions");
                        break;
                }
                Collections.sort(completions);
                return StringUtil.copyPartialMatches(args[1], completions, new ArrayList<>());
            }
            else if (args.length == 3) {
                switch (args[0].toLowerCase()){
                    case "pay", "top", "convert":
                        for (String currencies: CurrenciesConfig.get().getKeys(false)) {
                            completions.add(currencies);
                        }
                        break;
                    case "give-ownership":
                        for (Player player: Bukkit.getServer().getOnlinePlayers()){
                            String pName = player.getName();
                            completions.add(pName);
                        }
                        break;
                    case "config":
                        if(args[1].equals("language")){
                            List<String> locales = LanguageConfig.get().getStringList("locales");
                            completions.addAll(locales);
                        }
                        else if(args[1].equals("ore")) {
                            for (String ore : AsceciaCurrencies.plugin.getConfig().getKeys(true)) {
                                if (!ore.equals("ores_prices")) {
                                    completions.add(ore.substring(12));
                                }
                            }
                        }
                        break;
                    case "team":
                        if(!args[1].equals("list") && !args[1].equals("join")){
                            for (String player: PlayersConfig.get().getKeys(false)){
                                completions.add(player);
                            }
                            break;
                        }else if(args[1].equals("invite")){
                            for(Player player: Bukkit.getServer().getOnlinePlayers()){
                                completions.add(player.getName());
                            }
                        }
                        else{
                            for (String currencies: CurrenciesConfig.get().getKeys(false)) {
                                completions.add(currencies);
                            }
                            break;
                        }
                }
                Collections.sort(completions);
                return StringUtil.copyPartialMatches(args[2], completions, new ArrayList<>());
            }else if(args.length == 4){
                switch (args[0]){
                    case "team":
                        completions.add("mint");
                        completions.add("deposit");
                        completions.add("rename");
                        completions.add("description");
                        break;
                }
            }else if(args.length == 5){
                switch (args[0]){
                    case "team":
                        completions.add("true");
                        completions.add("false");
                        break;
                }
            }

        }
        return null;
    }

}

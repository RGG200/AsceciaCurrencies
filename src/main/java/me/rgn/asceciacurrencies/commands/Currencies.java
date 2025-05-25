package me.rgn.asceciacurrencies.commands;
import me.rgn.asceciacurrencies.AsceciaCurrencies;
import me.rgn.asceciacurrencies.api.CurrenciesAPI;
import me.rgn.asceciacurrencies.api.versions.CurrencyObject;
import me.rgn.asceciacurrencies.api.versions.Tuple;
import me.rgn.asceciacurrencies.files.CurrenciesConfig;
import me.rgn.asceciacurrencies.files.LanguageConfig;
import me.rgn.asceciacurrencies.files.PlayersConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import javax.annotation.processing.Completions;
import javax.naming.InsufficientResourcesException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class Currencies implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        CommandSender s = sender;
        if (args.length != 0) {
            switch(args[0]){
                case "config":
                    //check if the command has more then one argument
                    if (args.length == 1){
                        s.sendMessage(ChatColor.DARK_RED + "/currencies config (language/reload/ore)");
                    }else{
                        //check which command is being inputed
                        switch (args[1]){
                            case "language":
                                //check for perms
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
                                //check for perms
                                if (s.hasPermission("asceciacurrencies.admin.reloadconfig")) {
                                    CurrenciesAPI.currency.reloadConfig(s);
                                }else{
                                    s.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-11"));
                                }
                                break;
                            case "ore":
                                //check for perms + if value is double
                                if(args.length == 4 && !Double.valueOf(args[3]).isNaN()){
                                    if (s.hasPermission("asceciacurrencies.admin.ore")) {
                                        CurrenciesAPI.currency.setOrePrice(args[2], Double.parseDouble(args[3]), s);
                                    }
                                }else{
                                    s.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-15_1"));
                                }
                                break;
                            case "mint-material", "mm":
                                //check for perms
                                if(args.length == 4 && !Double.valueOf(args[3]).isNaN()){
                                    if (s.hasPermission("asceciacurrencies.admin.mm")) {
                                        CurrenciesAPI.currency.setMintMaterialPrice(args[2], Double.parseDouble(args[3]), s);
                                    }
                                }else{
                                    s.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-15_1"));
                                }
                                break;
                        }
                    }
                    break;
                case "create":
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (p.hasPermission("asceciacurrencies.player.manage")) {
                            if (args.length < 2 || args.length > 2) {
                                p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-0"));
                            } else {
                                CurrenciesAPI.currency.create(p, args[1]);
                            }
                        } else {
                            p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-11"));
                        }
                    }else{
                        Bukkit.getServer().getLogger().warning("You must be a player to execute this command");
                    }
                    break;
                case "force-create", "fc":
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (p.hasPermission("asceciacurrencies.admin.forcemanage")) {
                            if (args.length < 2 || args.length > 2) {
                                p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-19"));
                            } else {
                                CurrenciesAPI.currency.forceCreate(p, args[1]);
                            }
                        } else {
                            p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-11"));
                        }
                    }else{
                        Bukkit.getServer().getLogger().warning("You must be a player to execute this command");
                    }
                    break;
                case "convert":
                    //comming soon
                    break;
                case "delete", "del":
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (p.hasPermission("asceciacurrencies.player.manage")) {
                            if (args.length < 2 || args.length > 2) {
                                p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-1"));
                            } else {
                                CurrenciesAPI.currency.delete(p, args[1]);
                            }
                        } else {
                            p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-11"));
                        }
                    }else{
                        Bukkit.getServer().getLogger().warning("You must be a player to execute this command");
                    }
                    break;
                case "deposit", "depo":
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (args.length == 2) {
                            if (p.hasPermission("asceciacurrencies.player.deposit")) {
                                int itemamount = p.getInventory().getItemInMainHand().getAmount();
                                CurrenciesAPI.currency.deposit(p, args[1], itemamount);
                            } else {
                                p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-11"));
                            }
                        } else {
                            p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-8_2"));
                        }
                    }else{
                        Bukkit.getServer().getLogger().warning("You must be a player to execute this command");
                    }
                    break;
                case "description", "desc":
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
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
                    }else{
                        Bukkit.getServer().getLogger().warning("You must be a player to execute this command");
                    }
                    break;
                case "force-delete", "f-delete", "f-del":
                    if (s.hasPermission("asceciacurrencies.admin.forcemanage")) {
                        if (args.length != 2) {
                            s.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-3"));
                        } else {
                            CurrenciesAPI.currency.forceDelete(args[1], s);
                        }
                    }
                    break;
                case "give-ownership":
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (p.hasPermission("asceciacurrencies.player.manage")) {
                            if (args.length < 3 || args.length > 3) {
                                p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-17"));
                            } else {
                                CurrenciesAPI.currency.giveOwnership(p, args[1], args[2]);
                            }
                        } else {
                            p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-11"));
                        }
                    }else{
                        Bukkit.getServer().getLogger().warning("You must be a player to execute this command");
                    }
                    break;
                case "info", "inf", "i":
                    if (s.hasPermission("asceciacurrencies.player.infos")) {
                        if (args.length == 2) {
                            CurrenciesAPI.currency.info(s, args[1]);
                        } else {
                            s.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-4"));
                        }
                    } else {
                        s.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-11"));
                    }
                    break;
                case "list", "li":
                    if (s.hasPermission("asceciacurrencies.player.infos")) {
                        CurrenciesAPI.currency.list(s);
                    } else {
                        s.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-11"));
                    }
                    break;
                case "mint":
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (p.hasPermission("asceciacurrencies.player.mint")) {
                            if (args.length < 3 || args.length > 3 && Double.valueOf(args[2]).isNaN()) {
                                p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-6"));
                            } else {
                                int itemamount = p.getInventory().getItemInMainHand().getAmount();
                                CurrenciesAPI.currency.mint(p, args[1], Double.valueOf(args[2]), itemamount);
                            }
                        } else {
                            p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-11"));
                        }
                    }else{
                        Bukkit.getServer().getLogger().warning("You must be a player to execute this command");
                    }
                    break;
                case "pay":
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (p.hasPermission("asceciacurrencies.player.pay")) {
                            if (args.length < 4 || args.length > 4 && Double.valueOf(args[3]).isNaN()) {
                                p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-9"));
                            } else {
                                Player target = Bukkit.getServer().getPlayer(args[1]);
                                if(target != null && target.getName().equalsIgnoreCase(args[1])){
                                    CurrenciesAPI.currency.pay(p, target, args[2], Double.valueOf(args[3]));
                                }else{
                                    p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-9_1"));
                                }
                            }
                        } else {
                            p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-11"));
                        }

                    }else{
                        Bukkit.getServer().getLogger().warning("You must be a player to execute this command");
                    }
                    break;
                case "rename":
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (p.hasPermission("asceciacurrencies.player.rename")) {
                            if (args.length != 3 ) {
                                p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-12_1"));
                            } else {
                                CurrenciesAPI.currency.rename(p, args[1], args[2]);
                            }
                        } else {
                            p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-11"));
                        }

                    }else{
                        Bukkit.getServer().getLogger().warning("You must be a player to execute this command");
                    }
                    break;
                case "team":
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (p.hasPermission("asceciacurrencies.player.team")) {
                            if (args.length == 2 && args[1].equals("leave") || args.length == 2 && args[1].equals("join")) {
                                switch (args[1]) {
                                    case "leave":
                                        CurrenciesAPI.team.leaveTeam(p);
                                        break;
                                    case "join":
                                        CurrenciesAPI.team.addTeamMember(p, p.getUniqueId().toString());
                                        break;
                                }
                            } else if (args.length == 3) {
                                Boolean isPlayerRegistered = false;
                                switch (args[1]) {
                                    case "join":
                                        CurrenciesAPI.team.addTeamMember(p, p.getUniqueId().toString());
                                        break;
                                    case "kick":
                                        // Checking if the player ever connected
                                        for(String pid : CurrenciesAPI.playersConfig.get().getKeys(false)) {
                                            if (Objects.equals(CurrenciesAPI.playersConfig.get().getString(pid + ".name"), args[2])) {
                                                CurrenciesAPI.team.kickTeamMember(p, pid);
                                                isPlayerRegistered = true;
                                            }
                                        }
                                        if(!isPlayerRegistered) p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-18_1"));
                                        break;
                                    case "list":
                                        CurrenciesAPI.team.teamList(p, args[2]);
                                        break;
                                    case "permissions", "perms":
                                        // Checking if the player ever connected
                                        for(String pid : CurrenciesAPI.playersConfig.get().getKeys(false)) {
                                            if (Objects.equals(CurrenciesAPI.playersConfig.get().getString(pid + ".name"), args[2])) {
                                                CurrenciesAPI.team.getTeamMemberPermissions(p, pid);
                                                isPlayerRegistered = true;
                                            }
                                        }
                                        if(!isPlayerRegistered) p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-18_1"));
                                        break;
                                    case "invite":
                                        // Checking if the player ever connected
                                        for(String pid : CurrenciesAPI.playersConfig.get().getKeys(false)) {
                                            if (Objects.equals(CurrenciesAPI.playersConfig.get().getString(pid + ".name"), args[2])) {
                                                CurrenciesAPI.team.inviteMember(p, pid);
                                                isPlayerRegistered = true;
                                            }
                                        }
                                        if(!isPlayerRegistered) p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-18_1"));
                                        break;
                                }
                            } else if (args.length == 5) {
                                Boolean isPlayerRegistered = false;
                                switch (args[1]) {
                                    case "set":
                                        // Checking if the player ever connected
                                        for(String pid : CurrenciesAPI.playersConfig.get().getKeys(false)) {
                                            if (Objects.equals(CurrenciesAPI.playersConfig.get().getString(pid + ".name"), args[2])) {
                                                CurrenciesAPI.team.setTeamMemberPermission(p, pid, args[3], Boolean.valueOf(args[4]));
                                                isPlayerRegistered = true;
                                            }
                                        }
                                        if(!isPlayerRegistered) p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-18_1"));
                                        break;
                                }
                            } else {
                                s.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-16"));
                            }
                        } else {
                            p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-11"));
                        }
                    }else{
                        Bukkit.getServer().getLogger().warning("You must be a player to execute this command");
                    }
                    break;
                case "top":
                    if(args.length == 3){
                        if(s.hasPermission("asceciacurrencies.player.top")){
                            CurrenciesAPI.currency.top(args[1], s, Integer.parseInt(args[2]));
                        }else{
                            s.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-11"));
                        }
                    }else{
                        s.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-13"));
                    }
                    break;
                case "wallet":
                    if (s.hasPermission("asceciacurrencies.player.wallet")) {
                        if(args.length == 1) {
                            if(sender instanceof Player){
                                Player p = (Player) sender;
                                CurrenciesAPI.currency.wallet(p, p);
                            }else{
                                Bukkit.getServer().getLogger().warning("You must be a player to execute this command");
                            }
                        }else{
                            if(s.hasPermission("asceciacurrencies.admin.wallet")){
                                CurrenciesAPI.currency.wallet(s, Bukkit.getServer().getPlayer(args[1]));
                            }
                        }
                    } else {
                        s.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-11_1"));
                    }
                    break;
                case "withdraw":
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (p.hasPermission("asceciacurrencies.player.withdraw")) {
                            if (args.length < 3 || args.length > 3 && Double.valueOf(args[2]).isNaN()) {
                                p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-10"));
                            } else {
                                CurrenciesAPI.currency.withdraw(p, args[1], Double.valueOf(args[2]));
                            }
                        } else {
                            p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-11"));
                        }
                    }else{
                        Bukkit.getServer().getLogger().warning("You must be a player to execute this command");
                    }
                    break;
                case "account":
                    if(args.length == 1) {
                        if (sender instanceof Player) {
                            Player p = (Player) sender;
                            if (p.hasPermission("asceciacurrencies.player.account")) {
                                CurrenciesAPI.currency.showAccount(p);
                            }
                        } else {
                            Bukkit.getServer().getLogger().warning("You must be a player to execute this command");
                        }
                    } else if (args.length == 2) {
                        if (s.hasPermission("asceciacurrencies.admin.account")) {
                            OfflinePlayer p = Bukkit.getServer().getOfflinePlayer(args[1]);
                            Bukkit.getServer().getLogger().info(p.getUniqueId().toString());
                            if (PlayersConfig.get().contains(p.getUniqueId().toString())) {
                                CurrenciesAPI.currency.showAccountOther(s, p);
                            } else {
                                s.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-18_1"));
                            }
                        } else {
                            s.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-18"));
                        }
                    }
                    break;
                default:
                    if(args.length == 2) {
                        CurrenciesAPI.currency.help(s, Integer.parseInt(args[1]));
                    }else{
                        CurrenciesAPI.currency.help(s, 1);
                    }
            }
        }else {
            s.sendMessage(ChatColor.YELLOW + CurrenciesAPI.languageConfig.get().getString(CurrenciesAPI.languageConfig.get().getString("language") + ".message-7"));
        }
        // Updating currency objects
        for (String currencies : CurrenciesConfig.get().getKeys(false)) {
            int index = IntStream.range(0, CurrenciesAPI.currencyObjects.size())
                    .filter(i -> CurrenciesAPI.currencyObjects.get(i).currency_name.equals(currencies))
                    .findFirst()
                    .orElse(-1);
            // Researching currency team members for updating the currency object
            List<Tuple<String, List<Tuple<String, Boolean>>>> team = new ArrayList<>();
            Tuple<String, List<Tuple<String, Boolean>>> player = new Tuple<>();
            List<Tuple<String, Boolean>> playerperms = new ArrayList<>();
            Tuple<String, Boolean> playerperm = new Tuple<>();
            if(index != -1){
                for(String playerinteam: CurrenciesConfig.get().getConfigurationSection(currencies + ".team").getKeys(false)){
                    playerperm.First = "mint";
                    playerperm.Second = CurrenciesConfig.get().getBoolean(currencies + ".team." + playerinteam + ".mint");
                    playerperms.add(playerperm);
                    playerperm.First = "deposit";
                    playerperm.Second = CurrenciesConfig.get().getBoolean(currencies + ".team." + playerinteam + ".deposit");
                    playerperms.add(playerperm);
                    playerperm.First = "description";
                    playerperm.Second = CurrenciesConfig.get().getBoolean(currencies + ".team." + playerinteam + ".description");
                    playerperms.add(playerperm);
                    playerperm.First = "rename";
                    playerperm.Second = CurrenciesConfig.get().getBoolean(currencies + ".team." + playerinteam + ".rename");
                    playerperms.add(playerperm);
                    player.First = playerinteam;
                    player.Second = playerperms;
                    team.add(player);
                }
                CurrenciesAPI.currencyObjects.get(index).modify(currencies, CurrenciesConfig.get().getString(currencies + ".description"), CurrenciesConfig.get().getDouble(currencies + ".amount"), CurrenciesConfig.get().getDouble(currencies + ".totalvalue"), CurrenciesConfig.get().getDouble(currencies + ".power"), CurrenciesConfig.get().getDouble(currencies + ".economic-activity"), CurrenciesConfig.get().getString(currencies + ".author"), CurrenciesConfig.get().getInt(currencies + ".peers"), team);
            }
        }
            return true;
    }
    private static final String[] COMMANDS = {"create", "delete", "del", "mint", "description", "desc", "info", "list", "deposit", "depo", "withdraw", "wd", "wal", "wallet", "pay", "rename", "top", "team", "give-ownership", "account", "help"};
    private static final String[] COMMANDSPlUS = {"create", "delete", "del", "mint", "description", "desc", "info", "list", "deposit", "depo", "withdraw", "wd", "wal", "wallet", "pay", "rename", "top", "team", "force-create", "force-delete", "config", "give-ownership", "account", "help"};
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String CommandLabel, String[] args){
        if(cmd.getName().equals("currencies")){
            // create new array
            final List<String> completions = new ArrayList<>();
            if (args.length == 1) {
                //if first character is matching the subcommand
                if(sender.hasPermission("asceciacurrencies.admin")){
                    StringUtil.copyPartialMatches(args[0], Arrays.asList(COMMANDSPlUS), completions);
                }else if(sender.hasPermission("asceciacurrencies.player")){
                    StringUtil.copyPartialMatches(args[0], Arrays.asList(COMMANDS), completions);
                }
                //SORT THE LIST
                Collections.sort(completions);
                return completions;
            }
            else if (args.length == 2) {
                switch (args[0].toLowerCase()){
                    case "delete","del", "info", "withdraw","wd", "mint", "depo", "deposit", "rename", "give-ownership", "convert", "top":
                        completions.addAll(CurrenciesConfig.get().getKeys(false));
                        break;
                    case "config":
                        if(sender.hasPermission("asceciacurrencies.admin.reload-config")){
                            completions.add("reload");
                            completions.add("language");
                            completions.add("ore");
                            completions.add("mint-material");
                        }
                        break;
                    case "wallet":
                        for (Player player: Bukkit.getServer().getOnlinePlayers()){
                            String pName = player.getName();
                            completions.add(pName);
                        }
                    case "force-delete":
                        if(sender.hasPermission("asceciacurrencies.admin.forcemanage")) {
                            completions.addAll(CurrenciesConfig.get().getKeys(false));
                        }
                        break;
                    case "pay":
                        for (Player player: Bukkit.getServer().getOnlinePlayers()){
                            String pName = player.getName();
                            completions.add(pName);
                        }
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
                    case "account":
                        for(String player: PlayersConfig.get().getKeys(false)){
                            completions.add(PlayersConfig.get().getString(player + ".name"));
                        }
                    break;
                }
                Collections.sort(completions);
                return StringUtil.copyPartialMatches(args[1], completions, new ArrayList<>());
            }
            else if (args.length == 3) {
                switch (args[0].toLowerCase()){
                    case "pay", "convert":
                        completions.addAll(CurrenciesConfig.get().getKeys(false));
                        break;
                    case "give-ownership":
                        for (Player player: Bukkit.getServer().getOnlinePlayers()){
                            String pName = player.getName();
                            completions.add(pName);
                        }
                        break;
                    case "config":
                        switch (args[1]) {
                            case "language" -> {
                                List<String> locales = LanguageConfig.get().getStringList("locales");
                                completions.addAll(locales);
                            }
                            case "ore" -> {
                                for (String ore : AsceciaCurrencies.plugin.getConfig().getKeys(true)) {
                                    if (!ore.equals("ores_prices") && ore.contains("ores_prices")) {
                                        completions.add(ore.substring(12));
                                    }
                                }
                            }
                            case "mint-material", "mm" -> {
                                for (String mm : AsceciaCurrencies.plugin.getConfig().getKeys(true)) {
                                    if (!mm.equals("reserve_materials") && mm.contains("reserve_materials")) {
                                        completions.add(mm.substring(18));
                                    }
                                }
                            }
                        }
                        break;
                    case "team":
                        if(!args[1].equals("leave")) {
                            if (!args[1].equals("list") && !args[1].equals("join")) {
                                for (String player : PlayersConfig.get().getKeys(false)) {
                                    completions.add(PlayersConfig.get().getString(player + ".name"));
                                }
                            } else {
                                completions.addAll(CurrenciesConfig.get().getKeys(false));
                            }
                            if (args[1].equals("invite")) {
                                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                                    completions.add(player.getName());
                                }
                            }
                        }
                        break;
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
                Collections.sort(completions);
                return StringUtil.copyPartialMatches(args[3], completions, new ArrayList<>());
            }else if(args.length == 5){
                switch (args[0]){
                    case "team":
                        completions.add("true");
                        completions.add("false");
                        break;
                }
                Collections.sort(completions);
                return StringUtil.copyPartialMatches(args[4], completions, new ArrayList<>());
            }

        }
        return null;
    }

}

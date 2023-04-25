package me.rgn.asceciacurrencies.api;

import me.rgn.asceciacurrencies.AsceciaCurrencies;
import me.rgn.asceciacurrencies.files.CurrenciesConfig;
import me.rgn.asceciacurrencies.files.LanguageConfig;
import me.rgn.asceciacurrencies.files.PlayersConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.ChatPaginator;

import java.util.*;

public class Currency {
    public static boolean isCurrencyCreated;
    public static boolean add(Player p, String name, double amount){
        if(p != null){
            if (CurrenciesConfig.get().contains(name)){
                if (amount > 0) {
                    String id = p.getName();
                    double pBalance = PlayersConfig.get().getDouble(id + ".balance." + name);
                    double cMarketAmount = CurrenciesConfig.get().getDouble(name + ".amount");
                    double cValue = CurrenciesConfig.get().getDouble(name + ".totalvalue");
                    double cEcoActivity = CurrenciesConfig.get().getDouble(name + ".economic-activity");
                    double cPower = CurrenciesConfig.get().getDouble(name + ".power");
                    PlayersConfig.get().set(id + ".balance." + name, pBalance + amount);
                    CurrenciesConfig.get().set(name + ".amount", cMarketAmount + amount);
                    CurrenciesConfig.get().set(name + ".power",Double.valueOf(Math.round(((cValue + (cPower * amount)) / (cMarketAmount + amount)) * cEcoActivity)));
                    CurrenciesConfig.save();
                    CurrenciesConfig.reload();
                    PlayersConfig.save();
                    PlayersConfig.reload();
                }else {
                    System.out.println("The amount specified is too low !");
                }
            }else {
                System.out.println("The Currency specified is non-existant !");
            }
        }else{
            System.out.println("The player doesn't exist");
        }
        return true;
    }


    public static boolean create(Player p, String name){
        //getting player id
        int count = 0;
        boolean isNameValid = false;
        String id = p.getName();
        //check if player created a currency
        if (!PlayersConfig.get().contains(id + ".hascreated")) {
            //check if currency has the same name
            if (!CurrenciesConfig.get().contains(name)) {
                //creates the config keys
                if (name.length() > 2 && name.length() <= 9) {
                    for (int k = 0; k < name.length(); k++) {
                        if (Character.isLetter(name.charAt(k))) {
                            count++;
                        }
                    }
                    if (name.length() == count) {
                        isNameValid = true;
                    }
                    if (isNameValid == true) {
                        PlayersConfig.get().set(id + ".balance." + name, 1.0);
                        PlayersConfig.get().set(id + ".hascreated", true);
                        CurrenciesConfig.get().set(name + ".power", 0.0);
                        CurrenciesConfig.get().set(name + ".amount", 1.0);
                        CurrenciesConfig.get().set(name + ".totalvalue", 0.0);
                        CurrenciesConfig.get().set(name + ".economic-activity", 1.0);
                        CurrenciesConfig.get().set(name + ".description", "defaultDescription");
                        CurrenciesConfig.get().set(name + ".peers", 1);
                        CurrenciesConfig.get().set(name + ".author", id);
                        CurrenciesConfig.get().set(name + ".team." + id + ".mint", true);
                        CurrenciesConfig.get().set(name + ".team." + id + ".deposit", true);
                        CurrenciesConfig.get().set(name + ".team." + id + ".rename", true);
                        p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-0") + name + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-0_1"));
                        isCurrencyCreated = true;
                    } else {
                        p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-0_1"));
                    }
                } else {
                    p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-0_1"));
                }
            } else {
                p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-0_2"));
            }
        }else {
            p.sendMessage(ChatColor.RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-0_3"));
        }
        PlayersConfig.save();
        PlayersConfig.reload();
        CurrenciesConfig.save();
        CurrenciesConfig.reload();
        return true;
    }

    public static boolean delete(Player p, String name){
        //gets the sender and the author of the currency
        String id = p.getName();
        double cEcoActivity = CurrenciesConfig.get().getDouble(name + ".economic-activity");
        String author = CurrenciesConfig.get().getString(name + ".author");
        //check if sender is the author
        if (id.equals(author) || CurrenciesConfig.get().getList(name + ".team").contains(id)) {
            //gives the money contained in the currency and deletes the config keys
            String cname = name;
            double cMarketValue = CurrenciesConfig.get().getDouble(name + ".totalvalue");
            for (double i = 0; i < cMarketValue*cEcoActivity; i+=0.01) {
                double difference = (cMarketValue*cEcoActivity)-i;
                final List<String[]> material_prices = new ArrayList<>();
                for(String price: AsceciaCurrencies.plugin.getConfig().getKeys(true)){
                    if(!price.equals("ores_prices")){
                        final String[] material_price = {String.valueOf(AsceciaCurrencies.plugin.getConfig().getDouble(price)), price};
                        material_prices.add(material_price);
                    }
                }
                Collections.sort(material_prices, new Comparator<String[]>() {
                    @Override
                    public int compare(String[] o1, String[] o2) {
                        double d1 = Double.valueOf(o1[0]);
                        double d2 = Double.valueOf(o2[0]);
                        return Double.compare(d1, d2);
                    }
                });
                Collections.reverse(material_prices);
                for(String[] material: material_prices) {
                    String[] next_material = {"100000000", "ores_prices.dummy"};
                    if(0 <= material_prices.indexOf(material)-1){
                        next_material = material_prices.get(material_prices.indexOf(material)-1);
                    }
                    if (difference >= Double.valueOf(material[0]) && difference < Double.valueOf(next_material[0])) {
                        if(i+Double.valueOf(material[0])-0.01 > cMarketValue*cEcoActivity){
                            continue;
                        }else{
                            String item_string = material[1].substring(12);
                            Material itemMaterial = Material.valueOf(item_string.toUpperCase());
                            final ItemStack itemGiven = new ItemStack(itemMaterial, 1);
                            final Map<Integer, ItemStack> map = p.getInventory().addItem(itemGiven);
                            for (final ItemStack item : map.values()) {
                                p.getWorld().dropItemNaturally(p.getLocation(), item);
                            }
                            i += Double.valueOf(material[0]);
                            System.out.println(item_string + " i is equal to:" + i + " / " + (cMarketValue*cEcoActivity));
                            if(cMarketValue*cEcoActivity <= i){
                                break;
                            }
                        }
                    }
                }
                for(String key: PlayersConfig.get().getKeys(false)){
                    PlayersConfig.get().set(key + ".balance." + name, null);
                }
                PlayersConfig.save();
            }
            PlayersConfig.get().set(id + ".hascreated", null);
            isCurrencyCreated = false;
            CurrenciesConfig.get().set(cname, null);
            CurrenciesConfig.save();
            PlayersConfig.save();
            PlayersConfig.reload();
            CurrenciesConfig.reload();
            p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-1"));

        } else {
            p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-1_1"));
        }
        return true;
    }

    public static boolean description(Player p, String description){
        String id = p.getName();
        for (String currencies : CurrenciesConfig.get().getKeys(false)){
            String author = CurrenciesConfig.get().getString(currencies + ".author");
            if (author.equals(id)){
                CurrenciesConfig.get().set(currencies + ".description", description);
                p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-6"));
            }
        }
        return true;
    }

    public static boolean forceDelete(String name, CommandSender sender){
        String cname = name;
        String id = CurrenciesConfig.get().getString(name + ".author");
        ItemStack nuggets = new ItemStack(Material.IRON_NUGGET, 1);
        for (String key : PlayersConfig.get().getKeys(false)) {
            PlayersConfig.get().set(key + ".balance." + name, null);
        }
        if (CurrenciesConfig.get().contains(name)){
            PlayersConfig.get().set(id + ".hascreated", null);
            isCurrencyCreated = false;
            PlayersConfig.save();
            PlayersConfig.reload();
            CurrenciesConfig.save();
            CurrenciesConfig.reload();
            sender.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-1_1"));
        }else{
            sender.sendMessage(ChatColor.RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-10_2"));
        }
        return true;
    }


    public static boolean info(Player p, String name){
        if (CurrenciesConfig.get().contains(name)) {
            p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-8") + name + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-8_1") + CurrenciesConfig.get().getDouble(name + ".amount") + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-8_7") + CurrenciesConfig.get().getString(name + ".description") + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-8_2") + CurrenciesConfig.get().getDouble(name + ".power") + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-8_5") + CurrenciesConfig.get().getDouble(name + ".totalvalue") + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-8_6") + CurrenciesConfig.get().getString(name + ".author") + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-8_4") + CurrenciesConfig.get().getDouble(name + ".economic-activity") + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-8_3") + CurrenciesConfig.get().getInt(name + ".peers") + "\n");
        } else {
            p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-4_1"));
        }
        return true;
    }
    public static boolean list(Player p){
        //displays currencies
        if(CurrenciesConfig.get().getKeys(false).size() > 0){
            p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-9"));
            for (String currencies : CurrenciesConfig.get().getKeys(false)) {
                p.sendMessage(ChatColor.GOLD + "    " + currencies + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-9_1") + CurrenciesConfig.get().getDouble(currencies + ".power") + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-9_2") + CurrenciesConfig.get().getDouble(currencies + ".economic-activity") + "\n");
            }
        }else {
            p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-5"));
        }
        return true;
    }
    public static boolean language(String language, CommandSender sender){
        if(LanguageConfig.get().contains(language)){
            LanguageConfig.get().set("language", language);
            sender.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-14"));
        }else{
            sender.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-14_1"));
        }
        LanguageConfig.save();
        return true;
    }


    public static boolean mint(Player p,String currencies, double amount){
        //check until the currency of the player is found
        String id = p.getName();
            if(CurrenciesConfig.get().getKeys(false).size() > 0) {
                //check if any currency exists
                //init vars
                double globalamount = CurrenciesConfig.get().getDouble(currencies + ".amount");
                double cValue = CurrenciesConfig.get().getDouble(currencies + ".totalvalue");
                double cEcoActivity = CurrenciesConfig.get().getDouble(currencies + ".economic-activity");
                double cPower = CurrenciesConfig.get().getDouble(currencies + ".power");
                String author = CurrenciesConfig.get().getString(currencies + ".author");
                if (Double.valueOf(Math.round(amount*1000))/1000 >= 1) {
                    if (id.equals(author) || CurrenciesConfig.get().getBoolean(currencies + ".team." + id + ".mint") == true) {
                        double pbalance = PlayersConfig.get().getDouble(id + ".balance." + currencies);
                        PlayersConfig.get().set(id + ".balance." + currencies, pbalance + Double.valueOf(Math.round(amount*1000))/1000);
                        globalamount += Double.valueOf(Math.round(amount*1000))/1000;
                        CurrenciesConfig.get().set(currencies + ".amount", globalamount);
                        p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-2") + Double.valueOf(Math.round(amount*1000))/1000 + " " + currencies);
                        if (cEcoActivity > 0.2) {
                            CurrenciesConfig.get().set(currencies + ".economic-activity", cEcoActivity - (5e-5/cPower));
                        }
                        if (cEcoActivity <= 0.2){
                            CurrenciesConfig.get().set(currencies + ".economic-activity", 0.2);
                        }
                        if (cValue > 0){
                            CurrenciesConfig.get().set(currencies + ".power", (Double.valueOf(Math.round((cValue / globalamount) * 1000)) / 1000)*cEcoActivity);
                        }else {
                            CurrenciesConfig.get().set(currencies + ".power", 0.0);
                        }
                        CurrenciesConfig.save();
                        PlayersConfig.save();
                    }else{
                        p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-11"));
                    }
                } else {
                    p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-10_1"));
                }
            }else {
                p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-7"));
            }
        return true;
    }

    public static boolean setOrePrice(String ore, double amount, CommandSender sender){
        AsceciaCurrencies.plugin.getConfig().set("ores_prices." + ore, amount);
        sender.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-15"));
        AsceciaCurrencies.plugin.saveConfig();
        return true;
    }

    public static boolean deposit(Player p,String currencies, double itemamount){
        //init root variable
        String id = p.getName();
        if(CurrenciesConfig.get().getKeys(false).size() > 0) {
            //init variables
                double cValue = CurrenciesConfig.get().getDouble(currencies + ".totalvalue");
                double amount = 0;
                double cMarketAmount = CurrenciesConfig.get().getDouble(currencies + ".amount");
                double cEcoActivity = CurrenciesConfig.get().getDouble(currencies + ".economic-activity");
                String author = CurrenciesConfig.get().getString(currencies + ".author");
                List team = CurrenciesConfig.get().getList(currencies + ".team");
                //check if player has a currency
                //check if he's the author of the currency being checked
                if (author.equals(id) || CurrenciesConfig.get().getBoolean(currencies + ".team." + id + ".deposit") == true) {
                    //check useless to see if there's no currency minted
                    if (cMarketAmount == 0) {
                        p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-8"));
                    }/* Deposit the ores */ else {
                        if(AsceciaCurrencies.plugin.getConfig().contains("ores_prices." + p.getInventory().getItemInMainHand().getType().toString().toLowerCase())){
                            amount = AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices." + p.getInventory().getItemInMainHand().getType().toString().toLowerCase()) * itemamount;
                            CurrenciesConfig.get().set(currencies + ".totalvalue", cValue + amount);
                            CurrenciesConfig.get().set(currencies + ".power", Double.valueOf(Math.round(((cValue + amount) / cMarketAmount)*1000))/1000);;
                            p.getInventory().setItemInMainHand(null);
                            p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3") + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                        }else {
                            p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-8_1"));
                        }
                    }
                }else{
                    p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-11"));
                }
            CurrenciesConfig.save();
        }else {
            p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-7"));
        }
        return true;
    }


    public static boolean pay(Player p,Player target, String name, double amount){
        //init variables for some reason
        String targetidd = target.getName();
        String playeridd = p.getName();
        String pName = p.getName();
        String tName = target.getName();
        double cPower = CurrenciesConfig.get().getDouble(name + ".power");
        double cValue = CurrenciesConfig.get().getDouble(name + ".totalvalue");
        double cMarketAmount = CurrenciesConfig.get().getDouble(name + ".amount");
        double cEcoActivity = CurrenciesConfig.get().getDouble(name + ".economic-activity");
        int nPeers = CurrenciesConfig.get().getInt(name + ".peers");
        //if you don't have friends
        if (target == null) {
            p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-9_1"));
        }
        //if the currency exists
        if (!CurrenciesConfig.get().contains(name)) {
            p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-9_2"));
        }
        if (target != null) {
            //if currency exists
            if (CurrenciesConfig.get().contains(name)) {
                //if you're trying to pay yourself
                if (target == p) {
                    p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-9_4"));
                } else {
                    //if balance key not created
                    if (!PlayersConfig.get().contains(playeridd + ".balance." + name)) {
                        PlayersConfig.get().addDefault(playeridd + ".balance." + name, 0.0);
                    }
                    double pbalance = PlayersConfig.get().getDouble(playeridd + ".balance." + name);
                    if (!PlayersConfig.get().contains(targetidd + ".balance." + name)) {
                        PlayersConfig.get().addDefault(targetidd + ".balance." + name, 0.0);
                        CurrenciesConfig.get().set(name + ".peers", nPeers + 1);
                    }
                    double tbalance = PlayersConfig.get().getDouble(targetidd + ".balance." + name);
                    //if amount not too low
                    if (pbalance >= Double.valueOf(Math.round(amount*1000))/1000) {
                        if (Double.valueOf(Math.round(amount*1000))/1000 >= 0.01) {
                            //pay
                            nPeers = CurrenciesConfig.get().getInt(name + ".peers");
                            PlayersConfig.get().set(targetidd + ".balance." + name, tbalance + Double.valueOf(Math.round(amount*1000))/1000);
                            PlayersConfig.get().set(playeridd + ".balance." + name, pbalance - Double.valueOf(Math.round(amount*1000))/1000);
                            CurrenciesConfig.get().set(name + ".economic-activity", cEcoActivity + (0.001*nPeers));
                            CurrenciesConfig.get().set(name + ".power", Double.valueOf(Math.round((cValue/cMarketAmount)*1000))/1000);
                            p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-4") + Double.valueOf(Math.round(amount*1000))/1000 + " " + name + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-4_2") + tName);
                            target.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-4_1")+ Double.valueOf(Math.round(amount*1000))/1000 + " " + name + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-4_3") + pName);
                            PlayersConfig.save();
                            CurrenciesConfig.save();
                        } else {
                            p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-10_1"));
                        }
                    } else {
                        p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-9_3"));
                    }
                }
            }
        }
        return true;
    }
    // remove ( for developpers to use)
    public static boolean remove(Player p, String name, double amount){
        if(p != null){
            if (CurrenciesConfig.get().contains(name)) {
                if (amount > 0) {
                    String id = p.getName();
                    double pBalance = PlayersConfig.get().getDouble(id + ".balance." + name);
                    double cMarketAmount = CurrenciesConfig.get().getDouble(name + ".amount");
                    double cValue = CurrenciesConfig.get().getDouble(name + ".totalvalue");
                    double cEcoActivity = CurrenciesConfig.get().getDouble(name + ".economic-activity");
                    double cPower = CurrenciesConfig.get().getDouble(name + ".power");
                    PlayersConfig.get().set(id + ".balance." + name, pBalance - amount);
                    CurrenciesConfig.get().set(name + ".amount", cMarketAmount - amount);
                    CurrenciesConfig.get().set(name + ".power", Double.valueOf(Math.round((cValue-(cPower*amount))/(cMarketAmount-amount)*1000))/1000);
                    CurrenciesConfig.save();
                    CurrenciesConfig.reload();
                    PlayersConfig.save();
                    PlayersConfig.reload();
                } else {
                    System.out.println("The amount specified is too low !");
                }
            } else {
                System.out.println("The Currency specified doesnt exist !");
            }
        }else{
            System.out.println("The player doesn't exist");
        }
        return true;
    }
    public static boolean reloadConfig(CommandSender sender){
        CurrenciesConfig.reload();
        AsceciaCurrencies.plugin.reloadConfig();
        PlayersConfig.reload();
        LanguageConfig.reload();
        sender.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-12"));
        return true;
    }


    public static boolean rename (Player p,String currencies, String newName){
        // init vars
        String id = p.getName();
        int count = 0;
        boolean isNameValid = false;
        //checking if sender created a currency
        if (PlayersConfig.get().contains(id + ".hascreated")){
            //checking every currency
                double pBalance = PlayersConfig.get().getDouble(id + "." + currencies + ".balance");
                double cMarketAmount = CurrenciesConfig.get().getDouble(currencies + ".amount");
                double cValue = CurrenciesConfig.get().getDouble(currencies + ".totalvalue");
                double cEcoActivity = CurrenciesConfig.get().getDouble(currencies + ".economic-activity");
                double cPower = CurrenciesConfig.get().getDouble(currencies + ".power");
                int nPeers = CurrenciesConfig.get().getInt(currencies + ".peers");
                String description = CurrenciesConfig.get().getString(currencies + ".description");
                String author = CurrenciesConfig.get().getString(currencies + ".author");
                //checking if sender is the author of the currency
                if (id.equals(author) || CurrenciesConfig.get().getBoolean(currencies + ".team." + id + ".rename") == true){
                    if (!newName.equals(currencies)){
                        if (newName.length() > 2 && newName.length() <= 9) {
                            for (int k = 0; k < newName.length(); k++) {
                                if (Character.isLetter(newName.charAt(k))) {
                                    count++;
                                }
                            }
                            if (newName.length() == count) {
                                isNameValid = true;
                            }
                            if (isNameValid == true) {
                                //cloning the currency
                                PlayersConfig.get().set(id + ".balance." + newName, pBalance);
                                CurrenciesConfig.get().set(newName + ".power", cPower);
                                CurrenciesConfig.get().set(newName + ".amount", cMarketAmount);
                                CurrenciesConfig.get().set(newName + ".totalvalue", cValue);
                                CurrenciesConfig.get().set(newName + ".economic-activity", cEcoActivity);
                                CurrenciesConfig.get().set(newName + ".description", description);
                                CurrenciesConfig.get().set(newName + ".peers", nPeers);
                                CurrenciesConfig.get().set(newName + ".author", author);
                                //deleting the original
                                PlayersConfig.get().set(id + ".balance." + currencies, null);
                                CurrenciesConfig.get().set(currencies, null);
                                p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-0") + currencies + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-11") + newName);
                                CurrenciesConfig.save();
                                PlayersConfig.save();
                                CurrenciesConfig.reload();
                                PlayersConfig.reload();
                            } else {
                                p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-0_1"));
                            }
                        } else {
                            p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-0_1"));
                        }
                    }else {
                        p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-12_2"));
                    }
                }else{
                    p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-11"));
                }
        }else {
            p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-12"));
        }
        return true;
    }
    public static boolean addTeamMember(CommandSender s, String name){
        for(String currencies: CurrenciesConfig.get().getKeys(false)){
            if (CurrenciesConfig.get().getString(currencies + ".author").equals(s.getName().toString())){
                CurrenciesConfig.get().set(currencies + ".team." + name + ".rename", false);
                CurrenciesConfig.get().set(currencies + ".team." + name + ".mint", true);
                CurrenciesConfig.get().set(currencies + ".team." + name + ".deposit", true);
                s.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-15"));
            }
        }
        CurrenciesConfig.save();
        CurrenciesConfig.reload();
        return true;
    }
    public static boolean setTeamMemberPermission(CommandSender s, String name, String Permission, Boolean allowordeny){
        for(String currencies: CurrenciesConfig.get().getKeys(false)){
            if (CurrenciesConfig.get().getString(currencies + ".author").equals(s.getName().toString())){
                if(CurrenciesConfig.get().contains(currencies + ".team." + name)){
                    if(CurrenciesConfig.get().contains(currencies + ".team." + name + "." + Permission)){
                        CurrenciesConfig.get().set(currencies + ".team." + name + "." + Permission, allowordeny);
                        s.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-15"));
                    }else{
                        s.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-16_1"));
                    }
                }else{
                    s.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-16_2"));
                }
            }
            CurrenciesConfig.save();
            CurrenciesConfig.reload();
        }
        return true;
    }
    public static boolean teamList(CommandSender s, String name){
        if(CurrenciesConfig.get().contains(name)){
            s.sendMessage(ChatColor.AQUA + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-16"));
            for (String player: PlayersConfig.get().getKeys(false)){
                if (CurrenciesConfig.get().contains(name + ".team." + player)){
                    s.sendMessage("     " + ChatColor.GREEN + player + ", ");
                }
            }
        }
        CurrenciesConfig.save();
        CurrenciesConfig.reload();
        return true;
    }
    public static boolean removeTeamMember(CommandSender s, String name){
        final List<?> team = new ArrayList<>();
        for(String currencies: CurrenciesConfig.get().getKeys(false)){
            if (CurrenciesConfig.get().getString(currencies + ".author").equals(s.getName().toString())){
                team.equals(CurrenciesConfig.get().getList(currencies + ".team"));
                if(team.contains(name)){
                    CurrenciesConfig.get().set(currencies + ".team." + name, null);
                    s.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-15"));
                }
            }
        }
        CurrenciesConfig.save();
        CurrenciesConfig.reload();
        return true;
    }
    public static Boolean top(Boolean all, String name, Player p, String paginate){
        final List<String> scoreboard = new ArrayList<>();
        int i = 0;
        int j = 0;
        int page = 0;
        if(paginate.isEmpty() || paginate.equals("0")){
            paginate = "1";
        }
        StringBuilder leaderboard = new StringBuilder();
        page = Integer.parseInt(paginate);
        if(all){
            p.sendMessage(ChatColor.GOLD + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-13") + "\n ");
            PlayersConfig.reload();
            for(String currencies: CurrenciesConfig.get().getKeys(false)){
                p.sendMessage(ChatColor.GOLD + " " + currencies + ": \n");
                for (String player: PlayersConfig.get().getKeys(false)) {
                    scoreboard.add(String.valueOf(PlayersConfig.get().getDouble(player + ".balance." + currencies)));
                }
                Collections.sort(scoreboard);
                Collections.reverse(scoreboard);
                for(String playerBalance: scoreboard){
                    for(String player: PlayersConfig.get().getKeys(false)){
                        // if the player balance = current ranking player
                        System.out.println(scoreboard);
                        if(playerBalance.equals(String.valueOf(PlayersConfig.get().getDouble(player + ".balance." + currencies)))){
                            i++;
                            p.sendMessage(ChatColor.RED + "     " + i + ". " +  player + ": " + ChatColor.GREEN + playerBalance + "\n ");
                        }
                    }
                }
            }
        }else{
            p.sendMessage(ChatColor.GOLD + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-13") + " " + name + " |"  + "\n ");
            PlayersConfig.reload();
            for (String player: PlayersConfig.get().getKeys(false)) {
                scoreboard.add(String.valueOf(PlayersConfig.get().getDouble(player + ".balance." + name)));
            }
            Collections.sort(scoreboard);
            Collections.reverse(scoreboard);
            for(String playerBalance: scoreboard){
                for(String player: PlayersConfig.get().getKeys(false)){
                    if(playerBalance.equals(String.valueOf(PlayersConfig.get().getDouble(player + ".balance." + name)))){
                        i++;
                        if(i <= 5) {
                            leaderboard.append(ChatColor.RED + "     " + i + ". " + player + ": " + ChatColor.GREEN + playerBalance + "\n ");
                        }
                    }
                }
            }
            ChatPaginator.ChatPage chatPage = ChatPaginator.paginate(leaderboard.toString(), page);
            for(String line: chatPage.getLines()){
                p.sendMessage(line);
            }
        }
        return true;
    }

    public static boolean withdraw(Player p, String name, double amount){
        //init vars and config keys
        double cPower = CurrenciesConfig.get().getDouble(name + ".power");
        double cMarketValue = CurrenciesConfig.get().getDouble(name + ".totalvalue");
        double cMarketAmount = CurrenciesConfig.get().getDouble(name + ".amount");
        double cEcoActivity = CurrenciesConfig.get().getDouble(name + ".economic-activity");
        String id = p.getName().toString();
        String pname = p.getName().toString();
        //if currency exists
        if (CurrenciesConfig.get().contains(name)) {
            double pBalance = PlayersConfig.get().getDouble(id + ".balance." + name);
            //if you're not a rat
            if (pBalance >= Double.valueOf(Math.round(amount*1000))/1000) {
                if (Double.valueOf(Math.round(amount*1000))/1000 >= 1){
                    if (Double.valueOf(Math.round(amount*1000))/1000 < cMarketAmount){
                        for (double i = 0; i < (cPower*amount); i+=0.01) {
                            double difference = (cPower*amount)-i;
                            final List<String[]> material_prices = new ArrayList<>();
                            for(String price: AsceciaCurrencies.plugin.getConfig().getKeys(true)){
                                if(!price.equals("ores_prices")){
                                    final String[] material_price = {String.valueOf(AsceciaCurrencies.plugin.getConfig().getDouble(price)), price};
                                    material_prices.add(material_price);
                                }
                            }
                            Collections.sort(material_prices, new Comparator<String[]>() {
                                @Override
                                public int compare(String[] o1, String[] o2) {
                                    double d1 = Double.valueOf(o1[0]);
                                    double d2 = Double.valueOf(o2[0]);
                                    return Double.compare(d1, d2);
                                }
                            });
                            Collections.reverse(material_prices);
                            for(String[] material: material_prices) {
                                String[] next_material = {"100000000", "ores_prices.dummy"};
                                if(0 <= material_prices.indexOf(material)-1){
                                    next_material = material_prices.get(material_prices.indexOf(material)-1);
                                }
                                if (difference >= Double.valueOf(material[0]) && difference < Double.valueOf(next_material[0])) {
                                    if(Double.valueOf(material[0])+i > cPower*amount){
                                        continue;
                                    }else{
                                        String item_string = material[1].substring(12);
                                        Material itemMaterial = Material.valueOf(item_string.toUpperCase());
                                        final ItemStack itemGiven = new ItemStack(itemMaterial, 1);
                                        final Map<Integer, ItemStack> map = p.getInventory().addItem(itemGiven);
                                        for (final ItemStack item : map.values()) {
                                            p.getWorld().dropItemNaturally(p.getLocation(), item);
                                        }
                                        i += Double.valueOf(material[0]);
                                        System.out.println(item_string + " i is equal to:" + i);
                                        if(cPower*amount <= i){
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        CurrenciesConfig.get().set(name + ".amount", cMarketAmount - Double.valueOf(Math.round(amount*1000))/1000);
                        CurrenciesConfig.get().set(name + ".totalvalue", cMarketValue - (cPower * (Double.valueOf(Math.round(amount*1000))/1000)));
                        //if the eco activity is superior to 0.2
                        if(cEcoActivity > 0.2) {
                            CurrenciesConfig.get().set(name + ".economic-activity", cEcoActivity - (5e-5/cPower));
                        }
                        CurrenciesConfig.get().set(name + ".power", Double.valueOf(Math.round((cMarketValue-(cPower*Double.valueOf(Math.round(amount*1000))/1000))/(cMarketAmount-Double.valueOf(Math.round(amount*1000))/1000)*1000))/1000);
                        PlayersConfig.get().set(id + ".balance." + name, pBalance - Double.valueOf(Math.round(amount*1000))/1000);
                        p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-5") + Double.valueOf(Math.round(amount*1000))/1000 + " " + name);
                    }else{
                        p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-10_4"));
                    }
                }else{
                    p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-10_1"));
                }
            }else{
                p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-10_3"));
            }
            CurrenciesConfig.save();
            PlayersConfig.save();
        } else {
            p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-10_2"));
        }
        return true;
    }


    public static boolean wallet(Player p){
        //display currencies in your wallet
        String user = p.getName();
        if(CurrenciesConfig.get().getKeys(false).size() > 0) {
            p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-10"));
            for (String currencies : CurrenciesConfig.get().getKeys(false)) {
                p.sendMessage(ChatColor.GOLD + "    " + currencies + ": " + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-10_1") + PlayersConfig.get().getDouble(user + ".balance." + currencies) + "\n");
            }
        }else {
            p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-5"));
        }
        return true;
    }
}

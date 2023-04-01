package me.rgn.asceciacurrencies.api;

import me.rgn.asceciacurrencies.AsceciaCurrencies;
import me.rgn.asceciacurrencies.files.CurrenciesConfig;
import me.rgn.asceciacurrencies.files.LanguageConfig;
import me.rgn.asceciacurrencies.files.PlayersConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

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
        if (id.equals(author)) {
            //gives the money contained in the currency and deletes the config keys
            String cname = name;
            final ItemStack iNugget = new ItemStack(Material.IRON_NUGGET, 1);/*nugget tier*/
            final ItemStack gNugget = new ItemStack(Material.GOLD_NUGGET, 1);/*gold nugget tier*/
            final ItemStack iron = new ItemStack(Material.IRON_INGOT, 1);/*iron tier*/
            final ItemStack gold = new ItemStack(Material.GOLD_INGOT, 1);/*gold tier*/
            final ItemStack emerald = new ItemStack(Material.EMERALD, 1);/*emerald tier*/
            final ItemStack diamond = new ItemStack(Material.DIAMOND, 1);/*diamond tier*/
            final ItemStack gBlock = new ItemStack(Material.GOLD_BLOCK, 1);/*gold Block tier*/
            final ItemStack eBlock = new ItemStack(Material.EMERALD_BLOCK, 1);/*gold Block tier*/
            final ItemStack dBlock = new ItemStack(Material.DIAMOND_BLOCK, 1);/*Diamond Block tier*/
            final ItemStack netherite = new ItemStack(Material.NETHERITE_INGOT, 1);/*Netherite tier*/
            final ItemStack nBlock = new ItemStack(Material.NETHERITE_BLOCK, 1);/*Netherite tier*/
            for (String key : PlayersConfig.get().getKeys(false)) {
                double cMarketValue = CurrenciesConfig.get().getDouble(name + ".totalvalue");
                for (double i = 0; i < cMarketValue*9; i++) {
                    double difference = (cMarketValue*9)-i;
                    if (difference < AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.gold_nugget")*9 && difference >= AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.iron_nugget")*9){
                        final Map<Integer, ItemStack> map = p.getInventory().addItem(iNugget);
                        for (final ItemStack item : map.values()) {
                            p.getWorld().dropItemNaturally(p.getLocation(), item);
                        }
                        i -= 1-AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.iron_nugget")*9;
                    }else if (difference < AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.gold_nugget")*9 && difference >= AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.iron_ingot")*9){
                        final Map<Integer, ItemStack> map = p.getInventory().addItem(gNugget);
                        for (final ItemStack item : map.values()) {
                            p.getWorld().dropItemNaturally(p.getLocation(), item);
                        }
                        i -= 1-AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.gold_nugget")*9;
                    }else if (difference >= AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.iron_ingot")*9 && difference < AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.gold_ingot")*9) {
                        final Map<Integer, ItemStack> map = p.getInventory().addItem(iron);
                        for (final ItemStack item : map.values()) {
                            p.getWorld().dropItemNaturally(p.getLocation(), item);
                        }
                        i += AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.iron_ingot")*9-1;
                    } else if (difference >= AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.gold_ingot")*9 & difference < AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.emerald")*9) {
                        final Map<Integer, ItemStack> map = p.getInventory().addItem(gold);
                        for (final ItemStack item : map.values()) {
                            p.getWorld().dropItemNaturally(p.getLocation(), item);
                        }
                        i += AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.gold_ingot")*9-1;
                    }
                    else if (difference >= AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.emerald")*9 & difference < AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.diamond")*9) {
                        final Map<Integer, ItemStack> map = p.getInventory().addItem(emerald);
                        for (final ItemStack item : map.values()) {
                            p.getWorld().dropItemNaturally(p.getLocation(), item);
                        }
                        i += AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.emerald")*9-1;
                    }
                    else if (difference >= AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.diamond")*9 & difference < AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.gold_block")*9) {
                        final Map<Integer, ItemStack> map = p.getInventory().addItem(diamond);
                        for (final ItemStack item : map.values()) {
                            p.getWorld().dropItemNaturally(p.getLocation(), item);
                        }
                        i += AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.diamond")*9-1;
                    }
                    else if (difference >= AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.gold_block")*9 & difference < AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.emerald_block")*9) {
                        final Map<Integer, ItemStack> map = p.getInventory().addItem(gBlock);
                        for (final ItemStack item : map.values()) {
                            p.getWorld().dropItemNaturally(p.getLocation(), item);
                        }
                        i += AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.gold_block")*9-1;
                    }
                    else if (difference >= AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.emerald_block")*9 & difference < AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.netherite_ingot")*9) {
                        final Map<Integer, ItemStack> map = p.getInventory().addItem(eBlock);
                        for (final ItemStack item : map.values()) {
                            p.getWorld().dropItemNaturally(p.getLocation(), item);
                        }
                        i += AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.emerald_block")*9-1;
                    }else if (difference >= AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.netherite_ingot")*9 & difference < AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.diamond_block")*9) {
                        final Map<Integer, ItemStack> map = p.getInventory().addItem(netherite);
                        for (final ItemStack item : map.values()) {
                            p.getWorld().dropItemNaturally(p.getLocation(), item);
                        }
                        i += AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.netherite_ingot")*9-1;
                    }
                    else if (difference >= AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.diamond_block")*9 && difference < AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.netherite_block")*9) {
                        final Map<Integer, ItemStack> map = p.getInventory().addItem(dBlock);
                        for (final ItemStack item : map.values()) {
                            p.getWorld().dropItemNaturally(p.getLocation(), item);
                        }
                        i += AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.diamond_block")*9-2;
                    }else if (difference >= AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.netherite_block")*9) {
                        final Map<Integer, ItemStack> map = p.getInventory().addItem(nBlock);
                        for (final ItemStack item : map.values()) {
                            p.getWorld().dropItemNaturally(p.getLocation(), item);
                        }
                        i += AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.netherite_block")*9-1;
                    }
                }
                PlayersConfig.get().set(key + ".balance." + name, null);
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

    public static boolean forceDelete(String name){
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
            Bukkit.getServer().broadcastMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-1_1"));
        }else{
            Bukkit.getServer().broadcastMessage(ChatColor.RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-10_2"));
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


    public static boolean mint(Player p, double amount){
        //check until the currency of the player is found
        String id = p.getName();
        Boolean hasCreated = PlayersConfig.get().getBoolean(id + ".hascreated");
        if (!hasCreated.equals(null)) {
            if(CurrenciesConfig.get().getKeys(false).size() > 0) {
                for (String currencies : CurrenciesConfig.get().getKeys(false)) {
                    //check if any currency exists
                    //init vars
                    double globalamount = CurrenciesConfig.get().getDouble(currencies + ".amount");
                    double cValue = CurrenciesConfig.get().getDouble(currencies + ".totalvalue");
                    double cEcoActivity = CurrenciesConfig.get().getDouble(currencies + ".economic-activity");
                    double cPower = CurrenciesConfig.get().getDouble(currencies + ".power");
                    String author = CurrenciesConfig.get().getString(currencies + ".author");
                    if (Double.valueOf(Math.round(amount*1000))/1000 >= 1) {
                        if (id.equals(author)) {
                            double pbalance = PlayersConfig.get().getDouble(id + ".balance." + currencies);
                            PlayersConfig.get().set(id + ".balance." + currencies, pbalance + Double.valueOf(Math.round(amount*1000))/1000);
                            globalamount += Double.valueOf(Math.round(amount*1000))/1000;
                            CurrenciesConfig.get().set(currencies + ".amount", globalamount);
                            p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-2") + Double.valueOf(Math.round(amount*1000))/1000 + " " + currencies);
                            if (cEcoActivity > 0.2) {
                                CurrenciesConfig.get().set(currencies + ".economic-activity", cEcoActivity - (5e-7/cPower));
                            }
                            if (cEcoActivity <= 0.2){
                                CurrenciesConfig.get().set(currencies + ".economic-activity", 0.2);
                            }
                            if (cValue > 0){
                                CurrenciesConfig.get().set(currencies + ".power", Double.valueOf(Math.round((cValue/globalamount)*1000))/1000);
                            }else {
                                CurrenciesConfig.get().set(currencies + ".power", 0.0);
                            }
                            CurrenciesConfig.save();
                            PlayersConfig.save();
                        }
                    } else {
                        p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-10_1"));
                    }
                }
            }else {
                p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-7"));
            }
        }else {
            p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-7"));
        }
        return true;
    }


    public static boolean deposit(Player p, double itemamount){
        //init root variable
        String id = p.getName();
        Boolean hasCreated = PlayersConfig.get().getBoolean(id + ".hascreated");
        //search for player's currency
        if (!hasCreated.equals(null)){
            if(CurrenciesConfig.get().getKeys(false).size() > 0) {
                for (String currencies : CurrenciesConfig.get().getKeys(false)) {
                    //init variables
                    double cValue = CurrenciesConfig.get().getDouble(currencies + ".totalvalue");
                    double amount = 0;
                    double cMarketAmount = CurrenciesConfig.get().getDouble(currencies + ".amount");
                    double cEcoActivity = CurrenciesConfig.get().getDouble(currencies + ".economic-activity");
                    String author = CurrenciesConfig.get().getString(currencies + ".author");
                    //check if player has a currency
                    //check if he's the author of the currency being checked
                    if (author.equals(id)) {
                        //check useless to see if there's no currency minted
                        if (cMarketAmount == 0) {
                            p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-8"));
                        }/* Deposit the ores */ else {
                            if (p.getInventory().getItemInMainHand().getType().equals(Material.COAL)) {
                                amount = AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.coal") * itemamount;
                                CurrenciesConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CurrenciesConfig.get().set(currencies + ".power", Double.valueOf(Math.round(((cValue + amount) / cMarketAmount)*1000))/1000);;
                                p.getInventory().setItemInMainHand(null);
                                p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3") + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                            }else if (p.getInventory().getItemInMainHand().getType().equals(Material.COAL_BLOCK)) {
                                amount = AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.coal_block") * itemamount;
                                CurrenciesConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CurrenciesConfig.get().set(currencies + ".power", Double.valueOf(Math.round(((cValue + amount) / cMarketAmount)*1000))/1000);;
                                p.getInventory().setItemInMainHand(null);
                                p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3") + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                            } else if (p.getInventory().getItemInMainHand().getType().equals(Material.IRON_INGOT)) {
                                amount = AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.iron_ingot") * itemamount;
                                CurrenciesConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CurrenciesConfig.get().set(currencies + ".power", Double.valueOf(Math.round(((cValue + amount) / cMarketAmount)*1000))/1000);;
                                p.getInventory().setItemInMainHand(null);
                                p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3") + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                            } else if (p.getInventory().getItemInMainHand().getType().equals(Material.IRON_BLOCK)) {
                                amount = AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.iron_block") * itemamount;
                                CurrenciesConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CurrenciesConfig.get().set(currencies + ".power", Double.valueOf(Math.round(((cValue + amount) / cMarketAmount)*1000))/1000);;
                                p.getInventory().setItemInMainHand(null);
                                p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3") + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                            } else if (p.getInventory().getItemInMainHand().getType().equals(Material.IRON_NUGGET)) {
                                amount = AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.iron_nugget") * itemamount;
                                CurrenciesConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CurrenciesConfig.get().set(currencies + ".power", Double.valueOf(Math.round(((cValue + amount) / cMarketAmount)*1000))/1000);;
                                p.getInventory().setItemInMainHand(null);
                                p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3") + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                            } else if (p.getInventory().getItemInMainHand().getType().equals(Material.GOLD_INGOT)) {
                                amount = AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.gold_ingot") * itemamount;
                                CurrenciesConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CurrenciesConfig.get().set(currencies + ".power", Double.valueOf(Math.round(((cValue + amount) / cMarketAmount)*1000))/1000);;
                                p.getInventory().setItemInMainHand(null);
                                p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3") + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                            } else if (p.getInventory().getItemInMainHand().getType().equals(Material.GOLD_BLOCK)) {
                                amount = AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.gold_block") * itemamount;
                                CurrenciesConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CurrenciesConfig.get().set(currencies + ".power", Double.valueOf(Math.round(((cValue + amount) / cMarketAmount)*1000))/1000);;
                                p.getInventory().setItemInMainHand(null);
                                p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3") + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                            } else if (p.getInventory().getItemInMainHand().getType().equals(Material.GOLD_NUGGET)) {
                                amount = AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.gold_nugget") * itemamount;
                                CurrenciesConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CurrenciesConfig.get().set(currencies + ".power", Double.valueOf(Math.round(((cValue + amount) / cMarketAmount)*1000))/1000);;
                                p.getInventory().setItemInMainHand(null);
                                p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3") + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                            } else if (p.getInventory().getItemInMainHand().getType().equals(Material.DIAMOND)) {
                                amount = AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.diamond") * itemamount;
                                CurrenciesConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CurrenciesConfig.get().set(currencies + ".power", Double.valueOf(Math.round(((cValue + amount) / cMarketAmount)*1000))/1000);;
                                p.getInventory().setItemInMainHand(null);
                                p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3") + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                            } else if (p.getInventory().getItemInMainHand().getType().equals(Material.DIAMOND_BLOCK)) {
                                amount = AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.diamond_block") * itemamount;
                                CurrenciesConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CurrenciesConfig.get().set(currencies + ".power", Double.valueOf(Math.round(((cValue + amount) / cMarketAmount)*1000))/1000);;
                                p.getInventory().setItemInMainHand(null);
                                p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3") + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                            } else if (p.getInventory().getItemInMainHand().getType().equals(Material.NETHERITE_INGOT)) {
                                amount = AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.netherite_ingot") * itemamount;
                                CurrenciesConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CurrenciesConfig.get().set(currencies + ".power", Double.valueOf(Math.round(((cValue + amount) / cMarketAmount)*1000))/1000);;
                                p.getInventory().setItemInMainHand(null);
                                p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3") + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                            } else if (p.getInventory().getItemInMainHand().getType().equals(Material.NETHERITE_BLOCK)) {
                                amount = AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.netherite_block") * itemamount;
                                CurrenciesConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CurrenciesConfig.get().set(currencies + ".power", Double.valueOf(Math.round(((cValue + amount) / cMarketAmount)*1000))/1000);;
                                p.getInventory().setItemInMainHand(null);
                                p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3") + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                            }
                            else if (p.getInventory().getItemInMainHand().getType().equals(Material.NETHERITE_SCRAP)) {
                                amount = AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.netherite_scrap") * itemamount;
                                CurrenciesConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CurrenciesConfig.get().set(currencies + ".power", Double.valueOf(Math.round(((cValue + amount) / cMarketAmount)*1000))/1000);;
                                p.getInventory().setItemInMainHand(null);
                                p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3") + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                            } else if (p.getInventory().getItemInMainHand().getType().equals(Material.REDSTONE)) {
                                amount = AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.redstone") * itemamount;
                                CurrenciesConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CurrenciesConfig.get().set(currencies + ".power", Double.valueOf(Math.round(((cValue + amount) / cMarketAmount)*1000))/1000);;
                                p.getInventory().setItemInMainHand(null);
                                p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3")  + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                            } else if (p.getInventory().getItemInMainHand().getType().equals(Material.REDSTONE_BLOCK)) {
                                amount = AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.redstone_block") * itemamount;
                                CurrenciesConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CurrenciesConfig.get().set(currencies + ".power", Double.valueOf(Math.round(((cValue + amount) / cMarketAmount)*1000))/1000);
                                p.getInventory().setItemInMainHand(null);
                                p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3")  + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                            } else if (p.getInventory().getItemInMainHand().getType().equals(Material.COPPER_INGOT)) {
                                amount = AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.cooper_ingot") * itemamount;
                                CurrenciesConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CurrenciesConfig.get().set(currencies + ".power", Double.valueOf(Math.round(((cValue + amount) / cMarketAmount)*1000))/1000);
                                p.getInventory().setItemInMainHand(null);
                                p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3")  + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                            } else if (p.getInventory().getItemInMainHand().getType().equals(Material.COPPER_BLOCK)) {
                                amount = AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.cooper_block") * itemamount;
                                CurrenciesConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CurrenciesConfig.get().set(currencies + ".power", Double.valueOf(Math.round(((cValue + amount) / cMarketAmount)*1000))/1000);
                                p.getInventory().setItemInMainHand(null);
                                p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3")  + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                            } else if (p.getInventory().getItemInMainHand().getType().equals(Material.EMERALD)) {
                                amount = AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.emerald") * itemamount;
                                CurrenciesConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CurrenciesConfig.get().set(currencies + ".power", Double.valueOf(Math.round(((cValue + amount) / cMarketAmount)*1000))/1000);
                                p.getInventory().setItemInMainHand(null);
                                p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3")  + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                            } else if (p.getInventory().getItemInMainHand().getType().equals(Material.EMERALD_BLOCK)) {
                                amount = AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.emerald_block") * itemamount;
                                CurrenciesConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CurrenciesConfig.get().set(currencies + ".power", Double.valueOf(Math.round(((cValue + amount) / cMarketAmount)*1000))/1000);
                                p.getInventory().setItemInMainHand(null);
                                p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3")  + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                            }else {
                                p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-8_1"));
                            }
                        }
                    }
                }
                CurrenciesConfig.save();
            }else {
                p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-7"));
            }
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
                            CurrenciesConfig.get().set(name + ".economic-activity", cEcoActivity + (1e-5*nPeers));
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
    public static boolean reloadConfig(){
        CurrenciesConfig.reload();
        AsceciaCurrencies.plugin.reloadConfig();
        PlayersConfig.reload();
        LanguageConfig.reload();
        Bukkit.getServer().broadcastMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-12"));
        return true;
    }


    public static boolean rename (Player p, String newName){
        // init vars
        String id = p.getName();
        int count = 0;
        boolean isNameValid = false;
        //checking if sender created a currency
        if (PlayersConfig.get().contains(id + ".hascreated")){
            //checking every currency
            for (String currencies: CurrenciesConfig.get().getKeys(false)){
                double pBalance = PlayersConfig.get().getDouble(id + "." + currencies + ".balance");
                double cMarketAmount = CurrenciesConfig.get().getDouble(currencies + ".amount");
                double cValue = CurrenciesConfig.get().getDouble(currencies + ".totalvalue");
                double cEcoActivity = CurrenciesConfig.get().getDouble(currencies + ".economic-activity");
                double cPower = CurrenciesConfig.get().getDouble(currencies + ".power");
                int nPeers = CurrenciesConfig.get().getInt(currencies + ".peers");
                String description = CurrenciesConfig.get().getString(currencies + ".description");
                String author = CurrenciesConfig.get().getString(currencies + ".author");
                //checking if sender is the author of the currency
                if (id.equals(author)){
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
                }
            }
        }else {
            p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-12"));
        }
        return true;
    }

    public static boolean withdraw(Player p, String name, double amount){
        //init vars and config keys
        double cPower = CurrenciesConfig.get().getDouble(name + ".power");
        double cValue = CurrenciesConfig.get().getDouble(name + ".totalvalue");
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
                        final ItemStack iNugget = new ItemStack(Material.IRON_NUGGET, 1);/*nugget tier*/
                        final ItemStack gNugget = new ItemStack(Material.GOLD_NUGGET, 1);/*gold nugget tier*/
                        final ItemStack iron = new ItemStack(Material.IRON_INGOT, 1);/*iron tier*/
                        final ItemStack gold = new ItemStack(Material.GOLD_INGOT, 1);/*gold tier*/
                        final ItemStack emerald = new ItemStack(Material.EMERALD, 1);/*emerald tier*/
                        final ItemStack diamond = new ItemStack(Material.DIAMOND, 1);/*diamond tier*/
                        final ItemStack gBlock = new ItemStack(Material.GOLD_BLOCK, 1);/*gold Block tier*/
                        final ItemStack eBlock = new ItemStack(Material.EMERALD_BLOCK, 1);/*gold Block tier*/
                        final ItemStack dBlock = new ItemStack(Material.DIAMOND_BLOCK, 1);/*Diamond Block tier*/
                        final ItemStack netherite = new ItemStack(Material.NETHERITE_INGOT, 1);/*Netherite tier*/
                        final ItemStack nBlock = new ItemStack(Material.NETHERITE_BLOCK, 1);/*Netherite tier*/
                        for (int i = 0; i < (cValue/cMarketAmount*amount)*9; i++) {
                            double difference = (cValue/cMarketAmount*amount)*9-i;
                            if (difference < AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.gold_nugget")*9 && difference >= AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.iron_nugget")*9){
                                final Map<Integer, ItemStack> map = p.getInventory().addItem(iNugget);
                                for (final ItemStack item : map.values()) {
                                    p.getWorld().dropItemNaturally(p.getLocation(), item);
                                }
                                i -= 1-AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.gold_nugget")*9;
                            }else if (difference < AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.gold_nugget")*9 && difference >= AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.iron_ingot")*9){
                                final Map<Integer, ItemStack> map = p.getInventory().addItem(gNugget);
                                for (final ItemStack item : map.values()) {
                                    p.getWorld().dropItemNaturally(p.getLocation(), item);
                                }
                                i -= 1-AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.gold_nugget")*9;
                            }else if (difference >= AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.iron_ingot")*9 && difference < AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.gold_ingot")*9) {
                                final Map<Integer, ItemStack> map = p.getInventory().addItem(iron);
                                for (final ItemStack item : map.values()) {
                                    p.getWorld().dropItemNaturally(p.getLocation(), item);
                                }
                                i += AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.iron_ingot")*9-1;
                            } else if (difference >= AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.gold_ingot")*9 & difference < AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.emerald")*9) {
                                final Map<Integer, ItemStack> map = p.getInventory().addItem(gold);
                                for (final ItemStack item : map.values()) {
                                    p.getWorld().dropItemNaturally(p.getLocation(), item);
                                }
                                i += AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.gold_ingot")*9-1;
                            }
                            else if (difference >= AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.emerald")*9 & difference < AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.diamond")*9) {
                                final Map<Integer, ItemStack> map = p.getInventory().addItem(emerald);
                                for (final ItemStack item : map.values()) {
                                    p.getWorld().dropItemNaturally(p.getLocation(), item);
                                }
                                i += AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.emerald")*9-1;
                            }
                            else if (difference >= AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.diamond")*9 & difference < AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.gold_block")*9) {
                                final Map<Integer, ItemStack> map = p.getInventory().addItem(diamond);
                                for (final ItemStack item : map.values()) {
                                    p.getWorld().dropItemNaturally(p.getLocation(), item);
                                }
                                i += AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.diamond")*9-1;
                            }
                            else if (difference >= AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.gold_block")*9 & difference < AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.emerald_block")*9) {
                                final Map<Integer, ItemStack> map = p.getInventory().addItem(gBlock);
                                for (final ItemStack item : map.values()) {
                                    p.getWorld().dropItemNaturally(p.getLocation(), item);
                                }
                                i += AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.gold_block")*9-1;
                            }
                            else if (difference >= AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.emerald_block")*9 & difference < AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.netherite_ingot")*9) {
                                final Map<Integer, ItemStack> map = p.getInventory().addItem(eBlock);
                                for (final ItemStack item : map.values()) {
                                    p.getWorld().dropItemNaturally(p.getLocation(), item);
                                }
                                i += AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.emerald_block")*9-1;
                            }else if (difference >= AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.netherite_ingot")*9 & difference < AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.diamond_block")*9) {
                                final Map<Integer, ItemStack> map = p.getInventory().addItem(netherite);
                                for (final ItemStack item : map.values()) {
                                    p.getWorld().dropItemNaturally(p.getLocation(), item);
                                }
                                i += AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.netherite_ingot")*9-1;
                            }
                            else if (difference >= AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.diamond_block")*9 && difference < AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.netherite_block")*9) {
                                final Map<Integer, ItemStack> map = p.getInventory().addItem(dBlock);
                                for (final ItemStack item : map.values()) {
                                    p.getWorld().dropItemNaturally(p.getLocation(), item);
                                }
                                i += AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.diamond_block")*9-1;
                            }else if (difference >= AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.netherite_block")*9) {
                                final Map<Integer, ItemStack> map = p.getInventory().addItem(nBlock);
                                for (final ItemStack item : map.values()) {
                                    p.getWorld().dropItemNaturally(p.getLocation(), item);
                                }
                                i += AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices.netherite_block")*9-1;
                            }
                        }
                        CurrenciesConfig.get().set(name + ".amount", cMarketAmount - Double.valueOf(Math.round(amount*1000))/1000);
                        CurrenciesConfig.get().set(name + ".totalvalue", cValue - (cPower * (Double.valueOf(Math.round(amount*1000))/1000)));
                        //if the eco activity is superior to 0.2
                        if(cEcoActivity > 0.2) {
                            CurrenciesConfig.get().set(name + ".economic-activity", cEcoActivity - (5e-7/cPower));
                        }
                        CurrenciesConfig.get().set(name + ".power", Double.valueOf(Math.round((cValue-(cPower*Double.valueOf(Math.round(amount*1000))/1000))/(cMarketAmount-Double.valueOf(Math.round(amount*1000))/1000)*1000))/1000);
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

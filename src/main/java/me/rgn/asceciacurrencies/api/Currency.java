package me.rgn.asceciacurrencies.api;

import me.rgn.asceciacurrencies.files.CurrenciesConfig;
import me.rgn.asceciacurrencies.files.LanguageConfig;
import me.rgn.asceciacurrencies.files.PlayersConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class Currency {
    public static boolean isCurrencyCreated;
    public static boolean add(Player p, String name, double amount){
        if(p != null){
            if (CurrenciesConfig.get().contains(name)){
                if (amount > 0) {
                    String id = p.getName();
                    double pBalance = PlayersConfig.get().getDouble(id + "." + name + ".balance");
                    double cMarketAmount = CurrenciesConfig.get().getDouble(name + ".amount");
                    double cValue = CurrenciesConfig.get().getDouble(name + ".totalvalue");
                    double cEcoActivity = CurrenciesConfig.get().getDouble(name + ".economic-activity");
                    double cPower = CurrenciesConfig.get().getDouble(name + ".power");
                    PlayersConfig.get().set(id + name + ".balance", pBalance + amount);
                    CurrenciesConfig.get().set(name + ".amount", cMarketAmount + amount);
                    CurrenciesConfig.get().set(name + ".power", ((cValue + (cPower * amount)) / (cMarketAmount + amount)) * cEcoActivity);
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
                        PlayersConfig.get().set(id + "." + name + "balance", 1.0);
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
            final ItemStack dBlock = new ItemStack(Material.DIAMOND_BLOCK, 1);/*gold Block tier*/
            for (String key : PlayersConfig.get().getKeys(false)) {
                double cMarketValue = CurrenciesConfig.get().getDouble(name + ".totalvalue");
                for (double i = 0; i < cMarketValue; i++) {
                    double difference = cMarketValue-i;
                    if (difference < 0.555 && difference >= 0.111){
                        final Map<Integer, ItemStack> map = p.getInventory().addItem(iNugget);
                        for (final ItemStack item : map.values()) {
                            p.getWorld().dropItemNaturally(p.getLocation(), item);
                        }
                        i -= 0.9;
                    }else if (difference < 1 && difference >= 0.555){
                        final Map<Integer, ItemStack> map = p.getInventory().addItem(gNugget);
                        for (final ItemStack item : map.values()) {
                            p.getWorld().dropItemNaturally(p.getLocation(), item);
                        }
                        i -= 0.5;
                    }else if (difference >= 1 && difference < 5) {
                        final Map<Integer, ItemStack> map = p.getInventory().addItem(iron);
                        for (final ItemStack item : map.values()) {
                            p.getWorld().dropItemNaturally(p.getLocation(), item);
                        }
                    } else if (difference >= 5 & difference < 9) {
                        final Map<Integer, ItemStack> map = p.getInventory().addItem(gold);
                        for (final ItemStack item : map.values()) {
                            p.getWorld().dropItemNaturally(p.getLocation(), item);
                        }
                        i += 4;
                    }
                    else if (difference >= 9 & difference < 20) {
                        final Map<Integer, ItemStack> map = p.getInventory().addItem(emerald);
                        for (final ItemStack item : map.values()) {
                            p.getWorld().dropItemNaturally(p.getLocation(), item);
                        }
                        i += 8;
                    }
                    else if (difference >= 20 & difference < 45) {
                        final Map<Integer, ItemStack> map = p.getInventory().addItem(diamond);
                        for (final ItemStack item : map.values()) {
                            p.getWorld().dropItemNaturally(p.getLocation(), item);
                        }
                        i += 19;
                    }
                    else if (difference >= 45 & difference < 81) {
                        final Map<Integer, ItemStack> map = p.getInventory().addItem(gBlock);
                        for (final ItemStack item : map.values()) {
                            p.getWorld().dropItemNaturally(p.getLocation(), item);
                        }
                        i += 44;
                    }
                    else if (difference >= 81 & difference < 180) {
                        final Map<Integer, ItemStack> map = p.getInventory().addItem(eBlock);
                        for (final ItemStack item : map.values()) {
                            p.getWorld().dropItemNaturally(p.getLocation(), item);
                        }
                        i += 80;
                    }
                    else if (difference >= 180) {
                        final Map<Integer, ItemStack> map = p.getInventory().addItem(dBlock);
                        for (final ItemStack item : map.values()) {
                            p.getWorld().dropItemNaturally(p.getLocation(), item);
                        }
                        i += 179;
                    }
                }
                PlayersConfig.get().set(key + "." + cname + "balance", null);
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
            PlayersConfig.get().set(key + "." + cname + ".", null);
        }
        if (CurrenciesConfig.get().contains(name)){
            PlayersConfig.get().set(id + ".hascreated", null);
            CurrenciesConfig.get().set(cname + "balance", null);
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
                    if (amount >= 1) {
                        if (id.equals(author)) {
                            double pbalance = PlayersConfig.get().getDouble(id + "." + currencies + "balance");
                            PlayersConfig.get().set(id + "." + currencies + "balance", pbalance + amount);
                            globalamount += amount;
                            CurrenciesConfig.get().set(currencies + ".amount", globalamount);
                            p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-2") + amount + " " + currencies);
                            if (cEcoActivity > 0.2) {
                                CurrenciesConfig.get().set(currencies + ".economic-activity", cEcoActivity - (5e-7/cPower));
                            }
                            if (cEcoActivity <= 0.2){
                                CurrenciesConfig.get().set(currencies + ".economic-activity", 0.2);
                            }
                            if (cValue > 0){
                                CurrenciesConfig.get().set(currencies + ".power", (cValue / (globalamount+amount)) * cEcoActivity);

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
                                amount = 0.5 * itemamount;
                                CurrenciesConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CurrenciesConfig.get().set(currencies + ".power", ((cValue + amount) / cMarketAmount) * cEcoActivity);
                                p.getInventory().setItemInMainHand(null);
                                p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3") + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                            }else if (p.getInventory().getItemInMainHand().getType().equals(Material.COAL_BLOCK)) {
                                amount = 4.5 * itemamount;
                                CurrenciesConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CurrenciesConfig.get().set(currencies + ".power", ((cValue + amount) / cMarketAmount) * cEcoActivity);
                                p.getInventory().setItemInMainHand(null);
                                p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3") + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                            } else if (p.getInventory().getItemInMainHand().getType().equals(Material.IRON_INGOT)) {
                                amount = itemamount;
                                CurrenciesConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CurrenciesConfig.get().set(currencies + ".power", ((cValue + amount) / cMarketAmount) * cEcoActivity);
                                p.getInventory().setItemInMainHand(null);
                                p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3") + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                            } else if (p.getInventory().getItemInMainHand().getType().equals(Material.IRON_BLOCK)) {
                                amount = 9 * itemamount;
                                CurrenciesConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CurrenciesConfig.get().set(currencies + ".power", ((cValue + amount) / cMarketAmount) * cEcoActivity);
                                p.getInventory().setItemInMainHand(null);
                                p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3") + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                            } else if (p.getInventory().getItemInMainHand().getType().equals(Material.IRON_NUGGET)) {
                                amount = 0.11111111111111112 * itemamount;
                                CurrenciesConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CurrenciesConfig.get().set(currencies + ".power", ((cValue + amount) / cMarketAmount) * cEcoActivity);
                                p.getInventory().setItemInMainHand(null);
                                p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3") + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                            } else if (p.getInventory().getItemInMainHand().getType().equals(Material.GOLD_INGOT)) {
                                amount = 5 * itemamount;
                                CurrenciesConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CurrenciesConfig.get().set(currencies + ".power", ((cValue + amount) / cMarketAmount) * cEcoActivity);
                                p.getInventory().setItemInMainHand(null);
                                p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3") + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                            } else if (p.getInventory().getItemInMainHand().getType().equals(Material.GOLD_BLOCK)) {
                                amount = 45 * itemamount;
                                CurrenciesConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CurrenciesConfig.get().set(currencies + ".power", ((cValue + amount) / cMarketAmount) * cEcoActivity);
                                p.getInventory().setItemInMainHand(null);
                                p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3") + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                            } else if (p.getInventory().getItemInMainHand().getType().equals(Material.GOLD_NUGGET)) {
                                amount = 0.55555555555555556 * itemamount;
                                CurrenciesConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CurrenciesConfig.get().set(currencies + ".power", ((cValue + amount) / cMarketAmount) * cEcoActivity);
                                p.getInventory().setItemInMainHand(null);
                                p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3") + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                            } else if (p.getInventory().getItemInMainHand().getType().equals(Material.DIAMOND)) {
                                amount = 20 * itemamount;
                                CurrenciesConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CurrenciesConfig.get().set(currencies + ".power", ((cValue + amount) / cMarketAmount) * cEcoActivity);
                                p.getInventory().setItemInMainHand(null);
                                p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3") + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                            } else if (p.getInventory().getItemInMainHand().getType().equals(Material.DIAMOND_BLOCK)) {
                                amount = 180 * itemamount;
                                CurrenciesConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CurrenciesConfig.get().set(currencies + ".power", ((cValue + amount) / cMarketAmount) * cEcoActivity);
                                p.getInventory().setItemInMainHand(null);
                                p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3") + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                            } else if (p.getInventory().getItemInMainHand().getType().equals(Material.NETHERITE_INGOT)) {
                                amount = 40 * itemamount;
                                CurrenciesConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CurrenciesConfig.get().set(currencies + ".power", ((cValue + amount) / cMarketAmount) * cEcoActivity);
                                p.getInventory().setItemInMainHand(null);
                                p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3") + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                            } else if (p.getInventory().getItemInMainHand().getType().equals(Material.NETHERITE_SCRAP)) {
                                amount = 10 * itemamount;
                                CurrenciesConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CurrenciesConfig.get().set(currencies + ".power", ((cValue + amount) / cMarketAmount) * cEcoActivity);
                                p.getInventory().setItemInMainHand(null);
                                p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3") + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                            } else if (p.getInventory().getItemInMainHand().getType().equals(Material.REDSTONE)) {
                                amount = 1 * itemamount;
                                CurrenciesConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CurrenciesConfig.get().set(currencies + ".power", ((cValue + amount) / cMarketAmount) * cEcoActivity);
                                p.getInventory().setItemInMainHand(null);
                                p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3")  + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                            } else if (p.getInventory().getItemInMainHand().getType().equals(Material.REDSTONE_BLOCK)) {
                                amount = 9 * itemamount;
                                CurrenciesConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CurrenciesConfig.get().set(currencies + ".power", ((cValue + amount) / cMarketAmount) * cEcoActivity);
                                p.getInventory().setItemInMainHand(null);
                                p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3")  + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                            } else if (p.getInventory().getItemInMainHand().getType().equals(Material.COPPER_INGOT)) {
                                amount = 1 * itemamount;
                                CurrenciesConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CurrenciesConfig.get().set(currencies + ".power", ((cValue + amount) / cMarketAmount) * cEcoActivity);
                                p.getInventory().setItemInMainHand(null);
                                p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3")  + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                            } else if (p.getInventory().getItemInMainHand().getType().equals(Material.COPPER_BLOCK)) {
                                amount = 9 * itemamount;
                                CurrenciesConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CurrenciesConfig.get().set(currencies + ".power", ((cValue + amount) / cMarketAmount) * cEcoActivity);
                                p.getInventory().setItemInMainHand(null);
                                p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3")  + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                            } else if (p.getInventory().getItemInMainHand().getType().equals(Material.EMERALD)) {
                                amount = 9 * itemamount;
                                CurrenciesConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CurrenciesConfig.get().set(currencies + ".power", ((cValue + amount) / cMarketAmount) * cEcoActivity);
                                p.getInventory().setItemInMainHand(null);
                                p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3")  + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                            } else if (p.getInventory().getItemInMainHand().getType().equals(Material.EMERALD_BLOCK)) {
                                amount = 81 * itemamount;
                                CurrenciesConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CurrenciesConfig.get().set(currencies + ".power", ((cValue + amount) / cMarketAmount) * cEcoActivity);
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
                    if (!PlayersConfig.get().contains(playeridd + "." + name + "balance")) {
                        PlayersConfig.get().addDefault(playeridd + "." + name + "balance", 0.0);
                    }
                    double pbalance = PlayersConfig.get().getDouble(playeridd + "." + name + "balance");
                    if (!PlayersConfig.get().contains(targetidd + "." + name + "balance")) {
                        PlayersConfig.get().addDefault(targetidd + "." + name + "balance", 0.0);
                        CurrenciesConfig.get().set(name + ".peers", nPeers + 1);
                    }
                    double tbalance = PlayersConfig.get().getDouble(targetidd + "." + name + "balance");
                    //if amount not too low
                    if (pbalance >= amount) {
                        if (amount >= 0.01) {
                            //pay
                            nPeers = CurrenciesConfig.get().getInt(name + ".peers");
                            PlayersConfig.get().set(targetidd + "." + name + "balance", tbalance + amount);
                            PlayersConfig.get().set(playeridd + "." + name + "balance", pbalance - amount);
                            CurrenciesConfig.get().set(name + ".economic-activity", cEcoActivity + (1e-5*nPeers));
                            CurrenciesConfig.get().set(name + ".power", ((cValue - ((cValue / cMarketAmount) * amount)) / (cMarketAmount - amount)) * cEcoActivity);
                            p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-4") + amount + " " + name + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-4_2") + tName);
                            target.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-4_1")+ amount + " " + name + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-4_3") + pName);
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
                    double pBalance = PlayersConfig.get().getDouble(id + "." + name + ".balance");
                    double cMarketAmount = CurrenciesConfig.get().getDouble(name + ".amount");
                    double cValue = CurrenciesConfig.get().getDouble(name + ".totalvalue");
                    double cEcoActivity = CurrenciesConfig.get().getDouble(name + ".economic-activity");
                    double cPower = CurrenciesConfig.get().getDouble(name + ".power");
                    PlayersConfig.get().set(id + name + ".balance", pBalance - amount);
                    CurrenciesConfig.get().set(name + ".amount", cMarketAmount - amount);
                    CurrenciesConfig.get().set(name + ".power", ((cValue - (cPower * amount)) / (cMarketAmount - amount)) * cEcoActivity);
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

    public static boolean withdraw(Player p, String name, double amount){
        //init vars and config keys
        double cPower = CurrenciesConfig.get().getDouble(name + ".power");
        double cValue = CurrenciesConfig.get().getDouble(name + ".totalvalue");
        double cMarketAmount = CurrenciesConfig.get().getDouble(name + ".amount");
        double cEcoActivity = CurrenciesConfig.get().getDouble(name + ".economic-activity");
        String id = p.getName();
        String pname = p.getName().toString();
        //if currency exists
        if (CurrenciesConfig.get().contains(name)) {
            double pBalance = PlayersConfig.get().getDouble(id + "." + name + "balance");
            //if you're not a rat
            if (pBalance >= amount) {
                if (amount >= 1){
                    if (amount < cMarketAmount){
                        final ItemStack iNugget = new ItemStack(Material.IRON_NUGGET, 1);/*nugget tier*/
                        final ItemStack gNugget = new ItemStack(Material.GOLD_NUGGET, 1);/*gold nugget tier*/
                        final ItemStack iron = new ItemStack(Material.IRON_INGOT, 1);/*iron tier*/
                        final ItemStack gold = new ItemStack(Material.GOLD_INGOT, 1);/*gold tier*/
                        final ItemStack emerald = new ItemStack(Material.EMERALD, 1);/*emerald tier*/
                        final ItemStack diamond = new ItemStack(Material.DIAMOND, 1);/*diamond tier*/
                        final ItemStack gBlock = new ItemStack(Material.GOLD_BLOCK, 1);/*gold Block tier*/
                        final ItemStack eBlock = new ItemStack(Material.EMERALD_BLOCK, 1);/*gold Block tier*/
                        final ItemStack dBlock = new ItemStack(Material.DIAMOND_BLOCK, 1);/*gold Block tier*/
                        for (double i = 0; i < (cPower * amount); i++) {
                            double difference = (cPower * amount)-i;
                            if (difference < 0.5 && difference >= 0.1){
                                final Map<Integer, ItemStack> map = p.getInventory().addItem(iNugget);
                                for (final ItemStack item : map.values()) {
                                    p.getWorld().dropItemNaturally(p.getLocation(), item);
                                }
                                i -= 0.9;
                            }else if (difference < 1 && difference >= 0.5){
                                final Map<Integer, ItemStack> map = p.getInventory().addItem(gNugget);
                                for (final ItemStack item : map.values()) {
                                    p.getWorld().dropItemNaturally(p.getLocation(), item);
                                }
                                i -= 0.5;
                            }else if (difference >= 1 && difference < 5) {
                                final Map<Integer, ItemStack> map = p.getInventory().addItem(iron);
                                for (final ItemStack item : map.values()) {
                                    p.getWorld().dropItemNaturally(p.getLocation(), item);
                                }
                            } else if (difference >= 5 & difference < 9) {
                                final Map<Integer, ItemStack> map = p.getInventory().addItem(gold);
                                for (final ItemStack item : map.values()) {
                                    p.getWorld().dropItemNaturally(p.getLocation(), item);
                                }
                                i += 4;
                            }
                            else if (difference >= 9 & difference < 20) {
                                final Map<Integer, ItemStack> map = p.getInventory().addItem(emerald);
                                for (final ItemStack item : map.values()) {
                                    p.getWorld().dropItemNaturally(p.getLocation(), item);
                                }
                                i += 8;
                            }
                            else if (difference >= 20 & difference < 45) {
                                final Map<Integer, ItemStack> map = p.getInventory().addItem(diamond);
                                for (final ItemStack item : map.values()) {
                                    p.getWorld().dropItemNaturally(p.getLocation(), item);
                                }
                                i += 19;
                            }
                            else if (difference >= 45 & difference < 81) {
                                final Map<Integer, ItemStack> map = p.getInventory().addItem(gBlock);
                                for (final ItemStack item : map.values()) {
                                    p.getWorld().dropItemNaturally(p.getLocation(), item);
                                }
                                i += 44;
                            }
                            else if (difference >= 81 & difference < 180) {
                                final Map<Integer, ItemStack> map = p.getInventory().addItem(eBlock);
                                for (final ItemStack item : map.values()) {
                                    p.getWorld().dropItemNaturally(p.getLocation(), item);
                                }
                                i += 80;
                            }
                            else if (difference >= 180) {
                                final Map<Integer, ItemStack> map = p.getInventory().addItem(dBlock);
                                for (final ItemStack item : map.values()) {
                                    p.getWorld().dropItemNaturally(p.getLocation(), item);
                                }
                                i += 179;
                            }
                        }
                        CurrenciesConfig.get().set(name + ".amount", cMarketAmount - amount);
                        CurrenciesConfig.get().set(name + ".totalvalue", cValue - cPower * amount);
                        //if the eco activity is superior to 0.5
                        if(cEcoActivity > 0.2) {
                            CurrenciesConfig.get().set(name + ".economic-activity", cEcoActivity - (5e-7/cPower));
                        }
                        CurrenciesConfig.get().set(name + ".power", ((cValue - (cPower*amount)) / (cMarketAmount - amount))*cEcoActivity);
                        PlayersConfig.get().set(id + "." + name + "balance", pBalance - amount);
                        p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-5") + amount + " " + name);
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
                p.sendMessage(ChatColor.GOLD + currencies + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-10_1") + PlayersConfig.get().getDouble(user + "." + currencies + "balance") + "\n");
            }
        }else {
            p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-5"));
        }
        return true;
    }
}

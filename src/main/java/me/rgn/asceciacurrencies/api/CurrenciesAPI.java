package me.rgn.asceciacurrencies.api;

import jdk.tools.jlink.plugin.Plugin;
import me.rgn.asceciacurrencies.AsceciaCurrencies;
import me.rgn.asceciacurrencies.Currency;
import me.rgn.asceciacurrencies.files.CustomConfig;
import me.rgn.asceciacurrencies.files.LanguageConfig;
import me.rgn.asceciacurrencies.files.PlayersConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.UUID;

public class CurrenciesAPI{
    public static class CurrenciesMethods{
        public static Object get(String name, String path){
            return CustomConfig.get().get(name + "." + path);
        }
        public static void set(String name, String path, Object value){
            CustomConfig.get().set(name + "." + path, value);
        }
        public static boolean add(Player p, String name, double amount){
            if(p != null){
                if (CustomConfig.get().contains(name)){
                    if (amount > 0) {
                        String id = p.getName();
                        double pBalance = PlayersConfig.get().getDouble(id + "." + name + ".balance");
                        double cMarketAmount = CustomConfig.get().getDouble(name + ".amount");
                        double cValue = CustomConfig.get().getDouble(name + ".totalvalue");
                        double cEcoActivity = CustomConfig.get().getDouble(name + ".economic-activity");
                        double cPower = CustomConfig.get().getDouble(name + ".power");
                        PlayersConfig.get().set(id + name + ".balance", pBalance + amount);
                        CustomConfig.get().set(name + ".amount", cMarketAmount + amount);
                        CustomConfig.get().set(name + ".power", ((cValue + (cPower * amount)) / (cMarketAmount + amount)) * cEcoActivity);
                        CustomConfig.save();
                        CustomConfig.reload();
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
                    if (!CustomConfig.get().contains(name)) {
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
                                CustomConfig.get().set(name + ".power", 0.0);
                                CustomConfig.get().set(name + ".amount", 1.0);
                                CustomConfig.get().set(name + ".totalvalue", 0.0);
                                CustomConfig.get().set(name + ".economic-activity", 1.0);
                                CustomConfig.get().set(name + ".description", "");
                                CustomConfig.get().set(name + ".peers", 1);
                                CustomConfig.get().set(name + ".author", id);
                                p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-0") + name + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-0_1"));
                                Currency.isCurrencyCreated = true;
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
            CustomConfig.save();
            CustomConfig.reload();
            return true;
        }

        public static boolean delete(Player p, String name){
            //gets the sender and the author of the currency
            String id = p.getName();
            String author = CustomConfig.get().getString(name + ".author");
            //check if sender is the author
            if (id.equals(author)) {
                //gives the money contained in the currency and deletes the config keys
                String cname = name;
                ItemStack nuggets = new ItemStack(Material.IRON_NUGGET, 1);
                for (String key : PlayersConfig.get().getKeys(false)) {
                    double cMarketValue = CustomConfig.get().getDouble(name + ".totalvalue");
                    for (int i = 0; i < cMarketValue; i++) {
                        p.getInventory().addItem(nuggets);
                    }
                    PlayersConfig.get().set(key + "." + cname + "balance", null);
                    PlayersConfig.save();
                }
                PlayersConfig.get().set(id + ".hascreated", null);
                Currency.isCurrencyCreated = false;
                CustomConfig.get().set(cname, null);
                CustomConfig.save();
                PlayersConfig.save();
                PlayersConfig.reload();
                CustomConfig.reload();
                p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-1"));

            } else {
                p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-1_1"));
            }
            return true;
        }

        public static boolean description(Player p, String description){
            String id = p.getName();
            for (String currencies : CustomConfig.get().getKeys(false)){
                String author = CustomConfig.get().getString(currencies + ".author");
                if (author.equals(id)){
                    CustomConfig.get().set(currencies + ".description", description);
                    p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-6"));
                }
            }
            return true;
        }

        public static boolean forceDelete(String name){
            String cname = name;
            String id = CustomConfig.get().getString(name + ".author");
            ItemStack nuggets = new ItemStack(Material.IRON_NUGGET, 1);
            for (String key : PlayersConfig.get().getKeys(false)) {
                PlayersConfig.get().set(key + "." + cname + ".", null);
            }
            if (CustomConfig.get().contains(name)){
                PlayersConfig.get().set(id + ".hascreated", null);
                CustomConfig.get().set(cname + "balance", null);
                Currency.isCurrencyCreated = false;
                PlayersConfig.save();
                PlayersConfig.reload();
                CustomConfig.save();
                CustomConfig.reload();
                Bukkit.getServer().broadcastMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-1_1"));
            }else{
                Bukkit.getServer().broadcastMessage(ChatColor.RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-10_2"));
            }
            return true;
        }


        public static boolean info(Player p, String name){
            if (CustomConfig.get().contains(name)) {
                p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-8") + name + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-8_1") + CustomConfig.get().getDouble(name + ".amount") + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-8_7") + CustomConfig.get().getString(name + ".description") + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-8_2") + CustomConfig.get().getDouble(name + ".power") + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-8_5") + CustomConfig.get().getDouble(name + ".totalvalue") + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-8_6") + CustomConfig.get().getString(name + ".author") + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-8_4") + CustomConfig.get().getDouble(name + ".economic-activity") + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-8_3") + CustomConfig.get().getInt(name + ".peers") + "\n");
            } else {
                p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-4_1"));
            }
            return true;
        }
        public static boolean list(Player p){
            //displays currencies
            if(CustomConfig.get().getKeys(false).size() > 0){
                p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-9"));
                for (String currencies : CustomConfig.get().getKeys(false)) {
                    p.sendMessage(ChatColor.GOLD + "    " + currencies + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-9_1") + CustomConfig.get().getDouble(currencies + ".power") + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-9_2") + CustomConfig.get().getDouble(currencies + ".economic-activity") + "\n");
                }
            }else {
                p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-5"));
            }
            return true;
        }


        public static boolean mint(Player p, String stramount){
            //check until the currency of the player is found
            String id = p.getName();
            Boolean hasCreated = PlayersConfig.get().getBoolean(id + ".hascreated");
            if (!hasCreated.equals(null)) {
                if(CustomConfig.get().getKeys(false).size() > 0) {
                    for (String currencies : CustomConfig.get().getKeys(false)) {
                        //check if any currency exists
                        //init vars
                        double globalamount = CustomConfig.get().getDouble(currencies + ".amount");
                        double cValue = CustomConfig.get().getDouble(currencies + ".totalvalue");
                        double cEcoActivity = CustomConfig.get().getDouble(currencies + ".economic-activity");
                        double cPower = CustomConfig.get().getDouble(currencies + ".power");
                        double amount = 0;
                        String author = CustomConfig.get().getString(currencies + ".author");
                                amount = Double.valueOf(stramount);
                                if (amount >= 1) {
                                    if (id.equals(author)) {
                                        double pbalance = PlayersConfig.get().getDouble(id + "." + currencies + "balance");
                                        PlayersConfig.get().set(id + "." + currencies + "balance", pbalance + amount);
                                        globalamount += amount;
                                        CustomConfig.get().set(currencies + ".amount", globalamount);
                                        p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-2") + amount + " " + currencies);
                                        if (cEcoActivity > 0.2) {
                                            CustomConfig.get().set(currencies + ".economic-activity", cEcoActivity - (amount / (amount*10*cPower)));
                                        }
                                        if (cEcoActivity <= 0.2){
                                            CustomConfig.get().set(currencies + ".economic-activity", 0.2);
                                        }
                                        CustomConfig.get().set(currencies + ".power", (cValue / (globalamount+amount)) * cEcoActivity);
                                        CustomConfig.save();
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
                if(CustomConfig.get().getKeys(false).size() > 0) {
                    for (String currencies : CustomConfig.get().getKeys(false)) {
                        //init variables
                        double cValue = CustomConfig.get().getDouble(currencies + ".totalvalue");
                        double amount = 0;
                        double cMarketAmount = CustomConfig.get().getDouble(currencies + ".amount");
                        double cEcoActivity = CustomConfig.get().getDouble(currencies + ".economic-activity");
                        String author = CustomConfig.get().getString(currencies + ".author");
                        //check if player has a currency
                        //check if he's the author of the currency being checked
                        if (author.equals(id)) {
                            //check useless to see if there's no currency minted
                            if (cMarketAmount == 0) {
                                p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-8"));
                            }/* Deposit the ores */ else {
                                if (p.getInventory().getItemInMainHand().getType().equals(Material.COAL)) {
                                    amount = 5 * itemamount;
                                    CustomConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                    CustomConfig.get().set(currencies + ".power", ((cValue + amount) / cMarketAmount) * cEcoActivity);
                                    p.getInventory().setItemInMainHand(null);
                                    p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3") + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                                } else if (p.getInventory().getItemInMainHand().getType().equals(Material.IRON_INGOT)) {
                                    amount = 9 * itemamount;
                                    CustomConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                    CustomConfig.get().set(currencies + ".power", ((cValue + amount) / cMarketAmount) * cEcoActivity);
                                    p.getInventory().setItemInMainHand(null);
                                    p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3") + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                                } else if (p.getInventory().getItemInMainHand().getType().equals(Material.IRON_NUGGET)) {
                                    amount = 1 * itemamount;
                                    CustomConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                    CustomConfig.get().set(currencies + ".power", ((cValue + amount) / cMarketAmount) * cEcoActivity);
                                    p.getInventory().setItemInMainHand(null);
                                    p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3") + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                                } else if (p.getInventory().getItemInMainHand().getType().equals(Material.GOLD_INGOT)) {
                                    amount = 45 * itemamount;
                                    CustomConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                    CustomConfig.get().set(currencies + ".power", ((cValue + amount) / cMarketAmount) * cEcoActivity);
                                    p.getInventory().setItemInMainHand(null);
                                    p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3") + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                                } else if (p.getInventory().getItemInMainHand().getType().equals(Material.GOLD_NUGGET)) {
                                    amount = 5 * itemamount;
                                    CustomConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                    CustomConfig.get().set(currencies + ".power", ((cValue + amount) / cMarketAmount) * cEcoActivity);
                                    p.getInventory().setItemInMainHand(null);
                                    p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3") + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                                } else if (p.getInventory().getItemInMainHand().getType().equals(Material.DIAMOND)) {
                                    amount = 180 * itemamount;
                                    CustomConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                    CustomConfig.get().set(currencies + ".power", ((cValue + amount) / cMarketAmount) * cEcoActivity);
                                    p.getInventory().setItemInMainHand(null);
                                    p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3") + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                                } else if (p.getInventory().getItemInMainHand().getType().equals(Material.NETHERITE_INGOT)) {
                                    amount = 360 * itemamount;
                                    CustomConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                    CustomConfig.get().set(currencies + ".power", ((cValue + amount) / cMarketAmount) * cEcoActivity);
                                    p.getInventory().setItemInMainHand(null);
                                    p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3") + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                                } else if (p.getInventory().getItemInMainHand().getType().equals(Material.NETHERITE_SCRAP)) {
                                    amount = 90 * itemamount;
                                    CustomConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                    CustomConfig.get().set(currencies + ".power", ((cValue + amount) / cMarketAmount) * cEcoActivity);
                                    p.getInventory().setItemInMainHand(null);
                                    p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3") + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                                } else if (p.getInventory().getItemInMainHand().getType().equals(Material.REDSTONE)) {
                                    amount = 9 * itemamount;
                                    CustomConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                    CustomConfig.get().set(currencies + ".power", ((cValue + amount) / cMarketAmount) * cEcoActivity);
                                    p.getInventory().setItemInMainHand(null);
                                    p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3")  + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                                } else if (p.getInventory().getItemInMainHand().getType().equals(Material.COPPER_BLOCK)) {
                                    amount = 9 * itemamount;
                                    CustomConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                    CustomConfig.get().set(currencies + ".power", ((cValue + amount) / cMarketAmount) * cEcoActivity);
                                    p.getInventory().setItemInMainHand(null);
                                    p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3")  + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                                } else if (p.getInventory().getItemInMainHand().getType().equals(Material.EMERALD)) {
                                    amount = 90 * itemamount;
                                    CustomConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                    CustomConfig.get().set(currencies + ".power", ((cValue + amount) / cMarketAmount) * cEcoActivity);
                                    p.getInventory().setItemInMainHand(null);
                                    p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3")  + " " + amount + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-3_1"));
                                } else {
                                    p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-8_1"));
                                }
                            }
                        }
                    }
                    CustomConfig.save();
                }else {
                    p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-7"));
                }
            }else {
                p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-7"));
            }
            return true;
        }


        public static boolean pay(Player p,Player target, String name, String stramount){
            //init variables for some reason
            String targetidd = target.getName();
            String playeridd = p.getName();
            String pName = p.getName();
            String tName = target.getName();
            double cPower = CustomConfig.get().getDouble(name + ".power");
            double cValue = CustomConfig.get().getDouble(name + ".totalvalue");
            double cMarketAmount = CustomConfig.get().getDouble(name + ".amount");
            double cEcoActivity = CustomConfig.get().getDouble(name + ".economic-activity");
            int nPeers = CustomConfig.get().getInt(name + ".peers");
            //if you don't have friends
            if (target == null) {
                p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-9_1"));
            }
            //if the currency exists
            if (!CustomConfig.get().contains(name)) {
                p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-9_2"));
            }
            if (target != null) {
                //if currency exists
                if (CustomConfig.get().contains(name)) {
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
                            CustomConfig.get().getInt(name + ".peers", nPeers + 1);
                        }
                        double tbalance = PlayersConfig.get().getDouble(targetidd + "." + name + "balance");
                            //if amount not too low
                            double amount = Double.valueOf(stramount);
                            if (pbalance >= amount) {
                                if (amount >= 0.01) {
                                    //pay
                                    nPeers = CustomConfig.get().getInt(name + ".peers");
                                    PlayersConfig.get().set(targetidd + "." + name + "balance", tbalance + amount);
                                    PlayersConfig.get().set(playeridd + "." + name + "balance", pbalance - amount);
                                    CustomConfig.get().set(name + ".economic-activity", cEcoActivity + ((0.01) / (amount / nPeers)));
                                    CustomConfig.get().set(name + ".power", ((cValue - ((cValue / cMarketAmount) * amount)) / (cMarketAmount - amount)) * cEcoActivity);
                                    p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-4") + stramount + " " + name + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-4_2") + tName);
                                    target.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-4_1")+ stramount + " " + name + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-4_3") + pName);
                                    PlayersConfig.save();
                                    CustomConfig.save();
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
                if (CustomConfig.get().contains(name)) {
                    if (amount > 0) {
                        String id = p.getName();
                        double pBalance = PlayersConfig.get().getDouble(id + "." + name + ".balance");
                        double cMarketAmount = CustomConfig.get().getDouble(name + ".amount");
                        double cValue = CustomConfig.get().getDouble(name + ".totalvalue");
                        double cEcoActivity = CustomConfig.get().getDouble(name + ".economic-activity");
                        double cPower = CustomConfig.get().getDouble(name + ".power");
                        PlayersConfig.get().set(id + name + ".balance", pBalance - amount);
                        CustomConfig.get().set(name + ".amount", cMarketAmount - amount);
                        CustomConfig.get().set(name + ".power", ((cValue - (cPower * amount)) / (cMarketAmount - amount)) * cEcoActivity);
                        CustomConfig.save();
                        CustomConfig.reload();
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

        public static boolean withdraw(Player p, String name, String stramount){
            //init vars and config keys
            double cPower = CustomConfig.get().getDouble(name + ".power");
            double cValue = CustomConfig.get().getDouble(name + ".totalvalue");
            double cMarketAmount = CustomConfig.get().getDouble(name + ".amount");
            double cEcoActivity = CustomConfig.get().getDouble(name + ".economic-activity");
            double amount = 0;
            String id = p.getName();
            String pname = p.getName().toString();
            //if currency exists
            if (CustomConfig.get().contains(name)) {
                amount = Double.valueOf(stramount);
                double pBalance = PlayersConfig.get().getDouble(id + "." + name + ".balance");
                //if you're not a rat
                if (pBalance >= amount && amount >= 0.01) {
                    for (int i = 0; i < cPower * amount; i++) {
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "give " + pname + " iron_nugget 1");
                    }
                    CustomConfig.get().set(name + ".amount", cMarketAmount - amount);
                    CustomConfig.get().set(name + ".totalvalue", cValue - cPower * amount);
                    //if the eco activity is superior to 0.5
                    if(cEcoActivity > 0.2) {
                        CustomConfig.get().set(name + ".economic-activity", cEcoActivity - (amount / (amount*10*cPower)));
                    }
                    CustomConfig.get().set(name + ".power", ((cValue - (cPower*amount)) / (cMarketAmount - amount))*cEcoActivity);
                    PlayersConfig.get().set(id + "." + name + "balance", pBalance - amount);
                    p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-5") + amount + " " + name);
                }else{
                    p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-10_1"));
                }
                CustomConfig.save();
                PlayersConfig.save();
            } else {
                p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-10_2"));
            }
            return true;
        }


        public static boolean wallet(Player p){
            //display currencies in your wallet
            String user = p.getName();
            if(CustomConfig.get().getKeys(false).size() > 0) {
                p.sendMessage(ChatColor.GREEN + " | Ascecia Curencies | Your Wallet | \n\n ");
                for (String currencies : CustomConfig.get().getKeys(false)) {
                    p.sendMessage(ChatColor.GOLD + "" + currencies + ":" + " \n      Balance: " + PlayersConfig.get().getDouble(user + "." + currencies + "balance") + "\n");
                }
            }else {
                p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-5"));
            }
            return true;
        }
    }

}

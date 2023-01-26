package me.rgn.asceciacurrencies.api;

import jdk.tools.jlink.plugin.Plugin;
import me.rgn.asceciacurrencies.Currency;
import me.rgn.asceciacurrencies.files.CustomConfig;
import me.rgn.asceciacurrencies.files.PlayersConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.UUID;

public class CurrenciesAPI{
    public static class Currencies{
        public static Object get(String name, String path){
            CustomConfig.get().get(path);
            return CustomConfig.get().get(path);
        }
        public static void set(String name, String path, Object value){
            CustomConfig.get().set(path, value);
        }
        public static boolean Add(Player p, String name, double amount){
            if(p != null){
                if (CustomConfig.get().contains(name)){
                    if (amount > 0) {
                        String id = p.getUniqueId().toString();
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
                    System.out.println("The Currency specified is non-existante !");
                }
            }else{
                System.out.println("The player doesn't exist");
            }
            return true;
        }


        public static boolean Create(Player p, String name){
            //getting player id
            int count = 0;
            boolean isNameValid = false;
            String id = p.getUniqueId().toString();
            //check if player created a currency
            if (!PlayersConfig.get().contains(id + ".hascreated")) {
                //I don't know what this does
                if (name != null) {
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
                                CustomConfig.get().set(name + ".economic-development", 1.0);
                                CustomConfig.get().set(name + ".peers", 1);
                                CustomConfig.get().set(name + ".author", id);
                                p.sendMessage(ChatColor.GREEN + "[Currencies]: The Currency " + name + " Has been Created");
                                Currency.isCurrencyCreated = true;
                            } else {
                                p.sendMessage(ChatColor.DARK_RED + "[Currencies]: your currency need to be 3 caracthers long minimum and 9 max and not use any special characters or numbers or spaces");
                            }
                        } else {
                            p.sendMessage(ChatColor.DARK_RED + "[Currencies]: your currency need to be 3 caracthers long minimum and 9 max and not use any special characters or numbers or spaces");
                        }
                    } else {
                        p.sendMessage(ChatColor.DARK_RED + "[Currencies]: The Currency Already Exists");
                    }
                } else {
                    p.sendMessage(ChatColor.DARK_RED + "[Currencies]: /currencies create (currencyname)");
                }
            }else {
                p.sendMessage(ChatColor.RED + "[Currencies]: You Have Already Created a currency");
            }
            PlayersConfig.save();
            PlayersConfig.reload();
            CustomConfig.save();
            CustomConfig.reload();
            return true;
        }

        public static boolean Delete(Player p, String name){
            //gets the sender and the author of the currency
            String id = p.getUniqueId().toString();
            String author = CustomConfig.get().getString(name + ".author");
            //check if the currency name has been entered
            if (name == null) {
                p.sendMessage(ChatColor.DARK_RED + "[Currencies]: /currencies delete (currencyname)");
            } else {
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
                    p.sendMessage(ChatColor.GREEN + "[Currencies]: Your Currency Has been deleted");

                } else {
                    p.sendMessage(ChatColor.DARK_RED + "[Currencies]: You are not the owner of this currency !");
                }
            }
            return true;
        }


        public static boolean ForceDelete(String name){
            String cname = name;
            String id = CustomConfig.get().getString(name + ".author");
            ItemStack nuggets = new ItemStack(Material.IRON_NUGGET, 1);
            for (String key : PlayersConfig.get().getKeys(false)) {
                PlayersConfig.get().set(key + "." + cname + ".", null);
            }
            PlayersConfig.get().set(id + ".hascreated", null);
            CustomConfig.get().set(cname + "balance", null);
            Currency.isCurrencyCreated = false;
            PlayersConfig.save();
            PlayersConfig.reload();
            CustomConfig.save();
            CustomConfig.reload();
            Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "[Currencies]: You deleted a Currency and no ores will be given.");
            return true;
        }


        public static boolean Info(Player p, String name){
            if (!name.equals(null)) {
                if (CustomConfig.get().contains(name)) {
                    p.sendMessage(ChatColor.GREEN + "| Currency Info -> " + name + " | " + "\n \n" + ChatColor.GOLD + "Amount of Currency available on the market: " + CustomConfig.get().getDouble(name + ".amount") + "\n \n Power of the Currency: " + CustomConfig.get().getDouble(name + ".power") + "\n \n Total Value of the Currency: " + CustomConfig.get().getDouble(name + ".totalvalue") + " iron \n \n " + "author: " + CustomConfig.get().getString(name + ".author") + "\n \n Economic-Activity: " + CustomConfig.get().getDouble(name + ".economic-activity") + "\n \n Number of users: " + CustomConfig.get().getDouble(name + ".peers") + "\n");
                } else {
                    p.sendMessage(ChatColor.DARK_RED + "[Currencies]: This Currency Doesn't exist");
                }
            }else {
                p.sendMessage(ChatColor.DARK_RED + "[Currencies]: No currency specified");
            }
            return true;
        }
        public static boolean List(Player p){
            //displays currencies
            if(CustomConfig.get().getKeys(false).size() > 0){
                p.sendMessage(ChatColor.GREEN + "\n | Ascecia-Currencies | Currency-List | \n \n");
                for (String currencies : CustomConfig.get().getKeys(false)) {
                    p.sendMessage(ChatColor.GOLD + "    " + currencies + ":  \n     Power: " + CustomConfig.get().getDouble(currencies + ".power") + "\n      Economic-Activity: " + CustomConfig.get().getDouble(currencies + ".economic-activity") + "\n");
                }
            }else {
                p.sendMessage(ChatColor.DARK_RED + "[Currencies]: No currencies available. create one with /currencies create");
            }
            return true;
        }


        public static boolean Mint(Player p, String stramount){
            //check until the currency of the player is found
            String id = p.getUniqueId().toString();
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
                        //check if no amount is entered
                        if (stramount.equals(null)) {
                            p.sendMessage(ChatColor.DARK_RED + "[Currencies]: /currency make (amount)");
                        } else {
                            if (!stramount.equals(null)) {
                                amount = Double.valueOf(stramount);
                                if (amount >= 1) {
                                    if (id.equals(author)) {
                                        double pbalance = PlayersConfig.get().getDouble(id + "." + currencies + "balance");
                                        PlayersConfig.get().set(id + "." + currencies + "balance", pbalance + amount);
                                        globalamount += amount;
                                        CustomConfig.get().set(currencies + ".amount", globalamount);
                                        p.sendMessage(ChatColor.GREEN + "[Currencies]: You Made " + amount + " " + currencies);
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
                                    p.sendMessage(ChatColor.DARK_RED + "[Currencies]: The amount specified is too low !");
                                }
                            } else {
                                p.sendMessage(ChatColor.DARK_RED + "[Currencies]: No Amount Specified");
                            }
                        }
                    }
                }else {
                    p.sendMessage(ChatColor.DARK_RED + "[Currencies]: you don't have a currency. create one using /currencies create");
                }
            }else {
                p.sendMessage(ChatColor.DARK_RED + "[Currencies]: you don't have a currency. create one using /currencies create");
            }
            return true;
        }


        public static boolean Deposit(Player p, double itemamount){
            //init root variable
            String id = p.getUniqueId().toString();
            Boolean hasCreated = PlayersConfig.get().getBoolean(id + ".hascreated");
            //search for player's currency
            if (!hasCreated.equals(null)){
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
                            p.sendMessage(ChatColor.DARK_RED + "\n you need to make at least one of your currency do /currencies make (name) (amount) !");
                        }/* Deposit the ores */ else {
                            if (p.getInventory().getItemInMainHand().getType().equals(Material.COAL)) {
                                amount = 5 * itemamount;
                                CustomConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CustomConfig.get().set(currencies + ".power", ((cValue + amount) / cMarketAmount) * cEcoActivity);
                                p.getInventory().setItemInMainHand(null);
                                p.sendMessage(ChatColor.GREEN + "[Currencies]: " + amount + " iron Worth of ores to your currency");
                            } else if (p.getInventory().getItemInMainHand().getType().equals(Material.IRON_INGOT)) {
                                amount = 9 * itemamount;
                                CustomConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CustomConfig.get().set(currencies + ".power", ((cValue + amount) / cMarketAmount) * cEcoActivity);
                                p.getInventory().setItemInMainHand(null);
                                p.sendMessage(ChatColor.GREEN + "[Currencies]: " + amount + " iron Worth of ores to your currency");
                            } else if (p.getInventory().getItemInMainHand().getType().equals(Material.IRON_NUGGET)) {
                                amount = 1 * itemamount;
                                CustomConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CustomConfig.get().set(currencies + ".power", ((cValue + amount) / cMarketAmount) * cEcoActivity);
                                p.getInventory().setItemInMainHand(null);
                                p.sendMessage(ChatColor.GREEN + "[Currencies]: " + amount + " iron Worth of ores to your currency");
                            } else if (p.getInventory().getItemInMainHand().getType().equals(Material.GOLD_INGOT)) {
                                amount = 45 * itemamount;
                                CustomConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CustomConfig.get().set(currencies + ".power", ((cValue + amount) / cMarketAmount) * cEcoActivity);
                                p.getInventory().setItemInMainHand(null);
                                p.sendMessage(ChatColor.GREEN + "[Currencies]: " + amount + " iron Worth of ores to your currency");
                            } else if (p.getInventory().getItemInMainHand().getType().equals(Material.GOLD_NUGGET)) {
                                amount = 5 * itemamount;
                                CustomConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CustomConfig.get().set(currencies + ".power", ((cValue + amount) / cMarketAmount) * cEcoActivity);
                                p.getInventory().setItemInMainHand(null);
                                p.sendMessage(ChatColor.GREEN + "[Currencies]: " + amount + " iron Worth of ores to your currency");
                            } else if (p.getInventory().getItemInMainHand().getType().equals(Material.DIAMOND)) {
                                amount = 180 * itemamount;
                                CustomConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CustomConfig.get().set(currencies + ".power", ((cValue + amount) / cMarketAmount) * cEcoActivity);
                                p.getInventory().setItemInMainHand(null);
                                p.sendMessage(ChatColor.GREEN + "[Currencies]: " + amount + " iron Worth of ores to your currency");
                            } else if (p.getInventory().getItemInMainHand().getType().equals(Material.NETHERITE_INGOT)) {
                                amount = 360 * itemamount;
                                CustomConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CustomConfig.get().set(currencies + ".power", ((cValue + amount) / cMarketAmount) * cEcoActivity);
                                p.getInventory().setItemInMainHand(null);
                                p.sendMessage(ChatColor.GREEN + "[Currencies]: " + amount + " iron Worth of ores to your currency");
                            } else if (p.getInventory().getItemInMainHand().getType().equals(Material.NETHERITE_SCRAP)) {
                                amount = 90 * itemamount;
                                CustomConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CustomConfig.get().set(currencies + ".power", ((cValue + amount) / cMarketAmount) * cEcoActivity);
                                p.getInventory().setItemInMainHand(null);
                                p.sendMessage(ChatColor.GREEN + "[Currencies]: " + amount + " iron Worth of ores to your currency");
                            } else if (p.getInventory().getItemInMainHand().getType().equals(Material.REDSTONE)) {
                                amount = 9 * itemamount;
                                CustomConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CustomConfig.get().set(currencies + ".power", ((cValue + amount) / cMarketAmount) * cEcoActivity);
                                p.getInventory().setItemInMainHand(null);
                                p.sendMessage(ChatColor.GREEN + "[Currencies]: " + amount + " iron Worth of ores to your currency");
                            } else if (p.getInventory().getItemInMainHand().getType().equals(Material.COPPER_BLOCK)) {
                                amount = 9 * itemamount;
                                CustomConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CustomConfig.get().set(currencies + ".power", ((cValue + amount) / cMarketAmount) * cEcoActivity);
                                p.getInventory().setItemInMainHand(null);
                                p.sendMessage(ChatColor.GREEN + "[Currencies]: " + amount + " iron Worth of ores to your currency");
                            } else if (p.getInventory().getItemInMainHand().getType().equals(Material.EMERALD)) {
                                amount = 90 * itemamount;
                                CustomConfig.get().set(currencies + ".totalvalue", cValue + amount);
                                CustomConfig.get().set(currencies + ".power", ((cValue + amount) / cMarketAmount) * cEcoActivity);
                                p.getInventory().setItemInMainHand(null);
                                p.sendMessage(ChatColor.GREEN + "[Currencies]: " + amount + " iron Worth of ores to your currency");
                            } else {
                                p.sendMessage(ChatColor.DARK_RED + "Material not supported");
                            }
                        }
                    }
                }
                CustomConfig.save();
            }else {
                p.sendMessage(ChatColor.DARK_RED + "[Currencies]: You don't have a currency. create one with /currencies create");
            }
            return true;
        }


        public static boolean Pay(Player p,Player target, String name, String stramount){
            //init variables for some reason
            UUID targetid = target.getUniqueId();
            String targetidd = targetid.toString();
            UUID playerid = p.getUniqueId();
            String playeridd = playerid.toString();
            String pName = p.getName();
            String tName = target.getName();
            double cPower = CustomConfig.get().getDouble(name + ".power");
            double cValue = CustomConfig.get().getDouble(name + ".totalvalue");
            double cMarketAmount = CustomConfig.get().getDouble(name + ".amount");
            double cEcoActivity = CustomConfig.get().getDouble(name + ".economic-activity");
            int nPeers = CustomConfig.get().getInt(name + ".peers");
            //if no args
            if (name == null) {
                p.sendMessage(ChatColor.DARK_RED + "\n /currencypay (playername) (currencyname) (amount)");
            } else {
                //if you don't have friends
                if (target == null) {
                    p.sendMessage(ChatColor.DARK_RED + "\n The Player Specified isn't Online");
                }
                //if the currency exists
                if (!CustomConfig.get().contains(name)) {
                    p.sendMessage(ChatColor.DARK_RED + "\n The Currency Specified doesn't exist");
                }
                //if you're a scammer
                if (stramount == null) {
                    p.sendMessage(ChatColor.DARK_RED + "\n No Amount Specified");
                }
                if (target != null) {
                    //if currency exists
                    if (CustomConfig.get().contains(name)) {
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
                        if (!stramount.equals(null)) {
                            //if amount not too low
                            double amount = Double.valueOf(stramount);
                            if (pbalance >= amount) {
                                if (amount >= 0.01) {
                                    //pay
                                    nPeers = CustomConfig.get().getInt(name + ".peers");
                                    PlayersConfig.get().set(targetidd + "." + name + "balance", tbalance + amount);
                                    PlayersConfig.get().set(playeridd + "." + name + "balance", pbalance - amount);
                                    CustomConfig.get().set(name + ".economic-activity", cEcoActivity + ((0.01)/(amount/nPeers)));
                                    CustomConfig.get().set(name + ".power", ((cValue - ((cValue/cMarketAmount)*amount)) / (cMarketAmount - amount))*cEcoActivity);
                                    p.sendMessage(ChatColor.GREEN + "[Currencies]: You succesfully payed " + stramount + " " + name + " to " + tName);
                                    p.sendMessage(ChatColor.GREEN + "[Currencies]: You succesfully received " + stramount + " " + name + " from " + pName);
                                    PlayersConfig.save();
                                    CustomConfig.save();
                                }else{
                                    p.sendMessage(ChatColor.DARK_RED + "[Currencies]: The amount specified is too low !");
                                }
                            } else {
                                p.sendMessage(ChatColor.DARK_RED + "[Currencies]: You do not have enough money");
                            }
                        }
                    }
                }
            }
            return true;
        }

        public static boolean Remove(Player p, String name, double amount){
            if(p != null){
                if (CustomConfig.get().contains(name)) {
                    if (amount > 0) {
                        String id = p.getUniqueId().toString();
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

        public static boolean Withdraw(Player p, String name, String stramount){
            //init vars and config keys
            double cPower = CustomConfig.get().getDouble(name + ".power");
            double cValue = CustomConfig.get().getDouble(name + ".totalvalue");
            double cMarketAmount = CustomConfig.get().getDouble(name + ".amount");
            double cEcoActivity = CustomConfig.get().getDouble(name + ".economic-activity");
            double amount = 0;
            String id = p.getUniqueId().toString();
            String pname = p.getName().toString();
            //if currency not specified
            if (name == null) {
                p.sendMessage(ChatColor.DARK_RED + "[Currencies]: /currencies withdraw (currencyname) (amount)");
            } else {
                //if currency exists
                if (CustomConfig.get().contains(name)) {
                    //if no amount specified
                    if (stramount == null) {
                        p.sendMessage(ChatColor.DARK_RED + "[Currencies]: No amount specified");
                    } else {
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
                            p.sendMessage(ChatColor.GREEN + "[Currencies]: You Withdrew " + amount + " " + name);
                        }else{
                            p.sendMessage(ChatColor.DARK_RED + "[Currencies]: The amount entered is too low !");
                        }
                        CustomConfig.save();
                        PlayersConfig.save();
                    }
                } else {
                    p.sendMessage(ChatColor.DARK_RED + "[Currencies]: The Currency doesn't exist");
                }
            }
            return true;
        }


        public static boolean Wallet(Player p){
            //display currencies in your wallet
            String user = p.getUniqueId().toString();
            if(CustomConfig.get().getKeys(false).size() > 0) {
                p.sendMessage(ChatColor.GREEN + " | Ascecia Curencies | Your Wallet | \n\n ");
                for (String currencies : CustomConfig.get().getKeys(false)) {
                    p.sendMessage(ChatColor.GOLD + "" + currencies + ":" + " \n      Balance: " + PlayersConfig.get().getDouble(user + "." + currencies + "balance") + "\n");
                }
            }else {
                p.sendMessage(ChatColor.DARK_RED + "[Currencies]: No currencies available. create one with /currencies create ");
            }
            return true;
        }
    }

}

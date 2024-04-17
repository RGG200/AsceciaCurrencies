package me.rgn.asceciacurrencies.api.versions;

import me.rgn.asceciacurrencies.AsceciaCurrencies;
import me.rgn.asceciacurrencies.api.CurrenciesAPI;
import me.rgn.asceciacurrencies.commands.Currencies;
import me.rgn.asceciacurrencies.files.CurrenciesConfig;
import me.rgn.asceciacurrencies.files.LanguageConfig;
import me.rgn.asceciacurrencies.files.PlayersConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.ChatPaginator;


import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.IntStream;

public class Currency1_19 implements Currency {
    public boolean add(Player p, String name, double amount){
        if(p != null){
            if (CurrenciesConfig.get().contains(name)){
                if (amount > 0) {
                    String id = p.getUniqueId().toString();
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


    public boolean create(Player p, String name){
        //getting player id
        int count = 0;
        boolean isNameValid = false;
        String id = p.getUniqueId().toString();
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
                        CurrenciesConfig.get().set(name + ".team." + id + ".description", true);
                        PlayersConfig.get().set(id + ".team", name);
                        PlayersConfig.get().set(id + ".name", Bukkit.getServer().getOfflinePlayer(UUID.fromString(id)).getName());
                        //adding author to playerAcounts list
                        List<Tuple<String, Double>> pwallet = new ArrayList<>();
                        SimpleDateFormat DF = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
                        Date date = new Date();
                        for(String cbalance: PlayersConfig.get().getConfigurationSection(id + ".balance").getKeys(false)){
                            Tuple<String, Double> cbal = new Tuple<>();
                            cbal.First = cbalance;
                            cbal.Second = PlayersConfig.get().getDouble(id + ".balance." + cbalance);
                            pwallet.add(cbal);
                        }
                        PlayersConfig.get().set(id + ".DET", DF.format(date));
                        CurrenciesAPI.PlayerAccounts.add(new PlayerAccount(id, DF.format(date), pwallet));
                        //adding Currency to Currency list
                        List<Tuple<String, List<Tuple<String, Boolean>>>> team = new ArrayList<>();
                        Tuple<String, List<Tuple<String, Boolean>>> player = new Tuple<>();
                        List<Tuple<String, Boolean>> playerperms = new ArrayList<>();
                        Tuple<String, Boolean> playerperm = new Tuple<>();
                        playerperm.First = "mint";
                        playerperm.Second = CurrenciesConfig.get().getBoolean(name + ".team." + id + ".mint");
                        playerperms.add(playerperm);
                        playerperm.First = "deposit";
                        playerperm.Second = CurrenciesConfig.get().getBoolean(name + ".team." + id + ".deposit");
                        playerperms.add(playerperm);
                        playerperm.First = "description";
                        playerperm.Second = CurrenciesConfig.get().getBoolean(name + ".team." + id + ".description");
                        playerperms.add(playerperm);
                        playerperm.First = "rename";
                        playerperm.Second = CurrenciesConfig.get().getBoolean(name + ".team." + id + ".rename");
                        playerperms.add(playerperm);
                        player.First = id;
                        player.Second = playerperms;
                        team.add(player);

                        CurrenciesAPI.currencyObjects.add(new CurrencyObject(name,"defaultDescription", 1.0, 0.0, 0.0, 1.0, id, 1, team));
                        p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-0") + name + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-0_1"));
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

    public boolean delete(Player p, String name){
        //gets the sender and the author of the currency
        String id = p.getUniqueId().toString();
        double cEcoActivity = CurrenciesConfig.get().getDouble(name + ".economic-activity");
        String author = CurrenciesConfig.get().getString(name + ".author");
        //check if sender is the author
        if (id.equals(author)) {
            //gives the money contained in the currency and deletes the config keys
            String cname = name;
            double cMarketValue = CurrenciesConfig.get().getDouble(name + ".totalvalue");
            final List<String[]> material_prices = new ArrayList<>();
            for(String price: AsceciaCurrencies.plugin.getConfig().getConfigurationSection("ores_prices").getKeys(false)){
                final String[] material_price = {String.valueOf(AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices." + price)), price};
                material_prices.add(material_price);
            }
            for (double i = 0; i < cMarketValue*cEcoActivity; i+=0.01) {
                double difference = (cMarketValue*cEcoActivity)-i;
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
                            String item_string = material[1];
                            Material itemMaterial = Material.valueOf(item_string.toUpperCase());
                            final ItemStack itemGiven = new ItemStack(itemMaterial, 1);
                            final Map<Integer, ItemStack> map = p.getInventory().addItem(itemGiven);
                            for (final ItemStack item : map.values()) {
                                p.getWorld().dropItemNaturally(p.getLocation(), item);
                            }
                            i += Double.valueOf(material[0])-0.01;
                            if(cMarketValue*cEcoActivity <= i){
                                break;
                            }
                        }
                    }
                }
            }
            for(String key: PlayersConfig.get().getKeys(false)) {
                PlayersConfig.get().set(key + ".balance." + name, null);
                if(PlayersConfig.get().getString(key + ".team") != null){
                    if(PlayersConfig.get().getString(key + ".team").equals(name)){
                        PlayersConfig.get().set(key + ".team", null);
                    }
                }
            }
            PlayersConfig.save();
            PlayersConfig.get().set(id + ".hascreated", null);
            int index = IntStream.range(0, CurrenciesAPI.currencyObjects.size())
                    .filter(i -> CurrenciesAPI.currencyObjects.get(i).currency_name.equals(name))
                    .findFirst()
                    .orElse(-1);
            CurrenciesAPI.currencyObjects.remove(index);
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

    public boolean description(Player p, String description){
        String id = p.getUniqueId().toString();
        for (String currencies : CurrenciesConfig.get().getKeys(false)){
            String author = CurrenciesConfig.get().getString(currencies + ".author");
            if (author.equals(id) || CurrenciesConfig.get().getBoolean(currencies + ".team." + id + ".description") == true){
                CurrenciesConfig.get().set(currencies + ".description", description);
                p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-6"));
            }
        }
        return true;
    }

    public boolean forceDelete(String name, CommandSender sender){
        String id = CurrenciesConfig.get().getString(name + ".author");
        for (String key : PlayersConfig.get().getKeys(false)) {
            PlayersConfig.get().set(key + ".balance." + name, null);
            if(PlayersConfig.get().getString(key + ".team") != null){
                if(PlayersConfig.get().getString(key + ".team").equals(name)){
                    PlayersConfig.get().set(key + ".team", null);
                }
            }
        }
        if (CurrenciesConfig.get().contains(name)){
            int index = IntStream.range(0, CurrenciesAPI.currencyObjects.size())
                    .filter(i -> CurrenciesAPI.currencyObjects.get(i).currency_name.equals(name))
                    .findFirst()
                    .orElse(-1);
            CurrenciesAPI.currencyObjects.remove(index);
            CurrenciesConfig.get().set(name, null);
            PlayersConfig.get().set(id + ".hascreated", null);
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
    public boolean giveOwnership(Player author, String name, String new_author){
        if(!new_author.equals(author.getUniqueId().toString())) {
            if (CurrenciesConfig.get().contains(name)) {
                if (CurrenciesConfig.get().getString(name + ".author").equals(author.getUniqueId().toString())) {
                    if (PlayersConfig.get().getBoolean(new_author + ".hascreated") != true) {
                        CurrenciesConfig.get().set(name + ".author", new_author);
                        PlayersConfig.get().set(author.getUniqueId().toString() + ".hascreated", false);
                        CurrenciesConfig.save();
                        author.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-17"));
                        CurrenciesConfig.reload();
                    } else {
                        author.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-17_3"));
                    }
                } else {
                    author.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-17_2"));
                }
            } else {
                author.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-10_2"));
            }
        }else{
            author.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-17_1"));
        }
        return true;
    }

    public boolean info(CommandSender p, String name){
        if (CurrenciesConfig.get().contains(name)) {
            p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-8") + name + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-8_1") + CurrenciesConfig.get().getDouble(name + ".amount") + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-8_7") + CurrenciesConfig.get().getString(name + ".description") + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-8_2") + CurrenciesConfig.get().getDouble(name + ".power") + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-8_5") + CurrenciesConfig.get().getDouble(name + ".totalvalue") + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-8_6") + Bukkit.getServer().getOfflinePlayer(UUID.fromString(CurrenciesConfig.get().getString(name + ".author"))).getName() + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-8_4") + CurrenciesConfig.get().getDouble(name + ".economic-activity") + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-8_3") + CurrenciesConfig.get().getInt(name + ".peers") + "\n");
        } else {
            p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-4_1"));
        }
        return true;
    }
    public boolean list(CommandSender p){
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
    public boolean language(String language, CommandSender sender){
        if(LanguageConfig.get().contains(language)){
            LanguageConfig.get().set("language", language);
            sender.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-14"));
        }else{
            sender.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-14_1"));
        }
        LanguageConfig.save();
        return true;
    }


    public boolean mint(Player p,String currencies, double amount, double ia){
        //check until the currency of the player is found
        String id = p.getUniqueId().toString();
        if(CurrenciesConfig.get().getKeys(false).size() > 0) {
            //check if any currency exists
            //init vars
            double damount = Double.valueOf(Math.round(amount*100))/100;
            double iamount = 0;
            double globalamount = CurrenciesConfig.get().getDouble(currencies + ".amount");
            double cValue = CurrenciesConfig.get().getDouble(currencies + ".totalvalue");
            double cEcoActivity = CurrenciesConfig.get().getDouble(currencies + ".economic-activity");
            double cPower = CurrenciesConfig.get().getDouble(currencies + ".power");
            String author = CurrenciesConfig.get().getString(currencies + ".author");
            if (damount >= 1) {
                iamount = AsceciaCurrencies.plugin.getConfig().getDouble("reserve_materials." + p.getInventory().getItemInMainHand().getType().toString().toLowerCase())*ia;
                AsceciaCurrencies.plugin.getLogger().info(String.valueOf(iamount));
                if(damount <= iamount){
                    if (id.equals(author) || CurrenciesConfig.get().getBoolean(currencies + ".team." + id + ".mint") == true) {
                        double pbalance = PlayersConfig.get().getDouble(id + ".balance." + currencies);
                        PlayersConfig.get().set(id + ".balance." + currencies, pbalance + Double.valueOf(Math.round(amount*100))/100);
                        globalamount += Double.valueOf(Math.round(amount*100))/100;
                        p.getInventory().getItemInMainHand().setAmount((int) (ia-amount/(iamount/ia)));
                        CurrenciesConfig.get().set(currencies + ".amount", globalamount);
                        p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-2") + Double.valueOf(Math.round(amount*1000))/1000 + " " + currencies);
                        if (cEcoActivity > 0.2 && cPower > 0) {
                            CurrenciesConfig.get().set(currencies + ".economic-activity", cEcoActivity - (5e-5/cPower));
                        }
                        if (cEcoActivity <= 0.1){
                            CurrenciesConfig.get().set(currencies + ".economic-activity", 0.101);
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
                    p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-6_1"));
                }
            } else {
                p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-10_1"));
            }
        }else {
            p.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-7"));
        }
        return true;
    }

    public boolean setOrePrice(String ore, double amount, CommandSender sender){
        AsceciaCurrencies.plugin.getConfig().set("ores_prices." + ore, amount);
        sender.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-15"));
        AsceciaCurrencies.plugin.saveConfig();
        return true;
    }
    public boolean setMintMaterialPrice(String mm, double amount, CommandSender sender){
        AsceciaCurrencies.plugin.getConfig().set("reserve_materials." + mm, amount);
        sender.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-15"));
        AsceciaCurrencies.plugin.saveConfig();
        return true;
    }

    @Override
    public boolean showAccount(Player p) {
        p.sendMessage(ChatColor.GOLD + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-18") + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-18_1") + PlayersConfig.get().getString(p.getUniqueId().toString() + ".DET") + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-18_2"));
        return false;
    }

    @Override
    public boolean showAccountOther(CommandSender s, OfflinePlayer op) {
        s.sendMessage(ChatColor.GOLD + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-18_3") + op.getName() + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-18_1") + PlayersConfig.get().getString(op.getUniqueId().toString() + ".DET") + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-18_2"));
        return false;
    }

    public boolean deposit(Player p,String currencies, double itemamount){
        //init root variable
        String id = p.getUniqueId().toString();
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
                        CurrenciesConfig.get().set(currencies + ".power", Double.valueOf(Math.round(((cValue + amount) / cMarketAmount)*1000*cEcoActivity))/1000);;
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


    public boolean pay(Player p,Player target, String name, double amount) {
        //init variables for some reason
        String targetidd = target.getUniqueId().toString();
        String playeridd = p.getUniqueId().toString();
        String pName = p.getUniqueId().toString();
        String tName = target.getUniqueId().toString();
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
                        PlayersConfig.get().set(playeridd + ".name", Bukkit.getServer().getOfflinePlayer(UUID.fromString(playeridd)).getName());
                        List<Tuple<String, Double>> pwallet = new ArrayList<>();
                        SimpleDateFormat DF = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
                        Date date = new Date();
                        for (String cbalance : PlayersConfig.get().getConfigurationSection(playeridd + ".balance").getKeys(false)) {
                            Tuple<String, Double> cbal = new Tuple<>();
                            cbal.First = cbalance;
                            cbal.Second = PlayersConfig.get().getDouble(playeridd + ".balance." + cbalance);
                            pwallet.add(cbal);
                        }
                        PlayersConfig.get().set(playeridd + ".DET", DF.format(date));
                        CurrenciesAPI.PlayerAccounts.add(new PlayerAccount(playeridd, DF.format(date), pwallet));
                    }
                    double pbalance = PlayersConfig.get().getDouble(playeridd + ".balance." + name);
                    if (!PlayersConfig.get().contains(targetidd + ".balance." + name)) {
                        PlayersConfig.get().addDefault(targetidd + ".balance." + name, 0.0);
                        CurrenciesConfig.get().set(name + ".peers", nPeers + 1);
                        List<Tuple<String, Double>> pwallet = new ArrayList<>();
                        SimpleDateFormat DF = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
                        Date date = new Date();
                        for (String cbalance : PlayersConfig.get().getConfigurationSection(targetidd + ".balance").getKeys(false)) {
                            Tuple<String, Double> cbal = new Tuple<>();
                            cbal.First = cbalance;
                            cbal.Second = PlayersConfig.get().getDouble(targetidd + ".balance." + cbalance);
                            pwallet.add(cbal);
                        }
                        PlayersConfig.get().set(playeridd + ".DET", DF.format(date));
                        CurrenciesAPI.PlayerAccounts.add(new PlayerAccount(targetidd, DF.format(date), pwallet));
                        double tbalance = PlayersConfig.get().getDouble(targetidd + ".balance." + name);
                        //if amount not too low
                        if (pbalance >= Double.valueOf(Math.round(amount * 1000)) / 1000) {
                            if (Double.valueOf(Math.round(amount * 1000)) / 1000 >= 0.01) {
                                //pay
                                nPeers = CurrenciesConfig.get().getInt(name + ".peers");
                                PlayersConfig.get().set(targetidd + ".balance." + name, tbalance + Double.valueOf(Math.round(amount * 1000)) / 1000);
                                PlayersConfig.get().set(playeridd + ".balance." + name, pbalance - Double.valueOf(Math.round(amount * 1000)) / 1000);
                                CurrenciesConfig.get().set(name + ".economic-activity", cEcoActivity + (0.001 * nPeers));
                                CurrenciesConfig.get().set(name + ".power", Double.valueOf(Math.round((cValue / cMarketAmount) * 1000 * cEcoActivity)) / 1000);
                                p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-4") + Double.valueOf(Math.round(amount * 1000)) / 1000 + " " + name + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-4_2") + target.getName());
                                target.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-4_1") + Double.valueOf(Math.round(amount * 1000)) / 1000 + " " + name + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-4_3") + p.getName());
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
        }
        return true;
    }

    public boolean payOffline(Player p, OfflinePlayer target, String name, double amount){
        //init variables for some reason
        String targetidd = target.getUniqueId().toString();
        String playeridd = p.getUniqueId().toString();
        String pName = p.getUniqueId().toString();
        String tName = target.getUniqueId().toString();
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
                            CurrenciesConfig.get().set(name + ".power", Double.valueOf(Math.round((cValue/cMarketAmount)*1000*cEcoActivity))/1000);
                            p.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-4") + Double.valueOf(Math.round(amount*1000))/1000 + " " + name + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-4_2") + tName);
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
    public boolean remove(Player p, String name, double amount){
        if(p != null){
            if (CurrenciesConfig.get().contains(name)) {
                if (amount > 0) {
                    String id = p.getUniqueId().toString();
                    double pBalance = PlayersConfig.get().getDouble(id + ".balance." + name);
                    double cMarketAmount = CurrenciesConfig.get().getDouble(name + ".amount");
                    double cValue = CurrenciesConfig.get().getDouble(name + ".totalvalue");
                    double cEcoActivity = CurrenciesConfig.get().getDouble(name + ".economic-activity");
                    double cPower = CurrenciesConfig.get().getDouble(name + ".power");
                    PlayersConfig.get().set(id + ".balance." + name, pBalance - amount);
                    CurrenciesConfig.get().set(name + ".amount", cMarketAmount - amount);
                    CurrenciesConfig.get().set(name + ".power", Double.valueOf(Math.round((cValue-(cPower*amount))/(cMarketAmount-amount)*1000*cEcoActivity))/1000);
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
    public boolean reloadConfig(CommandSender sender){
        CurrenciesConfig.reload();
        AsceciaCurrencies.plugin.reloadConfig();
        PlayersConfig.reload();
        LanguageConfig.reload();
        sender.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-12"));
        return true;
    }


    public boolean rename (Player p,String currencies, String newName){
        // init vars
        String id = p.getUniqueId().toString();
        int count = 0;
        boolean isNameValid = false;
        //redudent check not useful anymore
        if (id.equals(id)){
            //checking every currency
            double pBalance = PlayersConfig.get().getDouble(id + ".balance." + currencies);
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
                            for(String player: PlayersConfig.get().getKeys(false)){
                                if(PlayersConfig.get().getString(player + ".team") != null){
                                    if(PlayersConfig.get().getString(player + ".team").equals(currencies)){
                                        PlayersConfig.get().set(player + ".team", newName);
                                    }
                                }
                                if(PlayersConfig.get().getString(player + ".invite") != null) {
                                    if (PlayersConfig.get().get(id + ".invite").equals(currencies)) {
                                        PlayersConfig.get().set(id + ".invite", newName);
                                    }
                                }
                            }
                            CurrenciesConfig.get().set(newName, CurrenciesConfig.get().get(currencies));
                            //deleting the original
                            PlayersConfig.get().set(id + ".balance." + currencies, null);
                            List<Tuple<String, List<Tuple<String, Boolean>>>> team = new ArrayList<>();
                            Tuple<String, List<Tuple<String, Boolean>>> player = new Tuple<>();
                            List<Tuple<String, Boolean>> playerperms = new ArrayList<>();
                            Tuple<String, Boolean> playerperm = new Tuple<>();
                            for(String playerinteam: CurrenciesConfig.get().getConfigurationSection(currencies + ".team").getKeys(false)) {
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
                            int index = IntStream.range(0, CurrenciesAPI.currencyObjects.size())
                                    .filter(i -> CurrenciesAPI.currencyObjects.get(i).currency_name.equals(currencies))
                                    .findFirst()
                                    .orElse(-1);
                            CurrenciesAPI.currencyObjects.get(index).modify(newName,"defaultDescription", 1.0, 0.0, 0.0, 1.0, id, 1, team);
                            Bukkit.getServer().getLogger().info(CurrenciesAPI.currencyObjects.get(index).currency_name);
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
    public boolean top(Boolean all, String name, CommandSender p){
        final List<Tuple<String, Double>> scoreboard = new ArrayList<>();
        Comparator<Tuple<String, Double>> myComparator = new Comparator<Tuple<String, Double>>() {
            @Override
            public int compare(Tuple<String, Double> o1, Tuple<String, Double> o2) {
                if(o1.Second < o2.Second){
                    return -1;
                }else{
                    return 1;
                }
            }
        };
        int i = 0;
        int j = 0;
        int page = 0;
        String paginate = "1";
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
                    Tuple<String, Double> playerbalance = new Tuple<>();
                    playerbalance.First = PlayersConfig.get().getString(player + ".name");
                    playerbalance.Second = PlayersConfig.get().getDouble(player + ".balance." + currencies);
                    scoreboard.add(playerbalance);
                }
                Collections.sort(scoreboard, myComparator);
                Collections.reverse(scoreboard);
                for(Tuple<String, Double> playerBalance: scoreboard){
                        i++;
                        if(i <= 5){
                            p.sendMessage(ChatColor.RED + "     " + i + ". " +  playerBalance.First + ": " + ChatColor.GREEN + playerBalance.Second + "\n ");
                        }
                }
            }
        }else{
            p.sendMessage(ChatColor.GOLD + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-13") + " " + name + " |"  + "\n ");
            PlayersConfig.reload();
            for (String player: PlayersConfig.get().getKeys(false)) {
                Tuple<String, Double> playerbalance = new Tuple<>();
                playerbalance.First = PlayersConfig.get().getString(player + ".name");
                playerbalance.Second = PlayersConfig.get().getDouble(player + ".balance." + name);
                scoreboard.add(playerbalance);
            }
            Collections.sort(scoreboard, myComparator);
            Collections.reverse(scoreboard);
            for(Tuple<String, Double> playerBalance: scoreboard){
                // if the player balance = current ranking player
                i++;
                if(i <= 5){
                    p.sendMessage(ChatColor.RED + "     " + i + ". " +  playerBalance.First + ": " + ChatColor.GREEN + playerBalance.Second + "\n ");
                }
            }
            ChatPaginator.ChatPage chatPage = ChatPaginator.paginate(leaderboard.toString(), page);
            for(String line: chatPage.getLines()){
                p.sendMessage(line);
            }
        }
        return true;
    }

    public boolean withdraw(Player p, String name, double amount){
        //init vars and config keys
        double cPower = CurrenciesConfig.get().getDouble(name + ".power");
        double cMarketValue = CurrenciesConfig.get().getDouble(name + ".totalvalue");
        double cMarketAmount = CurrenciesConfig.get().getDouble(name + ".amount");
        double cEcoActivity = CurrenciesConfig.get().getDouble(name + ".economic-activity");
        String id = p.getUniqueId().toString();
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
                            for(String price: AsceciaCurrencies.plugin.getConfig().getConfigurationSection("ores_prices").getKeys(false)){
                                final String[] material_price = {String.valueOf(AsceciaCurrencies.plugin.getConfig().getDouble("ores_prices." + price)), price};
                                material_prices.add(material_price);
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
                                    if(Double.valueOf(material[0])+i > cPower*amount) continue;
                                    else{
                                        String item_string = material[1];
                                        Material itemMaterial = Material.valueOf(item_string.toUpperCase());
                                        final ItemStack itemGiven = new ItemStack(itemMaterial, 1);
                                        final Map<Integer, ItemStack> map = p.getInventory().addItem(itemGiven);
                                        for (final ItemStack item : map.values()) {
                                            p.getWorld().dropItemNaturally(p.getLocation(), item);
                                        }
                                        i += Double.valueOf(material[0]);
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
                        CurrenciesConfig.get().set(name + ".power", Double.valueOf(Math.round((cMarketValue-(cPower*Double.valueOf(Math.round(amount*1000))/1000))/(cMarketAmount-Double.valueOf(Math.round(amount*1000))/1000)*1000))/1000*cEcoActivity);
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

    public boolean wallet(CommandSender s, Player p){
        //display currencies in your wallet
        if(p.isOnline() && p != null) {
            String user = p.getUniqueId().toString();
            if (CurrenciesConfig.get().getKeys(false).size() > 0) {
                s.sendMessage(ChatColor.GREEN + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-10"));
                for (String currencies : CurrenciesConfig.get().getKeys(false)) {
                    s.sendMessage(ChatColor.GOLD + "    " + currencies + ": " + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".message-10_1") + PlayersConfig.get().getDouble(user + ".balance." + currencies) + "\n");
                }
            } else {
                s.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-5"));
            }
        }else{
            s.sendMessage(ChatColor.DARK_RED + LanguageConfig.get().getString(LanguageConfig.get().getString("language") + ".error-9_1"));
        }
        return true;
    }
}

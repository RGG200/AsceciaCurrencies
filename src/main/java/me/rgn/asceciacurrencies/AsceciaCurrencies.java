package me.rgn.asceciacurrencies;

import me.rgn.asceciacurrencies.Listeners.JoinListener;
import me.rgn.asceciacurrencies.api.CurrenciesAPI;
import me.rgn.asceciacurrencies.api.versions.*;
import me.rgn.asceciacurrencies.files.CurrenciesConfig;
import me.rgn.asceciacurrencies.files.LanguageConfig;
import me.rgn.asceciacurrencies.commands.*;
import me.rgn.asceciacurrencies.files.PlayersConfig;
import me.rgn.asceciacurrencies.message.Messages;
import me.rgn.asceciacurrencies.web.ACWebServerController;
import org.bukkit.Bukkit;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

public final class AsceciaCurrencies extends JavaPlugin implements TabCompleter {

    public static JavaPlugin plugin;


    @Override
    public void onEnable() {
        String sVersion = Bukkit.getBukkitVersion().split("-")[0];switch (sVersion){
            case "1.20.4","1.20.3","1.20.2","1.20.1", "1.19", "1.19.2", "1.19.1", "1.18.2", "1.18.1", "1.18", "1.19.3", "1.19.4", "1.20","1.17.1","1.17":
                CurrenciesAPI.currency = new Currency1_19();
                CurrenciesAPI.team = new Team1_19();
                break;
            default:
                getLogger().severe("[Ascecia-Currencies]: Unsupported version disabling plugin...");
                Bukkit.getPluginManager().disablePlugin(this);
                break;
        }
        plugin = this;
        //init configs
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        Bukkit.getLogger().info("[Ascecia-Currencies]: Registering Events... !");
        getConfig().options().copyDefaults(true);
        getConfig().set("plugin-version", "'${project.version}'");
        saveDefaultConfig();
        CurrenciesConfig.setup();
        CurrenciesConfig.get().options().copyDefaults(true);
        CurrenciesConfig.save();
        LanguageConfig.setup();
        Messages.mlist();
        LanguageConfig.get().options().copyDefaults(true);
        PlayersConfig.setup();
        PlayersConfig.get().options().copyDefaults(true);
        PlayersConfig.save();
        CurrenciesAPI.currency.reloadConfig(Bukkit.getConsoleSender());
        //getting command
        getCommand("Currencies").setExecutor(new Currencies());
        getCommand("Currencies").setTabCompleter(new Currencies());
        //printing success
        Bukkit.getLogger().info("[Ascecia-Currencies]: Plugin Loaded !");
        //does stuff
        //init dynamic currencies
        for (String currencies : CurrenciesConfig.get().getKeys(false)) {
            int index = -1;
            if(CurrenciesAPI.currencyObjects != null){
                index = IntStream.range(0, CurrenciesAPI.currencyObjects.size())
                        .filter(i -> CurrenciesAPI.currencyObjects.get(i).currency_name.equals(currencies))
                        .findFirst()
                        .orElse(-1);
            }
            if(index == -1){
                List<Tuple<String, List<Tuple<String, Boolean>>>> team = new ArrayList<>();
                Tuple<String, List<Tuple<String, Boolean>>> player = new Tuple<>();
                List<Tuple<String, Boolean>> playerperms = new ArrayList<>();
                Tuple<String, Boolean> playerperm = new Tuple<>();
                if(CurrenciesConfig.get().getConfigurationSection(currencies + ".team") != null) {
                    for (String playerinteam : CurrenciesConfig.get().getConfigurationSection(currencies + ".team").getKeys(false)) {
                        if (playerinteam.equals(Bukkit.getOfflinePlayer(playerinteam).getName())) {
                            CurrenciesConfig.get().set(currencies + ".team." + Bukkit.getOfflinePlayer(playerinteam).getUniqueId().toString(), CurrenciesConfig.get().get(currencies + ".team." + playerinteam));
                            CurrenciesConfig.get().set(currencies + ".team." + playerinteam, null);
                            playerinteam = Bukkit.getOfflinePlayer(playerinteam).getUniqueId().toString();
                        }
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
                    CurrenciesAPI.currencyObjects.add(new CurrencyObject(currencies, CurrenciesConfig.get().getString(currencies + ".description"), CurrenciesConfig.get().getDouble(currencies + ".amount"), CurrenciesConfig.get().getDouble(currencies + ".totalvalue"), CurrenciesConfig.get().getDouble(currencies + ".power"), CurrenciesConfig.get().getDouble(currencies + ".economic-activity"), CurrenciesConfig.get().getString(currencies + ".author"), CurrenciesConfig.get().getInt(currencies + ".peers"), team));
                }
            }
        }
        for(String player: PlayersConfig.get().getKeys(false)){
            if(PlayersConfig.get().get(player + ".name") == null && !player.equals(Bukkit.getOfflinePlayer(UUID.fromString(player)).getName())){
                PlayersConfig.get().set(player + ".name", player);
            } else if (player.equals(Bukkit.getOfflinePlayer(UUID.fromString(player)).getName())) {
                PlayersConfig.get().set(Bukkit.getOfflinePlayer(player).getUniqueId().toString(), PlayersConfig.get().get(player));
            }
            if(player.equals(PlayersConfig.get().getString(player+ ".name"))) {
                PlayersConfig.get().set(Bukkit.getOfflinePlayer(player).getUniqueId().toString(), PlayersConfig.get().get(player));
                PlayersConfig.get().set(player, null);
                player = Bukkit.getOfflinePlayer(player).getUniqueId().toString();
            }
            List<Tuple<String, Double>> pwallet = new ArrayList<>();
            if(PlayersConfig.get().contains(player + ".balance")) {
                for (String cbalance : PlayersConfig.get().getConfigurationSection(player + ".balance").getKeys(false)) {
                    Tuple<String, Double> cbal = new Tuple<>();
                    cbal.First = cbalance;
                    cbal.Second = PlayersConfig.get().getDouble(player + ".balance." + cbalance);
                    pwallet.add(cbal);
                }
            }
            if(PlayersConfig.get().getString(player + ".DET") == null) {
                SimpleDateFormat DF = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
                Date date = new Date();
                CurrenciesAPI.PlayerAccounts.add(new PlayerAccount(player, DF.format(date), pwallet));
                PlayersConfig.get().set(player + ".DET", DF.format(date));
            }
            else{
                String date = PlayersConfig.get().getString(player + ".DET");
                CurrenciesAPI.PlayerAccounts.add(new PlayerAccount(player, date, pwallet));
            }
        }
        PlayersConfig.save();
        PlayersConfig.reload();
        CurrenciesConfig.save();
        CurrenciesConfig.reload();
        BukkitScheduler economic_evolution = getServer().getScheduler();
        BukkitScheduler economy_check = getServer().getScheduler();
        BukkitScheduler invites = getServer().getScheduler();
        economy_check.scheduleSyncRepeatingTask(this, new Runnable(){
            @Override
            public void run() {
                for (String currencies : CurrenciesConfig.get().getKeys(false)) {
                    int index = IntStream.range(0, CurrenciesAPI.currencyObjects.size())
                            .filter(i -> CurrenciesAPI.currencyObjects.get(i).currency_name.equals(currencies))
                            .findFirst()
                            .orElse(-1);
                    List<Tuple<String, List<Tuple<String, Boolean>>>> team = new ArrayList<>();
                    Tuple<String, List<Tuple<String, Boolean>>> player = new Tuple<>();
                    List<Tuple<String, Boolean>> playerperms = new ArrayList<>();
                    Tuple<String, Boolean> playerperm = new Tuple<>();
                    if(index != -1){
                        if(CurrenciesConfig.get().get(currencies + ".team") != null) {
                            for (String playerinteam : CurrenciesConfig.get().getConfigurationSection(currencies + ".team").getKeys(false)) {
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
                    double cMarketAmount = CurrenciesConfig.get().getDouble(currencies + ".amount");
                    double cValue = CurrenciesConfig.get().getDouble(currencies + ".totalvalue");
                    double cEcoAct = CurrenciesConfig.get().getDouble(currencies + ".economic-activity");
                    CurrenciesConfig.get().set(currencies + ".power", (Double.valueOf(Math.round((cValue / cMarketAmount) * 1000)) / 1000)*cEcoAct);
                    if (cEcoAct <= 0.1) {
                        CurrenciesConfig.get().set(currencies + ".economic-activity", 0.101);
                    }
                    CurrenciesConfig.save();
                }
            }
        },0L, 20L*60);
        economic_evolution.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                for (String currencies : CurrenciesConfig.get().getKeys(false)) {
                    double cMarketAmount = CurrenciesConfig.get().getDouble(currencies + ".amount");
                    double cValue = CurrenciesConfig.get().getDouble(currencies + ".totalvalue");
                    double cEcoAct = CurrenciesConfig.get().getDouble(currencies + ".economic-activity");
                    CurrenciesConfig.get().set(currencies + ".economic-activity", cEcoAct - 0.01*getConfig().getInt("economic-activity-decrease-rate")/10);
                    if (cEcoAct <= getConfig().getDouble("economic-activity-min")) {
                        CurrenciesConfig.get().set(currencies + ".economic-activity", getConfig().getDouble("economic-activity-min")+0.001);
                    }
                    CurrenciesConfig.get().set(currencies + ".totalvalue", cValue);
                    CurrenciesConfig.get().set(currencies + ".power", (Double.valueOf(Math.round((cValue / cMarketAmount) * 1000)) / 1000)*cEcoAct);
                    CurrenciesConfig.save();
                }
            }
        }, 0L, 20L*3600*8);
        ACWebServerController socketController = new ACWebServerController(29007);
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, socketController::transferData, 20, 20 * 15);
    }
}

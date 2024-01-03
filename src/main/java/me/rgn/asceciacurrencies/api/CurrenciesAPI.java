package me.rgn.asceciacurrencies.api;

import me.rgn.asceciacurrencies.api.versions.Currency;
import me.rgn.asceciacurrencies.api.versions.CurrencyObject;
import me.rgn.asceciacurrencies.api.versions.PlayerAccount;
import me.rgn.asceciacurrencies.api.versions.Team;
import me.rgn.asceciacurrencies.files.CurrenciesConfig;
import me.rgn.asceciacurrencies.files.LanguageConfig;
import me.rgn.asceciacurrencies.files.PlayersConfig;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class CurrenciesAPI{
    public static List<CurrencyObject> currencyObjects = new ArrayList<>();
    public static List<PlayerAccount> PlayerAccounts = new ArrayList<>();
    public static Currency currency;
    public static Team team;
    public static CurrenciesConfig currenciesConfig = new CurrenciesConfig();
    public static LanguageConfig languageConfig = new LanguageConfig();
    public static PlayersConfig playersConfig = new PlayersConfig();

}

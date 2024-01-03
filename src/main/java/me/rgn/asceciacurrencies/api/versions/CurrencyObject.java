package me.rgn.asceciacurrencies.api.versions;

import me.rgn.asceciacurrencies.files.CurrenciesConfig;

import java.util.List;

public class CurrencyObject {
    public String currency_name;
    public String description;
    public double totalamount;
    public double totalvalue;
    public double power;
    public double economic_activity;
    public String author;
    public int peers;
    public List<Tuple<String, List<Tuple<String,Boolean>>>> team;
    public CurrencyObject(String name, String description, double totalamount, double totalvalue, double power, double economic_activity, String author, int peers, List<Tuple<String, List<Tuple<String,Boolean>>>> team) {
        this.currency_name = name;
        this.description = description;
        this.totalamount = totalamount;
        this.totalvalue = totalvalue;
        this.power = power;
        this.economic_activity = economic_activity;
        this.author = author;
        this.peers = peers;
        this.team = team;
    }
    public void modify(String name, String description, double totalamount, double totalvalue, double power, double economic_activity, String author, int peers, List<Tuple<String, List<Tuple<String,Boolean>>>> team) {
        this.currency_name = name;
        this.description = description;
        this.totalamount = totalamount;
        this.totalvalue = totalvalue;
        this.power = power;
        this.economic_activity = economic_activity;
        this.author = author;
        this.peers = peers;
        this.team = team;
    }
    public void sendtoConfig() {
        CurrenciesConfig.get().set(this.currency_name + ".amount", this.totalamount);
        CurrenciesConfig.get().set(this.currency_name + ".totalvalue", this.totalvalue);
        CurrenciesConfig.get().set(this.currency_name + ".description", this.description);
        CurrenciesConfig.get().set(this.currency_name + ".power", this.power);
        CurrenciesConfig.get().set(this.currency_name + ".economic-activity", this.economic_activity);
        CurrenciesConfig.get().set(this.currency_name + ".author", this.author);
        CurrenciesConfig.get().set(this.currency_name + ".peers", this.peers);
        for(int i = 0; i < team.size(); i++){
            for (int j = 0; j < 4; j++) {
                CurrenciesConfig.get().set(this.currency_name + ".team." + this.team.get(i).First + "." + this.team.get(i).Second.get(j).First, team.get(i).Second.get(j).Second);
            }
        }
    }
}

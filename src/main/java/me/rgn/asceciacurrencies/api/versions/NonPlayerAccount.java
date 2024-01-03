package me.rgn.asceciacurrencies.api.versions;

import me.rgn.asceciacurrencies.files.PlayersConfig;

import java.util.List;

public class NonPlayerAccount {
    public String type;
    public String account_name;
    public String DET; //Date and time
    public List<Tuple<String, Double>> finances; // basically how much money does the business/state/organisation have
    public NonPlayerAccount(String name, String DET, List<Tuple<String, Double>> finances){
        this.account_name = name;
        this.DET = DET;
        this.finances = finances;
    }
    public void modify(String name, String DET, List<Tuple<String, Double>> finances){
        this.account_name = name;
        this.DET = DET;
        this.finances = finances;
    }

    public void sendToConfig(){
        PlayersConfig.get().set(this.account_name + ".DET", DET);
        for(int i = 0; i < this.finances.size(); i++){
            PlayersConfig.get().set(this.account_name + ".balance." + this.finances.get(i).First, this.finances.get(i).Second);
        }
    }

}

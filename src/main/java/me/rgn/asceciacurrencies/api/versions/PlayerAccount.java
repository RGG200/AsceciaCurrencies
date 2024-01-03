package me.rgn.asceciacurrencies.api.versions;

import me.rgn.asceciacurrencies.files.PlayersConfig;

import java.util.List;

public class PlayerAccount {
    public String player_name;
    public String DET; //Date and time
    public List<Tuple<String, Double>> wallet;
    public PlayerAccount(String name, String DET, List<Tuple<String, Double>> wallet){
        this.player_name = name;
        this.DET = DET;
        this.wallet = wallet;
    }
    public void modify(String name, String DET, List<Tuple<String, Double>> wallet){
        this.player_name = name;
        this.DET = DET;
        this.wallet = wallet;
    }

    public void sendToConfig(){
        PlayersConfig.get().set(this.player_name + ".DET", DET);
        for(int i = 0; i < this.wallet.size(); i++){
            PlayersConfig.get().set(this.player_name + ".balance." + this.wallet.get(i).First, this.wallet.get(i).Second);
        }
    }
}

package me.rgn.asceciacurrencies.web;

import be.ceau.chart.LineChart;
import be.ceau.chart.data.BarData;
import be.ceau.chart.data.LineData;
import be.ceau.chart.dataset.BarDataset;
import be.ceau.chart.dataset.LineDataset;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.rgn.asceciacurrencies.files.CurrenciesConfig;
import be.ceau.chart.color.Color;

import java.util.concurrent.CompletableFuture;

public class ACWebServerController {
    private ACWebServer socketServer;
    private final Gson gson = new Gson();

    public ACWebServerController(int port) {
        CompletableFuture.runAsync(() -> {
            socketServer = new ACWebServer(port);
            socketServer.run();
        });
    }

    public void transferData() {
        JsonElement parent = new JsonObject();
        JsonObject jsonObject = parent.getAsJsonObject();
        for(String currencies: CurrenciesConfig.get().getKeys(false)){
            jsonObject.addProperty(currencies, CurrenciesConfig.get().getDouble(currencies + ".power"));
        }
        socketServer.broadcast(gson.toJson(parent));
    }
}

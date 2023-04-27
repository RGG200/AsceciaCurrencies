package me.rgn.asceciacurrencies.web;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.rgn.asceciacurrencies.api.CurrenciesAPI;
import me.rgn.asceciacurrencies.files.CurrenciesConfig;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

public class AsceciaWebServerController {

    private AsceciaWebServer socketServer;
    private final Gson gson = new Gson();

    public AsceciaWebServerController(int port) {
        CompletableFuture.runAsync(() -> {
            socketServer = new AsceciaWebServer(port);
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

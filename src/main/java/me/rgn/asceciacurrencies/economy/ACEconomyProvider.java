package me.rgn.asceciacurrencies.economy;

import me.lokka30.treasury.api.common.NamespacedKey;
import me.lokka30.treasury.api.common.misc.TriState;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.account.AccountData;
import me.lokka30.treasury.api.economy.account.accessor.AccountAccessor;
import me.lokka30.treasury.api.economy.currency.Currency;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static me.rgn.asceciacurrencies.api.versions.Currency1_19.CurrenciesMap;

public class ACEconomyProvider implements EconomyProvider {


    @Override
    public @NotNull AccountAccessor accountAccessor() {
        return null;
    }

    @Override
    public @NotNull CompletableFuture<Boolean> hasAccount(@NotNull AccountData accountData) {
        return null;
    }

    @Override
    public @NotNull CompletableFuture<Collection<UUID>> retrievePlayerAccountIds() {
        return null;
    }

    @Override
    public @NotNull CompletableFuture<Collection<NamespacedKey>> retrieveNonPlayerAccountIds() {
        return null;
    }

    @Override
    public me.lokka30.treasury.api.economy.currency.@NotNull Currency getPrimaryCurrency() {
        return CurrenciesMap.get("defaultcurrency");
    }

    @Override
    public @NotNull Optional<me.lokka30.treasury.api.economy.currency.Currency> findCurrency(@NotNull String identifier) {
        return Optional.empty();
    }


    @Override
    public @NotNull Set<me.lokka30.treasury.api.economy.currency.Currency> getCurrencies() {
        return Set.copyOf(CurrenciesMap.values());
    }

    @Override
    public @NotNull CompletableFuture<TriState> registerCurrency(@NotNull Currency currency) {
        return null;
    }

    @Override
    public @NotNull CompletableFuture<TriState> unregisterCurrency(@NotNull Currency currency) {
        return null;
    }

}

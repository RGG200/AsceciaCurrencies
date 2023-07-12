package me.rgn.asceciacurrencies.economy;

import me.lokka30.treasury.api.economy.account.PlayerAccount;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.transaction.EconomyTransaction;
import me.rgn.asceciacurrencies.files.CurrenciesConfig;
import me.rgn.asceciacurrencies.files.PlayersConfig;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class APlayerAccount implements PlayerAccount {
    public String player_id;
    @Override
    public @NotNull Optional<String> getName() {
        return Optional.of(player_id);
    }

    @Override
    public @NotNull CompletableFuture<BigDecimal> retrieveBalance(@NotNull Currency currency) {
        return  CompletableFuture.completedFuture(BigDecimal.valueOf(PlayersConfig.get().getDouble(player_id + ".balance." + currency.getIdentifier())));
    }

    @Override
    public @NotNull CompletableFuture<BigDecimal> doTransaction(@NotNull EconomyTransaction economyTransaction) {
        switch(economyTransaction.getType()){
            case SET -> {
                Logger.getLogger("AsceciaCurrencies").severe("This type of transactions are not allowed");
                return null;
            }
            case DEPOSIT -> {
                PlayersConfig.get().set(player_id + ".balance." + economyTransaction.getCurrencyId(), PlayersConfig.get().getDouble(player_id + ".balance." + economyTransaction.getCurrencyId()) + economyTransaction.getAmount().doubleValue());
                CurrenciesConfig.get().set(economyTransaction.getCurrencyId() + ".amount", CurrenciesConfig.get().getDouble(economyTransaction.getCurrencyId() + ".amount") + economyTransaction.getAmount().doubleValue());
                CurrenciesConfig.get().set(economyTransaction.getCurrencyId() + ".totalvalue", CurrenciesConfig.get().getDouble(economyTransaction.getCurrencyId() + ".totalvalue") + economyTransaction.getAmount().doubleValue() * CurrenciesConfig.get().getDouble(economyTransaction.getCurrencyId() + ".power"));
            }
            case WITHDRAWAL -> {
                if(PlayersConfig.get().getDouble(player_id + ".balance." + economyTransaction.getCurrencyId()) > economyTransaction.getAmount().doubleValue()){
                    PlayersConfig.get().set(player_id + ".balance." + economyTransaction.getCurrencyId(), PlayersConfig.get().getDouble(player_id + ".balance." + economyTransaction.getCurrencyId()) - economyTransaction.getAmount().doubleValue());
                    CurrenciesConfig.get().set(economyTransaction.getCurrencyId() + ".amount", CurrenciesConfig.get().getDouble(economyTransaction.getCurrencyId() + ".amount") - economyTransaction.getAmount().doubleValue());
                    CurrenciesConfig.get().set(economyTransaction.getCurrencyId() + ".totalvalue", CurrenciesConfig.get().getDouble(economyTransaction.getCurrencyId() + ".totalvalue") - economyTransaction.getAmount().doubleValue() * CurrenciesConfig.get().getDouble(economyTransaction.getCurrencyId() + ".power"));
                }else{

                }
            }
        }
        return CompletableFuture.completedFuture(BigDecimal.valueOf(PlayersConfig.get().getDouble(player_id + ".balance." + economyTransaction.getCurrencyId())));
    }

    @Override
    public @NotNull CompletableFuture<Boolean> deleteAccount() {
        return null;
    }

    @Override
    public @NotNull CompletableFuture<Collection<String>> retrieveHeldCurrencies() {
        return CompletableFuture.failedFuture(new UnsupportedOperationException("Ascecia Currencies does not support the listing of help currencies instead you should look for the included command /c wallet"));
    }

    @Override
    public @NotNull CompletableFuture<Collection<EconomyTransaction>> retrieveTransactionHistory(int transactionCount, @NotNull Temporal from, @NotNull Temporal to) {
        return null;
    }

    @Override
    public @NotNull UUID identifier() {
        return UUID.fromString(player_id);
    }
}

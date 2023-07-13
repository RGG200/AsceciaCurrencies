package me.rgn.asceciacurrencies.economy;

import me.lokka30.treasury.api.common.NamespacedKey;
import me.lokka30.treasury.api.common.misc.TriState;
import me.lokka30.treasury.api.economy.account.AccountPermission;
import me.lokka30.treasury.api.economy.account.NonPlayerAccount;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.transaction.EconomyTransaction;
import me.rgn.asceciacurrencies.files.CurrenciesConfig;
import me.rgn.asceciacurrencies.files.PlayersConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class ANonPlayerAccount implements NonPlayerAccount {
    String non_player_id;
    @Override
    public @NotNull NamespacedKey identifier() {
        return null;
    }

    @Override
    public @NotNull Optional<String> getName() {
        return Optional.empty();
    }

    @Override
    public @NotNull CompletableFuture<Boolean> setName(@Nullable String name) {
        return null;
    }

    @Override
    public @NotNull CompletableFuture<BigDecimal> retrieveBalance(@NotNull Currency currency) {
        return  CompletableFuture.completedFuture(BigDecimal.valueOf(PlayersConfig.get().getDouble(non_player_id + ".balance." + currency.getIdentifier())));
    }

    @Override
    public @NotNull CompletableFuture<BigDecimal> doTransaction(@NotNull EconomyTransaction economyTransaction) {
        switch(economyTransaction.getType()){
            case SET -> {
                Logger.getLogger("AsceciaCurrencies").severe("This type of transactions are not allowed");
                return null;
            }
            case DEPOSIT -> {
                PlayersConfig.get().set(non_player_id + ".balance." + economyTransaction.getCurrencyId(), PlayersConfig.get().getDouble(non_player_id + ".balance." + economyTransaction.getCurrencyId()) + economyTransaction.getAmount().doubleValue());
                CurrenciesConfig.get().set(economyTransaction.getCurrencyId() + ".amount", CurrenciesConfig.get().getDouble(economyTransaction.getCurrencyId() + ".amount") + economyTransaction.getAmount().doubleValue());
                CurrenciesConfig.get().set(economyTransaction.getCurrencyId() + ".totalvalue", CurrenciesConfig.get().getDouble(economyTransaction.getCurrencyId() + ".totalvalue") + economyTransaction.getAmount().doubleValue() * CurrenciesConfig.get().getDouble(economyTransaction.getCurrencyId() + ".power"));
            }
            case WITHDRAWAL -> {
                if(PlayersConfig.get().getDouble(non_player_id + ".balance." + economyTransaction.getCurrencyId()) > economyTransaction.getAmount().doubleValue()){
                    PlayersConfig.get().set(non_player_id + ".balance." + economyTransaction.getCurrencyId(), PlayersConfig.get().getDouble(non_player_id + ".balance." + economyTransaction.getCurrencyId()) - economyTransaction.getAmount().doubleValue());
                    CurrenciesConfig.get().set(economyTransaction.getCurrencyId() + ".amount", CurrenciesConfig.get().getDouble(economyTransaction.getCurrencyId() + ".amount") - economyTransaction.getAmount().doubleValue());
                    CurrenciesConfig.get().set(economyTransaction.getCurrencyId() + ".totalvalue", CurrenciesConfig.get().getDouble(economyTransaction.getCurrencyId() + ".totalvalue") - economyTransaction.getAmount().doubleValue() * CurrenciesConfig.get().getDouble(economyTransaction.getCurrencyId() + ".power"));
                }else{

                }
            }
        }
        return CompletableFuture.completedFuture(BigDecimal.valueOf(PlayersConfig.get().getDouble(non_player_id + ".balance." + economyTransaction.getCurrencyId())));
    }

    @Override
    public @NotNull CompletableFuture<Boolean> deleteAccount() {
        return null;
    }

    @Override
    public @NotNull CompletableFuture<Collection<String>> retrieveHeldCurrencies() {
        return CompletableFuture.failedFuture(new UnsupportedOperationException("Ascecia Currencies does not support this feature"));
    }

    @Override
    public @NotNull CompletableFuture<Collection<EconomyTransaction>> retrieveTransactionHistory(int transactionCount, @NotNull Temporal from, @NotNull Temporal to) {
        return CompletableFuture.failedFuture(new UnsupportedOperationException("Ascecia Currencies does not support this feature"));
    }

    @Override
    public @NotNull CompletableFuture<Collection<UUID>> retrieveMemberIds() {
        return CompletableFuture.failedFuture(new UnsupportedOperationException("Ascecia Currencies does not support this feature"));
    }

    @Override
    public @NotNull CompletableFuture<Boolean> isMember(@NotNull UUID player) {
        return CompletableFuture.failedFuture(new UnsupportedOperationException("Ascecia Currencies does not support this feature"));
    }

    @Override
    public @NotNull CompletableFuture<Boolean> setPermissions(@NotNull UUID player, @NotNull Map<AccountPermission, TriState> permissionsMap) {
        return null;
    }

    @Override
    public @NotNull CompletableFuture<Map<AccountPermission, TriState>> retrievePermissions(@NotNull UUID player) {
        return null;
    }

    @Override
    public @NotNull CompletableFuture<Map<UUID, Map<AccountPermission, TriState>>> retrievePermissionsMap() {
        return null;
    }

    @Override
    public @NotNull CompletableFuture<TriState> hasPermissions(@NotNull UUID player, @NotNull AccountPermission @NotNull ... permissions) {
        return null;
    }
}

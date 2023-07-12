package me.rgn.asceciacurrencies.economy;

import me.lokka30.treasury.api.common.NamespacedKey;
import me.lokka30.treasury.api.common.misc.TriState;
import me.lokka30.treasury.api.economy.account.AccountPermission;
import me.lokka30.treasury.api.economy.account.NonPlayerAccount;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.transaction.EconomyTransaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ANonPlayerAccount implements NonPlayerAccount {
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
        return null;
    }

    @Override
    public @NotNull CompletableFuture<BigDecimal> doTransaction(@NotNull EconomyTransaction economyTransaction) {
        return null;
    }

    @Override
    public @NotNull CompletableFuture<Boolean> deleteAccount() {
        return null;
    }

    @Override
    public @NotNull CompletableFuture<Collection<String>> retrieveHeldCurrencies() {
        return null;
    }

    @Override
    public @NotNull CompletableFuture<Collection<EconomyTransaction>> retrieveTransactionHistory(int transactionCount, @NotNull Temporal from, @NotNull Temporal to) {
        return null;
    }

    @Override
    public @NotNull CompletableFuture<Collection<UUID>> retrieveMemberIds() {
        return null;
    }

    @Override
    public @NotNull CompletableFuture<Boolean> isMember(@NotNull UUID player) {
        return null;
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

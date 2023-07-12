package me.rgn.asceciacurrencies.economy;

import me.lokka30.treasury.api.economy.account.Account;
import me.lokka30.treasury.api.economy.currency.Currency;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class ACurrency implements Currency {
    public String name;
    public String symbol;
    public BigDecimal conversionRATE;

    @Override
    public @NotNull String getIdentifier() {
        return name;
    }

    @Override
    public @NotNull String getSymbol() {
        return symbol;
    }

    @Override
    public char getDecimal(@Nullable Locale locale) {
        return 0;
    }

    @Override
    public @NotNull Map<Locale, Character> getLocaleDecimalMap() {
        return Collections.emptyMap();
    }

    @Override
    public @NotNull String getDisplayName(@NotNull BigDecimal value, @Nullable Locale locale) {
        return name;
    }

    @Override
    public int getPrecision() {
        return 0;
    }

    @Override
    public boolean isPrimary() {
        return false;
    }

    @Override
    public @NotNull BigDecimal getStartingBalance(@NotNull Account account) {
        return BigDecimal.ZERO;
    }

    @Override
    public @NotNull BigDecimal getConversionRate() {
        return conversionRATE;
    }

    @Override
    public @NotNull BigDecimal to(@NotNull Currency currency, @NotNull BigDecimal amount) {
        Objects.requireNonNull(currency, "currency");
        Objects.requireNonNull(amount, "amount");

        return amount.multiply(this.getConversionRate()).divide(
                currency.getConversionRate(),
                RoundingMode.HALF_UP
        );
    }

    @Override
    public @NotNull CompletableFuture<BigDecimal> parse(@NotNull String formattedAmount, @Nullable Locale locale) {
        return null;
    }


    @Override
    public @NotNull String format(@NotNull BigDecimal amount, @Nullable Locale locale) {
        return null;
    }

    @Override
    public @NotNull String format(@NotNull BigDecimal amount, @Nullable Locale locale, int precision) {
        return null;
    }
}

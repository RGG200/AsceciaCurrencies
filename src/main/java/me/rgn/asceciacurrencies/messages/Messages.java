package me.rgn.asceciacurrencies.messages;

import me.rgn.asceciacurrencies.files.LanguageConfig;

public class Messages {
    public static void mlist() {
        LanguageConfig.get().set("language", "en-us");

        LanguageConfig.get().set("en-us.message-0", "[Currencies]: The Currency doesn't exist");
        LanguageConfig.get().set("en-us.message-1", "[Currencies]: your currency need to be 3 caracthers long minimum and 9 max and not use any special characters or numbers or spaces");
        LanguageConfig.get().set("en-us.message-2", "[Currencies]: The Currency Already Exists");
        LanguageConfig.get().set("en-us.message-3", "[Currencies]: /currencies create (currencyname)");
        LanguageConfig.get().set("en-us.message-4", "[Currencies]: You Have Already Created a currency");

        LanguageConfig.get().set("en-us.message-5", "[Currencies]: /currencies delete (currencyname)");
        LanguageConfig.get().set("en-us.message-6", "[Currencies]: Your Currency Has been deleted");
        LanguageConfig.get().set("en-us.message-7", "[Currencies]: You are not the owner of this currency !");

        LanguageConfig.get().set("en-us.message-8", "[Currencies]: /currencies description (description)");

        LanguageConfig.get().set("en-us.message-9", "[Currencies]: /currencies force delete (currencyname)");
        LanguageConfig.get().set("en-us.message-10", "[Currencies]: You deleted a Currency and no ores will be given.");

        LanguageConfig.get().set("en-us.message-11", "[Currencies]: This Currency Doesn't exist");
        LanguageConfig.get().set("en-us.message-12", "[Currencies]: /currencies info (currencyname)");

        LanguageConfig.get().set("en-us.message-13", "[Currencies]: No currencies available. create one with /currencies create");

        LanguageConfig.get().set("en-us.message-14", "[Currencies]: /currency make (amount)");

        LanguageConfig.get().set("en-us.message-15", "[Currencies]: You Made ");
        LanguageConfig.get().set("en-us.message-16", " ");
        LanguageConfig.get().set("en-us.message-17", "[Currencies]: you don't have a currency. create one using /currencies create");

        LanguageConfig.get().set("en-us.message-18", "you need to make at least one of your currency do /currencies make (name) (amount) !");

        LanguageConfig.get().set("en-us.message-19", "[Currencies]: ");
        LanguageConfig.get().set("en-us.message-20", " iron Worth of ores to your currency");
        LanguageConfig.get().set("en-us.message-21", "Material not supported");

        LanguageConfig.get().set("en-us.message-22", "/currencypay (playername) (currencyname) (amount)");
        LanguageConfig.get().set("en-us.message-23", "The Player Specified isn't Online");
        LanguageConfig.get().set("en-us.message-24", "The Currency Specified doesn't exist");
        LanguageConfig.get().set("en-us.message-25", "You succesfully payed ");
        LanguageConfig.get().set("en-us.message-26", "You succesfully payed ");
        LanguageConfig.get().set("en-us.message-27", "to ");
        LanguageConfig.get().set("en-us.message-28", "from ");
        LanguageConfig.get().set("en-us.message-29", "You do not have enough money");

        LanguageConfig.get().set("en-us.message-30", "[Currencies]: /currencies withdraw (currencyname) (amount)");
        LanguageConfig.get().set("en-us.message-31", "[Currencies]: You Withdrew ");
        LanguageConfig.get().set("en-us.message-32", "[Currencies]: The amount entered is too low !");

    }
}

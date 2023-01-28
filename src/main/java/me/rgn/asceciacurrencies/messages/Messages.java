package me.rgn.asceciacurrencies.messages;

import me.rgn.asceciacurrencies.files.LanguageConfig;

public class Messages {
    public static void mlist() {
        LanguageConfig.get().set("language", "en-us");

        LanguageConfig.get().set("en-us.message-0", "[Currencies]: The Currency ");
        LanguageConfig.get().set("en-us.message-0_1", " Has been Created ");

        LanguageConfig.get().set("en-us.message-1", "[Currencies]: Your Currency Has been deleted");
        LanguageConfig.get().set("en-us.message-1_1", "[Currencies]: You deleted a Currency and no ores will be given.");

        LanguageConfig.get().set("en-us.message-2", "[Currencies]: You Made ");

        LanguageConfig.get().set("en-us.message-3", "[Currencies]: You deposited");
        LanguageConfig.get().set("en-us.message-3_1", " iron Worth of ores to your currency");

        LanguageConfig.get().set("en-us.message-4", "You succesfully payed ");
        LanguageConfig.get().set("en-us.message-4_1", "You succesfully recieved ");
        LanguageConfig.get().set("en-us.message-4_2", "to ");
        LanguageConfig.get().set("en-us.message-4_3", "from ");

        LanguageConfig.get().set("en-us.message-5", "[Currencies]: You Withdrew ");

        LanguageConfig.get().set("en-us.message-6", "[Currencies]: Description set ! ");


        LanguageConfig.get().set("en-us.error-0", "[Currencies]: /currencies create (currencyname)");
        LanguageConfig.get().set("en-us.error-0_1", "[Currencies]: your currency need to be 3 caracthers long minimum and 9 max and not use any special characters or numbers or spaces");
        LanguageConfig.get().set("en-us.error-0_2", "[Currencies]: The Currency Already Exists");
        LanguageConfig.get().set("en-us.error-0_3", "[Currencies]: The Currency doesn't exist");
        LanguageConfig.get().set("en-us.error-0_4", "[Currencies]: You Have Already Created a currency");

        LanguageConfig.get().set("en-us.error-1", "[Currencies]: /currencies delete (currencyname)");
        LanguageConfig.get().set("en-us.error-1_1", "[Currencies]: You are not the owner of this currency !");

        LanguageConfig.get().set("en-us.error-2", "[Currencies]: /currencies description (description)");

        LanguageConfig.get().set("en-us.error-3", "[Currencies]: /currencies force delete (currencyname)");

        LanguageConfig.get().set("en-us.error-4", "[Currencies]: /currencies info (currencyname)");
        LanguageConfig.get().set("en-us.error-4_1", "[Currencies]: This Currency Doesn't exist");

        LanguageConfig.get().set("en-us.error-5", "[Currencies]: No currencies available. create one with /currencies create");

        LanguageConfig.get().set("en-us.error-6", "[Currencies]: /currency mint (amount)");

        LanguageConfig.get().set("en-us.error-7", "[Currencies]: you don't have a currency. create one using /currencies create");

        LanguageConfig.get().set("en-us.error-8", "you need to make at least one of your currency do /currencies make (name) (amount) !");
        LanguageConfig.get().set("en-us.error-8_1", "Material not supported");

        LanguageConfig.get().set("en-us.error-9", "/currencypay (playername) (currencyname) (amount)");
        LanguageConfig.get().set("en-us.error-9_1", "[Currencies]: The Player Specified isn't Online");
        LanguageConfig.get().set("en-us.error-9_2", "[Currencies]: The Currency Specified doesn't exist");
        LanguageConfig.get().set("en-us.error-9_3", "[Currencies]: You do not have enough money");
        LanguageConfig.get().set("en-us.error-9_4", "[Currencies]: You Can't give yourself money");

        LanguageConfig.get().set("en-us.error-10", "[Currencies]: /currencies withdraw (currencyname) (amount)");
        LanguageConfig.get().set("en-us.error-10_1", "[Currencies]: The amount entered is too low !");

    }
}

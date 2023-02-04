package me.rgn.asceciacurrencies.messages;

import me.rgn.asceciacurrencies.files.LanguageConfig;

public class Messages {
    public static void mlist() {
        //default language
        if (!LanguageConfig.get().contains("language")){
            LanguageConfig.get().set("language", "en-us");
        }

        //messages in english
        LanguageConfig.get().set("en-us.message-0", "[Currencies]: The Currency ");
        LanguageConfig.get().set("en-us.message-0_1", " Has been Created ");

        LanguageConfig.get().set("en-us.message-1", "[Currencies]: Your Currency Has been deleted");
        LanguageConfig.get().set("en-us.message-1_1", "[Currencies]: You deleted a Currency and no ores will be given.");

        LanguageConfig.get().set("en-us.message-2", "[Currencies]: You minted ");

        LanguageConfig.get().set("en-us.message-3", "[Currencies]: You deposited");
        LanguageConfig.get().set("en-us.message-3_1", " iron Worth of ores to your currency");

        LanguageConfig.get().set("en-us.message-4", "You succesfully payed ");
        LanguageConfig.get().set("en-us.message-4_1", "You succesfully recieved ");
        LanguageConfig.get().set("en-us.message-4_2", "to ");
        LanguageConfig.get().set("en-us.message-4_3", "from ");

        LanguageConfig.get().set("en-us.message-5", "[Currencies]: You Withdrew ");

        LanguageConfig.get().set("en-us.message-6", "[Currencies]: Description set ! ");

        LanguageConfig.get().set("en-us.message-7", "| Ascecia Currencies | Help | \n \n /currencies create (name) - creates a currency  \n \n /currencies delete (name) - deletes your currency \n \n /currencies withdraw (name) (amount) - turn back an amount of your currency into iron \n \n /currencies info (name) - gives you info about a currency \n \n /currencies list - gives you a list of all currencies available \n \n /currencies mint (amount) - makes an amount of currency \n \n /currencies deposit - deposit the amount of ores you're holding in your hand into your currency to increase its power \n \n /currencies pay (playername) (name) (amount) - pays the target with an amount of currency \n \n /currencies wallet - give you details about your wallet");

        LanguageConfig.get().set("en-us.message-8", "| Currency Info -> | ");
        LanguageConfig.get().set("en-us.message-8_1", " | \n \n Amount of Currency available on the market: ");
        LanguageConfig.get().set("en-us.message-8_2", "\n Power of the currency: ");
        LanguageConfig.get().set("en-us.message-8_3", "\n Number of users: ");
        LanguageConfig.get().set("en-us.message-8_4", "\n Economic-activity of the currency: ");
        LanguageConfig.get().set("en-us.message-8_5", "\n Total value of the currency: ");
        LanguageConfig.get().set("en-us.message-8_6", "\n Author: ");
        LanguageConfig.get().set("en-us.message-8_7", "\n Description: ");

        LanguageConfig.get().set("en-us.message-9","\n | Ascecia-Currencies | Currency-List | \n \n");
        LanguageConfig.get().set("en-us.message-9_1",": \n      Power: ");
        LanguageConfig.get().set("en-us.message-9_2","\n        Economic-Activity: ");

        LanguageConfig.get().set("en-us.message-10","\n | Ascecia-Currencies | Wallet | \n \n");
        LanguageConfig.get().set("en-us.message-10_1",": \n     Balance: ");

        //errors in english
        LanguageConfig.get().set("en-us.error-0", "[Currencies]: /currencies create (currencyname)");
        LanguageConfig.get().set("en-us.error-0_1", "[Currencies]: Your currency needs to be 3 caracthers long minimum and 9 max and not use any special characters or numbers or spaces");
        LanguageConfig.get().set("en-us.error-0_2", "[Currencies]: The Currency Already Exists");
        LanguageConfig.get().set("en-us.error-0_3", "[Currencies]: You Have Already Created a currency");

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
        LanguageConfig.get().set("en-us.error-8_1", "The chosen material is not an ore and thus can't be deposited in your currency !");

        LanguageConfig.get().set("en-us.error-9", "/currencypay (playername) (currencyname) (amount)");
        LanguageConfig.get().set("en-us.error-9_1", "[Currencies]: The Player Specified isn't Online");
        LanguageConfig.get().set("en-us.error-9_2", "[Currencies]: The Currency Specified doesn't exist");
        LanguageConfig.get().set("en-us.error-9_3", "[Currencies]: You do not have enough money");
        LanguageConfig.get().set("en-us.error-9_4", "[Currencies]: You Can't give yourself money");

        LanguageConfig.get().set("en-us.error-10", "[Currencies]: /currencies withdraw (currencyname) (amount)");
        LanguageConfig.get().set("en-us.error-10_1", "[Currencies]: The amount entered is too low !");
        LanguageConfig.get().set("en-us.error-10_2", "[Currencies]: The Currency specified does not exist !");
        LanguageConfig.get().set("en-us.error-10_3", "[Currencies]: You do not have enough money !");

        //messages en français
        LanguageConfig.get().set("fr-FR.message-0", "[Currencies]: La monnaie ");
        LanguageConfig.get().set("fr-FR.message-0_1", " à été crée ");

        LanguageConfig.get().set("fr-FR.message-1", "[Currencies]: Votre monnaie à été supprimée");
        LanguageConfig.get().set("fr-FR.message-1_1", "[Currencies]: Vous avez supprimé(e) une monnaie et aucun minerais ne seront distribués");

        LanguageConfig.get().set("fr-FR.message-2", "[Currencies]: Vous avez frappé(e) ");

        LanguageConfig.get().set("fr-FR.message-3", "[Currencies]: Vous avez déposé(e) l'équivalent de");
        LanguageConfig.get().set("fr-FR.message-3_1", " en fer");

        LanguageConfig.get().set("fr-FR.message-4", "Vous avez payé(e) ");
        LanguageConfig.get().set("fr-FR.message-4_1", "Vous avez recu(e) ");
        LanguageConfig.get().set("fr-FR.message-4_2", "à ");
        LanguageConfig.get().set("fr-FR.message-4_3", "de ");

        LanguageConfig.get().set("fr-FR.message-5", "[Currencies]: Vous avez repris ");

        LanguageConfig.get().set("fr-FR.message-6", "[Currencies]: Description définie ! ");

        LanguageConfig.get().set("fr-FR.message-7", "| Ascecia Currencies | Aide | \n \n /currencies create (nom) - creer une monnaie  \n \n /currencies delete (nom) - supprime une monnaie \n \n /currencies withdraw (nom) (montant) - vous redonne des minerais que vous avez mis en fonction du montant entré \n \n /currencies info (nom) - une liste d'infos sur une monnaie \n \n /currencies list - liste de toutes les monnaies existante \n \n /currencies mint (montant) - frappe un montant de monnaie \n \n /currencies deposit - depose le nombre de minerais que vous tenez dans votre main pour augmenter la valeur et la puissance de votre monnaie \n \n /currencies pay (nom du joueur) (nom de la monnaie) (montant) - paye la personne concernée \n \n /currencies wallet - vous montre votre porte-feuille");

        LanguageConfig.get().set("fr-FR.message-8", "| Monnaie Info -> | ");
        LanguageConfig.get().set("fr-FR.message-8_1", " | \n \n Montant de monnaie disponible sur le marché: ");
        LanguageConfig.get().set("fr-FR.message-8_2", "\n Puissance de la monnaie: ");
        LanguageConfig.get().set("fr-FR.message-8_3", "\n Nombre d'utilisateurs: ");
        LanguageConfig.get().set("fr-FR.message-8_4", "\n Activité economique de la monnaie: ");
        LanguageConfig.get().set("fr-FR.message-8_5", "\n Valeur totale de la monnaie: ");
        LanguageConfig.get().set("fr-FR.message-8_6", "\n Auteur: ");
        LanguageConfig.get().set("fr-FR.message-8_7", "\n Description: ");

        LanguageConfig.get().set("fr-FR.message-9","\n | Ascecia-Currencies | Liste des monnaies | \n \n");
        LanguageConfig.get().set("fr-FR.message-9_1",": \n      Puissance: ");
        LanguageConfig.get().set("fr-FR.message-9_2","\n        Activité Economique: ");

        LanguageConfig.get().set("fr-FR.message-10","\n | Ascecia-Currencies | Porte-feuille | \n \n");
        LanguageConfig.get().set("fr-FR.message-10_1"," \n    Solde: ");


        //erreur en français
        LanguageConfig.get().set("fr-FR.error-0", "[Currencies]: /currencies create (nom)");
        LanguageConfig.get().set("fr-FR.error-0_1", "[Currencies]: le nom de votre monnaie doit faire minimum 3 caractères et max 9 et il ne doit contenir ni nombre ni caractères spéciaux.");
        LanguageConfig.get().set("fr-FR.error-0_2", "[Currencies]: Une monnaie du même nom existe déja");
        LanguageConfig.get().set("fr-FR.error-0_3", "[Currencies]: Vous avez déja crée une monnaie");

        LanguageConfig.get().set("fr-FR.error-1", "[Currencies]: /currencies delete (nom)");
        LanguageConfig.get().set("fr-FR.error-1_1", "[Currencies]: Vous ne possèdez pas cette monnaie ou la monnaie n'éxiste pas !");

        LanguageConfig.get().set("fr-FR.error-2", "[Currencies]: /currencies description (description)");

        LanguageConfig.get().set("fr-FR.error-3", "[Currencies]: /currencies force delete (nom)");

        LanguageConfig.get().set("fr-FR.error-4", "[Currencies]: /currencies info (nom)");
        LanguageConfig.get().set("fr-FR.error-4_1", "[Currencies]: La monnaie spécifiée n'éxiste pas");

        LanguageConfig.get().set("fr-FR.error-5", "[Currencies]: Aucune monnaie disponible. Créez-en une avec /currencies create");

        LanguageConfig.get().set("fr-FR.error-6", "[Currencies]: /currency mint (montant)");

        LanguageConfig.get().set("fr-FR.error-7", "[Currencies]: Vous ne possèdez pas de monnaie. Créez-en une avec /currencies create");

        LanguageConfig.get().set("fr-FR.error-8", " Vous devez avoir frapper minimum 1 de votre monnaie pour pouvoir deposer des minerais /currencies make (nom) (montant) !");
        LanguageConfig.get().set("fr-FR.error-8_1", "Le materiaux tenu dans votre main n'est pas un minerais !");

        LanguageConfig.get().set("fr-FR.error-9", "/currencypay (nom du joueur) (nom de la monnaie) (montant)");
        LanguageConfig.get().set("fr-FR.error-9_1", "[Currencies]: Le joueur n'est pas en ligne");
        LanguageConfig.get().set("fr-FR.error-9_2", "[Currencies]: La monnaie spécifiée n'éxiste pas");
        LanguageConfig.get().set("fr-FR.error-9_3", "[Currencies]: Vous n'avez pas assez d'argent pour payer !");
        LanguageConfig.get().set("fr-FR.error-9_4", "[Currencies]: Vous ne pouvez pas vous donner de l'argent");

        LanguageConfig.get().set("fr-FR.error-10", "[Currencies]: /currencies withdraw (nom) (montant)");
        LanguageConfig.get().set("fr-FR.error-10_1", "[Currencies]: Le montant entré est trop faible");
        LanguageConfig.get().set("fr-FR.error-10_2", "[Currencies]: La monnaie spécifiée n'éxiste pas");
        LanguageConfig.get().set("fr-FR.error-10_3", "[Currencies]: Vous ne possèdez pas assez d'argent");
        LanguageConfig.save();
    }
}

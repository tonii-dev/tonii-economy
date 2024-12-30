package io.github.toniidev.ToniiEconomy.managers.specific;

import io.github.toniidev.ToniiEconomy.items.CreditCard;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Item stack instances are not temporary: it means that we can't add their builders into a list and handle them like inventories, by setting an action to
 * execute in the builder and making the manager wait for a specific event then running that specified action. We must handle everything every time specifically.
 * This manager, just like other specific ones, just gives us the opportunity to check things faster.
 * For these reasons, we will handle ItemStack events in the listeners folder.
 */
public class CreditCardManager {
    public static List<CreditCard> creditCards = new ArrayList<>();

    /**
     * Streams creditCards list and filters the results to find a CreditCard that has the
     * specified code
     * @param number The code of the CreditCard of which we need to find the instance
     * @return null if there is no CreditCard with that specified code, the CreditCard instance if there is one.
     */
    @Nullable
    public static CreditCard getCreditCardInstance(String number){
        return creditCards.stream()
                .filter(x -> x.getCode().equals(number))
                .findFirst().orElse(null);
    }

    /**
     * Uses getCreditCardInstance() <- getCardNumberFromItemStack() <- given ItemStack to get a CreditCardInstance
     * starting from an ItemStack
     * @param itemStack The ItemStack we must get the CreditCard instance of.
     * @return null if there is that ItemStack is not a CreditCard, the CreditCard instance if that's a CreditCard
     */
    @Nullable
    public static CreditCard getCreditCardInstance(ItemStack itemStack){
        return getCreditCardInstance(getCardNumberFromItemStack(itemStack));
    }

    /**
     * Gets the credit card number starting from an ItemStack.
     * It makes it by checking the seventh (6 for Java) lore line and isolating the code just by replacing other contents that should be
     * in that line if the ItemStack was a credit card
     * @param stack The CreditCard ItemStack we must get the code of.
     * @return null if the ItemStack is not a CreditCard, the code of the card if the ItemStack is a CreditCard
     */
    @Nullable
    public static String getCardNumberFromItemStack(ItemStack stack){
        ItemMeta meta = stack.getItemMeta();
        if(meta == null) return null;
        if(meta.getLore() == null) return null;
        if(meta.getLore().get(6) == null) return null;

        return meta.getLore().get(6)
                .replace("§7", "")
                .replace("§r", "")
                .replace("§f", "")
                .replace("Numero: ", "");
    }

    /**
     * Says whether an ItemStack is a CreditCard or not.
     * It can determine it by calling getCreditCardInstance(), a method that streams creditCards list and checks if there is one with the specified code.
     * It gets the credit card code from ItemStack by using getCardNumberFromItemStack()
     * If the given output is null, it means that the ItemStack is not a credit card.
     * @param itemStack The stack that we must check whether is a credit card or not
     * @return true if the ItemStack is a credit card, false if it's not.
     */
    public static boolean isCreditCard(ItemStack itemStack){
        return getCreditCardInstance(getCardNumberFromItemStack(itemStack)) != null;
    }
}

package io.github.toniidev.ToniiEconomy.items;

import io.github.toniidev.ToniiEconomy.builders.ItemStackFactory;
import io.github.toniidev.ToniiEconomy.enums.CustomItem;
import io.github.toniidev.ToniiEconomy.managers.specific.CreditCardManager;
import io.github.toniidev.ToniiEconomy.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class CreditCard {
    private final String code;
    private String pwd = "";
    private UUID owner;
    private double money = 0;

    private final ItemStackFactory baseItem = new ItemStackFactory(Material.GREEN_DYE)
            .setName("§6Carta di credito")
            .setCustomItem(CustomItem.CREDIT_CARD)
            .addLoreLine("Una carta di credito per fare acquisti")
            .addLoreLine("in modo semplice e veloce, gestire il tuo")
            .addLoreLine("denaro e tenerlo al sicuro.")
            .addBlankLoreLine()
            .addLoreLine("§eGestione carta §6§lTASTO DESTRO");

    private ItemStackFactory current;

    public CreditCard(){
        code = StringUtils.generateCode();
        refreshItemStack();
        CreditCardManager.creditCards.add(this);
    }

    /**
     * Makes this card able to be used, and it replaces the ItemStack the player has in main hand (the old card) with the new one (that is active).
     * Everything keeps being as it used to be, the only thing that changes is the lore.
     * @param password The password that the player has submitted
     * @param whoActivated The player that has just set the password of this card is the card owner
     */
    public CreditCard activate(String password, Player whoActivated){
        pwd = password;
        owner = whoActivated.getUniqueId();
        refreshItemStack();
        whoActivated.getInventory().setItemInMainHand(current.get());
        return this;
    }

    /**
     * Transforms this CreditCard instance into an ItemStack.
     * @return An ItemStack with the characteristics of this CreditCard instance
     */
    public ItemStack toItemStack(){
        return current.get();
    }

    public void refreshItemStack(){
        current = new ItemStackFactory(baseItem.get().clone())
                .addBlankLoreLine()
                .addLoreLine("Numero: §f" + code)
                .addLoreLine("Password: §f" + (pwd.isBlank() ? "Non impostata" : "Impostata"))
                .addLoreLine("Proprietario: §f" + (owner == null ? "Nessuno" : Bukkit.getPlayer(owner).getDisplayName()))
                .addLoreLine("Stato: " + (isActive() ? "§aAttiva" : "§cInattiva"))
                .setGlowing(isActive());
    }

    public String getCode() {
        return code;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public UUID getOwner() {
        return owner;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public void pay(double amount){
        money -= amount;
    }

    public void receive(double amount){
        money += amount;
    }

    /**
     * A CreditCard is active if it has a set password and a set owner.
     * @return true if it is active, false if it is not
     */
    public boolean isActive(){
        return owner != null && !pwd.isBlank();
    }
}

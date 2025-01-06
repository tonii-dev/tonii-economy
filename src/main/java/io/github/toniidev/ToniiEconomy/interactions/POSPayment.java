package io.github.toniidev.ToniiEconomy.interactions;

import io.github.toniidev.ToniiEconomy.builders.InventoryFactory;
import io.github.toniidev.ToniiEconomy.builders.ItemStackFactory;
import io.github.toniidev.ToniiEconomy.builders.SignInputFactory;
import io.github.toniidev.ToniiEconomy.enums.CVVStatus;
import io.github.toniidev.ToniiEconomy.enums.ErrorMessage;
import io.github.toniidev.ToniiEconomy.enums.GlassType;
import io.github.toniidev.ToniiEconomy.enums.PaymentStatus;
import io.github.toniidev.ToniiEconomy.interfaces.ToniiInventoryInterface;
import io.github.toniidev.ToniiEconomy.interfaces.ToniiSignInterface;
import io.github.toniidev.ToniiEconomy.items.CreditCard;
import io.github.toniidev.ToniiEconomy.managers.InventoryManager;
import io.github.toniidev.ToniiEconomy.managers.specific.CreditCardManager;
import io.github.toniidev.ToniiEconomy.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class POSPayment {
    private final Plugin plugin;
    private final Player owner;
    private final Player payer;
    private final String date;

    private CVVStatus payerCVVStatus = CVVStatus.ABSENT;
    private PaymentStatus ownerPaymentStatus = PaymentStatus.WAITING;

    private double amount;

    /**
     * A POSPayment instance
     * @param whoGetsMoney The owner of the POS
     * @param whoPays The client
     * @param amountToPay The amount the client has to pay to the owner
     * @param main The main class instance
     */
    public POSPayment(Player whoGetsMoney, Player whoPays, double amountToPay, Plugin main) {
        this.owner = whoGetsMoney;
        this.payer = whoPays;
        this.amount = amountToPay;
        this.plugin = main;

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = now.format(dateFormatter);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String formattedTime = now.format(timeFormatter);

        date = "§f" + formattedDate + " §7ora§f " + formattedTime;
    }

    public void show(){
        payer.openInventory(getPayerGUIFactory().getInventory());
        owner.openInventory(getOwnerGUIFactory().getInventory());
    }

    public InventoryFactory getOwnerGUIFactory(){
        return new InventoryFactory("Pagamento POS", 1, plugin)
                .setItem(0, new ItemStackFactory(ItemUtils.getPlayerSkull(payer))
                        .setName("§6Cliente")
                        .addLoreLine("Questo giocatore sta attualmente")
                        .addLoreLine("interagendo con questo POS.")
                        .addBlankLoreLine()
                        .addLoreLine("Nickname: §f" + payer.getDisplayName())
                        .addLoreLine("Stato pagamento: " + ownerPaymentStatus.getString())
                        .get())
                .setItem(7, new ItemStackFactory(new ItemStack(Material.FILLED_MAP))
                        .setName("§bPagamento")
                        .addLoreLine("Informazioni su questo pagamento")
                        .addBlankLoreLine()
                        .addLoreLine("Importo: §f" + amount + "€")
                        .addLoreLine("Quando: " + date)
                        .get())
                .setItem(8, new ItemStackFactory(new ItemStack(Material.RED_STAINED_GLASS_PANE))
                        .setName("§cAnnulla")
                        .addLoreLine("Questo pagamento verrà annullato.")
                        .get())
                .setAction(8, new ToniiInventoryInterface() {
                    @Override
                    public void run(InventoryClickEvent e) {
                        getPayerGUIFactory().enableClose();
                        getOwnerGUIFactory().enableClose();
                        payer.closeInventory();
                        owner.closeInventory();
                        InventoryManager.inventoryBuilders.remove(getOwnerGUIFactory());
                        InventoryManager.inventoryBuilders.remove(getPayerGUIFactory());
                    }
                })
                .disableClicks()
                .disableClose()
                .fillWithGlass(GlassType.WHITE);
    }

    public InventoryFactory getPayerGUIFactory(){
        return new InventoryFactory("Pagamento POS", 1, plugin)
                .setItem(0, new ItemStackFactory(payer.getInventory().getItemInMainHand())
                        .setName("§6Carta inserita")
                        .get())
                .setItem(1, new ItemStackFactory(switch (payerCVVStatus) {
                    case ABSENT -> Material.ORANGE_STAINED_GLASS_PANE;
                    case WRONG -> Material.RED_STAINED_GLASS_PANE;
                    case RIGHT -> Material.GREEN_STAINED_GLASS_PANE;
                })
                        .setName("§eInserisci password")
                        .addLoreLine("Inserisci la password che il proprietario della carta")
                        .addLoreLine("ha impostato quando l'ha attivata")
                        .addBlankLoreLine()
                        .addLoreLine("Stato CVV: " + payerCVVStatus.getString())
                        .get())
                .setAction(1, new ToniiInventoryInterface() {
                    @Override
                    public void run(InventoryClickEvent e) {
                        InventoryFactory builder = InventoryManager.getBuilder(e.getClickedInventory());
                        if(builder == null) return;
                        builder.enableClose();
                        new SignInputFactory((Player) e.getWhoClicked(), "-----------------", "Inserisci", "la password")
                                .setActionToExecute(new ToniiSignInterface() {
                                    @Override
                                    public void run(String input, Player player) {
                                        if(input.equals(CreditCardManager.getCardNumberFromItemStack(player.getInventory().getItemInMainHand()))){
                                            payerCVVStatus = CVVStatus.RIGHT;
                                            ownerPaymentStatus = PaymentStatus.PROCESSING;
                                            InventoryManager.inventoryBuilders.remove(getOwnerGUIFactory());
                                            owner.openInventory(getOwnerGUIFactory().getInventory());
                                        }
                                        else payerCVVStatus = CVVStatus.WRONG;
                                        player.openInventory(getPayerGUIFactory().getInventory());
                                    }
                                }).showSign(plugin);
                    }
                })
                .setItem(7, new ItemStackFactory(new ItemStack(Material.FILLED_MAP))
                        .setName("§bPagamento")
                        .addLoreLine("Informazioni su questo pagamento")
                        .addBlankLoreLine()
                        .addLoreLine("Importo: §f" + amount + "€")
                        .addLoreLine("Quando: " + date)
                        .get())
                .setItem(8, new ItemStackFactory(new ItemStack(Material.GREEN_STAINED_GLASS_PANE))
                        .setName("§aConferma")
                        .addLoreLine("Questo pagamento verrà confermato.")
                        .get())
                .setAction(8, new ToniiInventoryInterface() {
                    @Override
                    public void run(InventoryClickEvent e) {
                        if(payerCVVStatus.equals(CVVStatus.WRONG)){
                            payer.sendMessage(ErrorMessage.WRONG_PASSWORD.getMessage());
                        }
                        else if(payerCVVStatus.equals(CVVStatus.ABSENT)){
                            payer.sendMessage(ErrorMessage.NO_PASSWORD.getMessage());
                        }
                        else if(payerCVVStatus.equals(CVVStatus.RIGHT)){
                            CreditCard card = CreditCardManager.getCreditCardInstance(payer.getInventory().getItemInMainHand());
                            if(card == null) return;
                            if(card.getMoney() < amount) {
                                payer.sendMessage(ErrorMessage.PAYMENT_REJECTED.getMessage());
                                ownerPaymentStatus = PaymentStatus.DECLINED;
                                InventoryManager.inventoryBuilders.remove(getOwnerGUIFactory());
                                owner.openInventory(getOwnerGUIFactory().getInventory());
                            }
                            else {
                                card.pay(amount);
                                ownerPaymentStatus = PaymentStatus.ACCEPTED;
                                InventoryManager.inventoryBuilders.remove(getOwnerGUIFactory());
                                owner.openInventory(getOwnerGUIFactory().getInventory());
                            }

                            InventoryFactory builder = InventoryManager.getBuilder(e.getClickedInventory());
                            if(builder == null) return;
                            builder.enableClose();
                            payer.closeInventory();
                        }
                    }
                })
                .disableClicks()
                .disableClose()
                .fillWithGlass(GlassType.WHITE);
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double value){
        this.amount = value;
    }

    public Player getOwner() {
        return owner;
    }

    public Player getPayer() {
        return payer;
    }
}

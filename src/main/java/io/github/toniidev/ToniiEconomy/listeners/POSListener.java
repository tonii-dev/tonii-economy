package io.github.toniidev.ToniiEconomy.listeners;

import io.github.toniidev.ToniiEconomy.builders.InventoryFactory;
import io.github.toniidev.ToniiEconomy.builders.ItemStackFactory;
import io.github.toniidev.ToniiEconomy.builders.SignInputFactory;
import io.github.toniidev.ToniiEconomy.enums.CustomItem;
import io.github.toniidev.ToniiEconomy.enums.ErrorMessage;
import io.github.toniidev.ToniiEconomy.enums.GlassType;
import io.github.toniidev.ToniiEconomy.interactions.POSPayment;
import io.github.toniidev.ToniiEconomy.interfaces.ToniiInventoryInterface;
import io.github.toniidev.ToniiEconomy.interfaces.ToniiSignInterface;
import io.github.toniidev.ToniiEconomy.managers.InventoryManager;
import io.github.toniidev.ToniiEconomy.managers.SignInputManager;
import io.github.toniidev.ToniiEconomy.managers.specific.CreditCardManager;
import io.github.toniidev.ToniiEconomy.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import java.text.ParseException;

public class POSListener implements Listener {
    private final Plugin plugin;

    public POSListener(Plugin main){
        plugin = main;
    }

    /**
     * Handle to start POS interaction
     */
    @EventHandler
    public void onOwnerClickOnEntity(PlayerInteractAtEntityEvent e){
        if(e.getPlayer().getInventory().getItemInMainHand().getItemMeta() == null) return;
        if(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getCustomModelData() != CustomItem.POS.getCustomModelDataValue()) return; // owner is not holding a pos
        if(!(e.getRightClicked() instanceof Player)) return; // owner did not click an entity

        Player victim = (Player) e.getRightClicked();
        if(!CreditCardManager.isCreditCard(victim.getInventory().getItemInMainHand())){
            e.getPlayer().sendMessage(ErrorMessage.ENTITY_NOT_HOLDING_CC.getMessage());
            return;
        }

        // now owner has to choose the payment amount
        /*new SignInputFactory(e.getPlayer(), "-----------------", "Inserisci", "l'importo")
                .setActionToExecute(new ToniiSignInterface() {
                    @Override
                    public void run(String input, Player player) {
                        player.sendMessage(input);
                        //new POSPayment(e.getPlayer(), victim, amount, plugin).show();
                    }
                })
                .showSign(plugin);*/
        e.getPlayer().openInventory(getInitializePOSPaymentGUI(new POSPayment(e.getPlayer(), victim, 0, plugin)));
    }

    public Inventory getInitializePOSPaymentGUI(POSPayment payment){
        /*return new InventoryFactory("POS", 1, plugin)
                .setItem(0, new ItemStackFactory(ItemUtils.getPlayerSkull(payment.getPayer()))
                        .setName("§6Cliente")
                        .addLoreLine("A questo giocatore sarà")
                        .addLoreLine("richiesto il pagamento.")
                        .addBlankLoreLine()
                        .addLoreLine("Nickname: §f" + payment.getPayer().getDisplayName())
                        .get())
                .setItem(4, new ItemStackFactory(Material.OAK_SIGN)
                        .setName("§eScegli l'importo")
                        .addLoreLine("Questo importo dovrà essere pagato")
                        .addLoreLine("dal cliente. I soldi ti arriveranno")
                        .addLoreLine("sulla carta che sceglierai dopo.")
                        .addBlankLoreLine()
                        .addLoreLine("§bImporto scelto: §e" + payment.getAmount() + "€")
                        .get())
                .setAction(4, new ToniiInventoryInterface() {
                    @Override
                    public void run(InventoryClickEvent e) {
                        new SignInputFactory((Player) e.getWhoClicked(), "-----------------", "Scegli la password", "di questa carta")
                                .setActionToExecute(new ToniiSignInterface() {
                                    @Override
                                    public void run(String input, Player player) {
                                        payment.setAmount(Double.parseDouble(input));
                                        payment.getOwner().openInventory(getInitializePOSPaymentGUI(payment));
                                    }
                                })
                                .showSign(plugin);
                    }
                })
                .setItem(8, new ItemStackFactory(Material.GREEN_STAINED_GLASS_PANE)
                        .setName("§aConferma")
                        .addLoreLine("Verrà inviata la richiesta di")
                        .addLoreLine("pagamento al giocatore specificato.")
                        .get())
                .setAction(8, new ToniiInventoryInterface() {
                    @Override
                    public void run(InventoryClickEvent e) {
                        payment.getOwner().openInventory(payment.getOwnerGUIFactory().getInventory());
                        payment.getPayer().openInventory(payment.getPayerGUIFactory().getInventory());
                    }
                })
                .fillWithGlass(GlassType.WHITE)
                .getInventory();*/
        return new InventoryFactory("Attiva carta", 1, plugin)
                .setItem(0, GlassType.RED.getGlass())
                .setAction(0, new ToniiInventoryInterface() {
                    @Override
                    public void run(InventoryClickEvent e) {
                        //card.setPwd("");
                        InventoryFactory builder = InventoryManager.getBuilder(e.getClickedInventory());
                        if(builder == null) return;
                        builder.enableClose();
                        e.getWhoClicked().closeInventory();
                        InventoryManager.inventoryBuilders.remove(builder);
                    }
                })
                .setItem(4, new ItemStackFactory(Material.OAK_SIGN)
                        .setName("§eScegli una password")
                        .addLoreLine("Questa password ti verrà chiesta")
                        .addLoreLine("ogni volta che farai un acquisto o")
                        .addLoreLine("qualsiasi altra azione tramite questa")
                        .addLoreLine("carta.")
                        .addBlankLoreLine()
                        .addLoreLine("§bPassword scelta: §e" /*+*/ /*(card.getPwd().isBlank() ? "Nessuna" : card.getPwd())*/)
                        .get())
                /*setAction(4, new ToniiInventoryInterface() {
                    @Override
                    public void run(InventoryClickEvent e) {
                        InventoryFactory builder = InventoryManager.getBuilder(e.getClickedInventory());
                        if(builder == null) return;
                        builder.enableClose();
                        new SignInputFactory((Player) e.getWhoClicked(), "-----------------", "Scegli la password", "di questa carta")
                                .setActionToExecute(new ToniiSignInterface() {
                                    @Override
                                    public void run(String input, Player player) {
                                        card.setPwd(input);
                                        player.openInventory(getInitializeGUI(card));
                                    }
                                })
                                .showSign(main);
                    }
                })*/
                .setItem(8, GlassType.LIME_STAINED_GLASS_PANE.getGlass())
                .setAction(8, new ToniiInventoryInterface() {
                    @Override
                    public void run(InventoryClickEvent e) {
                        /*if(card.getPwd().isBlank()){
                            e.getWhoClicked().sendMessage(ErrorMessage.INVALID_PASSWORD.getMessage());
                            return;
                        }*/
                        //card.activate(card.getPwd(), (Player) e.getWhoClicked());
                        InventoryFactory builder = InventoryManager.getBuilder(e.getClickedInventory());
                        if(builder == null) return;
                        builder.enableClose();
                        e.getWhoClicked().closeInventory();
                        InventoryManager.inventoryBuilders.remove(builder);
                    }
                })
                .disableClicks()
                .fillWithGlass(GlassType.WHITE)
                .disableClose()
                .create();
    }
}

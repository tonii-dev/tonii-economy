package io.github.toniidev.ToniiEconomy.listeners;

import io.github.toniidev.ToniiEconomy.builders.InventoryFactory;
import io.github.toniidev.ToniiEconomy.builders.ItemStackFactory;
import io.github.toniidev.ToniiEconomy.builders.SignInputFactory;
import io.github.toniidev.ToniiEconomy.enums.ErrorMessage;
import io.github.toniidev.ToniiEconomy.enums.GlassType;
import io.github.toniidev.ToniiEconomy.interfaces.ToniiInventoryInterface;
import io.github.toniidev.ToniiEconomy.interfaces.ToniiSignInterface;
import io.github.toniidev.ToniiEconomy.items.CreditCard;
import io.github.toniidev.ToniiEconomy.managers.InventoryManager;
import io.github.toniidev.ToniiEconomy.managers.specific.CreditCardManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

public class CreditCardListener implements Listener {
    private final Plugin main;

    public CreditCardListener(Plugin plugin){
        main = plugin;
    }

    @EventHandler
    public void onInitialize(PlayerInteractEvent e){
        if(!CreditCardManager.isCreditCard(e.getPlayer().getInventory().getItemInMainHand())) return;
        CreditCard card = CreditCardManager.getCreditCardInstance(e.getPlayer().getInventory().getItemInMainHand());
        assert card != null; // card cannot be null, CreditCardManager#isCreditCard already checks if card is null.
        if(card.isActive()) return;

        e.getPlayer().openInventory(getInitializeGUI(card));
    }

    private Inventory getInitializeGUI(CreditCard card){
        return new InventoryFactory("Attiva carta", 1, main)
                .setItem(0, GlassType.RED.getGlass())
                .setAction(0, new ToniiInventoryInterface() {
                    @Override
                    public void run(InventoryClickEvent e) {
                        card.setPwd("");
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
                        .addLoreLine("§bPassword scelta: §e" + (card.getPwd().isBlank() ? "Nessuna" : card.getPwd()))
                        .get())
                .setAction(4, new ToniiInventoryInterface() {
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
                })
                .setItem(8, GlassType.LIME_STAINED_GLASS_PANE.getGlass())
                .setAction(8, new ToniiInventoryInterface() {
                    @Override
                    public void run(InventoryClickEvent e) {
                        if(card.getPwd().isBlank()){
                            e.getWhoClicked().sendMessage(ErrorMessage.INVALID_PASSWORD.getMessage());
                            return;
                        }
                        card.activate(card.getPwd(), (Player) e.getWhoClicked());
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

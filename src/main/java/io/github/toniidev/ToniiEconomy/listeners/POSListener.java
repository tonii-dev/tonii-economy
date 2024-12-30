package io.github.toniidev.ToniiEconomy.listeners;

import io.github.toniidev.ToniiEconomy.builders.SignInputFactory;
import io.github.toniidev.ToniiEconomy.enums.CustomItem;
import io.github.toniidev.ToniiEconomy.enums.ErrorMessage;
import io.github.toniidev.ToniiEconomy.interactions.POSPayment;
import io.github.toniidev.ToniiEconomy.interfaces.ToniiSignInterface;
import io.github.toniidev.ToniiEconomy.managers.SignInputManager;
import io.github.toniidev.ToniiEconomy.managers.specific.CreditCardManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
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
        /*if(!CreditCardManager.isCreditCard(victim.getInventory().getItemInMainHand())){
            e.getPlayer().sendMessage(ErrorMessage.ENTITY_NOT_HOLDING_CC.getMessage());
            return;
        }*/

        // now owner has to choose the payment amount
        new SignInputFactory(e.getPlayer(), "-----------------", "Inserisci", "l'importo")
                .setActionToExecute(new ToniiSignInterface() {
                    @Override
                    public void run(String input, Player player) {
                        double amount;
                        try{
                            amount = Double.parseDouble(input);
                        } catch(Exception ex){
                            e.getPlayer().sendMessage(ErrorMessage.INVALID_IMPORT.getMessage());
                            return;
                        }
                        new POSPayment(e.getPlayer(), victim, amount, plugin).show();
                    }
                })
                .showSign(plugin);
    }
}

package io.github.toniidev.ToniiEconomy.managers;

import io.github.toniidev.ToniiEconomy.builders.InventoryFactory;
import io.github.toniidev.ToniiEconomy.enums.ErrorMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class InventoryManager implements Listener {
    private final Plugin plugin;

    public InventoryManager(Plugin main){
        plugin = main;
    }

    public static List<InventoryFactory> inventoryBuilders = new ArrayList<>();

    @Nullable
    public static InventoryFactory getBuilder(Inventory inv){
        return inventoryBuilders.stream()
                .filter(x -> x.getInventory().equals(inv))
                .findFirst().orElse(null);
    }

    /**
     * Execute actions linked to inventories slots
     */
    @EventHandler
    public void handleActions(InventoryClickEvent e){
        InventoryFactory builder = getBuilder(e.getClickedInventory());

        if(builder == null) return; // it means that the clicked inventory is not built from us
        if(builder.getActions().isEmpty()) return; // it means that the inventory does not want us to handle clicks
        if(!builder.getActions().containsKey(e.getRawSlot())) return; // it means that this slot is not connected to an action

        builder.getActions().get(e.getRawSlot()).run(e);
    }

    /**
     * Disable clicks only for slots on which clicks are disabled
     */
    @EventHandler
    public void disableClicks(InventoryClickEvent e){
        InventoryFactory builder = getBuilder(e.getClickedInventory());

        if(builder == null) return;
        if(builder.getDisabledSlots().isEmpty()) return;
        if(!builder.getDisabledSlots().contains(e.getRawSlot())) return;

        e.setCancelled(true);
    }

    /**
     * Disable close event
     */
    @EventHandler
    public void disableClose(InventoryCloseEvent e){
        InventoryFactory builder = getBuilder(e.getInventory());

        if(builder == null) return;
        if(builder.canBeClosed()) return;

        new BukkitRunnable() {
            @Override
            public void run() {
                e.getPlayer().openInventory(e.getInventory());
            }
        }.runTaskLater(plugin, 1L);

        e.getPlayer().sendMessage(ErrorMessage.NO_CLOSE.getMessage());
    }
}

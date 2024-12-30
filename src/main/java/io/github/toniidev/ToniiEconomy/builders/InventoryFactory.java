package io.github.toniidev.ToniiEconomy.builders;

import io.github.toniidev.ToniiEconomy.enums.GlassType;
import io.github.toniidev.ToniiEconomy.interfaces.ToniiInventoryInterface;
import io.github.toniidev.ToniiEconomy.managers.InventoryManager;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class InventoryFactory {
    private final Inventory inv;
    private final Plugin p;

    private final List<Integer> disabledSlots = new ArrayList<>();
    private boolean close = true;
    private Map<Integer, ItemStack> contents = new HashMap<>();
    private Map<Integer, ToniiInventoryInterface> actions = new HashMap<>();

    public InventoryFactory(Inventory inventory, Plugin plugin){
        inv = inventory;
        p = plugin;
        for(int i = 0; i < inventory.getSize(); i++)
            if(inventory.getItem(i) != null) contents.put(i, inventory.getItem(i));
        InventoryManager.inventoryBuilders.add(this);
    }

    public InventoryFactory(String inventoryTitle, int columns, Plugin plugin){
        inv = Bukkit.createInventory(null, columns * 9, inventoryTitle);
        p = plugin;
        InventoryManager.inventoryBuilders.add(this);
    }

    public InventoryFactory(String inventoryTitle, int columns, Map<Integer, ItemStack> inventoryContents, Plugin plugin){
        inv = Bukkit.createInventory(null, columns * 9, inventoryTitle);
        contents = inventoryContents;
        p = plugin;
        InventoryManager.inventoryBuilders.add(this);
    }

    public InventoryFactory setAction(Integer slot, ToniiInventoryInterface action){
        actions.put(slot, action);
        return this;
    }

    public InventoryFactory disableClose(){
        close = false;
        return this;
    }

    public InventoryFactory enableClose(){
        close = true;
        return this;
    }

    public InventoryFactory setItem(Integer slot, ItemStack itemStack){
        contents.put(slot, itemStack);
        return this;
    }

    public InventoryFactory disableClicks(){
        for(int i = 0; i < inv.getSize(); i++)
            disabledSlots.add(i);
        return this;
    }

    public InventoryFactory disableClicks(Integer... slots){
        disabledSlots.addAll(Arrays.asList(slots));
        return this;
    }

    public InventoryFactory fillWithGlass(GlassType type){
        for(int i = 0; i < inv.getSize(); i++)
            contents.computeIfAbsent(i, k -> type.getGlass());
        return this;
    }

    public Inventory create(){
        for(Map.Entry<Integer, ItemStack> item : contents.entrySet())
            inv.setItem(item.getKey(), item.getValue());
        return inv;
    }

    public Inventory getInventory() {
        return inv;
    }

    public Map<Integer, ItemStack> getContents() {
        return contents;
    }

    public List<Integer> getDisabledSlots() {
        return disabledSlots;
    }

    public void setContents(Map<Integer, ItemStack> contents) {
        this.contents = contents;
    }

    public Map<Integer, ToniiInventoryInterface> getActions() {
        return actions;
    }

    public Plugin getPlugin() {
        return p;
    }

    public void addAction(Map<Integer, ToniiInventoryInterface> actions) {
        this.actions = actions;
    }

    public boolean canBeClosed() {
        return close;
    }
}

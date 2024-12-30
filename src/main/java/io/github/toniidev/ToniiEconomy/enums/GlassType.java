package io.github.toniidev.ToniiEconomy.enums;

import io.github.toniidev.ToniiEconomy.builders.ItemStackFactory;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum GlassType {
    WHITE(new ItemStackFactory(Material.WHITE_STAINED_GLASS_PANE)
            .setName(" ").get()),
    GRAY(new ItemStackFactory(Material.GRAY_STAINED_GLASS_PANE)
            .setName(" ").get()),
    LIME_STAINED_GLASS_PANE(new ItemStackFactory(Material.LIME_STAINED_GLASS_PANE)
            .setName("§aConferma").get()),
    RED(new ItemStackFactory(Material.RED_STAINED_GLASS_PANE)
            .setName("§cChiudi").get()),
    LIGHT_GRAY(new ItemStackFactory(Material.LIGHT_GRAY_STAINED_GLASS_PANE)
            .setName(" ").get());

    private final ItemStack stack;

    GlassType(ItemStack itemStack){
        stack = itemStack;
    }

    public ItemStack getGlass(){
        return stack;
    }
}

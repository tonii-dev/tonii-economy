package io.github.toniidev.ToniiEconomy.builders;

import io.github.toniidev.ToniiEconomy.enums.CustomItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemStackFactory {
    private final ItemStack stack;

    public ItemStackFactory(ItemStack itemStack){
        stack = itemStack;
    }

    public ItemStackFactory(Material material){
        stack = new ItemStack(material, 1);
    }

    public ItemStackFactory(Material material, Integer amount){
        stack = new ItemStack(material, amount);
    }

    public ItemStackFactory setName(String name){
        ItemMeta meta = stack.getItemMeta();
        if(meta == null) return this;
        meta.setDisplayName(name);
        stack.setItemMeta(meta);
        return this;
    }

    public ItemStackFactory setCustomItem(CustomItem item){
        ItemMeta meta = stack.getItemMeta();
        if(meta == null) return this;
        meta.setCustomModelData(item.getCustomModelDataValue());
        stack.setItemMeta(meta);
        return this;
    }

    public ItemStackFactory addLoreLine(String line){
        ItemMeta meta = stack.getItemMeta();
        List<String> loreToSet = new ArrayList<>();
        if(meta == null) return this;
        if(meta.getLore() != null) loreToSet = meta.getLore();
        loreToSet.add("§r§7" + line);
        meta.setLore(loreToSet);
        stack.setItemMeta(meta);
        return this;
    }

    public ItemStackFactory addBlankLoreLine(){
        return this.addLoreLine(" ");
    }

    public ItemStackFactory setGlowing(boolean value){
        ItemMeta meta;
        Enchantment enchantment = stack.getType().equals(Material.BOW) ? Enchantment.PROTECTION : Enchantment.INFINITY;

        if(value){
            stack.addUnsafeEnchantment(enchantment, 1);
            meta = stack.getItemMeta();
            if(meta == null) return this;
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else{
            stack.removeEnchantment(enchantment);
            meta = stack.getItemMeta();
            if(meta == null) return this;
            meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        stack.setItemMeta(meta);
        return this;
    }

    public ItemStack get(){
        return stack;
    }
}

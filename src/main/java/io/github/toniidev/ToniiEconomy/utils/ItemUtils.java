package io.github.toniidev.ToniiEconomy.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemUtils {
    public static ItemStack getPlayerSkull(Player player){
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        if(meta == null) return skull;
        meta.setOwnerProfile(player.getPlayerProfile());
        skull.setItemMeta(meta);
        return skull;
    }
}

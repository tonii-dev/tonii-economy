package io.github.toniidev.ToniiEconomy.commands;

import io.github.toniidev.ToniiEconomy.builders.InventoryFactory;
import io.github.toniidev.ToniiEconomy.builders.ItemStackFactory;
import io.github.toniidev.ToniiEconomy.enums.CustomItem;
import io.github.toniidev.ToniiEconomy.enums.ErrorMessage;
import io.github.toniidev.ToniiEconomy.enums.GlassType;
import io.github.toniidev.ToniiEconomy.items.CreditCard;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class EconomyGUI implements CommandExecutor {
    private final Plugin p;

    public EconomyGUI(Plugin plugin){
        p = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player player)) {
            commandSender.sendMessage(ErrorMessage.NOT_EXECUTABLE_FROM_CONSOLE.getMessage());
            return true;
        }
        if(!commandSender.isOp()) {
            commandSender.sendMessage(ErrorMessage.MISSING_PERMISSIONS.getMessage());
            return true;
        }

        ItemStack card = new ItemStackFactory(Material.GREEN_DYE)
                .setName("§6Carta di credito")
                .setCustomItem(CustomItem.CREDIT_CARD)
                .get();
        ItemStack pos = new ItemStackFactory(Material.GREEN_DYE)
                .setName("§bPOS")
                .setCustomItem(CustomItem.POS)
                .get();
        ItemStack car = new ItemStackFactory(Material.DIAMOND_HOE)
                .setName("§bMacchinone")
                .get();
        ItemStack carina = new ItemStackFactory(Material.DIAMOND_HOE)
                .setName("§bMachinetta")
                .get();

        final Inventory gui = new InventoryFactory("tonii-economy Admin", 1, p)
                .setItem(0, card)
                .setAction(0, e -> player.getInventory().addItem(new CreditCard().toItemStack()))
                .setItem(1, pos)
                .setAction(1, e -> player.getInventory().addItem(pos))
                .setItem(2, car)
                .setAction(2, e -> player.getInventory().addItem(car))
                .setItem(3, carina)
                .setAction(3, e -> player.getInventory().addItem(carina))
                .fillWithGlass(GlassType.WHITE)
                .disableClicks()
                .create();

        player.openInventory(gui);
        return true;
    }
}

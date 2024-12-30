package io.github.toniidev.ToniiEconomy;

import io.github.toniidev.ToniiEconomy.commands.EconomyGUI;
import io.github.toniidev.ToniiEconomy.commands.GetInput;
import io.github.toniidev.ToniiEconomy.listeners.CreditCardListener;
import io.github.toniidev.ToniiEconomy.listeners.POSListener;
import io.github.toniidev.ToniiEconomy.managers.SignInputManager;
import io.github.toniidev.ToniiEconomy.managers.InventoryManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ToniiEconomy extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        Bukkit.getPluginManager().registerEvents(new InventoryManager(this), this);
        Bukkit.getPluginManager().registerEvents(new SignInputManager(), this);
        Bukkit.getPluginManager().registerEvents(new CreditCardListener(this), this);
        Bukkit.getPluginManager().registerEvents(new POSListener(this), this);
        Bukkit.getPluginCommand("economygui").setExecutor(new EconomyGUI(this));
        Bukkit.getPluginCommand("getinput").setExecutor(new GetInput(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

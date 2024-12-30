package io.github.toniidev.ToniiEconomy.enums;

import org.bukkit.inventory.ItemStack;

public enum PaymentStatus {
    WAITING("§6In attesa..."),
    PROCESSING("§eVerifica..."),
    DECLINED("§cRifiutato"),
    ACCEPTED("§aAccettato");

    private final String status;

     PaymentStatus(String string){
        status = string;
    }

    public String getString(){return status;}
}

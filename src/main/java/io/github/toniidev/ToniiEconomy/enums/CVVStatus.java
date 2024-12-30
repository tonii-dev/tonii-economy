package io.github.toniidev.ToniiEconomy.enums;

public enum CVVStatus {
    ABSENT("§6Non inserito"),
    WRONG("§cSbagliato"),
    RIGHT("§cGiusto");

    private final String status;

    CVVStatus(String string){
        status = string;
    }

    public String getString(){return status;}
}

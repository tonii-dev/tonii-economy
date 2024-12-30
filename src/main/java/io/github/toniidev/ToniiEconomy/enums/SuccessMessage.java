package io.github.toniidev.ToniiEconomy.enums;

public enum SuccessMessage {
    PAYMENT_SUCCESSFUL("§a[POS]§e Conferma§7: Pagamento accettato.");

    private final String value;

    SuccessMessage(String message){
        value = message;
    }

    public String getMessage(){
        return value;
    }
}

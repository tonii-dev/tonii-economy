package io.github.toniidev.ToniiEconomy.enums;

public enum ErrorMessage {
    MISSING_PERMISSIONS("§a[tonii-economy]§e Comando§7: Non puoi eseguire questo comando: non hai i permessi necessari."),
    NOT_EXECUTABLE_FROM_CONSOLE("§a[tonii-economy]§e Comando§7: Non puoi eseguire questo comando dalla console."),
    NO_CLOSE("§a[Gioco]§e Inventario§7: Non puoi chiudere questo inventario."),
    INVALID_PASSWORD("§a[Carta di credito]§e Attivazione§7: Non hai inserito una password valida."),
    WRONG_PASSWORD("§a[POS]§e Conferma§7: Hai inserito una password sbagliata."),
    PAYMENT_REJECTED("§a[POS]§e Conferma§7: Pagamento rifiutato."),
    NO_PASSWORD("§a[POS]§e Conferma§7: Non hai inserito una password."),
    ENTITY_NOT_HOLDING_CC("§a[POS]§e Interazione§7: Il giocatore su cui hai cliccato non ha una carta di credito in mano."),
    INVALID_IMPORT("§a[POS]§e Interazione§7: Non hai inserito un importo valido.");

    private final String value;

    ErrorMessage(String message){
        value = message;
    }

    public String getMessage(){
        return value;
    }
}

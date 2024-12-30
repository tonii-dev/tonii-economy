package io.github.toniidev.ToniiEconomy.enums;

public enum CustomItem {
    POS(69001),
    CREDIT_CARD(69002);

    private final Integer customModelDataInt;

    CustomItem(Integer customModelDataValue){
        customModelDataInt = customModelDataValue;
    }

    public Integer getCustomModelDataValue(){
        return customModelDataInt;
    }
}

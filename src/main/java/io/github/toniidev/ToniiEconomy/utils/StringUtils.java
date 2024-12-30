package io.github.toniidev.ToniiEconomy.utils;

import java.util.Random;

public class StringUtils {
    /**
     * Generates a standard CreditCard code, composed by four sections, the first of which is "5333", following this pattern: 5333 xxxx xxxx xxxx
     * Example: 5333 1209 2490 2301
     * @return A standard CreditCard code, usually used in CreditCard constructor to generate a code for that CreditCard instance.
     */
    public static String generateCode(){
        String firstSection = "5333";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(firstSection);
        for (int i = 0; i < 3; i++) {
            sb.append(" ");
            for (int j = 0; j < 4; j++) {
                sb.append(random.nextInt(9) + 1);
            }
        }
        return sb.toString();
    }
}

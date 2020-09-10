package cashmachine;

import java.util.HashMap;
import java.util.Map;

public class CurrencyManipulator {
    private String currencyCode;                                    // Имя валюты (3 буквы)
    private Map<Integer, Integer> denominations = new HashMap<>();  // Map<номинал, количество>

    public CurrencyManipulator(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }
}

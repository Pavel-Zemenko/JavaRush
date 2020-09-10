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

    public void addAmount(int denomination, int count) {
        denominations.compute(denomination, (k, v) -> (v == null) ? count : v + count);
    }
}

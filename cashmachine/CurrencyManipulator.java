package javarush.cashmachine;

import javarush.cashmachine.exception.NotEnoughMoneyException;

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

    public int getTotalAmount() {
        int amount = 0;
        for (Map.Entry<Integer, Integer> entry : denominations.entrySet()) {
            amount += entry.getKey() * entry.getValue();
        }
        return amount;
    }

    public boolean hasMoney() {
        for (Integer i : denominations.values()) {
            if (i > 0) {
                return true;
            }
        }
        return false;
    }

    public boolean isAmountAvailable(int expectedAmount) {
        return getTotalAmount() >= expectedAmount;
    }

    public Map<Integer, Integer> withdrawAmount(int expectedAmount) throws NotEnoughMoneyException {
        Map<Integer, Integer> resultMap = new HashMap<>();

        // Для теста:
        resultMap.put(5, 7);
        resultMap.put(50, 2);
        resultMap.put(100, 3);
        resultMap.put(10, 5);

        return resultMap;
    }
}

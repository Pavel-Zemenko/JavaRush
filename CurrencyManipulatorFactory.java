package cashmachine;

import java.util.Map;
import java.util.WeakHashMap;

public class CurrencyManipulatorFactory {
    private static Map<String, CurrencyManipulator> manipulatorMap = new WeakHashMap<>();

    private CurrencyManipulatorFactory() {}

    public static CurrencyManipulator getManipulatorByCurrencyCode(String currencyCode) {
        CurrencyManipulator manipulator;
        if (manipulatorMap.containsKey(currencyCode)) {
            manipulator = manipulatorMap.get(currencyCode);
        } else {
            manipulator = new CurrencyManipulator(currencyCode);
            manipulatorMap.put(currencyCode, manipulator);
        }
        return manipulator;
    }
}

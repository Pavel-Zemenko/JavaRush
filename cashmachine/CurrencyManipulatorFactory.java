package cashmachine.cashmachine;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CurrencyManipulatorFactory {
    private static Map<String, CurrencyManipulator> manipulatorMap = new HashMap<>();

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

    public static Collection<CurrencyManipulator> getAllCurrencyManipulators() {
        return Collections.unmodifiableCollection(manipulatorMap.values());
    }
}